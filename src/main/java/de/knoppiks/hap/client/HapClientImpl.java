package de.knoppiks.hap.client;


import com.cognitect.transit.TransitFactory.Format;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.net.HttpHeaders;
import de.knoppiks.hap.client.model.HapEntity;
import de.knoppiks.hap.client.model.Link;
import de.knoppiks.hap.client.parser.ParseException;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ByteArrayEntity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.List;

import static com.cognitect.transit.TransitFactory.keyword;
import static com.cognitect.transit.TransitFactory.writer;
import static com.google.common.net.HttpHeaders.LOCATION;
import static de.knoppiks.hap.client.MediaTypes.fromFormat;
import static de.knoppiks.hap.client.model.Operation.UPDATE;
import static de.knoppiks.hap.client.model.Serializer.serialize;
import static java.lang.String.format;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_NO_CONTENT;

/**
 * @author <a href="mailto:jwagner@knoppiks.de">Jonas Wagner</a>
 */
public class HapClientImpl implements HapClient {

    private final Format format;
    private final HapHttpClient client;

    HapClientImpl(HapHttpClient client, Format format) {
        this.client = client;
        this.format = format;
    }

    private static void checkHeaderExists(HapHttpResponse response, String header) {
        if (response.getResponse().getFirstHeader(header) == null) {
            throw new IllegalStateException("The Location header must contain the URI of the created representation");
        }
    }

    private static void checkStatus(HapHttpResponse response, String action, int status) {
        HttpResponse response1 = response.getResponse();
        if (response1.getStatusLine().getStatusCode() != status) {
            throw new IllegalStateException(format("%s should respond with %d but returned %s",
                    action, status, response1.getStatusLine()));
        }
    }

    @Override
    public HapEntity fetch(URI uri) throws ParseException, WrongContentTypeException, IOException {
        return fetch(uri, ImmutableList.<Header>of());
    }

    @Override
    public HapEntity fetch(URI uri, List<Header> headers)
            throws ParseException, WrongContentTypeException, IOException {
        return checkEntityExists(client.execute(new HttpGet(uri), headers), "Fetch");
    }

    private HapEntity checkEntityExists(HapHttpResponse response, final String action) {
        Optional<HapEntity> entity = response.getEntity();
        if (entity.isPresent()) {
            return entity.get();
        } else {
            throw new IllegalStateException(format("%s must return an response but the response was %s.",
                    action, response.getResponse()));
        }
    }

    @Override
    public HapEntity fetch(Link link) throws ParseException, WrongContentTypeException, IOException {
        return fetch(link, ImmutableList.<Header>of());
    }

    @Override
    public HapEntity fetch(Link link, List<Header> headers)
            throws ParseException, WrongContentTypeException, IOException {
        return fetch(link.getHref(), headers);
    }

    @Override
    public HapEntity execute(QueryRequestBuilder request)
            throws ParseException, WrongContentTypeException, IOException {
        return execute(request, ImmutableList.<Header>of());
    }

    @Override
    public HapEntity execute(QueryRequestBuilder request, List<Header> headers)
            throws ParseException, WrongContentTypeException, IOException {
        return checkEntityExists(client.execute(request.build(format), headers), "Execute");
    }

    @Override
    public URI create(CreateRequestBuilder request)
            throws ParseException, WrongContentTypeException, IOException {
        return create(request, ImmutableList.<Header>of());
    }

    @Override
    public URI create(CreateRequestBuilder request, List<Header> headers)
            throws ParseException, WrongContentTypeException, IOException {
        HapHttpResponse response = client.execute(request.build(format), headers);

        checkStatus(response, "Create", SC_CREATED);
        checkHeaderExists(response, LOCATION);

        return response.getBaseUri().resolve(response.getResponse().getFirstHeader(LOCATION).getValue());
    }

    @Override
    public void delete(URI uri, List<Header> headers)
            throws ParseException, WrongContentTypeException, IOException {
        HapHttpResponse response = client.execute(new HttpDelete(uri), headers);

        checkStatus(response, "Delete", SC_NO_CONTENT);
    }

    @Override
    public void delete(URI uri) throws ParseException, WrongContentTypeException, IOException {
        delete(uri, ImmutableList.<Header>of());
    }

    @Override
    public void delete(Link link, List<Header> headers)
            throws ParseException, WrongContentTypeException, IOException {
        delete(link.getHref(), headers);
    }

    @Override
    public void delete(Link link) throws ParseException, WrongContentTypeException, IOException {
        delete(link, ImmutableList.<Header>of());
    }

    @Override
    public HapEntity update(HapEntity newHapEntity)
            throws ParseException, WrongContentTypeException, IOException {
        return update(newHapEntity, ImmutableList.<Header>of());
    }

    @Override
    public HapEntity update(HapEntity newHapEntity, List<Header> headers)
            throws ParseException, WrongContentTypeException, IOException {
        URI selfLink = newHapEntity.getLinks(keyword("self")).get(0).getHref();

        HttpPut post = new HttpPut(selfLink);
        post.addHeader(HttpHeaders.CONTENT_TYPE, fromFormat(format));

        if (newHapEntity.getETag().isPresent() && newHapEntity.operationAllowed(UPDATE)) {
            post.addHeader(HttpHeaders.IF_MATCH, newHapEntity.getETag().get());
        } else {
            throw new IllegalArgumentException("The HapEntity is not editable.");
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        writer(format, out).write(serialize(newHapEntity));
        ByteArrayEntity entity = new ByteArrayEntity(out.toByteArray());
        post.setEntity(entity);

        HapHttpResponse response = client.execute(post, headers);
        checkStatus(response, "Update", SC_NO_CONTENT);

        return fetch(selfLink);
    }
}
