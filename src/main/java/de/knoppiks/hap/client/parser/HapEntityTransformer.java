package de.knoppiks.hap.client.parser;

import com.cognitect.transit.Keyword;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;
import de.knoppiks.hap.client.model.Form;
import de.knoppiks.hap.client.model.HapEntity;
import de.knoppiks.hap.client.model.Link;
import de.knoppiks.hap.client.model.Operation;
import de.knoppiks.hap.client.model.Query;

import java.net.URI;
import java.util.Map;
import java.util.Set;

import static de.knoppiks.hap.client.model.HapEntity.DATA;
import static de.knoppiks.hap.client.model.HapEntity.EMBEDDED;
import static de.knoppiks.hap.client.model.HapEntity.FORMS;
import static de.knoppiks.hap.client.model.HapEntity.LINKS;
import static de.knoppiks.hap.client.model.HapEntity.OPERATIONS;
import static de.knoppiks.hap.client.model.HapEntity.QUERIES;
import static de.knoppiks.hap.client.parser.CollectionTransformers.setOf;
import static de.knoppiks.hap.client.parser.MapTransformers.keywordMapOf;
import static de.knoppiks.hap.client.parser.MapTransformers.keywordMultimapOf;

/**
 * @author <a href="mailto:jwagner@knoppiks.de">Jonas Wagner</a>
 */
class HapEntityTransformer extends Transformer<HapEntity> {

    private final URI baseUri;
    private final Optional<String> eTag;

    public HapEntityTransformer(URI baseUri, Optional<String> eTag) {
        this.baseUri = baseUri;
        this.eTag = eTag;
    }

    @Override
    public HapEntity transform(Object raw) throws TransformException {
        Map map = raw == null ? ImmutableMap.of() : (Map) raw;

        return new HapEntity(
                extractQueries(map, QUERIES),
                extractForms(map, FORMS),
                extractLinks(map, LINKS),
                extractEmbedded(map, EMBEDDED),
                extractOperations(map, OPERATIONS),
                extractData(map, DATA),
                eTag);
    }

    private Map<Keyword, Query> extractQueries(Map map, Keyword keyword) throws TransformException {
        if (map.containsKey(keyword)) {
            return keywordMapOf(new QueryTransformer(baseUri)).transform(map.get(keyword));
        } else {
            return ImmutableMap.of();
        }
    }

    private Map<Keyword, Form> extractForms(Map map, Keyword keyword) throws TransformException {
        if (map.containsKey(keyword)) {
            return keywordMapOf(new FormTransformer(baseUri)).transform(map.get(keyword));
        } else {
            return ImmutableMap.of();
        }
    }

    private Multimap<Keyword, Link> extractLinks(Map map, Keyword keyword) throws TransformException {
        if (map.containsKey(keyword)) {
            return keywordMultimapOf(new LinkTransformer(baseUri)).transform(map.get(keyword));
        } else {
            return ImmutableMultimap.of();
        }
    }

    private Multimap<Keyword, HapEntity> extractEmbedded(Map map, Keyword keyword) throws TransformException {
        if (map.containsKey(keyword)) {
            return keywordMultimapOf(new HapEntityTransformer(baseUri, Optional.<String>absent()))
                    .transform(map.get(keyword));
        } else {
            return ImmutableMultimap.of();
        }
    }

    private Set<Operation> extractOperations(Map<?, ?> map, Keyword keyword) throws TransformException {
        if (map.containsKey(keyword)) {
            Object obj = map.get(keyword);
            return setOf(new OperationTransformer()).transform(obj);
        } else {
            return ImmutableSet.of();
        }
    }

    private Object extractData(Map map, Keyword keyword) throws TransformException {
        return map.get(keyword);
    }
}
