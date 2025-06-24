package edu.ncsu.csc.CoffeeMaker.api;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith ( SpringExtension.class )
public class APIRecipeTest {

    /**
     * MockMvc uses Spring's testing framework to handle requests to the REST
     * API
     */
    private MockMvc               mvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private RecipeService         service;

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
    public void ensureRecipe () throws Exception {
        service.deleteAll();

        final Recipe r = new Recipe();
        r.addIngredient( new Ingredient("Chocolate", 5 ) );
        r.addIngredient( new Ingredient("Coffee", 3 ) );
        r.addIngredient( new Ingredient("Milk", 4 ) );
        r.addIngredient( new Ingredient("Sugar", 8 ) );
        r.setPrice( 10 );
        r.setName( "Mocha" );

        mvc.perform( MockMvcRequestBuilders.put( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r ) ) ).andExpect( status().isOk() );

    }

    @Test
    @Transactional
    public void testRecipeAPI () throws Exception {

        service.deleteAll();

        final Recipe recipe = new Recipe();
        recipe.setName( "Delicious Not-Coffee" );
        recipe.addIngredient( new Ingredient("Chocolate", 10 ) );
        recipe.addIngredient( new Ingredient("Coffee", 1 ) );
        recipe.addIngredient( new Ingredient("Milk", 20 ) );
        recipe.addIngredient( new Ingredient("Sugar", 5 ) );

        recipe.setPrice( 5 );

        mvc.perform( MockMvcRequestBuilders.put( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( recipe ) ) );

        Assertions.assertEquals( 1, (int) service.count() );

    }

    @Test
    @Transactional
    public void testAddRecipe2 () throws Exception {

        /* Tests a recipe with a duplicate name to make sure it's rejected */

        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
        final String name = "Coffee";
        final Recipe r1 = createRecipe( name, 50, 3, 1, 1, 0 );

        service.save( r1 );

        final Recipe r2 = createRecipe( name, 50, 3, 1, 1, 0 );
        mvc.perform( MockMvcRequestBuilders.put( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r2 ) ) ).andExpect( status().is4xxClientError() );

        Assertions.assertEquals( 1, service.findAll().size(), "There should only one recipe in the CoffeeMaker" );
    }

    @Test
    @Transactional
    public void testAddRecipe15 () throws Exception {

        /* Tests to make sure that our cap of 3 recipes is enforced */

        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = createRecipe( "Coffee", 50, 3, 1, 1, 0 );
        service.save( r1 );
        final Recipe r2 = createRecipe( "Mocha", 50, 3, 1, 1, 2 );
        service.save( r2 );
        final Recipe r3 = createRecipe( "Latte", 60, 3, 2, 2, 0 );
        service.save( r3 );

        Assertions.assertEquals( 3, service.count(),
                "Creating three recipes should result in three recipes in the database" );

        final Recipe r4 = createRecipe( "Hot Chocolate", 75, 0, 2, 1, 2 );

        mvc.perform( MockMvcRequestBuilders.put( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r4 ) ) ).andExpect( status().isInsufficientStorage() );

        Assertions.assertEquals( 3, service.count(), "Creating a fourth recipe should not get saved" );
    }

    private Recipe createRecipe ( final String name, final Integer price, final Integer coffee, final Integer milk,
            final Integer sugar, final Integer chocolate ) {
        final Recipe recipe = new Recipe();
        recipe.setName( name );
        recipe.setPrice( price );
        recipe.addIngredient( new Ingredient("Chocolate", chocolate ) );
        recipe.addIngredient( new Ingredient("Coffee", coffee ) );
        recipe.addIngredient( new Ingredient("Milk", milk ) );
        recipe.addIngredient( new Ingredient("Sugar", sugar ) );

        return recipe;
    }

    @Test
    @Transactional
    public void testEditRecipe () throws Exception {

        String recipe = mvc.perform( get( "/api/v1/recipes" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();

        // makes sure the recipe object is created
        if ( !recipe.contains( "Mocha" ) ) {
            final Recipe r = new Recipe();
            r.addIngredient( new Ingredient("Chocolate", 5 ) );
            r.addIngredient( new Ingredient("Coffee", 5 ) );
            r.addIngredient( new Ingredient("Milk", 5 ) );
            r.addIngredient( new Ingredient("Sugar", 3 ) );
            r.setPrice( 8 );
            r.setName( "Mocha" );

            mvc.perform( MockMvcRequestBuilders.put( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                    .content( TestUtils.asJsonString( r ) ) ).andExpect( status().isOk() );

        }
        recipe = mvc.perform( get( "/api/v1/recipes" ) ).andDo( print() ).andExpect( status().isOk() ).andReturn()
                .getResponse().getContentAsString();
        assertTrue( recipe.contains( "Mocha" ) );

        final Recipe r2 = new Recipe();
        r2.addIngredient( new Ingredient("Chocolate", 5 ) );
        r2.addIngredient( new Ingredient("Coffee", 5 ) );
        r2.addIngredient( new Ingredient("Milk", 5 ) );
        r2.addIngredient( new Ingredient("Sugar", 1234 ) );
        r2.setPrice( 16 );
        r2.setName( "Mocha" );
        
        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r2 ) ) ).andExpect( status().isOk() );

        recipe = mvc.perform( get( "/api/v1/recipes" ) ).andDo( print() ).andExpect( status().isOk() ).andReturn()
                .getResponse().getContentAsString();
        assertTrue( recipe.contains( "Mocha" ) );
        assertTrue( recipe.contains( "1234" ) );
        assertTrue( recipe.contains( "16" ) );

    }

    @Test
    @Transactional
    public void testDeleteRecipeAPI () throws Exception {

        String recipe = mvc.perform( get( "/api/v1/recipes" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();

        // makes sure the recipe object is created
        if ( !recipe.contains( "Mocha" ) ) {
            final Recipe r = new Recipe();
            r.addIngredient( new Ingredient("Chocolate", 5 ) );
            r.addIngredient( new Ingredient("Coffee", 5 ) );
            r.addIngredient( new Ingredient("Milk", 5 ) );
            r.addIngredient( new Ingredient("Sugar", 3 ) );
            r.setPrice( 8 );
            r.setName( "Mocha" );

            mvc.perform( MockMvcRequestBuilders.put( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                    .content( TestUtils.asJsonString( r ) ) ).andExpect( status().isOk() );

        }
        recipe = mvc.perform( get( "/api/v1/recipes" ) ).andDo( print() ).andExpect( status().isOk() ).andReturn()
                .getResponse().getContentAsString();
        assertTrue( recipe.contains( "Mocha" ) );

        // testing Delete Recipe function
        if ( recipe.contains( "Mocha" ) ) {
            mvc.perform( delete( "/api/v1/recipes/Mocha" ) ).andDo( print() ).andExpect( status().isOk() ).andReturn()
                    .getResponse().getContentAsString();
        }
        recipe = mvc.perform( get( "/api/v1/recipes" ) ).andDo( print() ).andExpect( status().isOk() ).andReturn()
                .getResponse().getContentAsString();

        assertFalse( recipe.contains( "Mocha" ) );

    }

}
