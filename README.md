__This software is ALPHA.__

# HAP Java Client

A Java library which implements a generic [Hypermedia Application Protocol (HAP)](https://github.com/alexanderkiel/hap-spec) 
client.

## Installation

When using maven you can simply add the following dependency to your `pom.xml`.

```xml
<dependency>
    <groupId>de.knoppiks</groupId>
    <artifactId>hap-java-client</artifactId>
    <version>0.1</version>
</dependency>
```

## Usage

Examples for all Methods the HapClient offers can be found in the `ClientTodoIT` in the *src/test/* directory.

### Fetch a Representation

```java
import de.knoppiks.hap.client.Hap;
import de.knoppiks.hap.client.HapClient;
import de.knoppiks.hap.client.HapEntity;
import org.apache.http.impl.client.HttpClients;
import static com.cognitect.transit.TransitFactory.Format.JSON;

HapClient client = Hap.createClient(HttpClients.createDefault(), JSON);
HapEntity entity = client.fetch(URI.create("..."));

### Execute a Query

```java
import de.knoppiks.hap.client.Hap;
import de.knoppiks.hap.client.HapClient;
import de.knoppiks.hap.client.HapEntity;
import de.knoppiks.hap.client.RequestBuilders;
import de.knoppiks.hap.client.model.Query;
import org.apache.http.impl.client.HttpClients;
import static com.cognitect.transit.TransitFactory.Format.JSON;

HapClient client = Hap.createClient(HttpClients.createDefault(), JSON);
HapEntity entity = client.fetch(URI.create("..."));
Query query = entity.getQuery(keyword("...")).get();
HapEntity result = client.execute(RequestBuilders.query(query).put(keyword("..."), ...));
```

### Create a new Resource

Use `RequestBuilders` to build a create request from an existing form.
You can specify your create request parameters by calling `put(...)` on the RequestBuilder.
*Note: Currently parameters are not checked against the parameters allowed in the form at all. You may inspect 
which parameters may be used by calling `getParams()` on the form. Type checking via [Schemas](#schema-support) 
may be added later.* 

```java
import de.knoppiks.hap.client.Hap;
import de.knoppiks.hap.client.HapClient;
import de.knoppiks.hap.client.HapEntity;
import de.knoppiks.hap.client.RequestBuilders;
import de.knoppiks.hap.client.model.Form;
import org.apache.http.impl.client.HttpClients;
import static com.cognitect.transit.TransitFactory.Format.JSON;

HapClient client = Hap.createClient(HttpClients.createDefault(), JSON);
HapEntity entity = client.fetch(URI.create("..."));
Form form = entity.getForm(keyword("...")).get();
URI location = client.create(RequestBuilders.create(form).put(keyword("..."), "..."));
```

### Delete a Resource

```java
import de.knoppiks.hap.client.Hap;
import de.knoppiks.hap.client.HapClient;
import org.apache.http.impl.client.HttpClients;
import static com.cognitect.transit.TransitFactory.Format.JSON;

HapClient client = Hap.createClient(HttpClients.createDefault(), JSON);
client.delete(URI.create("..."));
```

### Update a Resource

```java
import de.knoppiks.hap.client.Hap;
import de.knoppiks.hap.client.HapClient;
import de.knoppiks.hap.client.HapEntity;
import org.apache.http.impl.client.HttpClients;
import static com.cognitect.transit.TransitFactory.Format.JSON;

HapClient client = Hap.createClient(HttpClients.createDefault(), JSON);
HapEntity state = client.fetch(URI.create("..."));
HapEntity newState = client.update(state.updateData(...));
```

## Schema Support

There is currently no schema support.

## License

Copyright © 2015 Jonas Wagner

Hap Java Client is licensed under the open-source Apache 2.0 license.
