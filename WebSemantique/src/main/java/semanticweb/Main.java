package semanticweb;

import com.ibm.watson.developer_cloud.alchemy.v1.model.Keyword;
import semanticweb.controllers.SearchController;
import spark.ModelAndView;
import spark.Spark;
import semanticweb.util.VelocityTemplateEngineUTF8;

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
	}

	/**
	 * Declare our routes for the website
	 */
	private static void declareRoutes() {
		get("/search", (request, response) -> {
			Map<String, Object> model = new HashMap<>();
			return new ModelAndView(model, "public/velocity/search.vm");
		}, new VelocityTemplateEngineUTF8());

		post("/search", (request, response) -> {
			String searchString = request.queryParams("input-search");
			List<Keyword> keywords = new SearchController().getResults(searchString);

			Map<String, Object> model = new HashMap<>();
			model.put("keywords", keywords);

			return new ModelAndView(model, "public/velocity/searchResult.vm");
		}, new VelocityTemplateEngineUTF8());
	}
}