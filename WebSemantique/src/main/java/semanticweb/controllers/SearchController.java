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
	 * For the moment, this method only return a list of urls from Google results
	 *
	 * @param searchString the search string given to Google
	 * @return a list of urls
	 */
	public List<String> getGoogleSearchUrls(String searchString) {
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
	public List<String> getTextsFromUrls(List<String> googleSearchUrls) {
		List<String> alchemyTexts = Services.getTextsFromUrls(googleSearchUrls);

		return alchemyTexts;
	}
}
