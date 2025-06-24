package edu.ncsu.csc.CoffeeMaker.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.CoffeeMaker.models.ClientUser;
import edu.ncsu.csc.CoffeeMaker.models.Customer;
import edu.ncsu.csc.CoffeeMaker.models.CustomerOrder;
import edu.ncsu.csc.CoffeeMaker.models.UserType;
import edu.ncsu.csc.CoffeeMaker.services.OrderService;
import edu.ncsu.csc.CoffeeMaker.services.SessionService;
import edu.ncsu.csc.CoffeeMaker.services.CustomerService;

/**
 * This is the controller that holds the REST endpoints that handle CRUD
 * operations for Orders.
 *
 * Spring will automatically convert all of the ResponseEntity and List results
 * to JSON
 *
 * @author Lucas Getzen
 *
 */
@SuppressWarnings ( { "unchecked", "rawtypes" } )
@RestController
public class APIOrderController extends APIController {

    /**
     * OrderService object, to be autowired in by Spring to allow for
     * manipulating the Order model
     */
    @Autowired
    private OrderService service;

    /**
     * SessionService object, to be autowired in by Spring to allow for
     * manipulating the Session model
     */
    @Autowired
    private SessionService sessionService;

    /**
     * CustomerService object, to be autowired in by Spring to allow for
     * manipulating the Customer model
     */
    @Autowired
    private CustomerService customerService;

    /**
     * REST API method to provide GET access to all recipes in the system
     *
     * @param session 
     *            the session token
     * @return JSON representation of all orders
     */
    @GetMapping ( BASE_PATH + "/orders" )
    public ResponseEntity getOrders ( @CookieValue( 
            name = "coffeemaker-session", defaultValue = "") String session) {
        ClientUser user = sessionService.findUser( session );
        if(user == null) {
            return new ResponseEntity( HttpStatus.UNAUTHORIZED );
        }
        List<CustomerOrder> checked = service.findAll();
        List<CustomerOrder> out = new ArrayList<CustomerOrder>();
        if(user.type == UserType.CUSTOMER) {
            // Filter out other orders
            for(int i = 0; i < checked.size(); ++i) {
                if(checked.get( i ).customer.equals( user.username ))
                    out.add( checked.get( i ) );
            }
            return new ResponseEntity( out, HttpStatus.OK );
        }
        return new ResponseEntity( checked, HttpStatus.OK );
    }

    /**
     * REST API method to provide GET access to the number of orders
     *
     * @param session 
     *            the session token
     * @return response to the request
     */
    @GetMapping ( BASE_PATH + "/order/count" )
    public ResponseEntity getOrder ( @CookieValue( 
            name = "coffeemaker-session", defaultValue = "") String session) {
        ClientUser user = sessionService.findUser( session );
        if(user == null ) {
            return new ResponseEntity( HttpStatus.UNAUTHORIZED );
        }
        List<CustomerOrder> checked = service.findAll();
        return new ResponseEntity(checked.size(), HttpStatus.OK );
        
    }

    /**
     * REST API method to provide GET access to a specific Order, as indicated
     * by the path variable provided (the name of the Order desired)
     *
     * @param orderId
     *            Order name
     * @param session 
     *            the session token
     * @return response to the request
     */
    @GetMapping ( BASE_PATH + "/orders/{orderId}" )
    public ResponseEntity getOrder ( @PathVariable ( "orderId" ) final String orderId,
            @CookieValue( name = "coffeemaker-session", defaultValue = "") String session) {
        ClientUser user = sessionService.findUser( session );
        if(user == null ) {
            return new ResponseEntity( HttpStatus.UNAUTHORIZED );
        }

        final CustomerOrder order = service.findByOrderId( orderId );
        if(user.type == UserType.CUSTOMER &&
           !order.customer.equals(sessionService.findUser( session ).username)) {
            return new ResponseEntity( HttpStatus.UNAUTHORIZED );
        }
        return null == order
                ? new ResponseEntity( errorResponse( "No order found with id " + orderId ), HttpStatus.NOT_FOUND )
                : new ResponseEntity( order, HttpStatus.OK );
    }

    /**
     * REST API method to provide POST access to the Order model. This is used
     * to create a new Order by automatically converting the JSON RequestBody
     * provided to a Order object. Invalid JSON will fail.
     *
     * @param order
     *            The valid Order to be saved.
     * @param session 
     *            the session token
     * @return ResponseEntity indicating success if the Order could be saved to
     *         the database, or an error if it could not be
     */
    @PostMapping ( BASE_PATH + "/orders" )
    public ResponseEntity createOrder ( @RequestBody final CustomerOrder order,
            @CookieValue( name = "coffeemaker-session", defaultValue = "") String session) {
        if(sessionService.findUser( session ) == null) {
            return new ResponseEntity( HttpStatus.UNAUTHORIZED );
        }
        if ( null != service.findByOrderId( order.getOrderId() ) ) {
            return new ResponseEntity( errorResponse( "Order with the id " + order.getOrderId() + " already exists" ),
                    HttpStatus.CONFLICT );
        }
        service.save( order );
        return new ResponseEntity( successResponse( order.getOrderId() + " successfully created" ), HttpStatus.OK );
    }

    @PutMapping ( BASE_PATH + "/orders" )
    public ResponseEntity updateOrder ( @RequestBody final CustomerOrder order,
            @CookieValue( name = "coffeemaker-session", defaultValue = "") String session) {
        ClientUser user = sessionService.findUser( session );
        if(user == null ) {
            return new ResponseEntity( HttpStatus.UNAUTHORIZED );
        }

        final CustomerOrder newOrder = service.findByOrderId( order.getOrderId() );
        if(user.type == UserType.CUSTOMER &&
           !newOrder.customer.equals(sessionService.findUser( session ).username)) {
            return new ResponseEntity( HttpStatus.UNAUTHORIZED );
        }
        newOrder.status = order.status;
        service.save( newOrder );

        return new ResponseEntity( successResponse( order.getOrderId() + " successfully updated" ), HttpStatus.OK );

    }

    /**
     * REST API method to allow deleting a Order from the CoffeeMaker's
     * database, by making a DELETE request to the API endpoint and indicating
     * the order to delete (as a path variable)
     *
     * @param orderId
     *            The id of the Order to delete
     * @param session 
     *            the session token
     * @return Success if the order could be deleted; an error if the order does
     *         not exist
     */
    @DeleteMapping ( BASE_PATH + "/orders/{orderId}" )
    public ResponseEntity deleteOrder ( @PathVariable final String orderId,
            @CookieValue( name = "coffeemaker-session", defaultValue = "") String session) {
        ClientUser user = sessionService.findUser( session );
        if(user == null || user.type != UserType.STAFF) {
            return new ResponseEntity( HttpStatus.UNAUTHORIZED );
        }
        final CustomerOrder order = service.findByOrderId( orderId );
        if ( null == order ) {
            return new ResponseEntity( errorResponse( "No order found for id " + orderId ), HttpStatus.NOT_FOUND );
        }
        service.delete( order );

        return new ResponseEntity( successResponse( orderId + " was deleted successfully" ), HttpStatus.OK );
    }
}
