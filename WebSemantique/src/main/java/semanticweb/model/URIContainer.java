package semanticweb.model;

import java.util.*;

public class URIContainer {
	String uri;
	List<RDFTriplet> tripletList;
	URLContainer url;

	private static final Set<String> BANISHED_PREDICATES = new HashSet<>(Arrays.asList(new String[]{
			"http://www.w3.org/1999/02/22-rdf-syntax-ns#type",
			"http://www.w3.org/2002/07/owl#sameAs"
	}));

	public URIContainer(String uri) {
		this.uri = uri;
		tripletList = new ArrayList<>(10);
	}

	public String getUri() {
		return uri;
	}

	public List<RDFTriplet> getTripletList() {
		return tripletList;
	}

	public void addTriplet(RDFTriplet triplet) {
		if (BANISHED_PREDICATES.contains(triplet.getPredicate())) {
			tripletList.add(triplet);
			if (url != null) {
				url.rdfTriplets.add(triplet);
			}
		}
	}
}
