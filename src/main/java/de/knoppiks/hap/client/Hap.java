package de.knoppiks.hap.client;

import com.cognitect.transit.TransitFactory.Format;
import org.apache.http.client.HttpClient;

/**
 * @author <a href="mailto:jwagner@knoppiks.de">Jonas Wagner</a>
 */
public final class Hap {
    private Hap() {
    }

    public static HapClient createClient(HttpClient client, Format format) {
        return new HapClientImpl(new HapHttpClient(client, format), format);
    }
}
