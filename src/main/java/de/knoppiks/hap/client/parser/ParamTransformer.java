package de.knoppiks.hap.client.parser;

import com.google.common.base.Optional;
import de.knoppiks.hap.client.model.Param;

import java.util.Map;

import static com.google.common.base.Optional.fromNullable;
import static de.knoppiks.hap.client.parser.Transformers.castToMap;
import static de.knoppiks.hap.client.parser.Transformers.stringTransformer;

/**
 * @author <a href="mailto:jwagner@knoppiks.de">Jonas Wagner</a>
 */
class ParamTransformer extends Transformer<Param> {

    @Override
    public Param transform(Object obj) throws TransformException {
        Map<?, ?> map = castToMap(obj);

        Optional<String> description = fromNullable(map.get(Param.DESCRIPTION)).transform(stringTransformer());
        boolean optional = fromNullable((Boolean) map.get(Param.OPTIONAL)).or(false);

        return new Param(map.get(Param.TYPE).toString(), description, optional);
    }
}
