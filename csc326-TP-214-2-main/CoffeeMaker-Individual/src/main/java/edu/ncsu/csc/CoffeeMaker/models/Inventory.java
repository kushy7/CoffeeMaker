package edu.ncsu.csc.CoffeeMaker.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Inventory for the coffee maker. Inventory is tied to the database using
 * Hibernate libraries. See InventoryRepository and InventoryService for the
 * other two pieces used for database support.
 *
 * @author Kai Presler-Marshall
 * @author Quaker Schneider
 */
@Entity
public class Inventory extends DomainObject {

    /** id for inventory entry */
    @Id
    @GeneratedValue
    private Long    id;

    /** List of ingredients */
    @ElementCollection
    private Map<String, Integer> ingredients = new HashMap<String, Integer>();

    /**
     * Empty constructor for Hibernate
     */
    public Inventory () {
    }

    /**
     * Use this to create inventory with specified ingredients in store
     *
     * @param ingredientsIn
     *            The list of ingredients to instantiate the inventory with
     */
    public Inventory ( final Map<String, Integer> ingredientsIn ) {
        setIngredients(ingredientsIn);
    }

    /**
     * Returns the ID of the entry in the DB
     *
     * @return long
     */
    @Override
    public Serializable getId () {
        return id;
    }

    /**
     * Set the ID of the Inventory (Used by Hibernate)
     *
     * @param id
     *            the ID
     */
    public void setId ( final Long id ) {
        this.id = id;
    }

    /**
     * Sets the ingredients stored in the inventory object
     *
     * @param ingredientsIn
     *            the list of ingredients to update the inventory with
     */
    public void setIngredients ( final Map<String, Integer> ingredientsIn ) {
        for(Entry<String, Integer> ingredient : ingredientsIn.entrySet()) {
            setIngredient(ingredient.getKey(), ingredient.getValue());
        }
    }

    /**
     * Gets a the ingredients currently in the inventory as a collection.
     * 
     * @return a the ingredients currently in the inventory as a collection.
     */
    public Map<String, Integer> getIngredients () {
        return this.ingredients;
    }

    /**
     * Returns the current amount of an ingredient with the given name, or
     * null if it doesn't exist.
     * 
     * @param name the name of the ingredient
     *
     * @return the current amount of an ingredient with the given name, or
     *         null if it doesn't exist.
     */
    public Integer findByName ( final String name ) {
        return this.ingredients.get( name );
    }

    /**
     * Updates the given ingredient in the inventory
     *
     * @param ingredientIn
     *            the new version of the ingredient in this inventory
     */
    public void setIngredient ( final Ingredient ingredientIn ) {
        this.ingredients.put( ingredientIn.getName(), 
                ingredientIn.getAmount() );
    }

    /**
     * Updates the given ingredient in the inventory
     *
     * @param name   the name to set
     * @param amount the new amount of that ingredient
     */
    public void setIngredient ( final String name, final Integer amount ) {
        this.ingredients.put( name, amount );
    }

    /**
     * Returns true if there are enough ingredients to make the beverage.
     *
     * @param r
     *            recipe to check if there are enough ingredients
     * @return true if enough ingredients to make the beverage
     */
    public boolean enoughIngredients ( final Recipe r ) {
        for(final Ingredient ingredient : r.getIngredients()) {
            final Integer match = this.ingredients.get( 
                    ingredient.getName() );
            if(match == null || match < ingredient.getAmount())
                return false;
        }
        return true;
    }

    /**
     * Adds ingredients to the inventory
     *
     * @param ingredientsIn the ingredients to add
     * @return true if successful, false if not
     */
    public boolean addIngredients ( final List<Ingredient> ingredientsIn ) {
        for(Ingredient ingredient : ingredientsIn) {
            addIngredient(ingredient);
        }
        return true;
    }

    /**
     * Adds ingredients to the inventory
     *
     * @param ingredientsIn the ingredients to add
     * @return true if successful, false if not
     */
    public boolean addIngredients ( final Map<String, Integer> ingredientsIn ) {
        for(Entry<String, Integer> ingredient : ingredientsIn.entrySet()) {
            addIngredient(ingredient.getKey(), ingredient.getValue());
        }
        return true;
    }

    /**
     * Removes the ingredients used to make the specified recipe. Checks 
     * that there are enough ingredients to make the given recipe.
     *
     * @param r
     *            recipe to make
     * @return true if recipe is made.
     */
    public boolean useIngredients ( final Recipe r ) {
        if ( enoughIngredients( r ) ) {
            for(Ingredient ingredient : r.getIngredients()) {
                removeIngredient(ingredient.getName(), 
                        ingredient.getAmount());
            }
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Removes a single ingredient amount from the inventory, if available
     * in the inventory.
     *
     * @param name   the ingredient to remove
     * @param amount the amount to remove
     * 
     * @return true if successful, false if not (ingredient wasn't found or not
     *         enough available to remove)
     */
    public boolean removeIngredient ( final String name, final int amount ) {
        Integer match = this.ingredients.get( name );
        if(match == null || match < amount)
            return false;
        if(match - amount == 0) {
            this.ingredients.remove( name );
        } else {
            this.ingredients.put( name, match - amount );
        }
        return true;
    }

    /**
     * Adds a single ingredient instance to the inventory, stacking with
     * ingredients of the same name (combining amounts).
     *
     * @param name
     *            the name to add to
     * @param amount
     *            the amount to add
     *            
     * @return true if successful, false if not
     */
    public boolean addIngredient ( final String name, final Integer amount ) {
        Integer match = this.ingredients.get( name );
        if(match == null) {
            this.ingredients.put( 
                    name, 
                    amount );
        } else {
            this.ingredients.put( 
                    name, 
                    match + amount );
        }
        return true;
    }

    /**
     * Adds a single ingredient instance to the inventory, stacking with
     * ingredients of the same name (combining amounts).
     *
     * @param ingredientIn
     *            the ingredient to add
     * @return true if successful, false if not
     */
    public boolean addIngredient ( final Ingredient ingredientIn ) {
        Integer match = this.ingredients.get( ingredientIn.getName() );
        if(match == null) {
            this.ingredients.put( 
                    ingredientIn.getName(), 
                    ingredientIn.getAmount() );
        } else {
            this.ingredients.put( 
                    ingredientIn.getName(), 
                    match + ingredientIn.getAmount() );
        }
        return true;
    }

    /**
     * Returns a string describing the current contents of the inventory.
     *
     * @return String
     */
    @Override
    public String toString () {
        final StringBuffer buf = new StringBuffer();
        for(Entry<String, Integer> ingredient : this.ingredients.entrySet()) {
            buf.append( ingredient.getKey() );
            buf.append( ": " );
            buf.append( ingredient.getValue() );
            buf.append( "\n" );
        }
        return buf.toString();
    }

}
