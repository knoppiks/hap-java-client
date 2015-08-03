package de.knoppiks.hap.client;

import com.cognitect.transit.TransitFactory.Format;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.net.HttpHeaders;
import de.knoppiks.hap.client.parser.ParseException;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpCoreContext;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import static com.google.common.base.Optional.fromNullable;
import static com.google.common.collect.ImmutableList.of;
import static com.google.common.net.HttpHeaders.CONTENT_TYPE;
import static de.knoppiks.hap.client.MediaTypes.fromFormat;
import static de.knoppiks.hap.client.MediaTypes.fromMediaType;
import static de.knoppiks.hap.client.parser.HapParser.parseEntity;
import static java.lang.String.format;
import static org.apache.http.HttpHeaders.ACCEPT;
import static org.apache.http.HttpHeaders.ETAG;
import static org.apache.http.util.EntityUtils.toByteArray;

/**
 * @author <a href="mailto:jwagner@knoppiks.de">Jonas Wagner</a>
 */
class HapHttpClient {

    private static final List<String> FORBIDDEN_HEADERS = of(HttpHeaders.ACCEPT);
    private static final Function<Header, String> GET_HEADER_VALUE = new Function<Header, String>() {
        @Override
        public String apply(Header input) {
            return input.getValue();
        }
    };

    private final HttpClient client;
    private final BasicHeader acceptHeader;

    HapHttpClient(HttpClient client, Format format) {
        this.client = client;
        this.acceptHeader = new BasicHeader(ACCEPT, fromFormat(format));
    }

    private static void addHeaders(HttpUriRequest request, List<Header> headers) {
        for (Header header : headers) {
            if (!FORBIDDEN_HEADERS.contains(header.getName())) {
                request.addHeader(header);
            } else {
                throw new IllegalArgumentException("The header '" + header.getName() + "' is forbidden.");
            }
        }
    }

    private static URI createBaseUri(HttpContext context) {
        HttpHost host = (HttpHost) context.getAttribute(HttpCoreContext.HTTP_TARGET_HOST);
        HttpRequest request = (HttpRequest) context.getAttribute(HttpCoreContext.HTTP_REQUEST);
        return URI.create(host.toURI()).resolve(request.getRequestLine().getUri());
    }

    private static Format extractFormat(HttpResponse response) throws WrongContentTypeException {
        Optional<Header> typeHeader = fromNullable(response.getFirstHeader(CONTENT_TYPE));
        if (typeHeader.isPresent()) {
            Optional<Format> format = fromMediaType(typeHeader.get().getValue());
            if (format.isPresent()) {
                return format.get();
            } else {
                throw new WrongContentTypeException(
                        format("Unexpected Content-Type %s in: %s", typeHeader.get().getValue(), response));
            }
        } else {
            throw new IllegalArgumentException(format("No Content-Type is set in: %s", response));
        }
    }

    HapHttpResponse execute(HttpUriRequest request, List<Header> headers)
            throws IOException, ParseException, WrongContentTypeException {
        HttpContext context = new BasicHttpContext();
        addHeaders(request, headers);
        request.addHeader(acceptHeader);

        HttpResponse response = client.execute(request, context);

        return createResponse(response, createBaseUri(context));
    }

    private HapHttpResponse createResponse(HttpResponse response, URI baseUri)
            throws IOException, ParseException, WrongContentTypeException {
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            byte[] bytes = toByteArray(entity);
            if (bytes.length > 0) {
                Optional<String> eTag = Optional.fromNullable(response.getFirstHeader(ETAG))
                        .transform(GET_HEADER_VALUE);
                return new HapHttpResponse(response, baseUri,
                        Optional.of(parseEntity(baseUri, extractFormat(response), bytes, eTag)));
            }
        }
        return new HapHttpResponse(response, baseUri, Optional.<HapEntity>absent());
    }
}
