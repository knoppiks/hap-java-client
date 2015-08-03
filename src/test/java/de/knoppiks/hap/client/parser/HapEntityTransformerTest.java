package de.knoppiks.hap.client.parser;

import com.cognitect.transit.Keyword;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import de.knoppiks.hap.client.HapEntity;
import org.junit.Test;

import java.net.URI;

import static com.cognitect.transit.TransitFactory.keyword;
import static com.google.common.collect.ImmutableMap.of;
import static com.google.common.truth.Truth.assertThat;
import static de.knoppiks.hap.client.HapEntity.OPERATIONS;
import static de.knoppiks.hap.client.model.Operation.DELETE;
import static de.knoppiks.hap.client.model.Operation.UPDATE;

/**
 * @author <a href="mailto:jwagner@knoppiks.de">Jonas Wagner</a>
 */
public class HapEntityTransformerTest {

    private static final URI BASE_URI = URI.create("http://uri-083507");

    private Transformer<HapEntity> transformer = new HapEntityTransformer(BASE_URI, Optional.of("092202"));

    @Test
    public void withOperations() throws Exception {
        HapEntity entity = transformer.transform(
                of(OPERATIONS, ImmutableSet.of(keyword("delete"))));

        assertThat(entity.operationAllowed(DELETE)).isTrue();
        assertThat(entity.operationAllowed(UPDATE)).isFalse();
    }

    @Test
    public void listOfLinksIsAList() throws Exception {
        Keyword key = keyword("key-154737");

        HapEntity entity = transformer.transform(
                of(keyword("links"), of(key, ImmutableList.of(
                        of(keyword("href"), URI.create("uri-101035")),
                        of(keyword("href"), URI.create("uri-081517"))))));

        assertThat(entity.getLinks(key)).isNotEmpty();
        assertThat(entity.getLinks(key)).hasSize(2);
    }

    @Test
    public void singleLinkIsAList() throws Exception {
        Keyword key = keyword("key-154737");

        HapEntity entity = transformer.transform(
                of(keyword("links"), of(key, of(keyword("href"), URI.create("uri-101035")))));

        assertThat(entity.getLinks(key)).isNotEmpty();
        assertThat(entity.getLinks(key)).hasSize(1);
    }

    @Test
    public void listOfEmbeddedIsAList() throws Exception {
        Keyword key = keyword("key-154737");

        HapEntity entity = transformer.transform(
                of(keyword("embedded"), of(key,
                        of(keyword("links"), of(key, ImmutableList.of(
                                of(keyword("href"), URI.create("uri-101035"))))))));

        assertThat(entity.getEmbedded(key)).isNotEmpty();
        assertThat(entity.getEmbedded(key)).hasSize(1);
    }

    @Test
    public void singleEmbeddedIsAList() throws Exception {
        Keyword key = keyword("key-154737");

        HapEntity entity = transformer.transform(
                of(keyword("embedded"), of(key,
                        of(keyword("links"), of(key, of(keyword("href"), URI.create("uri-101035")))))));

        assertThat(entity.getEmbedded(key)).isNotEmpty();
        assertThat(entity.getEmbedded(key)).hasSize(1);
    }
}
