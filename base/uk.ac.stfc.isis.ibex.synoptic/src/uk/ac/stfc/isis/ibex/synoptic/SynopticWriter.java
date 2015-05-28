package uk.ac.stfc.isis.ibex.synoptic;

import javax.xml.bind.JAXBException;

import org.xml.sax.SAXException;

import uk.ac.stfc.isis.ibex.epics.writing.TransformingWriter;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;
import uk.ac.stfc.isis.ibex.model.SettableUpdatedValue;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.InstrumentDescription;
import uk.ac.stfc.isis.ibex.synoptic.xml.XMLUtil;

public class SynopticWriter 
	extends TransformingWriter<InstrumentDescription, String> {
	
	private SettableUpdatedValue<Boolean> canSave = new SettableUpdatedValue<>();

	public SynopticWriter(Writable<String> destination) {
		writeTo(destination);
		canSave.setValue(destination.canWrite());
		destination.subscribe(this);
	}
	
	@Override
	protected String transform(InstrumentDescription value) {
		try {
			return XMLUtil.toXml(value);
		} catch (JAXBException | SAXException e) {
			onError(e);
		};
		
		return null;
	}

	@Override
	public void onCanWriteChanged(boolean canWrite) {
		super.onCanWriteChanged(canWrite);
		canSave.setValue(canWrite);
	}
	
	public UpdatedValue<Boolean> canSave() {
		return canSave;
	}
}
