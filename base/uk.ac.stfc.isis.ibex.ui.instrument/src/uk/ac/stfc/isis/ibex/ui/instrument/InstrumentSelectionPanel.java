package uk.ac.stfc.isis.ibex.ui.instrument;

import java.util.Collection;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;

import uk.ac.stfc.isis.ibex.instrument.InstrumentInfo;

public class InstrumentSelectionPanel extends Composite {
	
	private final ComboViewer comboViewer;
	
	public InstrumentSelectionPanel(Composite parent, int style, Collection<InstrumentInfo> instruments) {
		super(parent, style);
		setLayout(new GridLayout(2, false));
		
		Label lblInstrument = new Label(this, SWT.NONE);
		lblInstrument.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblInstrument.setText("Instrument:");
		
		comboViewer = new ComboViewer(this, SWT.READ_ONLY);
		Combo combo = comboViewer.getCombo();
		combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		if (instruments != null) {
			bind(instruments);
		}
	}

	private void bind(Collection<InstrumentInfo> instruments) {
		comboViewer.setContentProvider(ArrayContentProvider.getInstance());
		comboViewer.setLabelProvider(new LabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof InstrumentInfo) {
					InstrumentInfo info = (InstrumentInfo) element;
					return info.name();
				}
				
				return super.getText(element);
			}
		});
		
		comboViewer.setInput(instruments);
	}

	public InstrumentInfo getSelected() {
		IStructuredSelection selection = (IStructuredSelection) comboViewer.getSelection();
		if (!selection.isEmpty()) {
			return (InstrumentInfo) selection.getFirstElement(); 				
		}
		
		return null;
	}
}
