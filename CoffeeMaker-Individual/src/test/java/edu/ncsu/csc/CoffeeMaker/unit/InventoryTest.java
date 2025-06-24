package edu.ncsu.csc.CoffeeMaker.unit;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;

@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class InventoryTest {

    @Autowired
    private InventoryService inventoryService;

    private Inventory        inventory;

    @BeforeEach
    public void setup () {
        final Inventory ivt = inventoryService.getInventory();
        inventory = new Inventory();

        final Map<String, Integer> ing = new HashMap<String, Integer>();
        ing.put( "Frog", 50 );
        ing.put( "Newt's Eye", 50 );
        ing.put( "Essence of Vitriol", 2 );
        ivt.setIngredients( ing );

        inventoryService.save( ivt );
    }

    @Test
    @Transactional
    public void testConsumeInventory () {
        final Inventory i = inventoryService.getInventory();

        final Recipe recipe = new Recipe();
        recipe.setName( "Normal Cappuccino" );
        recipe.addIngredient( new Ingredient( "Newt's Eye", 2 ) );
        recipe.addIngredient( new Ingredient( "Essence of Vitriol", 2 ) );
        recipe.addIngredient( new Ingredient( "Frog", 5 ) );
        recipe.setPrice( 5 );

        i.useIngredients( recipe );

        /*
         * Make sure that all of the inventory fields are now properly updated
         */

        Assertions.assertEquals( 48, (int) i.findByName( "Newt's Eye" ) );
        Assertions.assertEquals( 45, (int) i.findByName( "Frog" ) );
        Assertions.assertNull( i.findByName( "Essence of Vitriol" ) );
    }

    @Test
    @Transactional
    public void testAddInventory () {
        Inventory ivt = inventoryService.getInventory();

        final List<Ingredient> ing = new ArrayList<Ingredient>();
        ing.add( new Ingredient( "Frog", 450 ) );
        ing.add( new Ingredient( "Newt's Eye", 450 ) );
        ing.add( new Ingredient( "Essence of Vitriol", 498 ) );

        ivt.addIngredients( ing );

        /* Save and retrieve again to update with DB */
        inventoryService.save( ivt );

        ivt = inventoryService.getInventory();

        Assertions.assertEquals( 500, (int) ivt.findByName( "Frog" ) );
        Assertions.assertEquals( 500, (int) ivt.findByName( "Newt's Eye" ) );
        Assertions.assertEquals( 500, (int) ivt.findByName( "Essence of Vitriol" ) );

    }

    // Tests the toString method in the Inventory class
    @Test
    @Transactional
    public void testToString () {
        final Inventory ivt = inventoryService.getInventory();
        final String buf = ivt.toString();
        final String[] lines = buf.split( "\n" );
        boolean newtFound = false;
        boolean frogFound = false;
        boolean essFound = false;
        for ( int i = 0; i < lines.length; ++i ) {
            if ( lines[i].equals( "Newt's Eye: 50" ) ) {
                if ( newtFound ) {
                    fail( "Duplicate newt's eye in string" );
                }
                else {
                    newtFound = true;
                }
            }
            else if ( lines[i].equals( "Frog: 50" ) ) {
                if ( frogFound ) {
                    fail( "Duplicate frog in string" );
                }
                else {
                    frogFound = true;
                }
            }
            else if ( lines[i].equals( "Essence of Vitriol: 2" ) ) {
                if ( essFound ) {
                    fail( "Duplicate essence in string" );
                }
                else {
                    essFound = true;
                }
            }
            else {
                fail( "Unexpected line in toString: [" + lines[i] + "]" );
            }
        }
        assertTrue( frogFound );
        assertTrue( newtFound );
        assertTrue( essFound );
    }

}
