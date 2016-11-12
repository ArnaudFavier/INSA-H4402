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
	 * Main method for the search.
	 * 		Search on Google and get texts from the Google's results
	 *
	 * @param searchString the search string given to Google
	 * @return a container of urls and texts
	 */
	public Map<String, Object> doSearch(String searchString) {
		// Call private methods
		List<URLContainer> urlContainers = getGoogleSearchUrls(searchString);
		Services.initUrlTexts(urlContainers);
		List<URLGroup> groups = new ArrayList<>();
		// To be used by sparql and Jaccard index
		try{
			Services.initUrisFromUrlTexts(urlContainers, 0.1f);
			for(URLContainer urlContainer : urlContainers){
				Services.initSparqlRDFTripletFromUris(urlContainer.getUris());
			}
			double[][] similarities = Services.computeJaccardMatrix(urlContainers);
			groups = Services.makeUrlGroups(urlContainers, similarities);
			Services.initKeywordsOfGroups(groups);
			Services.initImageUrlOfGroups(groups);
		}
		catch (Exception e){
			System.err.println(e.getMessage());
			e.printStackTrace();
		}

		// Container for results
		Map<String, Object> results = new HashMap<>();
		//results.put("urls", googleResults);
		//results.put("texts", texts);
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
