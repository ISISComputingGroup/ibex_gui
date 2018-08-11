package uk.ac.stfc.isis.ibex.epics.conversion;

import java.util.function.Function;

public interface Converter<T, R> extends Function<T, R> {

}