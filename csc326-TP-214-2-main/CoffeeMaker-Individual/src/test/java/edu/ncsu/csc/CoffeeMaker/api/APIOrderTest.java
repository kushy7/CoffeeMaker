package edu.ncsu.csc.CoffeeMaker.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.servlet.http.Cookie;
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
import edu.ncsu.csc.CoffeeMaker.models.Staff;
import edu.ncsu.csc.CoffeeMaker.models.Customer;
import edu.ncsu.csc.CoffeeMaker.models.CustomerOrder;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.StaffService;
import edu.ncsu.csc.CoffeeMaker.services.CustomerService;
import edu.ncsu.csc.CoffeeMaker.services.OrderService;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

/**
 * Tests the APIOrder calls
 *
 * @author benyu & lmgetzen
 *
 */
@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith ( SpringExtension.class )
public class APIOrderTest {

    /**
     * MockMvc uses Spring's testing framework to handle requests to the REST
     * API
     */
    private MockMvc               mvc;

    /** Context */
    @Autowired
    private WebApplicationContext context;

    /** Service for orders */
    @Autowired
    private OrderService          service;

    /** Service for customers */
    @Autowired
    private CustomerService       cService;

    /** Service for staff */
    @Autowired
    private StaffService       sService;

    /** Service for recipe */
    @Autowired
    private RecipeService         rService;

    /**
     * Sets up the tests.
     */
    @BeforeEach
    public void setup () {
        mvc = MockMvcBuilders.webAppContextSetup( context ).build();

        service.deleteAll();
    }

