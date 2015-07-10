package de.knoppiks.hap.client.model;

import com.cognitect.transit.Keyword;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSet;
import org.junit.Test;

import java.net.URI;
import java.util.List;
import java.util.Map;

import static com.cognitect.transit.TransitFactory.keyword;
import static com.google.common.truth.Truth.assertThat;
import static de.knoppiks.hap.client.model.Serializer.serialize;

/**
 * @author <a href="mailto:jwagner@knoppiks.de">Jonas Wagner</a>
 */
public class SerializerTest {

    @Test
    public void empty() throws Exception {
        HapEntity entity = emptyHapEntity("092202");

        Map<?, ?> serialize = serialize(entity);
        assertThat(serialize).doesNotContainKey(keyword("queries"));
        assertThat(serialize).doesNotContainKey(keyword("forms"));
        assertThat(serialize).doesNotContainKey(keyword("links"));
        assertThat(serialize).doesNotContainKey(keyword("embedded"));
        assertThat(serialize).doesNotContainKey(keyword("ops"));
        assertThat(serialize).doesNotContainKey(keyword("data"));
    }

    private HapEntity emptyHapEntity(String eTag) {
        return new HapEntity(
                ImmutableMap.<Keyword, Query>of(),
                ImmutableMap.<Keyword, Form>of(),
                ImmutableListMultimap.<Keyword, Link>of(),
                ImmutableMultimap.<Keyword, HapEntity>of(),
                ImmutableSet.<Operation>of(),
                ImmutableMap.<Keyword, Object>of(), Optional.of(eTag));
    }

    @Test
    public void singleLinkIsMap() throws Exception {
        Keyword key = keyword("085455");

        HapEntity entity = new HapEntity(
                ImmutableMap.<Keyword, Query>of(),
                ImmutableMap.<Keyword, Form>of(),
                ImmutableListMultimap.of(key, new Link(URI.create("http://localhost/uri-135714"),
                        Optional.<String>absent())),
                ImmutableMultimap.<Keyword, HapEntity>of(),
                ImmutableSet.<Operation>of(),
                ImmutableMap.<Keyword, Object>of(), Optional.of("174432"));

        Map<?, ?> serialize = serialize(entity);
        assertThat(((Map<?, ?>) serialize.get(keyword("links"))).get(key))
                .isInstanceOf(Map.class);
    }

    @Test
    public void multipleLinksAreList() throws Exception {
        Keyword key = keyword("222556");

        HapEntity entity = new HapEntity(
                ImmutableMap.<Keyword, Query>of(),
                ImmutableMap.<Keyword, Form>of(),
                ImmutableListMultimap.<Keyword, Link>builder()
                        .put(key, new Link(URI.create("http://localhost/uri-135714"),
                                Optional.<String>absent()))
                        .put(key, new Link(URI.create("http://localhost/uri-080520"),
                                Optional.<String>absent()))
                        .build(),
                ImmutableMultimap.<Keyword, HapEntity>of(),
                ImmutableSet.<Operation>of(),
                ImmutableMap.<Keyword, Object>of(), Optional.of("174432"));

        Map<?, ?> serialize = serialize(entity);
        assertThat(((Map<?, ?>) serialize.get(keyword("links"))).get(key))
                .isInstanceOf(List.class);
    }

    @Test
    public void singleEmbeddedIsMap() throws Exception {
        Keyword key = keyword("093636");

        HapEntity entity = new HapEntity(
                ImmutableMap.<Keyword, Query>of(),
                ImmutableMap.<Keyword, Form>of(),
                ImmutableListMultimap.<Keyword, Link>of(),
                ImmutableMultimap.of(key, emptyHapEntity("104802")),
                ImmutableSet.<Operation>of(),
                ImmutableMap.<Keyword, Object>of(), Optional.of("174432"));

        Map<?, ?> serialize = serialize(entity);
        assertThat(((Map<?, ?>) serialize.get(keyword("embedded"))).get(key))
                .isInstanceOf(Map.class);
    }

    @Test
    public void multipleEmbeddedAreList() throws Exception {
        Keyword key = keyword("105805");

        HapEntity entity = new HapEntity(
                ImmutableMap.<Keyword, Query>of(),
                ImmutableMap.<Keyword, Form>of(),
                ImmutableListMultimap.<Keyword, Link>of(),
                ImmutableMultimap.<Keyword, HapEntity>builder()
                        .put(key, emptyHapEntity("212709"))
                        .put(key, emptyHapEntity("021458"))
                        .build(),
                ImmutableSet.<Operation>of(),
                ImmutableMap.<Keyword, Object>of(), Optional.of("174432"));

        Map<?, ?> serialize = serialize(entity);
        assertThat(((Map<?, ?>) serialize.get(keyword("embedded"))).get(key))
                .isInstanceOf(List.class);
    }

}
