package de.knoppiks.hap.client.parser;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import java.util.List;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author <a href="mailto:jwagner@knoppiks.de">Jonas Wagner</a>
 */
class CollectionTransformers {

    public static <T> ListTransformer<T> listOf(Transformer<T> valueTransformer) {
        return new ListTransformer<T>(checkNotNull(valueTransformer));
    }

    public static <T> SetTransformer<T> setOf(Transformer<T> valueTransformer) {
        return new SetTransformer<T>(checkNotNull(valueTransformer));
    }

    static class ListTransformer<V> extends Transformer<List<V>> {

        private final Transformer<V> valueTransformer;

        public ListTransformer(Transformer<V> valueTransformer) {
            this.valueTransformer = valueTransformer;
        }

        @Override
        public List<V> transform(Object obj) throws TransformException {
            List<?> list = (List<?>) obj;
            ImmutableList.Builder<V> builder = ImmutableList.builder();
            for (Object entry : list) {
                builder.add(valueTransformer.transform(entry));
            }
            return builder.build();
        }
    }

    static class SetTransformer<V> extends Transformer<Set<V>> {

        private final Transformer<V> valueTransformer;

        public SetTransformer(Transformer<V> valueTransformer) {
            this.valueTransformer = valueTransformer;
        }

        @Override
        public Set<V> transform(Object obj) throws TransformException {
            Set<?> set = (Set<?>) obj;
            ImmutableSet.Builder<V> builder = ImmutableSet.builder();
            for (Object entry : set) {
                builder.add(valueTransformer.transform(entry));
            }
            return builder.build();
        }

    }
}
