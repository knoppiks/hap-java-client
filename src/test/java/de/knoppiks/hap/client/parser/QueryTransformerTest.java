package de.knoppiks.hap.client.parser;

import com.cognitect.transit.Keyword;
import de.knoppiks.hap.client.model.Query;
import org.junit.Test;

import java.net.URI;

import static com.cognitect.transit.TransitFactory.keyword;
import static com.google.common.collect.ImmutableMap.of;
import static com.google.common.truth.Truth.assertThat;
import static java.lang.String.format;

/**
 * @author <a href="mailto:jwagner@knoppiks.de">Jonas Wagner</a>
 */
public class QueryTransformerTest extends AbstractTransitTransformerTest<Query> {

    private static final URI BASE_URI = URI.create("http://base-uri-092758/");

    public QueryTransformerTest() {
        super(new QueryTransformer(BASE_URI));
    }

    @Test(expected = TransformException.class)
    public void withoutParams() throws Exception {
        String uri = "uri-182140";

        Query query = transformer.transform(of(
                keyword("href"), uri));

        assertThat(query.getTarget()).isEqualTo(BASE_URI.resolve(uri));
    }

    @Test
    public void withEmptyParams() throws Exception {
        String uri = "uri-014513";

        Query query = transformer.transform(of(
                keyword("href"), uri,
                keyword("params"), of()));

        assertThat(query.getTarget()).isEqualTo(BASE_URI.resolve(uri));
        assertThat(query.getParams()).isEmpty();
    }

    @Test
    public void withParam() throws Exception {
        String uri = "uri-144231";
        Keyword param = keyword("param-201645");
        String type = "type-015705";

        Query query = transformer.transform(of(
                keyword("href"), uri,
                keyword("params"), of(
                        param, of(keyword("type"), type))));

        assertThat(query.getTarget()).isEqualTo(BASE_URI.resolve(uri));
        assertThat(query.getParams()).isNotEmpty();
        assertThat(query.getParams()).containsKey(param);
        assertThat(query.getParams().get(param).getType()).isEqualTo(type);
    }

    @Test
    public void parseAndTransform() throws Exception {
        String uri = "uri-225104";
        String param = "param-203216";
        String type = "type-120224";

        Query query = parse(format(resource("transit-query"), uri, param, type));

        assertThat(query.getTarget()).isEqualTo(BASE_URI.resolve(uri));
        assertThat(query.getParams()).isNotEmpty();
        assertThat(query.getParams()).containsKey(keyword(param));
        assertThat(query.getParams().get(keyword(param)).getType()).isEqualTo(type);
    }
}
