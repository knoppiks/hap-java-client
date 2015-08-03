package de.knoppiks.hap.client;

import com.cognitect.transit.Keyword;
import com.cognitect.transit.TransitFactory;
import org.apache.http.client.methods.HttpUriRequest;

/**
 * @author <a href="mailto:jwagner@knoppiks.de">Jonas Wagner</a>
 */
public abstract class RequestBuilder<T> {

    public abstract RequestBuilder<T> put(Keyword param, Object value);

    abstract HttpUriRequest build(TransitFactory.Format format);
}
