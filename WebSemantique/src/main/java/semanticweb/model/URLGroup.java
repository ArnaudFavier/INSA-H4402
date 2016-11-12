package semanticweb.model;

import com.ibm.watson.developer_cloud.alchemy.v1.model.Keyword;

import java.util.ArrayList;
import java.util.List;

public class URLGroup {
	List<URLContainer> urls;
	List<Keyword> keywords;
	String imageUrl;

	public URLGroup() {
		urls = new ArrayList<>(10);
		keywords = new ArrayList<>();
	}

	public void addUrl(URLContainer url) {
		urls.add(url);
	}

	public List<URLContainer> getUrls() {
		return urls;
	}

	public List<Keyword> getKeywords() {
		return keywords;
	}

	public void setKeywords(List<Keyword> keywords) {
		this.keywords = keywords;
	}

	public Keyword getBestKeyword() {
		if (!keywords.isEmpty())
			return keywords.get(0);
		return null;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
}
