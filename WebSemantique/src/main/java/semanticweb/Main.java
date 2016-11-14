package semanticweb;

import semanticweb.controllers.SearchController;
import semanticweb.util.VelocityTemplateEngineUTF8;
import spark.ModelAndView;
import spark.Spark;

import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;

/**
 * This class define the program's start point {@link Main#main(String[])}
 * Just run the project and go to http://localhost:9090/search
 */
public class Main {

    /**
     * The port associated to this app
     */
    private static final int PORT = 9090;

    /**
     * The main function of this app
     *
     * @param args not used here
     */
    public static void main(String[] args) {
        // Set the port
        port(PORT);

        // Set the public file
        Spark.staticFileLocation("public");

        declareRoutes();
    }

    /**
     * Declare our routes for the website
     */
    private static void declareRoutes() {
        get("/", (request, response) -> {
            response.redirect("/search");
            return null;
        });

        get("/search", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            return new ModelAndView(model, "public/velocity/search.vm");
        }, new VelocityTemplateEngineUTF8());

        post("/search", (request, response) -> {
            // Get the input search from the HTTP request
            String searchString = request.queryParams("input-search");

            // Execute the search in the controller
            Map<String, Object> results = new SearchController().doSearch(searchString);

            return new ModelAndView(results, "public/velocity/searchResult.vm");
        }, new VelocityTemplateEngineUTF8());
    }
}