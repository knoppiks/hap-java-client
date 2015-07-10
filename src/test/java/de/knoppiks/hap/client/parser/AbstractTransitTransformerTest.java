package de.knoppiks.hap.client.parser;

import java.io.ByteArrayInputStream;
import java.util.ResourceBundle;

import static com.cognitect.transit.TransitFactory.Format.JSON;
import static com.cognitect.transit.TransitFactory.reader;

/**
 * @author <a href="mailto:jwagner@knoppiks.de">Jonas Wagner</a>
 */
public class AbstractTransitTransformerTest<T> {

    protected final Transformer<T> transformer;
    private final ResourceBundle resources;

    public AbstractTransitTransformerTest(Transformer<T> transformer) {
        this.transformer = transformer;
        this.resources = ResourceBundle.getBundle(this.getClass().getName());
    }

    protected String resource(String key) {
        return resources.getString(key);
    }

    protected T parse(String transitString) throws TransformException {
        return transformer.transform(reader(JSON, new ByteArrayInputStream(transitString.getBytes()))
                .read());
    }
}
