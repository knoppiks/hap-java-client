package de.knoppiks.hap.client.model;

import com.cognitect.transit.Keyword;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.FluentIterable;

import java.util.Set;

import static com.google.common.base.Predicates.compose;
import static com.google.common.base.Predicates.equalTo;
import static com.google.common.collect.ImmutableSet.of;
import static de.knoppiks.hap.client.model.Operation.DELETE;
import static de.knoppiks.hap.client.model.Operation.UPDATE;

/**
 * @author <a href="mailto:jwagner@knoppiks.de">Jonas Wagner</a>
 */
public final class Operations {

    private static final Function<Operation, Keyword> TO_KEYWORD = new ToKeyword();
    private static final Set<Operation> OPERATIONS = of(DELETE, UPDATE);

    private Operations() {
    }

    public static Set<Operation> operations() {
        return OPERATIONS;
    }

    public static Optional<Operation> toOperation(Keyword keyword) {
        return FluentIterable.from(OPERATIONS)
                .filter(compose(equalTo(keyword), TO_KEYWORD))
                .first();
    }

    private static class ToKeyword implements Function<Operation, Keyword> {
        @Override
        public Keyword apply(Operation operation) {
            return operation.getKeyword();
        }
    }
}
