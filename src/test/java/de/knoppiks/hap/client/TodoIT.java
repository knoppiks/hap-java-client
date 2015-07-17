package de.knoppiks.hap.client;

import com.google.common.collect.ImmutableMap;
import de.knoppiks.hap.client.model.Form;
import de.knoppiks.hap.client.model.HapEntity;
import de.knoppiks.hap.client.model.Link;
import org.junit.Test;

import java.net.URI;
import java.util.Map;

import static com.cognitect.transit.TransitFactory.Format.JSON;
import static com.cognitect.transit.TransitFactory.keyword;
import static com.google.common.truth.Truth.assertThat;
import static java.lang.System.getProperty;
import static org.apache.http.impl.client.HttpClients.createDefault;

/**
 * This test uses <a href="https://github.com/alexanderkiel/hap-todo">https://github.com/alexanderkiel/hap-todo</a>.
 *
 * @author <a href="mailto:jwagner@knoppiks.de">Jonas Wagner</a>
 */
public class TodoIT {

    private static final HapClient CLIENT = Hap.createClient(createDefault(), JSON);
    private static final URI TEST_URL = URI.create(getProperty("test.url"));
    private static final String CREATE_ITEM = "todo/create-item";
    private static final String ITEM_STATE = "todo/item-state";

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
    public void createUpdateAndDelete() throws Exception {
        HapEntity serviceDoc = CLIENT.fetch(TEST_URL);

        assertThat(serviceDoc.getForm(keyword(CREATE_ITEM))).isPresent();
        Form form = serviceDoc.getForm(keyword(CREATE_ITEM)).get();
        URI itemUri = CLIENT.create(RequestBuilders.create(form).put(keyword("label"), "label-222612"));

        HapEntity item = CLIENT.fetch(itemUri);

        assertThat(item.getLinks(keyword(ITEM_STATE))).isNotEmpty();
        Link itemStateUri = item.getLinks(keyword(ITEM_STATE)).get(0);

        HapEntity itemState = CLIENT.fetch(itemStateUri);

        CLIENT.update(itemState.setData(ImmutableMap.of(keyword("state"), keyword("completed"))));

        CLIENT.delete(itemUri);
    }
}
