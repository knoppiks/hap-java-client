package de.knoppiks.hap.client;

import com.cognitect.transit.Keyword;
import com.cognitect.transit.TransitFactory.Format;
import com.google.common.collect.ImmutableMap;
import com.google.common.net.HttpHeaders;
import de.knoppiks.hap.client.model.Form;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ByteArrayEntity;

import java.io.ByteArrayOutputStream;

import static com.cognitect.transit.TransitFactory.writer;
import static de.knoppiks.hap.client.MediaTypes.fromFormat;

/**
 * @author <a href="mailto:jwagner@knoppiks.de">Jonas Wagner</a>
 */
class CreateRequestBuilder extends RequestBuilder<Form> {

    private final Form form;
    private final ImmutableMap<Keyword, Object> params;

    CreateRequestBuilder(Form form, ImmutableMap<Keyword, Object> params) {
        this.form = form;
        this.params = params;
    }

    @Override
    public CreateRequestBuilder put(Keyword param, Object value) {
        return new CreateRequestBuilder(form, ImmutableMap.<Keyword, Object>builder()
                .putAll(params).put(param, value).build());
    }
    @Override
    HttpUriRequest build(Format format) {
        HttpPost post = new HttpPost(form.getTarget());
        post.addHeader(HttpHeaders.CONTENT_TYPE, fromFormat(format));

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        writer(format, out).write(params);
        ByteArrayEntity entity = new ByteArrayEntity(out.toByteArray());
        post.setEntity(entity);

        return post;
    }
}
