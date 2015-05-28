package uk.ac.stfc.isis.ibex.ui.configserver.editing.pvsets;

import java.awt.CompositeContext;
import java.awt.RenderingHints;
import java.awt.image.ColorModel;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;

import uk.ac.stfc.isis.ibex.configserver.configuration.AvailablePVSet;
import uk.ac.stfc.isis.ibex.configserver.configuration.PVSet;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableIoc;
import uk.ac.stfc.isis.ibex.configserver.editing.EditablePVSet;
import uk.ac.stfc.isis.ibex.ui.configserver.dialogs.MessageDisplayer;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.iocs.IIocDependentPanel;

public class IocPVSetsEditorPanel extends Composite implements	IIocDependentPanel {
	private MessageDisplayer messageDisplayer;
	private IocPVSetsTable iocPVSetsTable;
	
	public IocPVSetsEditorPanel(Composite parent, int style, final MessageDisplayer messageDisplayer) {
		super(parent, style);
		this.messageDisplayer = messageDisplayer;
		setLayout(new GridLayout(1, false));
		
		iocPVSetsTable = new IocPVSetsTable(this, SWT.NONE, 0);
		iocPVSetsTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
	}

	@Override
	public CompositeContext createContext(ColorModel srcColorModel,
			ColorModel dstColorModel, RenderingHints hints) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setIoc(final EditableIoc ioc) {
		Collection<EditablePVSet> rows = new ArrayList<EditablePVSet>();
		
		for (AvailablePVSet pvset : ioc.getAvailablePVSets()) {
			EditablePVSet editableSet;
			PVSet existingSet = ioc.findPVSet(pvset.getName());
			if ( existingSet!=null ) {
				editableSet = new EditablePVSet(existingSet, pvset.getDescription());
			}
			else {
				editableSet = new EditablePVSet(pvset.getName(), false, pvset.getDescription());
			}
			rows.add(editableSet);
		}
		
		if ( ioc.isEditable() ) {
			for (final EditablePVSet pvset : rows) {
				pvset.addPropertyChangeListener(new PropertyChangeListener() {
					// Ensure Ioc.getPVSets always contains the list of enabled sets
					@Override
					public void propertyChange(PropertyChangeEvent arg0) {
						if (arg0.getPropertyName().equals("enabled")) {
							PVSet existingSet = ioc.findPVSet(pvset.getName());
							if ((boolean)arg0.getNewValue()) {
								if ( existingSet!=null ) {
									existingSet.setEnabled(true);
								}
								else {
									ioc.getPvSets().add(new PVSet(pvset.getName(), true));
								}
							}
							else if ( existingSet!=null ) {
								ioc.getPvSets().remove(existingSet);
							}
						}
					}
				});
			}
		}
		
		iocPVSetsTable.setIsEditable(ioc.isEditable());
		iocPVSetsTable.setRows(rows);
	}
}
