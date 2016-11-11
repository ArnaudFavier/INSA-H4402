package semanticweb.model;

import java.util.ArrayList;
import java.util.List;

public class URLGroup {
	List<URLContainer> urls;

	public URLGroup() {
		urls = new ArrayList<>(10);
	}

	public void addUrl(URLContainer url) {
		urls.add(url);
	}

	public List<URLContainer> getUrls() {
		return urls;
	}
}