    /**
     * Tests
     *
     * @throws Exception
     *             exception
     */
    @Test
    @Transactional
    public void ensureOrder () throws Exception {
        service.deleteAll();
        cService.deleteAll();

        final Recipe r = new Recipe();
        r.addIngredient( new Ingredient( "Chocolate", 5 ) );
        r.addIngredient( new Ingredient( "Coffee", 3 ) );
        r.addIngredient( new Ingredient( "Milk", 4 ) );
        r.addIngredient( new Ingredient( "Sugar", 8 ) );
        r.setPrice( 10 );
        r.setName( "Mocha" );

        rService.save( r );

        final Customer c = new Customer( "fkperson", "Fakepass123" );

        cService.save( c );
        final Cookie auth = mvc
                .perform( MockMvcRequestBuilders.post( "/api/v1/customer/login" )
                .contentType( MediaType.APPLICATION_JSON ).content(
                TestUtils.asJsonString( c ) ) )
                .andExpect( status().isOk() ).andReturn().getResponse().getCookie(
                "coffeemaker-session" );

        final CustomerOrder o = new CustomerOrder( c.getName(), r, "fkOrderId" );

        mvc.perform( MockMvcRequestBuilders.post( "/api/v1/orders" ).cookie(auth).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( o ) ) ).andExpect( status().isOk() );

    }

    /**
     * Tests
     *
     * @throws Exception
     *             exception
     */
    @Test
    @Transactional
    public void testOrderAPI () throws Exception {

        rService.deleteAll();
        service.deleteAll();
        cService.deleteAll();

        final Recipe r = new Recipe();
        r.addIngredient( new Ingredient( "Chocolate", 5 ) );
        r.addIngredient( new Ingredient( "Coffee", 3 ) );
        r.addIngredient( new Ingredient( "Milk", 4 ) );
        r.addIngredient( new Ingredient( "Sugar", 8 ) );
        r.setPrice( 10 );
        r.setName( "Mocha" );

        rService.save( r );

        final Customer c = new Customer( "fkperson", "Fakepass123" );

        cService.save( c );

        final Cookie auth = mvc
            .perform( MockMvcRequestBuilders.post( "/api/v1/customer/login" )
            .contentType( MediaType.APPLICATION_JSON ).content(
            TestUtils.asJsonString( c ) ) )
            .andExpect( status().isOk() ).andReturn().getResponse().getCookie(
            "coffeemaker-session" );


        final CustomerOrder o = new CustomerOrder( c.getName(), r, "fkOrderId" );

        mvc.perform( MockMvcRequestBuilders.post( "/api/v1/orders" ).cookie( auth ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( o ) ) );

        Assertions.assertEquals( 1, (int) service.count() );

    }

    /**
     * Tests
     *
     * @throws Exception
     *             exception
     */
    @Test
    @Transactional
    public void testAddOrder1 () throws Exception {
        /* Tests adding one order to the database */

        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Orders in the CoffeeMaker" );
        final String name = "Coffee";
        final Recipe r1 = createRecipe( name, 50, 3, 1, 1, 0 );
        final Customer c = new Customer( "fkperson", "Fakepass123" );
        final CustomerOrder o1 = new CustomerOrder( c.getName(), r1, "fkOrderId" );

        rService.save( r1 );
        cService.save( c );
        service.save( o1 );
        
        final Cookie auth = mvc
                .perform( MockMvcRequestBuilders.post( "/api/v1/customer/login" )
                .contentType( MediaType.APPLICATION_JSON ).content(
                TestUtils.asJsonString( c ) ) )
                .andExpect( status().isOk() ).andReturn().getResponse().getCookie(
                "coffeemaker-session" );

        final CustomerOrder o2 = new CustomerOrder( c.getName(), r1, "fkOrderId" );

        mvc.perform( MockMvcRequestBuilders.post( "/api/v1/orders" ).cookie(auth).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( o2 ) ) ).andExpect( status().is4xxClientError() );

        Assertions.assertEquals( 1, service.findAll().size(), "There should be only one order in the CoffeeMaker" );
    }

    /**
     * Tests
     *
     * @throws Exception
     *             exception
     */
    @Test
    @Transactional
    public void testAddOrder2 () throws Exception {

        /* Tests to make sure that multiple orders can be added */

        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Orders in the CoffeeMaker" );
        final String name = "Coffee";
        final Recipe r1 = createRecipe( name, 50, 3, 1, 1, 0 );
        final Customer c = new Customer( "fkperson", "Fakepass123" );
        final CustomerOrder o1 = new CustomerOrder( c.getName(), r1, "fkOrderId" );

        rService.save( r1 );

        cService.save( c );

        service.save( o1 );

        final CustomerOrder o2 = new CustomerOrder( c.getName(), r1, "fkOrderId" );

        service.save( o2 );

        Assertions.assertEquals( 2, service.count(),
                "Creating two orders should result in two orders in the database" );
    }

    private Recipe createRecipe ( final String name, final Integer price, final Integer coffee, final Integer milk,
            final Integer sugar, final Integer chocolate ) {
        final Recipe recipe = new Recipe();
        recipe.setName( name );
        recipe.setPrice( price );
        recipe.addIngredient( new Ingredient( "Chocolate", chocolate ) );
        recipe.addIngredient( new Ingredient( "Coffee", coffee ) );
        recipe.addIngredient( new Ingredient( "Milk", milk ) );
        recipe.addIngredient( new Ingredient( "Sugar", sugar ) );

        return recipe;
    }

    /**
     * Tests order count endpoint
     *
     * @throws Exception
     *             exception
     */
    @Test
    @Transactional
    public void testOrderCount () throws Exception {
        final String username = "fkperson";
        final String password = "Fakepass123";
        final Customer c = new Customer( username, password );
        cService.save( c );
        final Cookie auth = mvc
                .perform( MockMvcRequestBuilders.post( "/api/v1/customer/login" )
                .contentType( MediaType.APPLICATION_JSON ).content(
                TestUtils.asJsonString( c ) ) )
                .andExpect( status().isOk() ).andReturn().getResponse().getCookie(
                "coffeemaker-session" );

        String order = mvc.perform( get( "/api/v1/orders" ).cookie(auth) ).andDo( print() ).andExpect( status().isOk() ).andReturn()
                .getResponse().getContentAsString();
        for(int i = 0; i < 4; i++) {
            final Recipe r1 = createRecipe( "Coffee", 50, 3, 1, 1, 0 );
            final CustomerOrder o1 = new CustomerOrder( c.getName(), r1, "fkOrderId" + 
                    ((Integer)i).toString() );
    
            rService.save( r1 );
    
            mvc.perform( MockMvcRequestBuilders.post( "/api/v1/orders" ).cookie( auth ).contentType( MediaType.APPLICATION_JSON )
                    .content( TestUtils.asJsonString( o1 ) ) ).andExpect( status().isOk() );
            order = mvc.perform( get( "/api/v1/order/count" ).cookie( auth ) ).andDo( print() ).andExpect( status().isOk() ).andReturn()
                    .getResponse().getContentAsString();
            assertEquals(((Integer)(i + 1)).toString(), order);
        }

    }

    /**
     * Tests getting and order
     *
     * @throws Exception
     *             exception
     */
    @Test
    @Transactional
    public void testGetOrder () throws Exception {
        final String username = "fkperson";
        final String password = "Fakepass123";
        final Customer c = new Customer( username, password );
        cService.save( c );
        final Cookie auth = mvc
                .perform( MockMvcRequestBuilders.post( "/api/v1/customer/login" )
                .contentType( MediaType.APPLICATION_JSON ).content(
                TestUtils.asJsonString( c ) ) )
                .andExpect( status().isOk() ).andReturn().getResponse().getCookie(
                "coffeemaker-session" );

        String order = mvc.perform( get( "/api/v1/orders" ).cookie(auth) ).andDo( print() ).andExpect( status().isOk() ).andReturn()
                .getResponse().getContentAsString();

        // makes sure the order object is created
        if ( !order.contains( "0" ) ) {
            final Recipe r1 = createRecipe( "Coffee", 50, 3, 1, 1, 0 );
            final CustomerOrder o1 = new CustomerOrder( c.getName(), r1, "fkOrderId" );

            rService.save( r1 );

            mvc.perform( MockMvcRequestBuilders.post( "/api/v1/orders" ).cookie( auth ).contentType( MediaType.APPLICATION_JSON )
                    .content( TestUtils.asJsonString( o1 ) ) ).andExpect( status().isOk() );

        }
        order = mvc.perform( get( "/api/v1/orders" ).cookie( auth ) ).andDo( print() ).andExpect( status().isOk() ).andReturn()
                .getResponse().getContentAsString();
        assertTrue( order.contains( "0" ) );

    }

    /**
     * Tests getting a specific order
     *
     * @throws Exception
     *             exception
     */
    @Test
    @Transactional
    public void testGetSpecificOrder () throws Exception {
        final String username = "fkperson";
        final String password = "Fakepass123";
        final Customer c = new Customer( username, password );
        cService.save( c );
        final Cookie auth = mvc
                .perform( MockMvcRequestBuilders.post( "/api/v1/customer/login" )
                .contentType( MediaType.APPLICATION_JSON ).content(
                TestUtils.asJsonString( c ) ) )
                .andExpect( status().isOk() ).andReturn().getResponse().getCookie(
                "coffeemaker-session" );
        final Recipe r1 = createRecipe( "Coffee", 50, 3, 1, 1, 0 );
        final CustomerOrder o1 = new CustomerOrder( c.getName(), r1, "fkOrderId" );

        rService.save( r1 );

        mvc.perform( MockMvcRequestBuilders.post( "/api/v1/orders" ).cookie( auth ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( o1 ) ) ).andExpect( status().isOk() );

        String order = mvc.perform( get( "/api/v1/orders/fkOrderId" ).cookie( auth ) ).andDo( print() ).andExpect( status().isOk() ).andReturn()
                .getResponse().getContentAsString();
        assertTrue( order.contains( c.getName() ) );

    }

    /**
     * Deletes a recipe from the system
     *
     * @throws Exception
     *             exception
     */
    @Test
    @Transactional
    public void testDeleteOrderAPI () throws Exception {

        final Customer c = new Customer( "fkperson", "Fakepass123" );
        cService.save( c );
        final Staff s = new Staff( "fkperson", "Fakepass123", false, "admin123" );
        sService.save( s );
        final Cookie auth = mvc
                .perform( MockMvcRequestBuilders.post( "/api/v1/customer/login" )
                .contentType( MediaType.APPLICATION_JSON ).content(
                TestUtils.asJsonString( c ) ) )
                .andExpect( status().isOk() ).andReturn().getResponse().getCookie(
                "coffeemaker-session" );
        final Cookie sAuth = mvc
                .perform( MockMvcRequestBuilders.post( "/api/v1/staff/login" )
                .contentType( MediaType.APPLICATION_JSON ).content(
                TestUtils.asJsonString( s ) ) )
                .andExpect( status().isOk() ).andReturn().getResponse().getCookie(
                "coffeemaker-session" );
        String order = mvc.perform( get( "/api/v1/orders" ).cookie(auth) ).andDo( print() ).andExpect( status().isOk() ).andReturn()
                .getResponse().getContentAsString();


        if ( !order.contains( "fkOrderId" ) ) {

            final String name = "Coffee";
            final Recipe r1 = createRecipe( name, 50, 3, 1, 1, 0 );
            final CustomerOrder o1 = new CustomerOrder( c.getName(), r1, "fkOrderId" );

            rService.save( r1 );

            mvc.perform( MockMvcRequestBuilders.post( "/api/v1/orders" ).cookie( auth ).contentType( MediaType.APPLICATION_JSON )
                    .content( TestUtils.asJsonString( o1 ) ) ).andExpect( status().isOk() );

        }

        order = mvc.perform( get( "/api/v1/orders" ).cookie( auth ) ).andDo( print() ).andExpect( status().isOk() ).andReturn()
                .getResponse().getContentAsString();
        assertTrue( order.contains( "fkOrderId" ) );

        if ( order.contains( "fkOrderId" ) ) {
            mvc.perform( delete( "/api/v1/orders/fkOrderId" ).cookie(sAuth) ).andDo( print() ).andExpect( status().isOk() )
                    .andReturn().getResponse().getContentAsString();

        }

        order = mvc.perform( get( "/api/v1/orders" ).cookie( auth ) ).andDo( print() ).andExpect( status().isOk() ).andReturn()
                .getResponse().getContentAsString();
        assertFalse( order.contains( "fkOrderId" ) );

        Assertions.assertEquals( 0, service.count(), "Deleting an order should result in zero orders in the database" );

    }
    

}
