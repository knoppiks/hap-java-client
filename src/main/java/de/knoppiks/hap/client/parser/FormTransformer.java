package de.knoppiks.hap.client.parser;

import com.cognitect.transit.Keyword;
import com.google.common.base.Optional;
import de.knoppiks.hap.client.model.Form;
import de.knoppiks.hap.client.model.Param;

import java.net.URI;
import java.util.Map;

import static com.google.common.base.Optional.fromNullable;
import static com.google.common.base.Preconditions.checkNotNull;
import static de.knoppiks.hap.client.parser.MapTransformers.keywordMapOf;
import static de.knoppiks.hap.client.parser.Transformers.URITransformer;
import static de.knoppiks.hap.client.parser.Transformers.castToMap;
import static de.knoppiks.hap.client.parser.Transformers.stringTransformer;

/**
 * @author <a href="mailto:jwagner@knoppiks.de">Jonas Wagner</a>
 */
class FormTransformer extends Transformer<Form> {

    private final Transformer<URI> uriTransformer;

    public FormTransformer(URI baseUri) {
        this.uriTransformer = URITransformer(checkNotNull(baseUri));
    }

    @Override
    public Form transform(Object obj) throws TransformException {
        Map map = castToMap(obj);

        if (!map.containsKey(Form.TARGET)) {
            throw new TransformException("Missing :href.");
        }
        if (!map.containsKey(Form.PARAMS)) {
            throw new TransformException("Missing :params.");
        }

        URI target = uriTransformer.transform(map.get(Form.TARGET));
        Map<Keyword, Param> params = keywordMapOf(new ParamTransformer()).transform(map.get(Form.PARAMS));
        Optional<String> title = fromNullable(map.get(Form.LABEL)).transform(stringTransformer());

        return new Form(target, params, title);
    }
}
