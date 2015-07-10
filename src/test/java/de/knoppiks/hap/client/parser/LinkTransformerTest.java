package de.knoppiks.hap.client.parser;

import de.knoppiks.hap.client.model.Link;
import org.junit.Test;

import java.net.URI;

import static com.cognitect.transit.TransitFactory.keyword;
import static com.google.common.collect.ImmutableMap.of;
import static com.google.common.truth.Truth.assertThat;

/**
 * @author <a href="mailto:jwagner@knoppiks.de">Jonas Wagner</a>
 */
public class LinkTransformerTest extends AbstractTransitTransformerTest<Link> {

    private static final URI BASE_URI = URI.create("http://base-uri-225351/");

    public LinkTransformerTest() {
        super(new LinkTransformer(BASE_URI));
    }

    @Test
    public void hrefOnly() throws Exception {
        String uri = "uri-070616";

        Link link = transformer.transform(of(
                keyword("href"), uri));

        assertThat(link.getHref()).isEqualTo(BASE_URI.resolve(uri));
    }

    @Test
    public void hrefAndLabel() throws Exception {
        String uri = "uri-070616";
        String label = "label-080042";

        Link link = transformer.transform(of(
                keyword("href"), uri,
                keyword("label"), label));

        assertThat(link.getHref()).isEqualTo(BASE_URI.resolve(uri));
        assertThat(link.getLabel()).hasValue(label);
    }

    @Test
    public void parseAndTransform() throws Exception {
        String uri = "uri-112440";
        String label = "label-183420";

        Link link = parse(String.format(resource("transit-link"), uri, label));

        assertThat(link.getHref()).isEqualTo(BASE_URI.resolve(uri));
        assertThat(link.getLabel()).hasValue(label);
    }
}
