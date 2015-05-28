package uk.ac.stfc.isis.ibex.ui.configserver.editing.blocks;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;

import uk.ac.stfc.isis.ibex.configserver.editing.EditableBlock;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableConfiguration;
import uk.ac.stfc.isis.ibex.ui.configserver.dialogs.MessageDisplayer;
import uk.ac.stfc.isis.ibex.ui.configserver.dialogs.PvSelectorDialog;

public class BlockDetailsPanel extends Composite {
	
	private final Text name;
	private final Text pvAddress;
	private final Button visible;
	private final Button local;
	private final Button btnPickPV;
	
	private DataBindingContext bindingContext;
	
	private UpdateValueStrategy strategy = new UpdateValueStrategy();
	private final MessageDisplayer messageDisplayer;
	private EditableConfiguration config;

	public BlockDetailsPanel(Composite parent, int style, MessageDisplayer messageDisplayer) {
		super(parent, style);
		this.messageDisplayer = messageDisplayer;		
		
		setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Group grpBlock = new Group(this, SWT.NONE);
		grpBlock.setText("Selected block");
		grpBlock.setLayout(new GridLayout(7, false));
		
		Label lblName = new Label(grpBlock, SWT.NONE);
		lblName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblName.setText("Name:");
		
		name = new Text(grpBlock, SWT.BORDER);
		GridData gd_name = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_name.widthHint = 100;
		name.setLayoutData(gd_name);
		new Label(grpBlock, SWT.NONE);
		
		visible = new Button(grpBlock, SWT.CHECK);
		visible.setText("Visible");
		new Label(grpBlock, SWT.NONE);
		
		local = new Button(grpBlock, SWT.CHECK);
		local.setText("Local");
		new Label(grpBlock, SWT.NONE);
		
		Label lblPvAddress = new Label(grpBlock, SWT.NONE);
		lblPvAddress.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPvAddress.setText("PV address:");
		
		pvAddress = new Text(grpBlock, SWT.BORDER);
		GridData gd_pvAddress = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_pvAddress.widthHint = 250;
		pvAddress.setLayoutData(gd_pvAddress);
		
		btnPickPV = new Button(grpBlock, SWT.NONE);
		btnPickPV.setText("Select PV");
		btnPickPV.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				openPvDialog();
			}
		});
		
		setEnabled(false);
	}

	public void setConfig(EditableConfiguration config) {
		this.config = config;
	}
	
	public void setBlock(EditableBlock block) {	
		if (bindingContext != null) {
			bindingContext.dispose();
		}
		
		if (block == null) {
			setEnabled(false);
			name.setText("");
			pvAddress.setText("");
			visible.setSelection(false);
			local.setSelection(false);
	
			return;
		}
			
		setEnabled(block.isEditable());
		
		bindingContext = new DataBindingContext();
		strategy.setBeforeSetValidator(new BlockNameValidator(config, block, messageDisplayer));
		
		bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(name), BeanProperties.value("name").observe(block), strategy, null); 
		bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(pvAddress), BeanProperties.value("PV").observe(block));
		bindingContext.bindValue(WidgetProperties.selection().observe(visible), BeanProperties.value("isVisible").observe(block));
		bindingContext.bindValue(WidgetProperties.selection().observe(local), BeanProperties.value("isLocal").observe(block));
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		name.setEnabled(enabled);
		pvAddress.setEnabled(enabled);
		visible.setEnabled(enabled);
		local.setEnabled(enabled);
		btnPickPV.setEnabled(enabled);
	}
	
	private void openPvDialog() {
		PvSelectorDialog pvDialog = new PvSelectorDialog(null, config, pvAddress.getText());	
		if (pvDialog.open() == Window.OK) {
			pvAddress.setText(pvDialog.getPVAddress());
		}
	}
	
}
