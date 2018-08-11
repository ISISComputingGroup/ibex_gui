package uk.ac.stfc.isis.ibex.epics.conversion;

import java.util.Objects;
import java.util.function.Function;

public interface Converter<T, R> extends Function<T, R> {
	
	@Override
	default <V> Converter<T, V> andThen(Function<? super R, ? extends V> after) {
        Objects.requireNonNull(after);
        return (T t) -> after.apply(apply(t));
    }

}