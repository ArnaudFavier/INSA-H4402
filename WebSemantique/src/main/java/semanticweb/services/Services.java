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
import org.json.JSONArray;
import org.json.JSONObject;

import semanticweb.References;

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
	
	/**
	 * 
	 * @param A text associated with an url
	 * @param confidence The confidence range
	 * @return
	 * @throws Exception
	 */
	public static List<String> getURIsFromTexts(List<String> texts, String confidence) throws Exception {

		texts.add("President Obama called Wednesday on Congress to extend a tax break for students included in last year's economic stimulus package, arguing that the policy provides more generous assistance");
		
        StringBuilder result = new StringBuilder();
        URL url = new URL(dbpediaSpotlightUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Accept", "application/json"); //header format accepted
        conn.connect();
        
        List<String> uris = new ArrayList<String>();

        for(String text : texts) {
	        //formulating request
	        String request = "text= " + text;
	        request += "&confidence= " + confidence;
	        
	        byte[] inputBytes = request.getBytes("UTF-8");
	        OutputStream os = conn.getOutputStream();
	        os.write(inputBytes);
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
	        }
	        catch (Exception e) {
	        	System.err.println(e.getMessage());
	        	e.printStackTrace();
	        	resources = null;
	        }
	        
	        int i = 0;
	        JSONObject resource = null;
	        
	        while(i < resources.length()) {
	        	resource = (JSONObject) resources.get(i);
	        	uris.add(resource.getString("@URI"));
	        	i++;
	        }
        }
        
        return uris;
	}

	public static Map<String, List<RDFNode>> sparqlRDFTripletFromUri (List<String> uris){
		System.out.println("SPARQL:");

		Map<String, List<RDFNode>> listeTriplets = new HashMap<>();

		for (String uri : uris) {
			String queryString =
				"PREFIX : <http://dbpedia.org/resource/>\n" +
				"SELECT * WHERE {\n" +
				":" + uri + " ?p ?o\n" +
				"}";

			Query query = QueryFactory.create(queryString);

			QueryExecution qexec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", query);

			List<RDFNode> listeRDFNode = new ArrayList<>();

			try {
				ResultSet results = qexec.execSelect();

				for (; results.hasNext(); ) {
					QuerySolution qsol = results.nextSolution();

					try {
						RDFNode r = qsol.get("o");
						System.out.println("RDF Node: \t" + r);
						listeRDFNode.add(r);
					} catch (Exception e) {}
				}
			} finally {
				qexec.close();
			}

			listeTriplets.put(uri,listeRDFNode);
		}

		return listeTriplets;
	}



}
