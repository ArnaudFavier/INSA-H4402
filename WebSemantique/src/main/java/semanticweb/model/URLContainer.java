package semanticweb.model;

import java.util.ArrayList;
import java.util.List;

public class URLContainer {
	String url;
	String text;
	List<URIContainer> uris;
	List<RDFTriplet> rdfTriplets;

	public URLContainer(String url) {
		this.url = url;
		uris = new ArrayList<>(10);
		rdfTriplets = new ArrayList<>(100);
	}

	public String getUrl() {
		return url;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void addUri(URIContainer uri){
		uris.add(uri);
		uri.url = this;
		rdfTriplets.addAll(uri.getTripletList());
	}

	public List<URIContainer> getUris() {
		return uris;
	}

	public List<RDFTriplet> getRdfTriplets() {
		return rdfTriplets;
	}
}
