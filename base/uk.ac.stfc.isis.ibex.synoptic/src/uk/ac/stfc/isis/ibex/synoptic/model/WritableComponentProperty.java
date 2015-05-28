package uk.ac.stfc.isis.ibex.synoptic.model;

import uk.ac.stfc.isis.ibex.epics.observing.InitialiseOnSubscribeObservable;
import uk.ac.stfc.isis.ibex.epics.writing.SameTypeWriter;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;
import uk.ac.stfc.isis.ibex.epics.writing.Writer;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;

public class WritableComponentProperty extends ComponentProperty {

	private final SameTypeWriter<String> writer = new SameTypeWriter<>();

	private final ReadableComponentProperty valueSource;
	
	public WritableComponentProperty(
			String displayName, 
			InitialiseOnSubscribeObservable<String> source,
			Writable<String> destination) {
		super(displayName);
		valueSource = new ReadableComponentProperty(displayName, source);
		writer.writeTo(destination);	
	}
	
	public Writer<String> writer() {
		return writer;
	}
	
	public UpdatedValue<String> value() {
		return valueSource.value();
	}
}
