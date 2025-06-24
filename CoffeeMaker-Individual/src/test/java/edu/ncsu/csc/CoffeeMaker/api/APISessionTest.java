package edu.ncsu.csc.CoffeeMaker.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import edu.ncsu.csc.CoffeeMaker.controllers.APISessionController;
import edu.ncsu.csc.CoffeeMaker.models.ClientUser;
import edu.ncsu.csc.CoffeeMaker.services.SessionService;

@RunWith ( MockitoJUnitRunner.class )
@WebAppConfiguration
@ContextConfiguration ( classes = { WebApplicationContext.class } )
public class APISessionTest {

    @InjectMocks
    private APISessionController sessionController;

    @Mock
    private SessionService       sessionService;

    private MockMvc              mockMvc;

    @Before
    public void setUp () {
        mockMvc = MockMvcBuilders.standaloneSetup( sessionController ).build();
    }

    @Test
    public void testGetClientUser () {
        // Mock session ID value
        final String sessionId = "testSessionId";

        // Mock client user
        final ClientUser clientUser = new ClientUser();
        // clientUser.setId( 1 );
        // clientUser.setUsername( "testUsername" );
        // clientUser.setEmail( "testEmail@test.com" );

        // Mock session service response when findUser method is called
        when( sessionService.findUser( sessionId ) ).thenReturn( clientUser );

        // Invoke controller method and verify response
        final ResponseEntity response = sessionController.getClientUser( sessionId );
        assertEquals( HttpStatus.OK, response.getStatusCode() );
        assertEquals( clientUser, response.getBody() );
    }

    @Test
    public void testFindUserInvalidToken () {
        final var user = sessionService.findUser( "invalid token" );
        assertNull( user );
    }
}
