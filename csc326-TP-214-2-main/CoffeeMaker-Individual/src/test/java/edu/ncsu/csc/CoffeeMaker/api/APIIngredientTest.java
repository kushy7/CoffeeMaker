package edu.ncsu.csc.CoffeeMaker.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import edu.ncsu.csc.CoffeeMaker.common.TestUtils;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.services.IngredientService;

@RunWith ( SpringRunner.class )
@SpringBootTest
@AutoConfigureMockMvc
public class APIIngredientTest {

    private MockMvc               mvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private IngredientService     service;

    @Before
    public void setup () {
        mvc = MockMvcBuilders.webAppContextSetup( context ).build();

        service.deleteAll();
    }

    @Test
    @Transactional
    public void testIngredientAPI () throws Exception {
        service.deleteAll();

        final Ingredient ingredient = new Ingredient();
        final Long test = (long) 0;

        ingredient.setAmount( 5 );
        ingredient.setName( "Coffee" );
        ingredient.setId( test );

        mvc.perform( post( "/api/v1/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( ingredient ) ) );

        Assertions.assertEquals( 1, (int) service.count() );
    }

    @Test
    @Transactional
    public void testDeleteIngredientAPI () throws Exception {
        service.deleteAll();

        final Ingredient ingredient = new Ingredient();
        final Long test = (long) 0;

        ingredient.setAmount( 5 );
        ingredient.setName( "Coffee" );
        ingredient.setId( test );

        mvc.perform( post( "/api/v1/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( ingredient ) ) );

        assertEquals( 1, (int) service.count() );

        mvc.perform( delete("/api/v1/ingredients/Coffee")
                .contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( ingredient ) ) )
                .andDo( print() )
                .andExpect( status().isOk() );

        assertEquals( 0, (int) service.count() );
    }

    @Test
    @Transactional
    public void testDeleteIngredient2 () throws Exception {
        service.deleteAll();

        final Ingredient ingredient = new Ingredient();
        final Long test = (long) 0;

        ingredient.setAmount( 5 );
        ingredient.setName( "Coffee" );
        ingredient.setId( test );
        mvc.perform( delete( "/api/v1/ingredients/Coffee" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( ingredient ) ) ).andExpect( status().is4xxClientError() );

        assertEquals( 0, (int) service.count() );
    }

    @Test
    @Transactional
    public void testAddIngredients () throws Exception {
        service.deleteAll();

        final Ingredient ingredient = new Ingredient();
        final Long test = (long) 0;

        ingredient.setAmount( 5 );
        ingredient.setName( "Coffee" );
        ingredient.setId( test );
        mvc.perform( post( "/api/v1/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( ingredient ) ) );

        assertEquals( 1, (int) service.count() );

        mvc.perform( post( "/api/v1/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( ingredient ) ) ).andExpect( status().is4xxClientError() );

        assertEquals( 1, (int) service.count() );

    }

    @Test
    @Transactional
    public void testUpdateIngredients () throws Exception {

        service.deleteAll();

        final Ingredient ingredient = new Ingredient( "Coffee", 5 );
        final Ingredient ingredient2 = new Ingredient( "Coffee", 7 );

        mvc.perform( post( "/api/v1/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( ingredient ) ) );

        assertEquals( 1, (int) service.count() );

        mvc.perform( put( "/api/v1/ingredient/Coffee" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( ingredient2 ) ) );

        assertEquals( 1, (int) service.count() );

        mvc.perform( put( "/api/v1/ingredient/Coffee" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( ingredient ) ) );

        assertEquals( 1, (int) service.count() );
    }

    @Test
    @Transactional
    public void testGetIngredients () throws Exception {
        service.deleteAll();

        final Ingredient ingredient = new Ingredient();
        final Long test = (long) 0;

        ingredient.setAmount( 5 );
        ingredient.setName( "Coffee" );
        ingredient.setId( test );

        mvc.perform( post( "/api/v1/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( ingredient ) ) );

        assertEquals( 1, (int) service.count() );

        mvc.perform( get( "/api/v1/ingredients" ) ).andDo( print() ).andExpect( status().isOk() ).andReturn()
                .getResponse().getContentAsString();

        assertEquals( 1, (int) service.count() );

    }

    @Test
    @Transactional
    public void testGetIngredients2 () throws Exception {
        service.deleteAll();

        assertEquals( 0, (int) service.count() );

        mvc.perform( get( "/api/v1/ingredients/Coffee" ) ).andDo( print() ).andExpect( status().is4xxClientError() )
                .andReturn().getResponse().getContentAsString();

    }
}
