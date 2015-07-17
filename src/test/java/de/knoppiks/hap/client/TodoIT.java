package de.knoppiks.hap.client;

import com.cognitect.transit.Keyword;
import com.google.common.collect.ImmutableMap;
import de.knoppiks.hap.client.model.Form;
import de.knoppiks.hap.client.model.HapEntity;
import de.knoppiks.hap.client.model.Link;
import de.knoppiks.hap.client.parser.ParseException;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;

import static com.cognitect.transit.TransitFactory.Format.JSON;
import static com.cognitect.transit.TransitFactory.keyword;
import static com.google.common.truth.Truth.assertThat;
import static java.lang.System.getProperty;
import static org.apache.http.impl.client.HttpClients.createDefault;

/**
 * This test depends on <a href="https://github.com/alexanderkiel/hap-todo">https://github.com/alexanderkiel/hap-todo
 * </a>. As the example hap-todo API is in ALPHA one should not rely on this test to stay stable.
 *
 * @author <a href="mailto:jwagner@knoppiks.de">Jonas Wagner</a>
 */
public class TodoIT {

    private static final HapClient CLIENT = Hap.createClient(
            createDefault(), JSON);
    private static final URI TEST_URL = URI.create(getProperty("test.url"));

    private static List<HapEntity> getTodoItems(Link items)
            throws IOException, ParseException, WrongContentTypeException {
        return CLIENT.fetch(items).getEmbedded(keyword("todo/items"));
    }

    @Test
    public void fetchServiceDocument() throws Exception {
        HapEntity entity = CLIENT.fetch(TEST_URL);

        Map<?, ?> data = (Map<?, ?>) entity.getData();
        assertThat(data).containsKey(keyword("name"));
        assertThat(data).containsKey(keyword("version"));

        assertThat(entity.getForm(keyword("todo/create-item"))).isPresent();
        assertThat(entity.getForm(keyword("todo/create-item")).get().getTitle()).hasValue("Create Item");
    }

    @Test
    public void createUpdateAndDelete() throws Exception {
        HapEntity entity = CLIENT.fetch(TEST_URL);

        assertThat(entity.getLinks(keyword("todo/items"))).isNotEmpty();
        Link items = entity.getLinks(keyword("todo/items")).get(0);
        int size = getTodoItems(items).size();

        Form form = entity.getForm(keyword("todo/create-item")).get();
        URI created = CLIENT.create(
                RequestBuilders.create(form).put(keyword("label"),
                        "label-222612"));
        assertThat(getTodoItems(items)).hasSize(size + 1);

        HapEntity item = CLIENT.fetch(created);
        HapEntity itemState = CLIENT.fetch(item.getLinks(keyword("todo/item-state")).get(0));

        Keyword state = (Keyword) ((Map<?, ?>) item.getData()).get(keyword("state"));

        CLIENT.update(itemState
                .setData(ImmutableMap.of(keyword("state"), toggleState(state))));

        assertThat(((Map<?, ?>) CLIENT.fetch(created).getData()).get(keyword("state"))).isNotEqualTo(state);

        CLIENT.delete(created);

        assertThat(getTodoItems(items)).hasSize(size);
    }

    private Keyword toggleState(Keyword current) {
        return current.equals(keyword("active")) ? keyword("completed") : keyword("active");
    }
}
