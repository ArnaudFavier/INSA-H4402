package semanticweb.model;

import org.apache.jena.rdf.model.RDFNode;

public class RDFTriplet {
    private String uri;
    private String predicate;
    private String object;

    public RDFTriplet(String uri, RDFNode predicate, RDFNode object) {
        this.uri = uri;
        this.predicate = predicate.toString();
        this.object = object.toString();
    }

    public String getPredicate() {
        return this.predicate;
    }

    public String getUri() {
        return this.uri;
    }

    public String getObject() {
        return this.object;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RDFTriplet that = (RDFTriplet) o;

        if (predicate != null ? !predicate.equals(that.predicate) : that.predicate != null) return false;
        return object != null ? object.equals(that.object) : that.object == null;
    }

    @Override
    public int hashCode() {
        int result = predicate != null ? predicate.hashCode() : 0;
        result = 31 * result + (object != null ? object.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "RDFTriplet{" +
                "uri='" + uri + '\'' +
                ", predicate='" + predicate + '\'' +
                ", object='" + object + '\'' +
                '}';
    }
}
