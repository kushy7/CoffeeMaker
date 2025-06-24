package edu.ncsu.csc.CoffeeMaker.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller class for the URL mappings for CoffeeMaker. The controller returns
 * the approprate HTML page in the /src/main/resources/templates folder. For a
 * larger application, this should be split across multiple controllers.
 *
 * @author Kai Presler-Marshall
 */
@Controller
public class MappingController {

    /**
     * On a GET request to /index, the IndexController will return
     * /src/main/resources/templates/index.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/index", "/index.html", "/" } )
    public String index ( final Model model ) {
        return "index";
    }

    /**
     * On a GET request to /login, the IndexController will return
     * /src/main/resources/templates/login.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/login", "/login.html" } )
    public String login ( final Model model ) {
        return "login";
    }

    /**
     * On a GET request to /signup, the IndexController will return
     * /src/main/resources/templates/signup.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/signup", "/signup.html" } )
    public String signup ( final Model model ) {
        return "signup";
    }

    // Begin CUSTOMER routes

    /**
     * On a GET request to /customer/index, the IndexController will return
     * /src/main/resources/templates/customer/index.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/customer/index", "/customer/index.html", "/customer/" } )
    public String customerIndex ( final Model model ) {
        return "customer/index";
    }

    /**
     * On a GET request to /customer/login, the IndexController will return
     * /src/main/resources/templates/customer/login.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/customer/login", "/customer/login.html" } )
    public String customerLogin ( final Model model ) {
        return "customer/login";
    }

    /**
     * On a GET request to /customer/order, the IndexController will return
     * /src/main/resources/templates/customer/order.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/customer/order", "/customer/order.html" } )
    public String customerOrder ( final Model model ) {
        return "customer/order";
    }

    // Begin STAFF routes

    /**
     * On a GET request to /staff/index, the IndexController will return
     * /src/main/resources/templates/staff/index.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/staff/index", "/staff/index.html", "/staff/" } )
    public String staffIndex ( final Model model ) {
        return "staff/index";
    }

    /**
     * On a GET request to /staff/login, the IndexController will return
     * /src/main/resources/templates/staff/login.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/staff/login", "/staff/login.html" } )
    public String staffLogin ( final Model model ) {
        return "staff/login";
    }

    /**
     * On a GET request to /staff/deleterecipe, the IndexController will return
     * /src/main/resources/templates/staff/deleterecipe.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/staff/deleterecipe", "/staff/deleterecipe.html" } )
    public String staffDeleteRecipe ( final Model model ) {
        return "staff/deleterecipe";
    }

    /**
     * On a GET request to /staff/editrecipe, the IndexController will return
     * /src/main/resources/templates/staff/editrecipe.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/staff/editrecipe", "/staff/editrecipe.html" } )
    public String staffEditRecipe ( final Model model ) {
        return "staff/editrecipe";
    }

    /**
     * On a GET request to /staff/recipe, the IndexController will return
     * /src/main/resources/templates/staff/recipe.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/staff/recipe", "/staff/recipe.html" } )
    public String staffRecipe ( final Model model ) {
        return "staff/recipe";
    }

    /**
     * On a GET request to /staff/inventory, the IndexController will return
     * /src/main/resources/templates/staff/inventory.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/staff/inventory", "/staff/inventory.html" } )
    public String staffInventory ( final Model model ) {
        return "staff/inventory";
    }

    /**
     * On a GET request to /staff/makecoffee, the IndexController will return
     * /src/main/resources/templates/staff/makecoffee.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/staff/makecoffee", "/staff/makecoffee.html" } )
    public String staffMakeCoffee ( final Model model ) {
        return "staff/makecoffee";
    }

    /**
     * On a GET request to /staff/ingredient, the IndexController will return
     * /src/main/resources/templates/staff/ingredient.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/staff/ingredient", "/staff/ingredient.html" } )
    public String staffIngredient ( final Model model ) {
        return "staff/ingredient";
    }
}
