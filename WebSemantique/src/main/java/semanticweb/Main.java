package semanticweb;

import semanticweb.controllers.SearchController;
import semanticweb.model.RDFTriplet;
import semanticweb.services.Services;
import spark.ModelAndView;
import spark.Spark;
import semanticweb.util.VelocityTemplateEngineUTF8;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

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

        // Test of SPARQL
        List<String> urisA = new ArrayList<>();
        List<String> urisB = new ArrayList<>();
        urisA.add("Bill_Gates");
        urisB.add("Barack_Obama");
        List<RDFTriplet> tripletsA = Services.sparqlRDFTripletFromUri(urisA);
        List<RDFTriplet> tripletsB = Services.sparqlRDFTripletFromUri(urisB);

        System.out.println(tripletsA);
        System.out.println(tripletsB);

        System.out.println(Services.jaccardIndex(tripletsA, tripletsB));

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

            // Put the results into the model for the view
            Map<String, Object> model = new HashMap<>();
            model.put("urls", results.get("urls"));
            model.put("texts", results.get("texts"));

            return new ModelAndView(model, "public/velocity/searchResult.vm");
        }, new VelocityTemplateEngineUTF8());
    }
}