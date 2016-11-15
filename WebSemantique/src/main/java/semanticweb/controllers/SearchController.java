package semanticweb.controllers;

import com.google.api.services.customsearch.model.Result;
import semanticweb.model.URLContainer;
import semanticweb.model.URLGroup;
import semanticweb.services.Services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This controller can be used in our web page to get expected results when research in triggered
 */
public class SearchController {

	/**
	 * Main method for the search
	 *
	 * @param searchString the search string given to Google
	 * @return a container of urls and texts
	 */
	public Map<String, Object> doSearch(String searchString) {
		// Get Google results
		List<URLContainer> urlContainers = getGoogleSearchUrls(searchString);
		// Get Alchemy text
		Services.initUrlTexts(urlContainers);
		List<URLGroup> groups = new ArrayList<>();

		try{
			// Get uri
			Services.initUrisFromUrlTexts(urlContainers, 0.1f);
			// Get RDF triplet
			for (URLContainer urlContainer : urlContainers) {
				Services.initSparqlRDFTripletFromUris(urlContainer.getUris());
			}

			// Compute
			double[][] similarities = Services.computeSimilarityMatrix(urlContainers);
			groups = Services.makeUrlGroups(urlContainers, similarities);

			// Shaping
			Services.initKeywordsOfGroups(groups);
			Services.initImageUrlOfGroups(groups);
		}
		catch (Exception e){
			System.err.println(e.getMessage());
			e.printStackTrace();
		}

		// Container for results
		Map<String, Object> results = new HashMap<>();
		results.put("urlGroups", groups);

		return results;
	}
	
	/**
	 * For the moment, this method only return a list of urls from Google results
	 *
	 * @param searchString the search string given to Google
	 * @return a list of urls
	 */
	private List<URLContainer> getGoogleSearchUrls(String searchString) {
		// Get google result objects
		List<Result> googleSearchResults = Services.getGoogleResultsFromString(searchString);

		// Get urls from result objects
		List<URLContainer> googleSearchUrls = new ArrayList<>(googleSearchResults.size());
		for (Result result : googleSearchResults) {
			String urlString = result.getLink();

			System.out.println(urlString);
			googleSearchUrls.add(new URLContainer(urlString));
		}

		return googleSearchUrls;
	}
}
