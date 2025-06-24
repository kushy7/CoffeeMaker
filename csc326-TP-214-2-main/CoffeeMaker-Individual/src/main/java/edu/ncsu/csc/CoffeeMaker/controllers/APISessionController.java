package edu.ncsu.csc.CoffeeMaker.controllers;

import java.io.IOException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.CoffeeMaker.models.ClientUser;
import edu.ncsu.csc.CoffeeMaker.models.UserType;
import edu.ncsu.csc.CoffeeMaker.services.SessionService;

/**
 * This is the controller that holds the REST endpoints that handle auth and sessions.
 *
 * Spring will automatically convert all of the ResponseEntity and List results
 * to JSON
 *
 * @author Quaker Schneider
 */
@SuppressWarnings ( { "unchecked", "rawtypes" } )
@RestController
public class APISessionController extends APIController {

    /**
     * SessionService object, to be autowired in by Spring to allow for
     * manipulating the Session model
     */
    @Autowired
    private SessionService service;

    @GetMapping( BASE_PATH + "/user" )
    public ResponseEntity getClientUser(@CookieValue(
            name = "coffeemaker-session", 
            defaultValue = "") String session) {
        if(session.isEmpty()) {
            return new ResponseEntity( errorResponse( "No session token provided." ), 
                    HttpStatus.UNAUTHORIZED);
        }
        ClientUser c = service.findUser( session );
        if(c == null) 
            return new ResponseEntity( errorResponse( "No user found." ), 
                    HttpStatus.NOT_FOUND );
        return new ResponseEntity(c, HttpStatus.OK);    

    }

    @GetMapping( BASE_PATH + "/logout" )
    public ResponseEntity logoutUser(@CookieValue(
            name = "coffeemaker-session", 
            defaultValue = "") String session, final HttpServletResponse response ) {
        service.endSession(session);
        final Cookie cookie = new Cookie( "coffeemaker-session", "" );
        cookie.setPath( BASE_PATH );
        response.addCookie( cookie );
        try {
            response.sendRedirect( "/index");
        }
        catch ( IOException e ) {
            e.printStackTrace();
        }
        return new ResponseEntity( HttpStatus.OK ); 

    }

}
