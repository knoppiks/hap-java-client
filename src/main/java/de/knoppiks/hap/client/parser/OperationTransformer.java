package de.knoppiks.hap.client.parser;

import com.cognitect.transit.Keyword;
import com.google.common.base.Optional;
import de.knoppiks.hap.client.model.Operation;

import static de.knoppiks.hap.client.model.Operations.operationKeywords;
import static de.knoppiks.hap.client.model.Operations.toOperation;
import static java.lang.String.format;

/**
 * @author <a href="mailto:jwagner@knoppiks.de">Jonas Wagner</a>
 */
class OperationTransformer extends Transformer<Operation> {

    @Override
    public Operation transform(final Object obj) throws TransformException {
        if (obj instanceof Keyword) {
            Optional<Operation> operation = toOperation((Keyword) obj);
            if (operation.isPresent()) {
                return operation.get();
            } else {
                throw new TransformException(format("Operation must be one of %s but was %s.",
                        operationKeywords(), obj.toString()));
            }
        } else {
            throw new TransformException("Operation must be a keyword, but was: " + obj.getClass());
        }
    }
}
