package uk.ac.stfc.isis.ibex.ui.configserver.editing.iocs;

import java.util.Collection;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;

import uk.ac.stfc.isis.ibex.configserver.configuration.Ioc;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableConfiguration;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableIoc;
import uk.ac.stfc.isis.ibex.ui.configserver.dialogs.MessageDisplayer;

public class IocSelectorPanel extends Composite {
	private MessageDisplayer msgDisp;
	private EditableIoc ioc;
	private ComboViewer iocCombo;
	private Label lbDescTarget;
	private IIocDependentPanel target;
	
	public IocSelectorPanel(Composite parent, int style, MessageDisplayer msgDisp, IIocPanelCreator panelFactory) {
		super(parent, style);
		this.msgDisp = msgDisp;
		setLayout(new GridLayout(2, false));
		
		Label lblIoc = new Label(this, SWT.NONE);
		lblIoc.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblIoc.setText("IOC:");
		
		iocCombo = new ComboViewer(this, SWT.DROP_DOWN | SWT.READ_ONLY);
		GridData gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gridData.widthHint = 150;
		iocCombo.getCombo().setLayoutData(gridData);
		iocCombo.setContentProvider(ArrayContentProvider.getInstance());
		
		Label lblDescription = new Label(this, SWT.NONE);
		lblDescription.setText("Description:");
		
		lbDescTarget = new Label(this, SWT.NONE);
		lbDescTarget.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		target = panelFactory.factory(this);
		Composite targetPanel = (Composite)target;
		targetPanel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
	}
	
	public void setConfig(EditableConfiguration config) {
		iocCombo.setLabelProvider(new LabelProvider() {
			public String getText(Object element) {
				Ioc ioc = (Ioc) element;
				return ioc.getName();
			};
		});
		Collection<EditableIoc> iocs = config.getEditableIocs();
		iocCombo.setInput(iocs);
		
		iocCombo.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent arg0) {
				StructuredSelection selection = (StructuredSelection)arg0.getSelection();
				ioc = (EditableIoc)selection.getFirstElement();
				lbDescTarget.setText(ioc.getDescription());
				target.setIoc(ioc);
			}
		});

		if (iocs.size()>0) {
			iocCombo.setSelection(new StructuredSelection(iocs.iterator().next()));
		}
	}
}
