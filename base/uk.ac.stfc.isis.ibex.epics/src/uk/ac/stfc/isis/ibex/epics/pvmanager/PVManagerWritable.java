package uk.ac.stfc.isis.ibex.epics.pvmanager;

import org.epics.pvmanager.PVManager;
import org.epics.pvmanager.PVWriter;
import org.epics.pvmanager.PVWriterEvent;
import org.epics.pvmanager.PVWriterListener;
import org.epics.pvmanager.expression.WriteExpressionImpl;

import uk.ac.stfc.isis.ibex.epics.pv.PVInfo;
import uk.ac.stfc.isis.ibex.epics.pv.WritablePV;
import uk.ac.stfc.isis.ibex.epics.writing.WriteException;

public class PVManagerWritable<T> extends WritablePV<T> {

	private final WriteExpressionImpl<T> writeExpression;
	private final PVWriter<T> pv;
	
	private final PVWriterListener<T> observingListener = new PVWriterListener<T>() {
		@Override
		public void pvChanged(PVWriterEvent<T> evt) {
			if (pv == null) {
				return;
			}
			
			if (evt.isConnectionChanged()) {
				canWriteChanged(pv.isWriteConnected());
			}
			
			if (evt.isExceptionChanged()) {
				error(pv.lastWriteException());
			}
			
			if (evt.isWriteFailed()) {
				error(new WriteException("Write failed"));
			}
		}
	};
	
	public PVManagerWritable(PVInfo<T> info) {
		super(info);
		writeExpression = new WriteExpressionImpl<T>(info.address());		
		pv = PVManager
				.write(writeExpression)
				.writeListener(observingListener)
				.async();	
	}
	
	@Override
	public void write(T value) {
		pv.write(value);
	}

	@Override
	public void close() {
		pv.close();
	}
}
