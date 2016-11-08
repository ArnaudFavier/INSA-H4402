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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Contains some services usable by controllers
 */
public class Services {

	/**
	 * @param searchString The string given to Google
	 * @return A list of Google results, resulting of the Google research
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
	 * @return A list of texts, created by Alchemy from a list of URL
	 */
	public static List<String> getTextsFromUrls(List<String> urls) {

		final AlchemyLanguage language = new AlchemyLanguage();
		language.setApiKey(References.ALCHEMY_API_KEY);

		List<String> texts = new ArrayList<>(urls.size());

		for (String url : urls) {
			try {
				texts.add(language.getText(new HashMap<String, Object>(){{
					put(AlchemyLanguage.URL, url);
				}}).execute().getText());
			} catch(Exception e) {
				texts.add("");
			}
		}

		return texts;
	}
}
