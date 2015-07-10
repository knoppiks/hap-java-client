package de.knoppiks.hap.client.parser;

import com.google.common.base.Optional;
import de.knoppiks.hap.client.model.Link;

import java.net.URI;
import java.util.Map;

import static com.google.common.base.Optional.fromNullable;
import static de.knoppiks.hap.client.parser.Transformers.URITransformer;
import static de.knoppiks.hap.client.parser.Transformers.castToMap;
import static de.knoppiks.hap.client.parser.Transformers.stringTransformer;

/**
 * @author <a href="mailto:jwagner@knoppiks.de">Jonas Wagner</a>
 */
class LinkTransformer extends Transformer<Link> {

    private final URI baseUri;

    public LinkTransformer(URI baseUri) {
        this.baseUri = baseUri;
    }

    @Override
    public Link transform(Object obj) throws TransformException {
        Map<?, ?> map = castToMap(obj);

        URI href = URITransformer(baseUri).transform(map.get(Link.HREF));
        Optional<String> label = fromNullable(map.get(Link.LABEL)).transform(stringTransformer());

        return new Link(href, label);
    }
}
