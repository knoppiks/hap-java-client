package de.knoppiks.hap;

import de.knoppiks.hap.client.Hap;
import de.knoppiks.hap.client.HapClient;
import de.knoppiks.hap.client.HapEntity;
import de.knoppiks.hap.client.RequestBuilders;
import de.knoppiks.hap.client.model.Form;
import de.knoppiks.hap.client.model.Link;
import de.knoppiks.hap.client.model.Query;
import org.junit.Test;

import java.net.URI;
import java.util.Map;

import static com.cognitect.transit.TransitFactory.Format.JSON;
import static com.cognitect.transit.TransitFactory.keyword;
import static com.google.common.collect.ImmutableMap.of;
import static com.google.common.truth.Truth.assertThat;
import static java.lang.System.getProperty;
import static org.apache.http.impl.client.HttpClients.createDefault;

/**
 * This test uses <a href="https://github.com/alexanderkiel/hap-todo">https://github.com/alexanderkiel/hap-todo</a>.
 *
 * @author <a href="mailto:jwagner@knoppiks.de">Jonas Wagner</a>
 */
public class ClientTodoIT {

    private static final HapClient CLIENT = Hap.createClient(createDefault(), JSON);
    private static final URI TEST_URL = URI.create(getProperty("test.url"));
    private static final String CREATE_ITEM = "todo/create-item";
    private static final String ITEM_STATE = "todo/item-state";
    private static final String FILTER_ITEM = "todo/filter-item";
    private static final String ITEMS = "todo/items";

    @Test
    public void fetchServiceDocument() throws Exception {
        HapEntity entity = CLIENT.fetch(TEST_URL);

        Map<?, ?> data = (Map<?, ?>) entity.getData();
        assertThat(data).containsKey(keyword("name"));
        assertThat(data).containsKey(keyword("version"));

        assertThat(entity.getForm(keyword(CREATE_ITEM))).isPresent();
        assertThat(entity.getForm(keyword(CREATE_ITEM)).get().getTitle()).hasValue("Create Item");
    }

    @Test
    public void createAndDelete() throws Exception {
        HapEntity serviceDoc = CLIENT.fetch(TEST_URL);

        assertThat(serviceDoc.getForm(keyword(CREATE_ITEM))).isPresent();
        Form form = serviceDoc.getForm(keyword(CREATE_ITEM)).get();
        URI itemUri = CLIENT.create(RequestBuilders.create(form).put(keyword("label"), "label-222612"));

        HapEntity item = CLIENT.fetch(itemUri);
        assertThat((Map<?, ?>) item.getData()).containsEntry(keyword("label"), "label-222612");

        CLIENT.delete(itemUri);
    }

    @Test
    public void createQueryAndDelete() throws Exception {
        HapEntity serviceDoc = CLIENT.fetch(TEST_URL);
        String label = "0460941b-2114-4622-8f23-a85639f39278";

        assertThat(serviceDoc.getForm(keyword(CREATE_ITEM))).isPresent();
        Form form = serviceDoc.getForm(keyword(CREATE_ITEM)).get();
        URI itemUri = CLIENT.create(RequestBuilders.create(form).put(keyword("label"), label));

        assertThat(serviceDoc.getQuery(keyword(FILTER_ITEM))).isPresent();
        Query filter = serviceDoc.getQuery(keyword(FILTER_ITEM)).get();
        HapEntity filtered = CLIENT.execute(RequestBuilders.query(filter).put(keyword("label"), label));
        assertThat(filtered.getEmbedded(keyword(ITEMS))).isNotEmpty();

        HapEntity item = CLIENT.fetch(itemUri);
        assertThat((Map<?, ?>) item.getData()).containsEntry(keyword("label"), label);

        CLIENT.delete(itemUri);
    }

    @Test
    public void createUpdateAndDelete() throws Exception {
        HapEntity serviceDoc = CLIENT.fetch(TEST_URL);

        assertThat(serviceDoc.getForm(keyword(CREATE_ITEM))).isPresent();
        Form form = serviceDoc.getForm(keyword(CREATE_ITEM)).get();
        URI itemUri = CLIENT.create(RequestBuilders.create(form).put(keyword("label"), "label-222612"));

        HapEntity item = CLIENT.fetch(itemUri);

        assertThat(item.getLinks(keyword(ITEM_STATE))).isNotEmpty();
        Link itemStateUri = item.getLinks(keyword(ITEM_STATE)).get(0);

        HapEntity itemState = CLIENT.fetch(itemStateUri);

        CLIENT.update(itemState.updateData(of(keyword("state"), keyword("completed"))));

        CLIENT.delete(itemUri);
    }
}
