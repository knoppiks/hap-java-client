package de.knoppiks.hap.client;

import de.knoppiks.hap.client.model.HapEntity;
import de.knoppiks.hap.client.model.Link;
import de.knoppiks.hap.client.parser.ParseException;
import org.apache.http.Header;

import java.io.IOException;
import java.net.URI;
import java.util.List;

/**
 * @author <a href="mailto:jwagner@knoppiks.de">Jonas Wagner</a>
 */
public interface HapClient {

    /* Fetch - simple get */
    HapEntity fetch(URI uri) throws ParseException, WrongContentTypeException, IOException;

    HapEntity fetch(URI uri, List<Header> headers)
            throws ParseException, WrongContentTypeException, IOException;

    HapEntity fetch(Link link) throws ParseException, WrongContentTypeException, IOException;

    HapEntity fetch(Link link, List<Header> headers)
            throws ParseException, WrongContentTypeException, IOException;

    /* Execute - get with params */
    HapEntity execute(QueryRequestBuilder request)
            throws ParseException, WrongContentTypeException, IOException;

    HapEntity execute(QueryRequestBuilder request, List<Header> headers)
            throws ParseException, WrongContentTypeException, IOException;

    /* Create - post */
    URI create(CreateRequestBuilder request)
            throws ParseException, WrongContentTypeException, IOException;

    URI create(CreateRequestBuilder request, List<Header> headers)
            throws ParseException, WrongContentTypeException, IOException;

    /* Delete */
    void delete(URI uri, List<Header> headers)
            throws ParseException, WrongContentTypeException, IOException;

    void delete(URI uri) throws ParseException, WrongContentTypeException, IOException;

    void delete(Link link, List<Header> headers)
            throws ParseException, WrongContentTypeException, IOException;

    void delete(Link link) throws ParseException, WrongContentTypeException, IOException;

    /* Update */
    HapEntity update(HapEntity newHapEntity) throws ParseException, WrongContentTypeException, IOException;

    HapEntity update(HapEntity newHapEntity, List<Header> headers)
            throws ParseException, WrongContentTypeException, IOException;
}
