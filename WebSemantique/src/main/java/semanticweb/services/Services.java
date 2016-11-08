package semanticweb.services;

import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.customsearch.Customsearch;
import com.google.api.services.customsearch.model.Result;
import com.google.api.services.customsearch.model.Search;
import com.ibm.watson.developer_cloud.alchemy.v1.AlchemyLanguage;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import semanticweb.References;

import java.io.IOException;
import java.util.*;

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

	public static void sparqlRDFTripletFromUri (List<String> uri){
		System.out.println("SPARQL:");
		
		String queryString =
			"PREFIX : <http://dbpedia.org/resource/>\n" +
			"SELECT * WHERE {\n" +
			":Bill_Gates ?p ?o\n" +
			"}";

		Query query = QueryFactory.create(queryString);

		QueryExecution qexec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", query);

		try {
			ResultSet results = qexec.execSelect();

			for (; results.hasNext(); ) {
				QuerySolution qsol = results.nextSolution();

				try {
					RDFNode r = qsol.get("o");
					System.out.println("RDF Node: \t" + r);
				} catch (Exception e) {}

				try {
					Resource r = qsol.getResource("o");
					System.out.println("Ressource: \t" + r);
				} catch (Exception e) {}

				try {
					Literal l = qsol.getLiteral("o");
					System.out.println("Literal: \t" + l);
				} catch (Exception e) {}
			}
		} finally {
			qexec.close();
		}
	}

}
