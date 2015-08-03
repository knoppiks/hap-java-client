package de.knoppiks.hap.client;

import com.google.common.base.Optional;
import org.apache.http.HttpResponse;

import java.net.URI;

/**
 * @author <a href="mailto:jwagner@knoppiks.de">Jonas Wagner</a>
 */
class HapHttpResponse {

    private final HttpResponse response;
    private final Optional<HapEntity> entity;
    private final URI baseUri;

    HapHttpResponse(HttpResponse response, URI baseUri, Optional<HapEntity> entity) {
        this.response = response;
        this.entity = entity;
        this.baseUri = baseUri;
    }

    HttpResponse getResponse() {
        return response;
    }

    Optional<HapEntity> getEntity() {
        return entity;
    }

    URI getBaseUri() {
        return baseUri;
    }
}
