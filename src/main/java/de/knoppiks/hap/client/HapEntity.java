package de.knoppiks.hap.client;

import com.cognitect.transit.Keyword;
import com.google.common.base.MoreObjects;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;
import de.knoppiks.hap.client.model.Form;
import de.knoppiks.hap.client.model.Link;
import de.knoppiks.hap.client.model.Operation;
import de.knoppiks.hap.client.model.Query;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.cognitect.transit.TransitFactory.keyword;
import static com.google.common.base.Optional.fromNullable;
import static com.google.common.base.Predicates.equalTo;
import static com.google.common.base.Predicates.not;
import static com.google.common.collect.ImmutableListMultimap.copyOf;
import static com.google.common.collect.ImmutableMap.copyOf;
import static com.google.common.collect.Maps.newHashMap;
import static com.google.common.collect.Multimaps.filterKeys;

/**
 * @author <a href="mailto:jwagner@knoppiks.de">Jonas Wagner</a>
 */
public class HapEntity {

    public static final Keyword QUERIES = keyword("queries");
    public static final Keyword FORMS = keyword("forms");
    public static final Keyword LINKS = keyword("links");
    public static final Keyword EMBEDDED = keyword("embedded");
    public static final Keyword OPERATIONS = keyword("ops");
    public static final Keyword DATA = keyword("data");

    private final ImmutableMap<Keyword, Query> queries;
    private final ImmutableMap<Keyword, Form> forms;
    private final ImmutableListMultimap<Keyword, Link> links;
    private final ImmutableListMultimap<Keyword, HapEntity> embedded;
    private final ImmutableSet<Operation> operations;
    private final Object data;
    private final Optional<String> eTag;

    public HapEntity(Map<Keyword, Query> queries, Map<Keyword, Form> forms, Multimap<Keyword, Link> links,
                     Multimap<Keyword, HapEntity> embedded, Set<Operation> operations,
                     Object data, Optional<String> eTag) {
        this.queries = copyOf(queries);
        this.forms = copyOf(forms);
        this.links = ImmutableListMultimap.copyOf(links);
        this.embedded = ImmutableListMultimap.copyOf(embedded);
        this.operations = ImmutableSet.copyOf(operations);
        this.data = data;
        this.eTag = eTag;
    }

    /**
     * Merge b into a.
     */
    @SuppressWarnings("unchecked")
    private static Object merge(Object i, Object f) {
        if (Map.class.isInstance(i) && Map.class.isInstance(f)) {
            Map<Object, Object> into = newHashMap((Map<Object, Object>) i), from = (Map<Object, Object>) f;
            for (Map.Entry<Object, Object> entry : from.entrySet()) {
                if (into.containsKey(entry.getKey())) {
                    into.put(entry.getKey(), merge(into.get(entry.getKey()), entry.getValue()));
                } else {
                    into.put(entry.getKey(), entry.getValue());
                }
            }
            return into;
        } else {
            return f;
        }
    }

    ImmutableMap<Keyword, Query> getQueries() {
        return queries;
    }

    public Optional<Query> getQuery(Keyword key) {
        return fromNullable(queries.get(key));
    }

    ImmutableMap<Keyword, Form> getForms() {
        return forms;
    }

    public Optional<Form> getForm(Keyword key) {
        return fromNullable(forms.get(key));
    }

    ImmutableListMultimap<Keyword, Link> getLinks() {
        return links;
    }

    public List<Link> getLinks(Keyword key) {
        return links.get(key);
    }

    public HapEntity removeLinks(Keyword key) {
        return new HapEntity(queries, forms, copyOf(filterKeys(links, not(equalTo(key)))),
                embedded, operations, data, eTag);
    }

    public HapEntity addLink(Keyword key, Link link) {
        return new HapEntity(queries, forms, ImmutableListMultimap.<Keyword, Link>builder().putAll(links).put(key, link)
                .build(), embedded, operations, data, eTag);
    }

    public Object getData() {
        return data;
    }

    public HapEntity updateData(Object data) {
        return new HapEntity(queries, forms, links, embedded, operations, merge(this.data, data), eTag);
    }

    ImmutableMultimap<Keyword, HapEntity> getEmbedded() {
        return embedded;
    }

    public List<HapEntity> getEmbedded(Keyword key) {
        return embedded.get(key);
    }

    ImmutableSet<Operation> getOperations() {
        return operations;
    }

    public boolean operationAllowed(Operation operation) {
        return operations.contains(operation);
    }

    public Optional<String> getETag() {
        return eTag;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add(QUERIES.toString(), queries)
                .add(FORMS.toString(), forms)
                .add(LINKS.toString(), links)
                .add(EMBEDDED.toString(), embedded)
                .add(OPERATIONS.toString(), operations)
                .add(DATA.toString(), data)
                .toString();
    }
}
