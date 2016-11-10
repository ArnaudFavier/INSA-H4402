package semanticweb;

import semanticweb.controllers.SearchController;
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
	 * @param args not used here
	 */
	public static void main(String[] args) {
		// Set the port
		port(PORT);

		// Set the public file
		Spark.staticFileLocation("public");

		declareRoutes();

        // Test of SPARQL
		List<String> uris = new ArrayList<>();
		uris.add("Bill_Gates");
		uris.add("Steve_Jobs");
		Services.sparqlRDFTripletFromUri(uris);
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
			String searchString = request.queryParams("input-search");
			List<String> googleResults = new SearchController().getGoogleSearchUrls(searchString);
			List<String> texts = new SearchController().getTextsFromUrls(googleResults);

			//To be used by sparql and Jaccard index
			List<String> uris = new SearchController().getURIsFromTexts(texts, "0.1");

			Map<String, Object> model = new HashMap<>();
			model.put("urls", googleResults);
			model.put("texts", texts);

			return new ModelAndView(model, "public/velocity/searchResult.vm");
		}, new VelocityTemplateEngineUTF8());
	}
}