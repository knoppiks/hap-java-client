package de.knoppiks.hap.client.model;

import com.cognitect.transit.Keyword;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;

import java.util.Set;

import static com.google.common.base.Predicates.compose;
import static com.google.common.collect.ImmutableSet.copyOf;
import static com.google.common.collect.ImmutableSet.of;
import static com.google.common.collect.Iterables.transform;
import static de.knoppiks.hap.client.model.Operation.DELETE;
import static de.knoppiks.hap.client.model.Operation.UPDATE;

/**
 * @author <a href="mailto:jwagner@knoppiks.de">Jonas Wagner</a>
 */
public final class Operations {

    private static final Function<Operation, Keyword> TO_KEYWORD = new Function<Operation, Keyword>() {
        @Override
        public Keyword apply(Operation operation) {
            return operation.getKeyword();
        }
    };
    private static final Set<Operation> OPERATIONS = of(DELETE, UPDATE);
    private static final Set<Keyword> OPERATION_KEYWORDS = copyOf(transform(OPERATIONS, TO_KEYWORD));
    private static final Predicate<Keyword> IS_OPERATION = new Predicate<Keyword>() {
        @Override
        public boolean apply(Keyword keyword) {
            return isOperation(keyword);
        }
    };

    private Operations() {
    }

    public static Set<Keyword> operationKeywords() {
        return OPERATION_KEYWORDS;
    }

    public static boolean isOperation(Keyword keyword) {
        return OPERATION_KEYWORDS.contains(keyword);
    }

    public static Predicate<Keyword> isOperation() {
        return IS_OPERATION;
    }

    public static Function<Operation, Keyword> toKeyword() {
        return TO_KEYWORD;
    }

    public static Optional<Operation> toOperation(Keyword keyword) {
        return FluentIterable.from(OPERATIONS)
                .filter(compose(IS_OPERATION, TO_KEYWORD))
                .first();
    }
}
