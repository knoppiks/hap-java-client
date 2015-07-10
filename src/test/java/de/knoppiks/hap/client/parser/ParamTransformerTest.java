package de.knoppiks.hap.client.parser;

import de.knoppiks.hap.client.model.Param;
import org.junit.Test;

import static com.cognitect.transit.TransitFactory.keyword;
import static com.google.common.collect.ImmutableMap.of;
import static com.google.common.truth.Truth.assertThat;
import static java.lang.String.format;

/**
 * @author <a href="mailto:jwagner@knoppiks.de">Jonas Wagner</a>
 */
public class ParamTransformerTest extends AbstractTransitTransformerTest<Param> {

    public ParamTransformerTest() {
        super(new ParamTransformer());
    }

    @Test
    public void typeOnly() throws Exception {
        String type = "type-051447";

        Param param = transformer.transform(of(
                keyword("type"), type));

        assertThat(param.getDescription()).isAbsent();
        assertThat(param.getType()).isEqualTo(type);
        assertThat(param.isOptional()).isFalse();
    }

    @Test
    public void typeAndDescription() throws Exception {
        String type = "type-043227";
        String description = "description-055304";

        Param param = transformer.transform(of(
                keyword("type"), type,
                keyword("description"), description));

        assertThat(param.getDescription()).hasValue(description);
        assertThat(param.getType()).isEqualTo(type);
        assertThat(param.isOptional()).isFalse();
    }

    @Test
    public void typeDescriptionOptional() throws Exception {
        String type = "type-032609";
        String description = "description-210200";

        Param param = transformer.transform(of(
                keyword("type"), type,
                keyword("description"), description,
                keyword("optional"), true));

        assertThat(param.getDescription()).hasValue(description);
        assertThat(param.getType()).isEqualTo(type);
        assertThat(param.isOptional()).isTrue();
    }

    @Test
    public void parseAndTransform() throws Exception {
        String type = "type-032026";
        String description = "description-203309";

        Param param = parse(format(resource("transit-param"), type, description, true));

        assertThat(param.getDescription()).hasValue(description);
        assertThat(param.getType()).isEqualTo(type);
        assertThat(param.isOptional()).isTrue();
    }
}
