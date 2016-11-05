package semanticweb.services;

import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.customsearch.Customsearch;
import com.google.api.services.customsearch.model.Result;
import com.google.api.services.customsearch.model.Search;
import com.ibm.watson.developer_cloud.alchemy.v1.AlchemyLanguage;
import com.ibm.watson.developer_cloud.alchemy.v1.model.Keyword;
import semanticweb.References;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Contains some services usable by controllers
 */
public class Services {

	/**
	 * @param searchString The string given to Google
	 * @return A list of Google Results, resulting of the Google research
	 */
	public static List<Result> getGoogleResultsFromString(String searchString) {
		Customsearch customsearch = new Customsearch(new NetHttpTransport(), new JacksonFactory(), null);

		List<Result> results = null;
		try {
			com.google.api.services.customsearch.Customsearch.Cse.List list = customsearch.cse().list(searchString);
			list.setKey(References.GOOGLE_API_KEY);
			list.setCx(References.GOOGLE_SEARCH_ENGINE_ID);
			Search search = list.execute();
			results = search.getItems();
		} catch (IOException ex) {
			System.err.println(ex);
		}
		return results;
	}

	/**
	 * @param urls List of URL
	 * @return A list of keywords, created by Alchemy from a list of URL
	 */
	public static List<Keyword> getKeywordsFromUrls(List<String> urls) {
		final Map<String, Object> params = new HashMap<String, Object>();

		for (String url : urls) {
			params.put(AlchemyLanguage.URL, url);
		}

		final AlchemyLanguage language = new AlchemyLanguage();

		language.setApiKey(References.ALCHEMY_API_KEY);
		List<Keyword> keywords = language.getKeywords(params).execute().getKeywords();

		return keywords;
	}
}
