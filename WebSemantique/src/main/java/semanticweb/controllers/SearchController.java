package semanticweb.controllers;

import com.google.api.services.customsearch.model.Result;
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
		List<String> googleResults = getGoogleSearchUrls(searchString);
		List<String> texts = getTextsFromUrls(googleResults);
		// To be used by sparql and Jaccard index
		//List<String> uris = new SearchController().getURIsFromTexts(texts, "0.1");

		// Container for results
		Map<String, Object> results = new HashMap<>();
		results.put("urls", googleResults);
		results.put("texts", texts);

		return results;
	}
	
	/**
	 * For the moment, this method only return a list of urls from Google results
	 *
	 * @param searchString the search string given to Google
	 * @return a list of urls
	 */
	private List<String> getGoogleSearchUrls(String searchString) {
		// Get google result objects
		List<Result> googleSearchResults = Services.getGoogleResultsFromString(searchString);

		// Get urls from result objects
		List<String> googleSearchUrls = new ArrayList<>(googleSearchResults.size());
		for (Result result : googleSearchResults) {
			String urlString = result.getLink();

			System.out.println(urlString);
			googleSearchUrls.add(urlString);
		}

		return googleSearchUrls;
	}

	/**
	 * This method return a list of texts associated to a list of Google Urls
	 *
	 * @param googleSearchUrls the search urls get from Google
	 * @return a list of texts
	 */
	private List<String> getTextsFromUrls(List<String> googleSearchUrls) {
		List<String> alchemyTexts = Services.getTextsFromUrls(googleSearchUrls);

		return alchemyTexts;
	}
	
	private List<String> getURIsFromTexts(List<String> texts, String confidence) throws Exception {
        List<String> uris = Services.getURIsFromTexts(texts, confidence);
        
        // Debug
        System.out.println("URIS");
        for(String uri : uris) {
        	System.out.println(uri);
        }
        
        return uris;
    }
}
