
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
import java.util.Optional;

import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.wb.swt.ResourceManager;

import uk.ac.stfc.isis.ibex.ui.configserver.CheckboxLabelProvider;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.CellDecorator;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.DecoratedCellLabelProvider;
import uk.ac.stfc.isis.ibex.ui.tables.DataboundCellLabelProvider;
import uk.ac.stfc.isis.ibex.ui.tables.DataboundTable;
import uk.ac.stfc.isis.ibex.ui.widgets.StringEditingSupport;

/**
 * The table for editing IOC macros.
 */
@SuppressWarnings("checkstyle:magicnumber")
public class MacroTable extends DataboundTable<MacroViewModel> {
	
    private final CellDecorator<MacroViewModel> rowDecorator = new MacroRowCellDecorator();
    private MacroValueValidator valueValidator;
    private StringEditingSupport<MacroViewModel> editingSupport;
    private boolean canEdit;
    private static final Color READONLY_COLOR = ResourceManager.getColor(SWT.COLOR_DARK_GRAY);
    
    /**
     * Constructor for the table.
     * 
     * @param parent
     *            The composite to put the table in.
     * @param style
     *            The SWT style of this databound table.
     * @param tableStyle
     *            The SWT style of the inner table object.
     */
	public MacroTable(Composite parent, int style, int tableStyle) {
		super(parent, style, tableStyle | SWT.BORDER);

		initialise();
	}
	
	@Override
	public void setRows(Collection<MacroViewModel> rows) {
		super.setRows(rows);
	}

	@Override
	protected void addColumns() {
		name();
		value();
		useDefault();
		defaultValue();
		description();
		pattern();
	}
	
	private void name() {
		createColumn("Macro name", 5, new DataboundCellLabelProvider<MacroViewModel>(observeProperty("name")) {
			@Override
			protected String stringFromRow(MacroViewModel row) {
				return row.getName();
			}
		});
	}
	
	private void value() {
		TableViewerColumn value = createColumn("Value", 5, new DecoratedCellLabelProvider<MacroViewModel>(observeProperty("value"), Arrays.asList(rowDecorator)) {
			@Override
			protected String stringFromRow(MacroViewModel row) {
			    return row.getDisplayValue();
			}
		});
		
		editingSupport = new StringEditingSupport<MacroViewModel>(viewer(), MacroViewModel.class) {
            
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
            protected String valueFromRow(MacroViewModel row) {
                // This is called when the user clicks on a cell to edit it.
                return row.getEditCellValue();
            }

            @Override
            protected void setValueForRow(MacroViewModel row, String value) {
                if (valueValidator.validate(value) == ValidationStatus.ok()) {
                    row.setValue(Optional.of(value));
                } else {
                    valueValidator.validate("");
                }
            }
        };
		
		value.setEditingSupport(editingSupport);
	}
	
	private void useDefault() {
	    createColumn("Use Default?", 3, false, new CheckboxLabelProvider<MacroViewModel>(observeProperty("useDefault")) {

	        @Override
	        protected boolean checked(MacroViewModel macro) {
	            return macro.getUseDefault();
	        }
	        
	        @Override
	        protected void setChecked(MacroViewModel macro, boolean checked) {
	            macro.updateFromUseDefaultCheck(checked);
	        }
	        
            @Override
            protected boolean isEditable(MacroViewModel model) {
                return canEdit;
            }
	    });
	}
	
	private void defaultValue() {
	    createColumn("Default", 5, new DataboundCellLabelProvider<MacroViewModel>(observeProperty("defaultValue")) {
	        @Override
	        public void update(ViewerCell cell) {
	            super.update(cell);
	            cell.setForeground(READONLY_COLOR);
	        }
            @Override
            protected String stringFromRow(MacroViewModel row) {
                return row.getDisplayDefault();
            }
        });
    }
	
	private void description() {
		createColumn("Description", 12, new DataboundCellLabelProvider<MacroViewModel>(observeProperty("description")) {
			@Override
			protected String stringFromRow(MacroViewModel row) {
				return row.getDescription();
			}
		});	
	}
	
	private void pattern() {
		createColumn("Pattern", 6, new DataboundCellLabelProvider<MacroViewModel>(observeProperty("pattern")) {
			@Override
			protected String stringFromRow(MacroViewModel row) {
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

    /**
     * Sets whether the table can be edited.
     * @param canEdit whether or not the table can be edited.
     */
    public void setCanEdit(boolean canEdit) {
        this.canEdit = canEdit;
        editingSupport.setEnabled(canEdit);
    }
}
