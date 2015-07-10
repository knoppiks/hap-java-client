package de.knoppiks.hap.client.parser;

import com.cognitect.transit.Keyword;
import de.knoppiks.hap.client.model.Form;
import org.junit.Test;

import java.net.URI;

import static com.cognitect.transit.TransitFactory.keyword;
import static com.google.common.collect.ImmutableMap.of;
import static com.google.common.truth.Truth.assertThat;
import static java.lang.String.format;

/**
 * @author <a href="mailto:jwagner@knoppiks.de">Jonas Wagner</a>
 */
public class FormTransformerTest extends AbstractTransitTransformerTest<Form> {

    private static final URI BASE_URI = URI.create("http://base-uri-092758/");

    public FormTransformerTest() {
        super(new FormTransformer(BASE_URI));
    }

    @Test(expected = TransformException.class)
    public void withoutParams() throws Exception {
        String uri = "uri-182140";

        Form query = transformer.transform(of(
                keyword("href"), uri));

        assertThat(query.getTarget()).isEqualTo(BASE_URI.resolve(uri));
    }

    @Test
    public void withEmptyParams() throws Exception {
        String uri = "uri-014513";

        Form form = transformer.transform(of(
                keyword("href"), uri,
                keyword("params"), of()));

        assertThat(form.getTarget()).isEqualTo(BASE_URI.resolve(uri));
        assertThat(form.getParams()).isEmpty();
        assertThat(form.getTitle()).isAbsent();
    }

    @Test
    public void withTitle() throws Exception {
        String uri = "uri-014513";
        String title = "title-194614";

        Form form = transformer.transform(of(
                keyword("href"), uri,
                keyword("title"), title,
                keyword("params"), of()));

        assertThat(form.getTarget()).isEqualTo(BASE_URI.resolve(uri));
        assertThat(form.getParams()).isEmpty();
        assertThat(form.getTitle()).hasValue(title);
    }

    @Test
    public void withParam() throws Exception {
        String uri = "uri-144231";
        Keyword param = keyword("param-201645");
        String type = "type-015705";

        Form form = transformer.transform(of(
                keyword("href"), uri,
                keyword("params"), of(
                        param, of(keyword("type"), type))));

        assertThat(form.getTarget()).isEqualTo(BASE_URI.resolve(uri));
        assertThat(form.getParams()).isNotEmpty();
        assertThat(form.getParams()).containsKey(param);
        assertThat(form.getParams().get(param).getType()).isEqualTo(type);
    }

    @Test
    public void parseAndTransform() throws Exception {
        String uri = "uri-225104";
        String param = "param-203216";
        String type = "type-120224";
        String title = "title-013749";

        Form form = parse(format(resource("transit-form"), uri, title, param, type));

        assertThat(form.getTarget()).isEqualTo(BASE_URI.resolve(uri));
        assertThat(form.getParams()).isNotEmpty();
        assertThat(form.getParams()).containsKey(keyword(param));
        assertThat(form.getParams().get(keyword(param)).getType()).isEqualTo(type);
    }
}
