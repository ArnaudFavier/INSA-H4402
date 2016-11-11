package semanticweb.services;

import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.customsearch.Customsearch;
import com.google.api.services.customsearch.model.Result;
import com.google.api.services.customsearch.model.Search;
import com.ibm.watson.developer_cloud.alchemy.v1.AlchemyLanguage;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.RDFNode;
import org.json.JSONArray;
import org.json.JSONObject;

import semanticweb.References;
import semanticweb.model.RDFTriplet;
import semanticweb.model.URIContainer;
import semanticweb.model.URLContainer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

/**
 * Contains some services usable by controllers
 */
public class Services {

	private static final String dbpediaSpotlightUrl = "http://spotlight.sztaki.hu:2222/rest/annotate";

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
	public static void initUrlTexts(List<URLContainer> urls) {
		final AlchemyLanguage language = new AlchemyLanguage();
		language.setApiKey(References.ALCHEMY_API_KEY);

		for (URLContainer url : urls) {
			try {
				url.setText(language.getText(new HashMap<String, Object>() {{
					put(AlchemyLanguage.URL, url.getUrl());
				}}).execute().getText());
			} catch (Exception e) {
				url.setText("");
			}
		}
	}

	/**
	 * @param urls       The url objects containing the text
	 * @param confidence The confidence range
	 * @throws Exception Can throw exceptions
	 */
	public static void initUrisFromUrlTexts(List<URLContainer> urls, float confidence) throws Exception {

		URL dbpediaURL = new URL(dbpediaSpotlightUrl);

		for (URLContainer urlContainer : urls) {
			StringBuilder result = new StringBuilder();
			HttpURLConnection conn = (HttpURLConnection) dbpediaURL.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Accept", "application/json"); //header format accepted
			conn.connect();

			//formulating request
			String request = "text= " + urlContainer.getText();
			request += "&confidence= " + confidence;

			byte[] inputBytes = request.getBytes("UTF-8");
			OutputStream os = conn.getOutputStream();
			os.write(inputBytes);
			os.flush();
			os.close();


			//result of request in json
			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
			rd.close();


			String jsonString = result.toString();
			JSONObject jsonObject = new JSONObject(jsonString);
			JSONArray resources;
			try {
				resources = jsonObject.getJSONArray("Resources");
			} catch (Exception e) {
				System.err.println(e.getMessage());
				e.printStackTrace();
				resources = null;
			}

			int i = 0;
			JSONObject resource = null;

			while (i < resources.length()) {
				resource = (JSONObject) resources.get(i);
				String uri = resource.getString("@URI");
				URIContainer uriContainer = new URIContainer(uri);
				urlContainer.addUri(uriContainer);
				i++;
			}
			conn.disconnect();
		}
	}

	/**
	 * Return a list of RDF Triplet obtained from DBPedia
	 * based on the list of uri given in parameter.
	 *
	 * @param uris A list of uri
	 * @return A list of RDF Triplet
	 * @throws Exception
	 */
	public static void initSparqlRDFTripletFromUris(List<URIContainer> uris) {
		for (URIContainer uri : uris) {
			String queryString =
					"PREFIX : <http://dbpedia.org/resource/>\n" +
							"SELECT * WHERE {\n" +
							"<" + uri.getUri() + "> ?p ?o\n" +
							"}";

			Query query = QueryFactory.create(queryString);

			QueryExecution qexec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", query);

			try {
				ResultSet results = qexec.execSelect();

				for (; results.hasNext(); ) {
					QuerySolution qsol = results.nextSolution();

					try {
						RDFNode obj = qsol.get("o");
						RDFNode predicate = qsol.get("p");
						RDFTriplet triplet = new RDFTriplet(uri.getUri(), predicate, obj);

						uri.addTriplet(triplet);
					} catch (Exception e) {
						System.err.println(e);
					}
				}
			} finally {
				qexec.close();
			}
		}
	}

	private static double jaccardIndex(List<RDFTriplet> tripletsA, List<RDFTriplet> tripletsB) {

		Set<RDFTriplet> union = new HashSet<>();
		union.addAll(tripletsA);
		union.addAll(tripletsB);

		int inter = 0;

		for (RDFTriplet triplet : union) {
			if (tripletsA.contains(triplet) && tripletsB.contains(triplet)) {
				inter++;
			}
		}

		return 1.0 * inter / union.size();
	}

	/**
	 * @param urls all urls objects filled with uris and rdf-triplets
	 * @return a similarity matrix between each URL. 1 = All RDP triplet are the same
	 * @see <a href="https://fr.wikipedia.org/wiki/Indice_et_distance_de_Jaccard">Jaccard Distance</a>
	 */
	public static double[][] computeJaccardMatrix(List<URLContainer> urls) {

		final int urlCount = urls.size();
		double[][] jacquartMatrix = new double[urlCount][urlCount];

		// Set the diagonal to 1
		for (int urlId = 0; urlId < urlCount; urlId++) {
			jacquartMatrix[urlId][urlId] = 1;
		}

		// We compute only on triangular matrix, then copy
		int i = 0;
		int j;
		for (URLContainer urlContainer1 : urls) {
			j = 0;
			List<RDFTriplet> tripletsUrl1 = urlContainer1.getRdfTriplets();
			for (URLContainer urlContainer2 : urls) {
				// When we reach the diagonal, break
				if (i == j)
					break;
				List<RDFTriplet> tripletsUrl2 = urlContainer2.getRdfTriplets();

				jacquartMatrix[i][j] = jacquartMatrix[j][i] = jaccardIndex(tripletsUrl1, tripletsUrl2);

				j++;
			}
			i++;
		}

		for (i = 0; i < urlCount; i++) {
			for (j = 0; j < urlCount; j++) {
				StringBuilder additionnalSpace = new StringBuilder();
				for (int k = Double.toString(jacquartMatrix[i][j]).length(); k <= 20 ; k++) {
					additionnalSpace.append(' ');
				}
				System.out.print(jacquartMatrix[i][j]+additionnalSpace.toString());
			}
			System.out.println();
		}

		return jacquartMatrix;
	}

}
