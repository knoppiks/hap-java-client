package de.knoppiks.hap.client.parser;

import com.cognitect.transit.Reader;
import com.cognitect.transit.TransitFactory;
import com.cognitect.transit.TransitFactory.Format;
import com.google.common.base.Optional;
import de.knoppiks.hap.client.model.HapEntity;

import java.io.ByteArrayInputStream;
import java.net.URI;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author <a href="mailto:jwagner@knoppiks.de">Jonas Wagner</a>
 */
public class HapParser {
    public static HapEntity parseEntity(URI baseUri, Format format, byte[] bytes, Optional<String> eTag)
            throws ParseException {
        checkNotNull(baseUri);
        checkNotNull(bytes);

        HapEntityTransformer transformer = new HapEntityTransformer(baseUri, eTag);
        try {
            return transformer.transform(parse(bytes, format));
        } catch (TransformException e) {
            throw new ParseException("Semantic error: " + e.getMessage(), e);
        }
    }

    private static Object parse(byte[] bytes, Format format) {
        Reader reader = TransitFactory.reader(format, new ByteArrayInputStream(bytes));
        return reader.read();
    }
}
