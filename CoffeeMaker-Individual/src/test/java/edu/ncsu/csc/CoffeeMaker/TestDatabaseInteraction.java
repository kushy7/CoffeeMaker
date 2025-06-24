package edu.ncsu.csc.CoffeeMaker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class TestDatabaseInteraction {

    @Autowired
    private RecipeService recipeService;

    /**
     * Sets up the tests.
     */
    @BeforeEach
    public void setup () {
        recipeService.deleteAll();
    }

    /**
     * Tests the RecipeService class's ability to save and retrieve recipes
     */
    @Test
    @Transactional
    public void testRecipes () {

        // Create a new recipe to A/B test
        final Recipe r = new Recipe();

        // Set the recipe's fields
        r.setName( "Tiny Coffee" );
        r.setPrice( 1 );
        // r.setChocolate( 1 );
        // r.setCoffee( 1 );
        // r.setMilk( 1 );
        // r.setSugar( 1 );

        // Save the object to the database
        recipeService.save( r );

        // Get a list of saved recipes in the database
        final List<Recipe> dbRecipes = recipeService.findAll();

        // Check that there's only one recipe saved
        assertEquals( 1, dbRecipes.size() );

        // Get that single recipe
        final Recipe dbRecipe = dbRecipes.get( 0 );

        // Check that the original and the fetched version are the same
        assertEquals( r.getName(), dbRecipe.getName() );
        assertEquals( r.getPrice(), dbRecipe.getPrice() );
        // assertEquals( r.getChocolate(), dbRecipe.getChocolate() );
        // assertEquals( r.getCoffee(), dbRecipe.getCoffee() );
        // assertEquals( r.getMilk(), dbRecipe.getMilk() );
        // assertEquals( r.getSugar(), dbRecipe.getSugar() );
    }

    /**
     * Tests the RecipeService class's ability to get recipes by name
     */
    @Test
    @Transactional
    public void testGetRecipeByName () {

        // Create a new recipe to A/B test
        final Recipe r = new Recipe();

        // Set the recipe's fields
        r.setName( "Tiny Coffee" );
        r.setPrice( 1 );
        // r.setChocolate( 1 );
        // r.setCoffee( 1 );
        // r.setMilk( 1 );
        // r.setSugar( 1 );

        // Save the object to the database
        recipeService.save( r );

        // Get that recipe via the recipeService#findByName method
        final Recipe fetched = recipeService.findByName( "Tiny Coffee" );

        // Check that the original and the fetched version are the same
        assertEquals( r.getName(), fetched.getName() );
        assertEquals( r.getPrice(), fetched.getPrice() );
        // assertEquals( r.getChocolate(), fetched.getChocolate() );
        // assertEquals( r.getCoffee(), fetched.getCoffee() );
        // assertEquals( r.getMilk(), fetched.getMilk() );
        // assertEquals( r.getSugar(), fetched.getSugar() );
    }

    /**
     * Tests the RecipeService class's ability to get recipes by id
     */
    @Test
    @Transactional
    public void testGetRecipeById () {

        // Create a new recipe to A/B test
        final Recipe r = new Recipe();

        // Set the recipe's fields
        r.setName( "Big Coffee" );
        r.setPrice( 5 );
        // r.setChocolate( 5 );
        // r.setCoffee( 5 );
        // r.setMilk( 5 );
        // r.setSugar( 5 );

        // Save the object to the database
        recipeService.save( r );

        // Get that recipe via the recipeService#findById method
        final Recipe fetched = recipeService.findById( r.getId() );

        // Check that the original and the fetched version are the same
        assertEquals( r.getName(), fetched.getName() );
        // assertEquals( r.getMilk(), fetched.getMilk() );
        // assertEquals( r.getCoffee(), fetched.getCoffee() );
        // assertEquals( r.getChocolate(), fetched.getChocolate() );
        // assertEquals( r.getSugar(), fetched.getSugar() );
        assertEquals( r.getPrice(), fetched.getPrice() );
    }

    /**
     * Tests the Service class's getById via the RecipeService class
     */
    @Test
    @Transactional
    public void testGetRecipeByIdNull () {
        assertNull( recipeService.findById( 987654321L ) );
        assertNull( recipeService.findById( null ) );
    }

    /**
     * Tests the RecipeService class's ability to delete recipes
     */
    @Test
    @Transactional
    public void testDeleteRecipe () {

        // Create a new recipe to A/B test
        final Recipe r = new Recipe();

        // Set the recipe's fields
        r.setName( "Tiny Coffee" );
        r.setPrice( 1 );
        // r.setChocolate( 1 );
        // r.setCoffee( 1 );
        // r.setMilk( 1 );
        // r.setSugar( 1 );

        // Save the object to the database
        recipeService.save( r );

        // Get that recipe via the recipeService#findByName method
        final Recipe fetched = recipeService.findByName( "Tiny Coffee" );

        // Check that the original and the fetched version are the same for now
        assertEquals( r.getName(), fetched.getName() );
        assertEquals( r.getPrice(), fetched.getPrice() );
        // assertEquals( r.getChocolate(), fetched.getChocolate() );
        // assertEquals( r.getCoffee(), fetched.getCoffee() );
        // assertEquals( r.getMilk(), fetched.getMilk() );
        // assertEquals( r.getSugar(), fetched.getSugar() );

        // Alter the fetched version
        recipeService.delete( fetched );

        // Ensure that the recipe doesn't exist in the database anymore
        assertNull( recipeService.findByName( "Tiny Coffee" ) );

        // Get a list of saved recipes in the database
        final List<Recipe> dbRecipes = recipeService.findAll();

        // Double check deletion -- that there's no recipes saved
        assertEquals( 0, dbRecipes.size() );

    }

    /**
     * Tests the RecipeService class's ability to edit recipes
     */
    @Test
    @Transactional
    public void testEditRecipe () {

        // Create a new recipe to A/B test
        final Recipe r = new Recipe();

        // Set the recipe's fields
        r.setName( "Tiny Coffee" );
        r.setPrice( 1 );
        // r.setChocolate( 1 );
        // r.setCoffee( 1 );
        // r.setMilk( 1 );
        // r.setSugar( 1 );

        // Save the object to the database
        recipeService.save( r );

        // Get that recipe via the recipeService#findByName method
        final Recipe fetched = recipeService.findByName( "Tiny Coffee" );

        // Check that the original and the fetched version are the same for now
        assertEquals( r.getName(), fetched.getName() );
        assertEquals( r.getPrice(), fetched.getPrice() );
        // assertEquals( r.getChocolate(), fetched.getChocolate() );
        // assertEquals( r.getCoffee(), fetched.getCoffee() );
        // assertEquals( r.getMilk(), fetched.getMilk() );
        // assertEquals( r.getSugar(), fetched.getSugar() );

        // // Alter the fetched version
        // fetched.setCoffee( 100 );

        // Save this fetched version back to the database
        recipeService.save( fetched );

        // Ensure that the fetched and the original are now in sync
        // with new values
        // assertEquals( r.getCoffee(), fetched.getCoffee() );
        // assertEquals( (Integer) 100, fetched.getCoffee() );

        // Get a list of saved recipes in the database
        final List<Recipe> dbRecipes = recipeService.findAll();

        // Check that there's only one recipe saved
        assertEquals( 1, dbRecipes.size() );

        // Get that single recipe
        final Recipe fetched2 = dbRecipes.get( 0 );

        // Ensure that this second fetch is identical to the first (updated) one
        assertEquals( fetched.getName(), fetched2.getName() );
        assertEquals( fetched.getPrice(), fetched2.getPrice() );
        // assertEquals( fetched.getChocolate(), fetched2.getChocolate() );
        // assertEquals( fetched.getCoffee(), fetched2.getCoffee() );
        // assertEquals( fetched.getMilk(), fetched2.getMilk() );
        // assertEquals( fetched.getSugar(), fetched2.getSugar() );

    }
}
