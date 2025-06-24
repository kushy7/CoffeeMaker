package edu.ncsu.csc.CoffeeMaker.controllers;

import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.CoffeeMaker.models.ClientUser;
import edu.ncsu.csc.CoffeeMaker.models.Customer;
import edu.ncsu.csc.CoffeeMaker.services.CustomerService;
import edu.ncsu.csc.CoffeeMaker.services.SessionService;

/**
 * This is the controller that holds the REST endpoints that handle CRUD
 * operations for Recipes.
 *
 * Spring will automatically convert all of the ResponseEntity and List results
 * to JSON
 *
 * @author Lucas Getzen
 * @author Ben Yu
 * @author Quaker Schneider
 */
@SuppressWarnings ( { "unchecked", "rawtypes" } )
@RestController
public class APICustomerController extends APIController {

    /**
     * CustomerService object, to be autowired in by Spring to allow for
     * manipulating the Customer model
     */
    @Autowired
    private CustomerService service;

    /**
     * SessionService object, to be autowired in by Spring to allow for
     * manipulating the Session model
     */
    @Autowired
    private SessionService sessionService;

    /**
     * REST API method to provide GET access to all recipes in the system
     *
     * @return JSON representation of all Users
     */
    @GetMapping ( BASE_PATH + "/customers" )
    public List<Customer> getUsers () {
        return service.findAll();
    }

    /**
     * REST API method to provide GET access to a specific user, as indicated by
     * the path variable provided (the name of the recipe desired)
     *
     * @param name
     *            user name
     * @return response to the request
     */
    @GetMapping ( BASE_PATH + "/customers/{name}" )
    public ResponseEntity getUser ( @PathVariable ( "name" ) final String name ) {
        final Customer customer = service.findByUsername( name );
        return null == customer
                ? new ResponseEntity( errorResponse( "No Customer found with username " + name ), HttpStatus.NOT_FOUND )
                : new ResponseEntity( customer, HttpStatus.OK );
    }

    /**
     * REST API method to provide POST access to the user model. This is used to
     * create a new user by automatically converting the JSON RequestBody
     * provided to a user object. Invalid JSON will fail.
     *
     * @param user
     *            The valid User to be saved.
     * @return ResponseEntity indicating success if the User could be saved to
     *         the inventory, or an error if it could not be
     */
    @PutMapping ( BASE_PATH + "/customers" )
    public ResponseEntity createUser ( @RequestBody final Customer customer, final HttpServletResponse response ) {
        if ( null != service.findByUsername( customer.getName() ) ) {
            return new ResponseEntity( errorResponse( "User with the name " + customer.getName() + " already exists" ),
                    HttpStatus.CONFLICT );
        }
        if ( customer.getPassword() == "" ) {
            return new ResponseEntity(
                    errorResponse( "User with name: " + customer.getName() + " has an invalid password" ),
                    HttpStatus.CONFLICT );
        }
        service.save( customer );
        return loginCustomer(customer, response);
    }

    /**
     * Endpoint for customer authentication. Logs a user in with a Customer
     * object and sets a session cookie if successful.
     *
     * @param customer
     *            the customer to attempt to authenticate
     * @param response
     *            HttpServletResponse used in setting cookie
     * @return
     */
    @PostMapping ( BASE_PATH + "/customer/login" )
    public ResponseEntity loginCustomer ( @RequestBody final Customer customer, final HttpServletResponse response ) {
        final String token = service.authenticateCustomer( customer.getName(), customer.getPassword() );
        if ( token == null ) {
            return new ResponseEntity( errorResponse( "Invalid username or password." ), HttpStatus.FORBIDDEN );
        }
        final Cookie cookie = new Cookie( "coffeemaker-session", token );
        cookie.setPath( BASE_PATH );
        response.addCookie( cookie );
        ClientUser c = sessionService.findUser( token );
        return new ResponseEntity(c, HttpStatus.OK);
    }

}
