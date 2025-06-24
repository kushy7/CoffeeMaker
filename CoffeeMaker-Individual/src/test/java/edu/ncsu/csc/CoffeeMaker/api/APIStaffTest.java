package edu.ncsu.csc.CoffeeMaker.api;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.transaction.Transactional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.env.PropertySourceLoader;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import edu.ncsu.csc.CoffeeMaker.common.TestUtils;
import edu.ncsu.csc.CoffeeMaker.models.Staff;
import edu.ncsu.csc.CoffeeMaker.services.StaffService;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith ( SpringExtension.class )
class APIStaffTest {

    /**
     * MockMvc uses Spring's testing framework to handle requests to the REST
     * API
     */
    private MockMvc               mvc;

    /** Context */
    @Autowired
    private WebApplicationContext context;

    /** Service user to interact with database */
    @Autowired
    private StaffService          service;

    @Value("${auth.staffSignupKey}")
    private String staffSignupKey;

    /**
     * Sets up the tests.
     */
    @BeforeEach
    public void setup () {
        mvc = MockMvcBuilders.webAppContextSetup( context ).build();
        service.deleteAll();
    }

    @Test
    @Transactional
    public void ensureStaff () throws Exception {
        service.deleteAll();

        final Staff u = new Staff( "fkperson", "Fakepass123", false, staffSignupKey );
        mvc.perform( MockMvcRequestBuilders.put( "/api/v1/staff" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( u ) ) ).andExpect( status().isOk() );

    }

    @Test
    @Transactional
    public void testStaffAPI () throws Exception {

        service.deleteAll();

        final Staff u = new Staff( "fkperson", "Fakepass123", false, staffSignupKey );

        mvc.perform( MockMvcRequestBuilders.put( "/api/v1/staff" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( u ) ) );

        Assertions.assertEquals( 1, (int) service.count() );

    }

    @Test
    @Transactional
    public void testStaffAPI2 () throws Exception {

        /* Tests a recipe with a duplicate name to make sure it's rejected */

        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Users in the CoffeeMaker" );
        final String username = "fkperson";
        final String password = "Fakepass123";
        final Staff u1 = new Staff( username, password, false, staffSignupKey );

        service.save( u1 );

        final Staff u2 = new Staff( username, password, false, staffSignupKey );
        mvc.perform( MockMvcRequestBuilders.put( "/api/v1/staff" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( u2 ) ) ).andExpect( status().is4xxClientError() );

        Assertions.assertEquals( 1, service.findAll().size(), "There should only one user in the CoffeeMaker" );
    }

    @Test
    @Transactional
    public void testStaffAPI15 () throws Exception {

        /* Tests to make sure that our cap of 3 recipes is enforced */

        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final String username = "fkperson";
        final String password = "Fakepass123";
        final Staff r1 = new Staff( username, password, false, staffSignupKey );
        service.save( r1 );

        final String username1 = "fkperson1";
        final String password1 = "Fakepass1234";
        final Staff r2 = new Staff( username1, password1, false, staffSignupKey );
        service.save( r2 );

        Assertions.assertEquals( 2, service.count(),
                "Creating two recipes should result in two recipes in the database" );

    }

    // @Test
    // @Transactional
    // public void testLoginStaff () throws Exception {
    // try {
    // final String username = "fkperson";
    // final String password = "123";
    // final Staff s1 = new Staff( username, password, false, "admin123" );
    //
    // service.save( s1 );
    // // Log in
    // final Cookie auth = mvc
    // .perform( MockMvcRequestBuilders.post( "/api/v1/staff/login" )
    // .contentType( MediaType.APPLICATION_JSON ).content(
    // TestUtils.asJsonString( s1 ) ) )
    // .andExpect( status().isOk() ).andReturn().getResponse().getCookie(
    // "coffeemaker-session" );
    // assertNotNull( auth );
    //
    // // Test user cookie session
    // final String cu = mvc.perform( get( "/api/v1/user" ).cookie( auth )
    // ).andDo( print() )
    // .andExpect( status().isOk()
    // ).andReturn().getResponse().getContentAsString();
    //
    // // Assert has correct username
    // assertTrue( cu.contains( "fkperson" ) );
    // }
    // catch ( final Exception e ) {
    // fail( "An exception occurred: " + e.getMessage() );
    // }
    // }

}
