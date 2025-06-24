package edu.ncsu.csc.CoffeeMaker.unit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import edu.ncsu.csc.CoffeeMaker.models.Ingredient;

class IngredientTest {

    @Test
    public void testGetIngredient () {

        final Ingredient ingredient = new Ingredient( "Milk", 5 );

        Assertions.assertEquals( ingredient.getName(), "Milk" );
    }

    @Test
    public void testSetIngredient () {

        final Ingredient ingredient = new Ingredient( "Milk", 5 );
        ingredient.setName( "PUMPKIN_SPICE" );

        Assertions.assertEquals( ingredient.getName(), "PUMPKIN_SPICE" );
        Assertions.assertFalse( ingredient.getName() == "Milk" );

    }

    @Test
    public void testGetAmount () {

        final Ingredient ingredient = new Ingredient( "Milk", 5 );

        Assertions.assertEquals( ingredient.getAmount(), 5 );
    }

    @Test
    public void testSetAmount () {

        final Ingredient ingredient = new Ingredient( "Milk", 5 );
        ingredient.setAmount( 10 );

        Assertions.assertFalse( ingredient.getAmount() == 5 );
        Assertions.assertEquals( ingredient.getAmount(), 10 );
    }

}
