package edu.ncsu.csc.CoffeeMaker.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.List;

import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import edu.ncsu.csc.CoffeeMaker.TestConfig;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class RecipeTest {

    @Autowired
    private RecipeService service;

    @BeforeEach
    public void setup () {
        service.deleteAll();
    }

    @Test
    @Transactional
    public void testAddRecipe () {

        final Recipe r1 = new Recipe();
        r1.setName( "Black Coffee" );
        r1.setPrice( 1 );
        r1.addIngredient( new Ingredient( "coffee", 1 ) );
        r1.addIngredient( new Ingredient( "milk", 0 ) );
        r1.addIngredient( new Ingredient( "sugar", 0 ) );
        r1.addIngredient( new Ingredient( "chocolate", 0 ) );
        service.save( r1 );

        final Recipe r2 = new Recipe();
        r2.setName( "Mocha" );
        r2.setPrice( 1 );
        r2.addIngredient( new Ingredient( "coffee", 1 ) );
        r2.addIngredient( new Ingredient( "milk", 1 ) );
        r2.addIngredient( new Ingredient( "sugar", 1 ) );
        r2.addIngredient( new Ingredient( "chocolate", 1 ) );
        service.save( r2 );

        final List<Recipe> recipes = service.findAll();
        Assertions.assertEquals( 2, recipes.size(),
                "Creating two recipes should result in two recipes in the database" );

        Assertions.assertEquals( r1, recipes.get( 0 ), "The retrieved recipe should match the created one" );
    }

    @Test
    @Transactional
    public void testAddRecipe1 () {

        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
        final String name = "Coffee";
        final Recipe r1 = createRecipe( name, 50, 3, 1, 1, 0 );

        service.save( r1 );

        Assertions.assertEquals( 1, service.findAll().size(), "There should only one recipe in the CoffeeMaker" );
        Assertions.assertNotNull( service.findByName( name ) );

    }

    /* Test2 is done via the API for different validation */

    @Test
    @Transactional
    public void testAddRecipe3 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
        final String name = "Coffee";
        final Recipe r1 = createRecipe( name, -50, 3, 1, 1, 0 );

        try {
            service.save( r1 );

            Assertions.assertNull( service.findByName( name ),
                    "A recipe was able to be created with a negative price" );
        }
        catch ( final ConstraintViolationException cvee ) {
            // expected
        }

    }

    @Test
    @Transactional
    public void testAddRecipe4 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
        final String name = "Coffee";
        final Recipe r1;
        boolean hugeGiantExplosionScaryShouldNotBeTrue = true;
        try {
            r1 = createRecipe( name, 50, -3, 1, 1, 2 );
        }
        catch ( final IllegalArgumentException e ) {
            hugeGiantExplosionScaryShouldNotBeTrue = false;
        }
        assertFalse( "Recipe instantiation failed to throw " + "IllegalArgumentException on negative amount",
                hugeGiantExplosionScaryShouldNotBeTrue );

    }

    @Test
    @Transactional
    public void testAddRecipe5 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = createRecipe( "Coffee", 50, 3, 1, 1, 0 );
        service.save( r1 );
        final Recipe r2 = createRecipe( "Mocha", 50, 3, 1, 1, 2 );
        service.save( r2 );

        Assertions.assertEquals( 2, service.count(),
                "Creating two recipes should result in two recipes in the database" );

    }

    @Test
    @Transactional
    public void testAddRecipe6 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = createRecipe( "Coffee", 50, 3, 1, 1, 0 );
        service.save( r1 );
        final Recipe r2 = createRecipe( "Mocha", 50, 3, 1, 1, 2 );
        service.save( r2 );
        final Recipe r3 = createRecipe( "Latte", 60, 3, 2, 2, 0 );
        service.save( r3 );

        Assertions.assertEquals( 3, service.count(),
                "Creating three recipes should result in three recipes in the database" );

    }

    @Test
    @Transactional
    public void testDeleteRecipe1 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = createRecipe( "Coffee", 50, 3, 1, 1, 0 );
        service.save( r1 );

        Assertions.assertEquals( 1, service.count(), "There should be one recipe in the database" );

        service.delete( r1 );
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
    }

    @Test
    @Transactional
    public void testDeleteRecipe2 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = createRecipe( "Coffee", 50, 3, 1, 1, 0 );
        service.save( r1 );
        final Recipe r2 = createRecipe( "Mocha", 50, 3, 1, 1, 2 );
        service.save( r2 );
        final Recipe r3 = createRecipe( "Latte", 60, 3, 2, 2, 0 );
        service.save( r3 );

        Assertions.assertEquals( 3, service.count(), "There should be three recipes in the database" );

        service.deleteAll();

        Assertions.assertEquals( 0, service.count(), "`service.deleteAll()` should remove everything" );

    }

    @Test
    @Transactional
    public void testEditRecipe1 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = createRecipe( "Coffee", 50, 3, 1, 1, 0 );
        service.save( r1 );

        r1.setPrice( 70 );

        service.save( r1 );

        final Recipe retrieved = service.findByName( "Coffee" );

        Assertions.assertEquals( 70, (int) retrieved.getPrice() );
        // Assertions.assertEquals( 3, (int) retrieved.getCoffee() );
        // Assertions.assertEquals( 1, (int) retrieved.getMilk() );
        // Assertions.assertEquals( 1, (int) retrieved.getSugar() );
        // Assertions.assertEquals( 0, (int) retrieved.getChocolate() );

        Assertions.assertEquals( 1, service.count(), "Editing a recipe shouldn't duplicate it" );

    }

    /** Testing the functionality of the equals method */
    @SuppressWarnings ( "unlikely-arg-type" )
    @Test
    public void testEquals () {
        // Basis recipe
        final Recipe rOrig = createRecipe( "Coffee", 50, 3, 1, 1, 0 );
        // Copy
        final Recipe rDup = createRecipe( "Coffee", 50, 3, 1, 1, 0 );
        // Clone
        final Recipe rClone = rOrig;
        // Recipes with one different field
        final Recipe r1 = createRecipe( "Tea", 50, 3, 1, 1, 0 );

        // Since the name is the same they should be equal
        final Recipe r2 = createRecipe( "Coffee", 5, 3, 1, 1, 0 );
        final Recipe r3 = createRecipe( "Coffee", 50, 30, 1, 1, 0 );
        final Recipe r4 = createRecipe( "Coffee", 50, 3, 2, 1, 0 );
        final Recipe r5 = createRecipe( "Coffee", 50, 3, 1, 2, 0 );
        final Recipe r6 = createRecipe( "Coffee", 50, 3, 1, 1, 1 );
        // Different object
        final String name = "Coffee";

        // Compare to Clone and duplicate
        Assertions.assertTrue( rOrig.equals( rDup ) );
        Assertions.assertTrue( rOrig.equals( rClone ) );

        // Compare to recipes with different fields
        Assertions.assertFalse( rOrig.equals( r1 ) );

        // Compare to recipe with the same name
        Assertions.assertTrue( rOrig.equals( r2 ) );
        Assertions.assertTrue( rOrig.equals( r3 ) );
        Assertions.assertTrue( rOrig.equals( r4 ) );
        Assertions.assertTrue( rOrig.equals( r5 ) );
        Assertions.assertTrue( rOrig.equals( r6 ) );

        // Comparison to different object and null/null name
        Assertions.assertFalse( rOrig.equals( new Recipe() ) );
        Assertions.assertFalse( rOrig.equals( name ) );
        Assertions.assertFalse( rOrig.equals( null ) );

    }

    @Test
    public void testHashCode () {
        // Create two recipes objects with the same name
        final Recipe r1 = createRecipe( "Coffee", 50, 3, 1, 1, 0 );
        final Recipe r2 = createRecipe( "Coffee", 70, 3, 1, 2, 0 );

        // Check that the hash codes are equal
        assertEquals( r1.hashCode(), r2.hashCode() );
    }

    @Test
    /** Testing the functionality of the toString() method */
    public void testToString () {

        // Basis recipe
        final Recipe r1 = createRecipe( "Coffee", 50, 3, 1, 1, 0 );
        Assertions.assertEquals( "Coffee", r1.toString().substring( 0, 6 ) );

        // Edit the recipe
        r1.setName( "Mocha" );

        // Check that the toString is updated
        Assertions.assertEquals( "Mocha", r1.toString().substring( 0, 5 ) );
    }

    private Recipe createRecipe ( final String name, final Integer price, final Integer coffee, final Integer milk,
            final Integer sugar, final Integer chocolate ) {
        final Recipe recipe = new Recipe();
        recipe.setName( name );
        recipe.setPrice( price );
        recipe.addIngredient( new Ingredient( "coffee", coffee ) );
        recipe.addIngredient( new Ingredient( "milk", milk ) );
        recipe.addIngredient( new Ingredient( "sugar", sugar ) );
        recipe.addIngredient( new Ingredient( "chocolate", chocolate ) );
        return recipe;
    }

}
