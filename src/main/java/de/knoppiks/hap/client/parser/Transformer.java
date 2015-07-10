package de.knoppiks.hap.client.parser;

import com.google.common.base.Function;

/**
 * @author <a href="mailto:alexanderkiel@gmx.net">Alexander Kiel</a>
 * @author <a href="mailto:jwagner@knoppiks.de">Jonas Wagner</a>
 */
abstract class Transformer<T> implements Function<Object, T> {

    public abstract T transform(Object obj) throws TransformException;

    @Override
    public T apply(Object o) {
        try {
            return this.transform(o);
        } catch (TransformException e) {
            throw new RuntimeException(e);
        }
    }
}
