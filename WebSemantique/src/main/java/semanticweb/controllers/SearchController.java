package semanticweb.controllers;

import com.google.api.services.customsearch.model.Result;
import com.ibm.watson.developer_cloud.alchemy.v1.model.Keyword;
import org.apache.commons.validator.routines.UrlValidator;
import semanticweb.services.Services;

import java.util.ArrayList;
import java.util.List;

/**
 * This controller can be used in our web page to get expected results when research in triggered
 */
public class SearchController {

	/**
	 * For the moment, this method only return a list of keywords associated to our searchString
	 *
	 * @param searchString the search string given to Google
	 * @return a list of keywords
	 */
	public List<Keyword> getResults(String searchString) {
		// Get google result objects
		List<Result> googleSearchResults = Services.getGoogleResultsFromString(searchString);

		// Get urls from result objects
		List<String> googleSearchUrls = new ArrayList<>(googleSearchResults.size());
		for (Result result : googleSearchResults) {
			String urlString = result.getFormattedUrl();

			System.out.println(urlString);
			googleSearchUrls.add(urlString);
		}

		List<Keyword> alchemyKeywords = Services.getKeywordsFromUrls(googleSearchUrls);

		return alchemyKeywords;
	}
}
