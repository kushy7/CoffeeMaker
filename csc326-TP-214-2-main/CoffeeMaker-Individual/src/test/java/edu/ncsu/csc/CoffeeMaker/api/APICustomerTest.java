package edu.ncsu.csc.CoffeeMaker.api;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import edu.ncsu.csc.CoffeeMaker.common.TestUtils;
import edu.ncsu.csc.CoffeeMaker.models.Customer;
import edu.ncsu.csc.CoffeeMaker.services.CustomerService;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith ( SpringExtension.class )
public class APICustomerTest {

    /**
     * MockMvc uses Spring's testing framework to handle requests to the REST
     * API
     */
    private MockMvc               mvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private CustomerService       service;

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
    public void ensureCustomer () throws Exception {
        service.deleteAll();

        final Customer c = new Customer( "fkperson", "Fakepass123" );

        mvc.perform( MockMvcRequestBuilders.put( "/api/v1/customers" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( c ) ) ).andExpect( status().isOk() );

    }

    @Test
    @Transactional
    public void testCustomerAPI () throws Exception {

        service.deleteAll();

        final Customer c = new Customer( "fkperson", "Fakepass123" );

        mvc.perform( MockMvcRequestBuilders.put( "/api/v1/customers" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( c ) ) );

        Assertions.assertEquals( 1, (int) service.count() );

    }

    @Test
    @Transactional
    public void testAddCustomer1 () throws Exception {

        /* Tests a recipe with a duplicate name to make sure it's rejected */

        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Users in the CoffeeMaker" );
        final String username = "fkperson";
        final String password = "Fakepass123";
        final Customer c1 = new Customer( username, password );

        service.save( c1 );

        final Customer c2 = new Customer( username, password );
        mvc.perform( MockMvcRequestBuilders.put( "/api/v1/customers" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( c2 ) ) ).andExpect( status().is4xxClientError() );

        Assertions.assertEquals( 1, service.findAll().size(), "There should only one customer in the CoffeeMaker" );
    }

    @Test
    @Transactional
    public void testAddCustomer2 () throws Exception {

        /* Tests to make sure that our cap of 3 recipes is enforced */

        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final String username = "fkperson";
        final String password = "Fakepass123";
        final Customer c1 = new Customer( username, password );
        service.save( c1 );

        final String username1 = "fkperson1";
        final String password1 = "Fakepass1234";
        final Customer c2 = new Customer( username1, password1 );
        service.save( c2 );

        Assertions.assertEquals( 2, service.count(),
                "Creating two customers should result in two customers in the database" );

    }

    @Test
    @Transactional
    public void testGetCustomer () throws Exception {

        String customer = mvc.perform( get( "/api/v1/customers" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();

        // makes sure the recipe object is created
        if ( !customer.contains( "fkperson" ) ) {
            final String username = "fkperson";
            final String password = "Fakepass123";
            final Customer c = new Customer( username, password );

            mvc.perform( MockMvcRequestBuilders.put( "/api/v1/customers" ).contentType( MediaType.APPLICATION_JSON )
                    .content( TestUtils.asJsonString( c ) ) ).andExpect( status().isOk() );

        }
        customer = mvc.perform( get( "/api/v1/customers" ) ).andDo( print() ).andExpect( status().isOk() ).andReturn()
                .getResponse().getContentAsString();
        assertTrue( customer.contains( "fkperson" ) );

    }

    @Test
    @Transactional
    public void testValidateCustomer () throws Exception {

        final String customer = mvc.perform( get( "/api/v1/customers" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();

        // makes sure the recipe object is created
        if ( !customer.contains( "fkperson" ) ) {
            final String username = "fkperson";
            final String password = "Fakepass123";
            final Customer c = new Customer( username, password );
            service.save( c );

            // mvc.perform( MockMvcRequestBuilders.put( "/api/v1/customers"
            // ).contentType( MediaType.APPLICATION_JSON )
            // .content( TestUtils.asJsonString( c ) ) ).andExpect(
            // status().isOk() );

            final Customer c1 = service.findByUsername( "fkperson" );

            assertTrue( c.validate( c1.getUsername(), c1.getPassword() ) );

        }

    }

    // @Test
    // @Transactional
    // public void testLoginCustomer () throws Exception {
    //
    // final String username = "fkperson";
    // final String password = "123";
    // final Customer c1 = new Customer( username, password );
    //
    // service.save( c1 );
    // // Log in
    // final Cookie auth = mvc
    // .perform( MockMvcRequestBuilders.post( "/api/v1/customer/login" )
    // .contentType( MediaType.APPLICATION_JSON ).content(
    // TestUtils.asJsonString( c1 ) ) )
    // .andExpect( status().isOk() ).andReturn().getResponse().getCookie(
    // "coffeemaker-session" );
    // assertNotNull( auth );
    // // Test user cookie session
    // final String cu = mvc.perform( get( "/api/v1/user" ).cookie( auth )
    // ).andDo( print() )
    // .andExpect( status().isOk()
    // ).andReturn().getResponse().getContentAsString();
    // // Assert has correct username
    // assertTrue( cu.contains( "fkperson" ) );
    // }

}
