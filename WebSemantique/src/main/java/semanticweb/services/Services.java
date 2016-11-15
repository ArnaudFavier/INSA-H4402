package semanticweb.services;

import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.customsearch.Customsearch;
import com.google.api.services.customsearch.model.Result;
import com.google.api.services.customsearch.model.Search;
import com.ibm.watson.developer_cloud.alchemy.v1.AlchemyLanguage;
import com.ibm.watson.developer_cloud.alchemy.v1.model.Keyword;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.RDFNode;
import org.json.JSONArray;
import org.json.JSONObject;

import semanticweb.References;
import semanticweb.model.RDFTriplet;
import semanticweb.model.URIContainer;
import semanticweb.model.URLContainer;
import semanticweb.model.URLGroup;

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
	private static final int TEXT_MAX_LENGTH = 800;
	private static final double COEF_DIRECT_LINK = 0.25;
	private static final double COEF_THRESHOLD = 0.8;
    private static final double LIST_SIZE_INFLUENCE = 0.5;
    private static final double COEF_PREDICAT_SIMILARITY = 0.9;

	/**
	 * Call Google Custom Search API for the search string given
	 *
	 * @param searchString the string given to Google
	 * @return q list of Google results, resulting of the Google research
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
	 * For each url, set the text given by Alchemy from this url
	 *
	 * @param urls list of URL
	 */
	public static void initUrlTexts(List<URLContainer> urls) {
		final AlchemyLanguage language = new AlchemyLanguage();
		language.setApiKey(References.ALCHEMY_API_KEY);

		for (Iterator<URLContainer> iterator = urls.iterator(); iterator.hasNext(); ) {
			URLContainer url = iterator.next();
			try {
				url.setText(language.getText(new HashMap<String, Object>() {{
					put(AlchemyLanguage.URL, url.getUrl());
				}}).execute().getText());
			} catch (Exception e) {
				// If there is an error, remove this URL
				iterator.remove();
			}
		}
	}

	/**
	 * For each url, give the list of uris associate to this url's text
	 *
	 * @param urls the url objects containing the text
	 * @param confidence the confidence range
	 * @throws Exception can throw exceptions
	 */
	public static void initUrisFromUrlTexts(List<URLContainer> urls, float confidence) throws Exception {
		URL dbpediaURL = new URL(dbpediaSpotlightUrl);

		for (Iterator<URLContainer> iterator = urls.iterator(); iterator.hasNext(); ) {
			URLContainer urlContainer = iterator.next();
			StringBuilder result = new StringBuilder();
			HttpURLConnection conn = (HttpURLConnection) dbpediaURL.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Accept", "application/json"); // header format accepted
			conn.connect();

			String text = urlContainer.getText();
			text = text.length() > TEXT_MAX_LENGTH ? text.substring(0, TEXT_MAX_LENGTH) : text;

			// Formulating request
			String request = "text= " + text;
			request += "&confidence= " + confidence;

			byte[] inputBytes = request.getBytes("UTF-8");
			OutputStream os = conn.getOutputStream();
			os.write(inputBytes);
			os.flush();
			os.close();

			// Result of request in json
			try {
				BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				String line;
				while ((line = rd.readLine()) != null) {
					result.append(line);
				}
				rd.close();
			} catch (Exception ex) {
				System.err.println(ex.toString());
				iterator.remove();
				continue;
			}

			JSONArray resources;
			String jsonString = result.toString();
			try {
				JSONObject jsonObject = new JSONObject(jsonString);
				resources = jsonObject.getJSONArray("Resources");
			} catch (Exception e) {
				System.err.println("line = "+jsonString);
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
	 * For each uri, set a list of RDF Triplet obtained from DBPedia Sparql based on this uri
	 *
	 * @param uris A list of uri
	 * @throws Exception
	 */
	public static void initSparqlRDFTripletFromUris(List<URIContainer> uris) {
		// Create the query
		for (URIContainer uri : uris) {
			String queryString =
					"PREFIX : <http://dbpedia.org/resource/>\n" +
							"SELECT * WHERE {\n" +
							"<" + uri.getUri() + "> ?p ?o\n" +
							"}";

			Query query = QueryFactory.create(queryString);
			// Execute the query
			QueryExecution qexec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", query);

			// Fecth results
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

	/**
	 * Compute the Jaccard index with additions
	 *
	 * @param tripletsA list of triplets from the first url
	 * @param tripletsB list of triplets from the second url
	 * @return the value of jaccard index for the two given urls
	 */
	private static double jaccardIndex(List<RDFTriplet> tripletsA, List<RDFTriplet> tripletsB) {
		// Union of all urls
		Set<RDFTriplet> union = new HashSet<>();
		union.addAll(tripletsA);
		union.addAll(tripletsB);

		// Same triplet
		int intersection = 0;
		for (RDFTriplet triplet : union) {
			if (tripletsA.contains(triplet) && tripletsB.contains(triplet)) {
				intersection++;
			}
		}

		// First compute of index's value
		double indexValue = 1.0 * intersection / union.size();

		// Direct link exists
		for (RDFTriplet tripletA : tripletsA) {
			for (RDFTriplet tripletB : tripletsB) {
			if (tripletA.getObject() == tripletB.getUri()
					|| tripletA.getUri() == tripletB.getObject()) {
				indexValue += COEF_DIRECT_LINK;
				}
			}
		}

		// Call computeDirectLink
		return computeDirectLink(tripletsA, tripletsB, indexValue);
	}

	/**
	 * Search for direct link between the two lists of triplets,
	 * and modify the index value consequently
	 *
	 * @param tripletsA list of triplets from the first url
	 * @param tripletsB list of triplets from the second url
	 * @param indexValue the value of jaccard index for the two given urls, previously computed
	 * @return index's value modified by direct link coefficient
	 */
	public static double computeDirectLink(List<RDFTriplet> tripletsA, List<RDFTriplet> tripletsB, double indexValue) {
		// Direct link exists
		for (RDFTriplet tripletA : tripletsA) {
			for (RDFTriplet tripletB : tripletsB) {
				if (tripletA.getObject() == tripletB.getUri()
						|| tripletA.getUri() == tripletB.getObject()) {
					indexValue += COEF_DIRECT_LINK;
				}
			}
		}

		// Return max 1.0
		return ((indexValue) < (1.0) ? (indexValue) : (1.0));
	}

	/**
	 * Give the similarity matrix for a list of urls
	 * 1 = All RDP triplet are the same
	 *
	 * @param urls all urls objects filled with uris and rdf-triplets
	 * @return a similarity matrix between each URL
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

		// Print log in console
		for (i = 0; i < urlCount; i++) {
			for (j = 0; j < urlCount; j++) {
				StringBuilder additionnalSpace = new StringBuilder();
				for (int k = Double.toString(jacquartMatrix[i][j]).length(); k <= 20; k++) {
					additionnalSpace.append(' ');
				}
				System.out.print(jacquartMatrix[i][j] + additionnalSpace.toString());
			}
			System.out.println();
		}

		return jacquartMatrix;
	}

    /**
     * @param sizeListA size of the first list
     * @param sizeListB size of the second list
     * @return a coef about sizes of lists
     */
    public static double listSizeCoef(int sizeListA, int sizeListB) {
        return sizeListA >= sizeListB ? (double)(sizeListA)/sizeListB : (double)(sizeListB)/sizeListA;
    }

    /**
     * Give the similarity matrix for a list of urls balance by the coefficient between the two list sizes
     * 1 = All RDP triplet are the same
     *
     * @param urls all urls objects filled with uris and rdf-triplets
     * @return a similarity matrix between each URL balance by the coefficient between the two list sizes
     */
    public static double[][] computeJaccardMatrixWithListSizeCoef(List<URLContainer> urls) {

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

                jacquartMatrix[i][j] = jacquartMatrix[j][i] = jaccardIndex(tripletsUrl1, tripletsUrl2) * LIST_SIZE_INFLUENCE * listSizeCoef(tripletsUrl1.size(), tripletsUrl2.size());

                j++;
            }
            i++;
        }

        for (i = 0; i < urlCount; i++) {
            for (j = 0; j < urlCount; j++) {
                StringBuilder additionnalSpace = new StringBuilder();
                for (int k = Double.toString(jacquartMatrix[i][j]).length(); k <= 20; k++) {
                    additionnalSpace.append(' ');
                }
                System.out.print(jacquartMatrix[i][j] + additionnalSpace.toString());
            }
            System.out.println();
        }

        return jacquartMatrix;
    }



    /**
     * @param triplets A list of RDFTriplet
     * @param predicate The RDFTriplet's predicate we search
     * @return true if there at least one RDFTriplets with the same predicate as this we have in parameters, else return
     * false
     */
    public static boolean containsPredicate(List<RDFTriplet> triplets, String predicate){
        for (RDFTriplet triplet : triplets) {
            if(triplet.getPredicate().equals(predicate))
                return true;
        }

        return false;
    }

    /**
     * @param tripletsA First list of RDFTriplet
     * @param tripletsB Second list of RDFTriplet
     * @return an index based on jaccard index and the fact that RDFTriplets of the two list have same predicat
     * but different object
     */
    public static double PredicateSimilarityAndJaccardIndex(List<RDFTriplet> tripletsA, List<RDFTriplet> tripletsB){
        Set<RDFTriplet> union = new HashSet<>();
        union.addAll(tripletsA);
        union.addAll(tripletsB);

        int intersection = 0;
        double coef = 1.0;

        for (RDFTriplet triplet : union) {
            if (tripletsA.contains(triplet) && tripletsB.contains(triplet)) {
                intersection++;
            }else if(containsPredicate(tripletsA, triplet.getPredicate()) && containsPredicate(tripletsB, triplet.getPredicate())){
                coef *= COEF_PREDICAT_SIMILARITY;
            }

        }
        return coef * ((double)(intersection) / union.size());
    }

    /**
     * Give the similarity matrix for a list of urls
     * 1 = All RDP triplet are the same
     *
     * @param urls all urls objects filled with uris and rdf-triplets
     * @return a similarity matrix between each URL
     */
    public static double[][] computeJaccardMatrixWithPredicateSimilarityIndex(List<URLContainer> urls) {
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

                jacquartMatrix[i][j] = jacquartMatrix[j][i] = PredicateSimilarityAndJaccardIndex(tripletsUrl1, tripletsUrl2);

                j++;
            }
            i++;
        }

        // Print log in console
        for (i = 0; i < urlCount; i++) {
            for (j = 0; j < urlCount; j++) {
                StringBuilder additionnalSpace = new StringBuilder();
                for (int k = Double.toString(jacquartMatrix[i][j]).length(); k <= 20; k++) {
                    additionnalSpace.append(' ');
                }
                System.out.print(jacquartMatrix[i][j] + additionnalSpace.toString());
            }
            System.out.println();
        }

        return jacquartMatrix;
    }

    /**
     * Compute the Sorensen-Dice index with additions
     *
     * @param tripletsA list of triplets from the first url
     * @param tripletsB list of triplets from the second url
     * @return the value of Sorensen-Dice index for the two given urls
     */
    private static double SorensenDiceIndex(List<RDFTriplet> tripletsA, List<RDFTriplet> tripletsB) {
        // Intersection of the two lists
        Set<RDFTriplet> intersection = new HashSet<>();

        // compute intersection of the two list
        for (RDFTriplet tripletA : tripletsA) {
            if(tripletsB.contains(tripletA)){
                intersection.add(tripletA);
            }
        }

        //  return index's value computed
        return (double)(2 * intersection.size())/(tripletsA.size() + tripletsB.size());
    }

    /**
     * Compute the Tversky index with additions
     *
     * @param tripletsA list of triplets from the first url
     * @param tripletsB list of triplets from the second url
     * @param alpha alpha coefficient for Tversky index
     * @param beta beta coefficient for Tversky index
     * @return the value of Tversky index for the two given urls
     */
    private static double TverskyIndex(List<RDFTriplet> tripletsA, List<RDFTriplet> tripletsB, double alpha, double beta) {
        // Union of all urls
        Set<RDFTriplet> union = new HashSet<>();
        union.addAll(tripletsA);
        union.addAll(tripletsB);

        // Intersection of the two lists
        Set<RDFTriplet> intersection = new HashSet<>();
        // compute intersection of the two list
        for (RDFTriplet tripletA : tripletsA) {
            if(tripletsB.contains(tripletA)){
                intersection.add(tripletA);
            }
        }

        // tripletsA - tripletsB list
        Set<RDFTriplet> tripletsAminusB = new HashSet<>();
        for (RDFTriplet tripletA: tripletsA) {
            if(!(tripletsB.contains(tripletA))){
                tripletsAminusB.add(tripletA);
            }
        }

        // tripletsB - tripletsA list
        Set<RDFTriplet> tripletsBminusA = new HashSet<>();
        for (RDFTriplet tripletB: tripletsB) {
            if(!(tripletsA.contains(tripletB))){
                tripletsAminusB.add(tripletB);
            }
        }

        //  return index's value computed
        return (double)(intersection.size())/(union.size() + (alpha * tripletsAminusB.size()) + (beta * tripletsBminusA.size()));
    }



	/**
	 * Compute the threshold to create the group
	 *
	 * @param urls list of urls
	 * @param similarities the similarities matrix previously computed for this urls
	 * @return call to makeUrlGroups function, which creates lists of groups
	 */
	public static List<URLGroup> makeUrlGroups(List<URLContainer> urls, double[][] similarities) {
		// Compute the threshold
		double similaritiesSum = 0.0;
		int countedElems = 0;
		for (int i = 0; i < urls.size(); i++) {
			for (int j = 0; j < i; j++) {
				similaritiesSum += similarities[i][j];
				countedElems++;
			}
		}
		double threshold = similaritiesSum / countedElems;

		threshold *= COEF_THRESHOLD;

		return makeUrlGroups(urls, similarities, threshold);
	}

	/**
	 * Create groups of urls according to their similarities and the previous computed threshold
	 *
	 * @param urls list of urls
	 * @param similarities the similarities matrix previously computed for this urls
	 * @param threshold the threshold previously computed for this urls
	 * @return lists of groups which contains urls
	 */
	public static List<URLGroup> makeUrlGroups(List<URLContainer> urls, double[][] similarities, double threshold) {
		List<URLGroup> groups = new ArrayList<>();

		for (int i = 0; i < urls.size(); i++) {
			URLContainer urlToAdd = urls.get(i);
			boolean added = false;
			for (URLGroup group : groups) {
				boolean canAdd = true;
				for (URLContainer otherUrl : group.getUrls()) {
					int j = urls.indexOf(otherUrl);
					canAdd &= similarities[i][j] > threshold;
				}
				if (canAdd) {
					group.addUrl(urlToAdd);
					added = true;
				}
			}

			// If we haven't added the url to any group, then we create an group especially for this url
			if (!added) {
				URLGroup newGroup = new URLGroup();
				newGroup.addUrl(urlToAdd);
				groups.add(newGroup);
			}
		}

		// Look again if firsts url can fit in lasts groups
		for (int i = 0; i < urls.size(); i++) {
			URLContainer urlToAdd = urls.get(i);
			for (URLGroup group : groups) {
				boolean canAdd = true;
				for (URLContainer otherUrl : group.getUrls()) {
					if(otherUrl != urlToAdd) {
						int j = urls.indexOf(otherUrl);
						canAdd &= similarities[i][j] > threshold;
					}
					else{
						canAdd = false;
						break;
					}
				}
				if (canAdd) {
					group.addUrl(urlToAdd);
				}
			}
		}

		// Print log in console
		for (URLGroup group : groups) {
			System.out.println("-- New group --");
			for (URLContainer url : group.getUrls()) {
				System.out.println(url.getUrl());
			}
		}

		return groups;
	}

	/**
	 * Set the keywords from Alchemy for a list of groups
	 *
	 * @param groups the list of groups
	 */
	public static void initKeywordsOfGroups(List<URLGroup> groups) {
		final AlchemyLanguage language = new AlchemyLanguage();
		language.setApiKey(References.ALCHEMY_API_KEY);

		for (URLGroup group : groups) {
			// Create the Alchemy url container
			HashMap<String, Object> urlsMap = new HashMap<>();
			for (URLContainer url : group.getUrls()) {
				urlsMap.put(AlchemyLanguage.URL, url.getUrl());
			}

			try {
				// Get the keyword from Alchemy
				List<Keyword> keywords = language.getKeywords(urlsMap).execute().getKeywords();
				// Set the keywords to the group
				group.setKeywords(keywords);
			} catch (Exception ex) {
				System.err.println("No keywords found for the group " + group.getUrls());
				System.out.println(ex.toString());
			}
		}
	}

	/**
	 * Give an image for the group based on best group's keywords
	 *
	 * @param groups the group of url
	 */
	public static void initImageUrlOfGroups(List<URLGroup> groups) {
		Customsearch customsearch = new Customsearch(new NetHttpTransport(), new JacksonFactory(), null);

		System.out.println("Groups : ");
		for (URLGroup group : groups) {
			String keyword = group.getBestKeyword().getText();

			System.out.println("Keywords = "+group.getKeywords().toString());
			try {
				com.google.api.services.customsearch.Customsearch.Cse.List list = customsearch.cse().list(keyword);
				list.setSearchType("image");
				list.setFileType("png,jpg");
				list.setKey(References.GOOGLE_API_KEY);
				list.setCx(References.GOOGLE_SEARCH_ENGINE_ID);

				Search search = list.execute();
				List<Result> results = search.getItems();
				group.setImageUrl(results.get(0).getLink());
				System.out.println("Image = "+group.getImageUrl());
			} catch (Exception ex) {
				System.out.println(ex.toString());
			}
		}
	}
}
