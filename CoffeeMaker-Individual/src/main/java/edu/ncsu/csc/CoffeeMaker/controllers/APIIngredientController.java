/**
 *
 */
package edu.ncsu.csc.CoffeeMaker.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.services.IngredientService;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;

/**
 * @author Yi Zhang
 *
 */
@SuppressWarnings ( { "unchecked", "rawtypes" } )
@RestController
public class APIIngredientController extends APIController {
    
    /**
     * IngredientService object, to be autowired in by Spring to allow for
     * manipulating the Recipe model
     */
    @Autowired
    private IngredientService service;
    
    /**
     * InventoryService object, to be autowired in by Spring to allow for
     * manipulating the Inventory model
     */
    @Autowired
    private InventoryService inventoryService;

    /**
     * REST API method to provide GET access to all ingredients in the system
     *
     * @return JSON representation of all ingredients
     */
    @GetMapping ( BASE_PATH + "/ingredients" )
    public List<Ingredient> getIngredients () {
        return service.findAll();
    }

    /**
     * REST API method to provide GET access to a specific ingredient, as
     * indicated by the path variable provided (the name of the ingredient
     * desired)
     *
     * @param name
     *            ingredient name
     * @return response to the request
     */
    @GetMapping ( BASE_PATH + "/ingredients/{name}" )
    public ResponseEntity getIngredient ( @PathVariable final String name ) {
        final Ingredient ingr = service.findByName( name );
        return null == ingr
                ? new ResponseEntity( errorResponse( "No ingredient found with name " + name ), HttpStatus.NOT_FOUND )
                : new ResponseEntity( ingr, HttpStatus.OK );
    }

    /**
     * REST API method to provide POST access to the Ingredient model. This is
     * used to create a new Ingredient by automatically converting the JSON
     * RequestBody provided to a Ingredient object. Invalid JSON will fail.
     *
     * @param ingredient
     *            The valid Ingredient to be saved.
     * @return ResponseEntity indicating success if the Ingredient could be
     *         saved to the inventory, or an error if it could not be
     */
    @PostMapping ( BASE_PATH + "/ingredients" )
    public ResponseEntity createIngredient ( @RequestBody final Ingredient ingredient ) {
        final Ingredient ingr = service.findByName( ingredient.getName() );
        Inventory inv = inventoryService.getInventory();
        if ( null != ingr ) {
            return new ResponseEntity(
                    errorResponse( "Ingredient with the name " + ingredient.getName() + " already exists" ),
                    HttpStatus.CONFLICT );
        }
        inv.addIngredient( ingredient );
        inventoryService.save(inv);
        service.save( ingredient );
        return new ResponseEntity( successResponse( ingredient.getName() + " successfully created" ),
                HttpStatus.OK );
    }

    /**
     * REST API method to allow deleting an Ingredient from the CoffeeMaker's
     * Inventory, by making a DELETE request to the API endpoint and indicating
     * the ingredient to delete (as a path variable)
     *
     * @param name
     *            The name of the Ingredient to delete
     * @return Success if the ingredient could be deleted; an error if the
     *         ingredient does not exist
     */
    @DeleteMapping ( BASE_PATH + "/ingredients/{name}" )
    public ResponseEntity deleteIngredient ( @PathVariable final String name ) {
        final Ingredient ingr = service.findByName( name );
        Inventory inv = inventoryService.getInventory();
        if ( ingr == null ) {
            return new ResponseEntity( errorResponse( "No ingredient found for name " + name ), HttpStatus.NOT_FOUND );
        }
        inv.setIngredient( name, 0 );
        inventoryService.save(inv);
        service.delete( ingr );
        return new ResponseEntity( successResponse( name + " was deleted successfully" ), HttpStatus.OK );

    }

    /**
     * REST API endpoint to provide update access to CoffeeMaker's singleton
     * Ingredient. This will update the Inventory of the CoffeeMaker by adding
     * amounts from the Ingredient
     *
     * @param ingredient
     *            amounts to add to inventory
     * @return response to the request
     */
    @PutMapping ( BASE_PATH + "/ingredient/{name}" )
    public ResponseEntity updateIngredient ( @RequestBody final Ingredient ingredient ) {
        final Ingredient ingr = service.findByName( ingredient.getName() );
        Inventory inv = inventoryService.getInventory();
        if ( ingr == null ) {
            return new ResponseEntity( HttpStatus.NOT_FOUND );
        }

        ingr.setAmount( ingredient.getAmount() );
        ingr.setName( ingredient.getName() );
        inv.setIngredient( ingredient );

        try {
            inventoryService.save(inv);
            service.save( ingr );
            return new ResponseEntity( HttpStatus.OK );
        }
        catch ( final Exception e ) {
            return new ResponseEntity( HttpStatus.BAD_REQUEST );
        }
    }

}
