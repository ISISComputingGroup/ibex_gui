
/*
* This file is part of the ISIS IBEX application.
* Copyright (C) 2012-2019 Science & Technology Facilities Council.
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

package uk.ac.stfc.isis.ibex.ui.configserver.editing.macros;

import java.util.Arrays;
import java.util.Collection;

import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.widgets.Composite;

import uk.ac.stfc.isis.ibex.configserver.configuration.Macro;
import uk.ac.stfc.isis.ibex.configserver.editing.MacroValueValidator;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.CellDecorator;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.DecoratedCellLabelProvider;
import uk.ac.stfc.isis.ibex.ui.tables.DataboundCellLabelProvider;
import uk.ac.stfc.isis.ibex.ui.tables.DataboundTable;
import uk.ac.stfc.isis.ibex.ui.widgets.StringEditingSupport;

/**
 * The table for editing IOC macros.
 */
@SuppressWarnings("checkstyle:magicnumber")
public class MacroTable extends DataboundTable<Macro> {
	
    private final CellDecorator<Macro> rowDecorator = new MacroRowCellDecorator();
    private MacroValueValidator valueValidator;
    
    /**
     * Constructor for the table.
     * 
     * @param parent
     *            The composite to put the table in.
     * @param style
     *            The SWT style of this databound table.
     * @param tableStyle
     *            The SWT style of the inner table object.
     * @param valueValidator
     *            The macro value validator of the macro panel.
     */
	public MacroTable(Composite parent, int style, int tableStyle, MacroValueValidator valueValidator) {
		super(parent, style, tableStyle | SWT.BORDER);
		this.valueValidator = valueValidator;

		initialise();
	}
	
	@Override
	public void setRows(Collection<Macro> rows) {
		super.setRows(rows);
	}

	@Override
	protected void addColumns() {
		name();
		value();
		description();
		pattern();
	}
	
	private void name() {
		createColumn("Macro name", 6, new DataboundCellLabelProvider<Macro>(observeProperty("name")) {
			@Override
			protected String stringFromRow(Macro row) {
				return row.getName();
			}
		});
	}
	
	private void value() {
		TableViewerColumn value = createColumn("Value", 5, new DecoratedCellLabelProvider<Macro>(observeProperty("value"), Arrays.asList(rowDecorator)) {
			@Override
			protected String stringFromRow(Macro row) {
			    if (row.getValue() == null && row.getDefaultValue() != null) {
			        return "(default: " + row.getDefaultValue() + ")";
			    } else if (row.getValue() == null) {
			        return "(default)";
			    } else {
			        return row.getValue();
			    }
			}
		});
		
		value.setEditingSupport(new StringEditingSupport<Macro>(viewer(), Macro.class) {
		    
		    @Override
		    protected TextCellEditor createTextCellEditor(ColumnViewer viewer) {
		        return new TextCellEditor((Composite) viewer.getControl()) {
		            @Override
		            protected void editOccured(ModifyEvent e) {
		                super.editOccured(e);
		                valueValidator.validate(text.getText());
		            }
		        };
		    }

            @Override
            protected String valueFromRow(Macro row) {
                if (row == null || row.getValue() == null) {
                    return "";
                } else {
                    return row.getValue();
                }
            }

            @Override
            protected void setValueForRow(Macro row, String value) {
                if (valueValidator.validate(value) == ValidationStatus.ok()) {
                    row.setValue(value);
                }
            }
        });
	}
	
	private void description() {
		createColumn("Description", 8, new DataboundCellLabelProvider<Macro>(observeProperty("description")) {
			@Override
			protected String stringFromRow(Macro row) {
				return row.getDescription();
			}
		});	
	}
	
	private void pattern() {
		createColumn("Pattern", 8, new DataboundCellLabelProvider<Macro>(observeProperty("pattern")) {
			@Override
			protected String stringFromRow(Macro row) {
				return row.getPattern();
			}
		});	
	}

	/**
	 * Sets the validator used to validate user input.
	 * @param valueValidator a MacroValueValidator
	 */
    public void setValidator(MacroValueValidator valueValidator) {
        this.valueValidator = valueValidator;
    }
}
