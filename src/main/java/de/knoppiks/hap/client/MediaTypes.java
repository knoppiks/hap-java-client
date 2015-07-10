package de.knoppiks.hap.client;

import com.cognitect.transit.TransitFactory.Format;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableBiMap;

import static com.cognitect.transit.TransitFactory.Format.JSON;
import static com.cognitect.transit.TransitFactory.Format.JSON_VERBOSE;
import static com.cognitect.transit.TransitFactory.Format.MSGPACK;
import static com.google.common.base.Optional.fromNullable;

/**
 * @author <a href="mailto:jwagner@knoppiks.de">Jonas Wagner</a>
 */
public final class MediaTypes {

    private static final ImmutableBiMap<Format, String> TYPES = ImmutableBiMap.of(
            JSON, "application/transit+json",
            JSON_VERBOSE, "application/transit+json;verbose",
            MSGPACK, "application/transit+msgpack");

    private MediaTypes() {
    }

    public static String fromFormat(Format format) {
        return TYPES.get(format);
    }

    public static Optional<Format> fromMediaType(String mediaType) {
        return fromNullable(TYPES.inverse().get(mediaType));
    }
}
