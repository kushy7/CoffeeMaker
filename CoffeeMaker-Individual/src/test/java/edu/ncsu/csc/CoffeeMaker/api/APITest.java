package edu.ncsu.csc.CoffeeMaker.api;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import edu.ncsu.csc.CoffeeMaker.common.TestUtils;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;

/**
 * API tests for Order Beverage
 *
 * @author Yi Zhang
 * @author Quaker Schneider
 */
@RunWith ( SpringRunner.class )
@SpringBootTest
@AutoConfigureMockMvc
public class APITest {
    /**
     * MockMvc uses Spring's testing framework to handle requests to the REST
     * API
     */
    private MockMvc               mvc;

    @Autowired
    private WebApplicationContext context;

    /**
     * Sets up the tests.
     */
    @Before
    public void setup () {
        mvc = MockMvcBuilders.webAppContextSetup( context ).build();
    }
    

    /**
     * Tests the inventory get endpoint
     *
     */
    @Test
    @Transactional
    public void testAPIInventoryGet () throws Exception {
        mvc.perform( get( "/api/v1/inventory" ) ).andDo( print() ).andExpect( status().isOk() );
        
    }

    /**
     * Tests if the server creates a recipe object
     *
     */
    @Test
    @Transactional
    public void testAPIRecipe () throws Exception {
        String recipe = mvc.perform( get( "/api/v1/recipes" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();

        // create a recipe object for testing
        if ( !recipe.contains( "Mocha" ) ) {
            final Recipe r = new Recipe();
            r.addIngredient( new Ingredient("Chocolate", 3) );
            r.addIngredient( new Ingredient("Coffee", 5) );
            r.addIngredient( new Ingredient("Milk", 5) );
            r.addIngredient( new Ingredient("Sugar", 3) );
            r.setPrice( 8 );
            r.setName( "Mocha" );

            mvc.perform( put( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                    .content( TestUtils.asJsonString( r ) ) ).andExpect( status().isOk() );

        }
        
        recipe = mvc.perform( get( "/api/v1/recipes" ) ).andDo( print() ).andExpect( status().isOk() ).andReturn()
                .getResponse().getContentAsString();

        // make sure the recipe is created
        assertTrue( recipe.contains( "Mocha" ) );
        
        // Adding Inventory
        final Inventory inv = new Inventory();
        inv.addIngredient( new Ingredient("Coffee", 50) );
        inv.addIngredient( new Ingredient("Milk", 50) );
        inv.addIngredient( new Ingredient("Sugar", 50) );
        inv.addIngredient( new Ingredient("Chocolate", 50) );
        
        mvc.perform( put( "/api/v1/inventory" )
                .contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( inv ) ) )
                .andDo( print() )
                .andExpect( status().isOk() );

        // make coffee
        mvc.perform( post( String.format( "/api/v1/makecoffee/%s", "Mocha" ) ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( 100 ) ) ).andExpect( status().isOk() ).andDo( print() );
        
    }
}
