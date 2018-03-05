
/*
* This file is part of the ISIS IBEX application.
* Copyright (C) 2012-2018 Science & Technology Facilities Council.
* All rights reserved.
*
* This program is distributed in the hope that it will be useful.
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License v1.0 which accompanies this distribution.
* EXCEPT AS EXPRESSLY SET FORTH IN THE ECLIPSE PUBLIC LICENSE V1.0, THE PROGRAM 
* AND ACCOMPANYING MATERIALS ARE PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES 
* OR CONDITIONS OF ANY KIND.  See the Eclipse Public License v1.0 for more details.
*
* You should have received a copy of the Eclipse Public License v1.0
* along with this program; if not, you can obtain a copy from
* https://www.eclipse.org/org/documents/epl-v10.php or 
* http://opensource.org/licenses/eclipse-1.0.php
*/

package uk.ac.stfc.isis.ibex.ui.journalviewer;

import java.awt.FlowLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.SWTResourceManager;

import uk.ac.stfc.isis.ibex.journal.JournalField;
import uk.ac.stfc.isis.ibex.journal.JournalRow;
import uk.ac.stfc.isis.ibex.ui.journalviewer.models.JournalViewModel;
import uk.ac.stfc.isis.ibex.ui.tables.DataboundCellLabelProvider;
import uk.ac.stfc.isis.ibex.ui.tables.DataboundTable;

/**
 * Journal viewer main view.
 */
@SuppressWarnings("checkstyle:magicnumber")
public class JournalViewerView extends ViewPart {

    /**
     * The view ID.
     */
	public static final String ID = "uk.ac.stfc.isis.ibex.ui.journalviewer.JournalViewerView"; //$NON-NLS-1$
	
	private static final int HEADER_FONT_SIZE = 16;
	
    private Label lblError;
    private Label lblLastUpdate;
    
    private final DataBindingContext bindingContext = new DataBindingContext();
    private final JournalViewModel model = JournalViewerUI.getDefault().getModel();
    
    private Button btnRefresh;
    private Spinner spinnerPageNumber;

	private DataboundTable<JournalRow> table;

	/**
	 * Create contents of the view part.
	 * 
	 * @param parent - Parent UI element
	 */
	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new GridLayout(1, false));
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		parent.setLayoutData(gd);

		Label lblTitle = new Label(parent, SWT.NONE);
		lblTitle.setFont(SWTResourceManager.getFont("Segoe UI", HEADER_FONT_SIZE, SWT.BOLD));
		lblTitle.setText("Journal Viewer");
		
		Composite controls = new Composite(parent, SWT.FILL);
		controls.setLayout(new GridLayout(3, false));
		
		Label lblPage = new Label(controls, SWT.NONE);
		lblPage.setText("Page number: ");
        
        spinnerPageNumber = new Spinner(controls, SWT.BORDER);
        spinnerPageNumber.setMinimum(1);
		
        btnRefresh = new Button(controls, SWT.NONE);
        btnRefresh.setText("Refresh data");
		
		Composite selectedContainer = new Composite(parent, SWT.FILL);
		RowLayout rl = new RowLayout();
		rl.justify = false;
		rl.pack = true;
		rl.type = SWT.HORIZONTAL;
		selectedContainer.setLayout(rl);
		selectedContainer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		
		for (final JournalField property : JournalField.values()) {
			final Button checkbox = new Button(selectedContainer, SWT.CHECK);
			checkbox.setText(property.getFriendlyName());
			checkbox.setSelection(model.getFieldSelected(property));
			checkbox.addSelectionListener(new SelectionAdapter() {
	            @Override
	            public void widgetSelected(SelectionEvent e) {
	                super.widgetSelected(e);
	                model.setFieldSelected(property, checkbox.getSelection());
	            }
	        });
		}
		
		final int tableStyle = SWT.FILL;
		table = new DataboundTable<JournalRow>(parent, tableStyle, JournalRow.class, tableStyle) {
			@Override
			protected void addColumns() {
				changeTableColumns();
			}
		};
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		table.initialise();

        lblError = new Label(parent, SWT.NONE);
        lblError.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, false));
        lblError.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
        lblError.setText("placeholder");
        
        lblLastUpdate = new Label(parent, SWT.NONE);
        lblLastUpdate.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
        lblLastUpdate.setText("placeholder");

        bind();
	}
	
	private void changeTableColumns() {
		// Dispose all the columns and re-add them, otherwise the columns may not be in the expected order.
		for (TableColumn col : table.table().getColumns()) {
			col.dispose();
		}
		
		for (JournalField field : JournalField.values()) {
			if (model.getFieldSelected(field)) {
				TableViewerColumn col = table.createColumn(field.getFriendlyName(), 1, true);
				col.getColumn().setText(field.getFriendlyName());
				
				col.setLabelProvider(new DataboundCellLabelProvider<JournalRow>(table.observeProperty("row")) {
					@Override
		            protected String valueFromRow(JournalRow row) {
						return row.get(field);
					}
				});
				
			}
		}
		
		forceResizeTable();
	}
	
	/**
	 * Forces the table to display the columns correctly.
	 * 
	 * This is a dirty hack but is the only way I found to ensure the columns displayed properly.
	 */
	private void forceResizeTable() {
		table.setRedraw(false);
		Point prevSize = table.getSize();
		table.setSize(prevSize.x, prevSize.y - 1);
		table.setSize(prevSize);
		table.setRedraw(true);
	}
	
    private void bind() {
        bindingContext.bindValue(WidgetProperties.text().observe(lblError),
                BeanProperties.value("message").observe(model));
        bindingContext.bindValue(WidgetProperties.text().observe(lblLastUpdate),
                BeanProperties.value("lastUpdate").observe(model));
        bindingContext.bindValue(WidgetProperties.selection().observe(spinnerPageNumber),
                BeanProperties.value("pageNumber").observe(model));
        bindingContext.bindValue(WidgetProperties.maximum().observe(spinnerPageNumber),
                BeanProperties.value("pageNumberMax").observe(model));

        
        btnRefresh.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                model.refresh();
            }
        });
        
        model.addPropertyChangeListener("runs", new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {		
				Display.getDefault().asyncExec(new Runnable() {
					@Override
					public void run() {
						changeTableColumns();	
						table.setRows(model.getRuns());
					}
				});
			}
		});
        
        model.refresh();
    }

    /**
     * {@inheritDoc}
     */
	@Override
	public void setFocus() {
	}
}
