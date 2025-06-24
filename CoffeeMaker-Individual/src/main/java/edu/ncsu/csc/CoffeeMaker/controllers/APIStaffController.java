/**
 *
 */
package edu.ncsu.csc.CoffeeMaker.controllers;

import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.CoffeeMaker.models.ClientUser;
import edu.ncsu.csc.CoffeeMaker.models.Staff;
import edu.ncsu.csc.CoffeeMaker.services.SessionService;
import edu.ncsu.csc.CoffeeMaker.services.StaffService;

/**
 * @author benyu
 *
 */
@SuppressWarnings ( { "unchecked", "rawtypes" } )
@RestController
public class APIStaffController extends APIController {

    /**
     * StaffService object, to be autowired in by Spring to allow for
     * manipulating the Staff model
     */
    @Autowired
    private StaffService service;

    /**
     * SessionService object, to be autowired in by Spring to allow for
     * manipulating the Session model
     */
    @Autowired
    private SessionService sessionService;

    @Value("${auth.staffSignupKey}")
    private String staffSignupKey;

    /**
     * REST API method to provide GET access to all recipes in the system
     *
     * @return JSON representation of all recipies
     */
    @GetMapping ( BASE_PATH + "/staff" )
    public List<Staff> getAllStaff () {
        return service.findAll();
    }

    /**
     * REST API method to provide GET access to a specific recipe, as indicated
     * by the path variable provided (the name of the recipe desired)
     *
     * @param name
     *            recipe name
     * @return response to the request
     */
    @GetMapping ( BASE_PATH + "/staff/{username}" )
    public ResponseEntity getStaff ( @PathVariable ( "name" ) final String name ) {
        final Staff staff = service.findByName( name );
        return null == staff
                ? new ResponseEntity( errorResponse( "No staff found with name " + name ), HttpStatus.NOT_FOUND )
                : new ResponseEntity( staff, HttpStatus.OK );
    }

    /**
     * REST API method to provide POST access to the Ingredient model. This is
     * used to create a new Ingredient by automatically converting the JSON
     * RequestBody provided to a Ingredient object. Invalid JSON will fail.
     *
     * @param staff
     *            The valid Ingredient to be saved.
     * @return ResponseEntity indicating success if the Ingredient could be
     *         saved to the inventory, or an error if it could not be
     */
    @PutMapping ( BASE_PATH + "/staff" )
    public ResponseEntity createStaff ( @RequestBody final Staff staff, 
            HttpServletResponse response ) {
        final Staff temp = service.findByName( staff.getUsername() );
        if ( null != temp ) {
            return new ResponseEntity(
                    errorResponse( "Staff with the name " + staff.getUsername() + " already exists" ),
                    HttpStatus.CONFLICT );
        }

        if ( ! ( staff.getUserAdminPassword().equals( 
                staffSignupKey ) ) ) {
            return new ResponseEntity( errorResponse( "Incorrect admin password. Try again." ), HttpStatus.CONFLICT );
        }

        if ( staff.getPassword() == "" ) {
            return new ResponseEntity(
                    errorResponse( "Staff with name: " + staff.getUsername() + " has an invalid password" ),
                    HttpStatus.CONFLICT );
        }

        service.save( staff );
        return loginStaff(staff, response);
    }
    
    /**
     * Endpoint for staff authentication. Logs a user in with a Staff object and
     * sets a session cookie if successful.
     * 
     * @param staff    the staff user to attempt to authenticate
     * @param response HttpServletResponse used in setting cookie
     * @return
     */
    @PostMapping( BASE_PATH + "/staff/login" )
    public ResponseEntity loginStaff( @RequestBody final Staff staff, 
            HttpServletResponse response) {
        String token = service.authenticateStaff( staff.getUsername(), staff.getPassword() );
        if(token == null) return new ResponseEntity(errorResponse( "Invalid username or password." ), 
                HttpStatus.FORBIDDEN);
        Cookie cookie = new Cookie("coffeemaker-session", token);
        cookie.setPath( BASE_PATH );
        response.addCookie( cookie );
        ClientUser c = sessionService.findUser( token );
        return new ResponseEntity(c, HttpStatus.OK);

    }
}
