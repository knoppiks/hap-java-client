package de.knoppiks.hap.client;

import com.cognitect.transit.TransitFactory;
import org.apache.http.client.methods.HttpUriRequest;

/**
 * @author <a href="mailto:jwagner@knoppiks.de">Jonas Wagner</a>
 */
abstract class RequestBuilder {

    abstract HttpUriRequest build(TransitFactory.Format format);
}
