package de.knoppiks.hap.client.parser;

import com.google.common.base.Optional;
import de.knoppiks.hap.client.HapEntity;
import org.junit.Test;

import java.net.URI;
import java.util.Map;
import java.util.ResourceBundle;

import static com.cognitect.transit.TransitFactory.Format.JSON;
import static com.cognitect.transit.TransitFactory.keyword;
import static com.google.common.truth.Truth.assertThat;
import static de.knoppiks.hap.client.model.Operation.DELETE;
import static de.knoppiks.hap.client.model.Operation.UPDATE;
import static de.knoppiks.hap.client.parser.HapParser.parseEntity;

/**
 * @author <a href="mailto:jwagner@knoppiks.de">Jonas Wagner</a>
 */
public class HapParserTest {

    private static final ResourceBundle RESOURCES = ResourceBundle.getBundle(HapParserTest.class.getName());
    private static final String SERVICE_DOCUMENT = RESOURCES.getString("service-document");
    private static final String ITEM_LIST = RESOURCES.getString("item-list");
    private static final String DELETABLE_ITEM = RESOURCES.getString("deletable-item");

    private static final URI BASE_URI = URI.create("http://localhost");

    @Test
    public void testServiceDocument() throws Exception {
        HapEntity entity = parseEntity(BASE_URI, JSON, SERVICE_DOCUMENT.getBytes(), Optional.of("092202"));

        Map<?, ?> data = (Map<?, ?>) entity.getData();
        assertThat(data).hasSize(2);
        assertThat(data).containsKey(keyword("name"));
        assertThat(data).containsKey(keyword("version"));

        assertThat(entity.getLinks(keyword("self"))).isNotEmpty();
        assertThat(entity.getLinks(keyword("todo/items"))).isNotEmpty();

        assertThat(entity.getForm(keyword("todo/create-item"))).isPresent();

        assertThat(entity.operationAllowed(DELETE)).isFalse();
        assertThat(entity.operationAllowed(UPDATE)).isFalse();
    }

    @Test
    public void testEmbeddedList() throws Exception {
        HapEntity entity = parseEntity(BASE_URI, JSON, ITEM_LIST.getBytes(), Optional.of("092202"));

        assertThat(entity.getLinks(keyword("up"))).isNotEmpty();
        assertThat(entity.getLinks(keyword("self"))).isNotEmpty();

        assertThat(entity.getEmbedded(keyword("todo/items"))).isNotEmpty();
        assertThat(entity.getEmbedded(keyword("todo/items"))).hasSize(2);

        assertThat(entity.getForm(keyword("todo/create-item"))).isPresent();

        assertThat(entity.operationAllowed(DELETE)).isFalse();
        assertThat(entity.operationAllowed(UPDATE)).isFalse();
    }

    @Test
    public void testDeletableItem() throws Exception {
        HapEntity entity = parseEntity(BASE_URI, JSON, DELETABLE_ITEM.getBytes(), Optional.of("092202"));

        Map<?, ?> data = (Map<?, ?>) entity.getData();
        assertThat(data).hasSize(2);

        assertThat(entity.getLinks(keyword("up"))).isNotEmpty();
        assertThat(entity.getLinks(keyword("self"))).isNotEmpty();
        assertThat(entity.getLinks(keyword("todo/item-state"))).isNotEmpty();

        assertThat(entity.operationAllowed(DELETE)).isTrue();
        assertThat(entity.operationAllowed(UPDATE)).isFalse();
    }
}
