package de.knoppiks.hap.client.model;

import com.cognitect.transit.Keyword;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Multimap;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import static com.google.common.collect.ImmutableSet.copyOf;
import static com.google.common.collect.Iterables.transform;
import static com.google.common.collect.Maps.newHashMap;
import static com.google.common.collect.Maps.transformValues;
import static com.google.common.collect.Multimaps.transformValues;

/**
 * @author <a href="mailto:jwagner@knoppiks.de">Jonas Wagner</a>
 */
public final class Serializer {

    private static final Function<Param, Map<?, ?>> PARAM_SERIALIZER =
            new Function<Param, Map<?, ?>>() {
                @Override
                public Map<?, ?> apply(Param param) {
                    return builder()
                            .put(Param.TYPE, param.getType())
                            .put(Param.DESCRIPTION, param.getDescription())
                            .put(Param.OPTIONAL, param.getDescription())
                            .build();
                }
            };
    private static final Function<AbstractExecutable, Map<?, ?>> EXECUTABLE_SERIALIZER =
            new Function<AbstractExecutable, Map<?, ?>>() {
                @Override
                public Map<?, ?> apply(AbstractExecutable executable) {
                    return builder()
                            .put(Query.PARAMS, serializeParams(executable.getParams()))
                            .put(Query.TARGET, executable.getTarget())
                            .put(Query.TITLE, executable.getTitle())
                            .build();
                }
            };
    private static final Function<Link, Map<?, ?>> LINK_SERIALIZER =
            new Function<Link, Map<?, ?>>() {
                @Override
                public Map<?, ?> apply(Link link) {
                    return builder()
                            .put(Link.HREF, link.getHref())
                            .put(Link.LABEL, link.getLabel())
                            .build();
                }
            };

    private static final Function<Collection<Map<?, ?>>, Object> COLLECTION_OR_SINGLE =
            new Function<Collection<Map<?, ?>>, Object>() {
                @Override
                public Object apply(Collection<Map<?, ?>> collection) {
                    return collection.size() == 1
                            ? Iterables.get(collection, 0)
                            : collection;
                }
            };
    private static final Function<Operation, Keyword> OPERATION_SERIALIZER =
            new Function<Operation, Keyword>() {
                @Override
                public Keyword apply(Operation operation) {
                    return operation.getKeyword();
                }
            };
    private static final Function<HapEntity, Map<?, ?>> HAP_ENTITY_SERIALIZER =
            new Function<HapEntity, Map<?, ?>>() {
                @Override
                public Map<?, ?> apply(HapEntity entity) {
                    return serialize(entity);
                }
            };

    private Serializer() {

    }

    public static Map<?, ?> serialize(HapEntity entity) {
        return builder()
                .put(HapEntity.QUERIES, serializeExecutable(entity.getQueries()))
                .put(HapEntity.FORMS, serializeExecutable(entity.getForms()))
                .put(HapEntity.LINKS, serializeLinks(entity.getLinks()))
                .put(HapEntity.EMBEDDED, serializeEmbedded(entity.getEmbedded()))
                .put(HapEntity.OPERATIONS, serializeOperations(entity.getOperations()))
                .put(HapEntity.DATA, entity.getData())
                .build();
    }

    private static Map<?, ?> serializeExecutable(final Map<Keyword, ? extends AbstractExecutable> queries) {
        return transformValues(queries, EXECUTABLE_SERIALIZER);
    }

    private static Map<?, ?> serializeParams(Map<Keyword, Param> params) {
        return transformValues(params, PARAM_SERIALIZER);
    }

    private static Map<?, ?> serializeLinks(ImmutableListMultimap<Keyword, Link> links) {
        return transformValues(transformValues(links, LINK_SERIALIZER).asMap(), COLLECTION_OR_SINGLE);
    }

    private static Map<?, ?> serializeEmbedded(Multimap<Keyword, HapEntity> embedded) {
        return transformValues(transformValues(embedded, HAP_ENTITY_SERIALIZER).asMap(), COLLECTION_OR_SINGLE);
    }

    private static Set<?> serializeOperations(Set<Operation> operations) {
        return copyOf(transform(operations, OPERATION_SERIALIZER));
    }

    private static NonEmptyValueMapBuilder builder() {
        return new NonEmptyValueMapBuilder();
    }

    /**
     * A MapBuilder ignoring (key, value) pairs with empty values. Values are considered empty if they are {@link
     * Optional#absent()}, empty {@link Map Maps}, empty {@link Collection Collections} or {@code null}.
     */
    private static class NonEmptyValueMapBuilder {

        private final Map<Object, Object> map;

        NonEmptyValueMapBuilder() {
            this.map = newHashMap();
        }

        NonEmptyValueMapBuilder put(Keyword key, Object value) {
            if (value == null) {
                return this;
            } else if (value instanceof Optional<?>) {
                return put(key, (Optional<?>) value);
            } else if (value instanceof Map<?, ?>) {
                return put(key, (Map<?, ?>) value);
            } else if (value instanceof Collection<?>) {
                return put(key, (Collection<?>) value);
            } else {
                map.put(key, value);
                return this;
            }
        }

        NonEmptyValueMapBuilder put(Keyword key, Map<?, ?> value) {
            if (!value.isEmpty()) {
                map.put(key, value);
            }
            return this;
        }

        NonEmptyValueMapBuilder put(Keyword key, Collection<?> value) {
            if (!value.isEmpty()) {
                map.put(key, value);
            }
            return this;
        }

        NonEmptyValueMapBuilder put(Keyword key, Optional<?> value) {
            if (value.isPresent()) {
                map.put(key, value.get());
            }
            return this;
        }

        Map<?, ?> build() {
            return map;
        }
    }
}
