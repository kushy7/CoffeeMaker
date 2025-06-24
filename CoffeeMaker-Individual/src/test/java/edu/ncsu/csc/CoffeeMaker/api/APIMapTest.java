package edu.ncsu.csc.CoffeeMaker.api;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;
import org.springframework.validation.support.BindingAwareModelMap;

import edu.ncsu.csc.CoffeeMaker.controllers.MappingController;

public class APIMapTest {

    private MappingController controller;
    private Model             model;

    @BeforeEach
    void setUp () {
        controller = new MappingController();
        model = new BindingAwareModelMap();
    }

    @Test
    void testIndex () {
        final String result = controller.index( model );
        assertEquals( "index", result );
    }

    @Test
    void testLogin () {
        final String result = controller.login( model );
        assertEquals( "login", result );
    }

    @Test
    void testSignup () {
        final String result = controller.signup( model );
        assertEquals( "signup", result );
    }

    @Test
    void testCustomerIndex () {
        final String result = controller.customerIndex( model );
        assertEquals( "customer/index", result );
    }

    @Test
    void testCustomerLogin () {
        final String result = controller.customerLogin( model );
        assertEquals( "customer/login", result );
    }

    @Test
    void testCustomerOrder () {
        final String result = controller.customerOrder( model );
        assertEquals( "customer/order", result );
    }

    @Test
    void testStaffIndex () {
        final String result = controller.staffIndex( model );
        assertEquals( "staff/index", result );
    }

    @Test
    void testStaffLogin () {
        final String result = controller.staffLogin( model );
        assertEquals( "staff/login", result );
    }

    @Test
    void testStaffDeleteRecipe () {
        final String result = controller.staffDeleteRecipe( model );
        assertEquals( "staff/deleterecipe", result );
    }

    @Test
    void testStaffEditRecipe () {
        final String result = controller.staffEditRecipe( model );
        assertEquals( "staff/editrecipe", result );
    }

    @Test
    void testStaffRecipe () {
        final String result = controller.staffRecipe( model );
        assertEquals( "staff/recipe", result );
    }

}
