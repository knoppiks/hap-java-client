package de.knoppiks.hap.client.parser;

import com.cognitect.transit.Keyword;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Multimap;

import java.util.Map;

import static de.knoppiks.hap.client.parser.Transformers.KEYWORD_TRANSFORMER;
import static de.knoppiks.hap.client.parser.Transformers.castToMap;

/**
 * Transforms a map of untyped entries into a map of key and value pairs returned by the key and value transformers.
 * <p/>
 * Skips invalid keys and values.
 *
 * @author <a href="mailto:jwagner@knoppiks.de">Jonas Wagner</a>
 */
class MapTransformers {

    public static <T> Transformer<Map<Keyword, T>> keywordMapOf(Transformer<T> valueTransformer) {
        return mapTransformer(KEYWORD_TRANSFORMER, valueTransformer);
    }

    public static <K, V> Transformer<Map<K, V>> mapTransformer(Transformer<K> keyTransformer,
                                                               Transformer<V> valueTransformer) {
        return new MapTransformer<>(keyTransformer, valueTransformer);
    }

    public static <T> Transformer<Multimap<Keyword, T>> keywordMultimapOf(Transformer<T> valueTransformer) {
        return multimapTransformer(KEYWORD_TRANSFORMER, valueTransformer);
    }

    public static <K, V> Transformer<Multimap<K, V>> multimapTransformer(Transformer<K> keyTransformer,
                                                                         Transformer<V> valueTransformer) {
        return new MultimapTransformer<>(keyTransformer, valueTransformer);
    }

    private static class MapTransformer<K, V> extends Transformer<Map<K, V>> {
        private final Transformer<K> keyTransformer;
        private final Transformer<V> valueTransformer;

        public MapTransformer(Transformer<K> keyTransformer, Transformer<V> valueTransformer) {
            this.keyTransformer = keyTransformer;
            this.valueTransformer = valueTransformer;
        }

        @Override
        public Map<K, V> transform(Object obj) throws TransformException {
            Map<?, ?> map = castToMap(obj);
            ImmutableMap.Builder<K, V> builder = ImmutableMap.builder();
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                builder.put(keyTransformer.transform(entry.getKey()), valueTransformer.transform(entry.getValue()));
            }
            return builder.build();
        }
    }

    private static class MultimapTransformer<K, V> extends Transformer<Multimap<K, V>> {
        private final Transformer<K> keyTransformer;
        private final Transformer<V> valueTransformer;

        public MultimapTransformer(Transformer<K> keyTransformer, Transformer<V> valueTransformer) {
            this.keyTransformer = keyTransformer;
            this.valueTransformer = valueTransformer;
        }

        @Override
        public Multimap<K, V> transform(Object obj) throws TransformException {
            Map<?, ?> map = castToMap(obj);
            ImmutableMultimap.Builder<K, V> builder = ImmutableMultimap.builder();
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                if (entry.getValue() instanceof Iterable<?>) {
                    builder.putAll(keyTransformer.transform(entry.getKey()),
                            Iterables.transform((Iterable<?>) entry.getValue(), valueTransformer));
                } else {
                    builder.put(keyTransformer.transform(entry.getKey()), valueTransformer.transform(entry.getValue()));
                }
            }
            return builder.build();
        }
    }
}
