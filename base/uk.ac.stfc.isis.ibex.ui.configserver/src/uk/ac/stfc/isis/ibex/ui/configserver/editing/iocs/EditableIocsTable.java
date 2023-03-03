
/*
 * This file is part of the ISIS IBEX application. Copyright (C) 2012-2017
 * Science & Technology Facilities Council. All rights reserved.
 *
 * This program is distributed in the hope that it will be useful. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution. EXCEPT AS
 * EXPRESSLY SET FORTH IN THE ECLIPSE PUBLIC LICENSE V1.0, THE PROGRAM AND
 * ACCOMPANYING MATERIALS ARE PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND. See the Eclipse Public License v1.0 for more
 * details.
 *
 * You should have received a copy of the Eclipse Public License v1.0 along with
 * this program; if not, you can obtain a copy from
 * https://www.eclipse.org/org/documents/epl-v10.php or
 * http://opensource.org/licenses/eclipse-1.0.php
 */

package uk.ac.stfc.isis.ibex.ui.configserver.editing.iocs;

import java.util.Arrays;
import java.util.Collection;

import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import uk.ac.stfc.isis.ibex.configserver.editing.EditableIoc;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.CellDecorator;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.DecoratedCellLabelProvider;
import uk.ac.stfc.isis.ibex.ui.ioccontrol.IocModePreferenceHandler;
import uk.ac.stfc.isis.ibex.ui.tables.DataboundCellLabelProvider;
import uk.ac.stfc.isis.ibex.ui.tables.DataboundTable;
import uk.ac.stfc.isis.ibex.ui.widgets.CheckboxLabelProvider;

/**
 * Table describing all the IOCs to be started with a configuration.
 * 
 * Note that all columns in this table are not resizable as the H_SCROLL 
 * has been removed and resizing could cause columns to disappear.
 * 
 * The H_SCROLL has been removed as it was appearing despite no extra 
 * data being in the table (unsure why)
 */
@SuppressWarnings("checkstyle:magicnumber")
public class EditableIocsTable extends DataboundTable<EditableIoc> {

    TableViewerColumn autoStart;
    TableViewerColumn autoRestart;
    TableViewerColumn remotePvPrefix;

	private final CellDecorator<EditableIoc> rowDecorator = new IocRowCellDecorator();
	private final CellDecorator<EditableIoc> simulationDecorator = new IocSimulationCellDecorator();

    /**
     * Checkbox Label provider for the auto-start column.
     */
    private CheckboxLabelProvider<EditableIoc> autoStartLabelProvider = new CheckboxLabelProvider<EditableIoc>(observeProperty("autostart")) {

    @Override
    protected boolean checked(EditableIoc ioc) {
        return ioc.getAutostart();
    }
    @Override
    protected void setChecked(EditableIoc ioc, boolean checked) {
        ioc.setAutostart(checked);
    }
    @Override
        protected boolean isEditable(EditableIoc ioc) {
            return !ioc.inComponent();
        }
    };

    /**
     * Checkbox Label provider for the auto-restart column.
     */
    private CheckboxLabelProvider<EditableIoc> autoRestartLabelProvider = new CheckboxLabelProvider<EditableIoc>(observeProperty("restart")) {
        @Override
        protected boolean checked(EditableIoc ioc) {
            return ioc.getRestart();
        }
        @Override
        protected void setChecked(EditableIoc ioc, boolean checked) {
            ioc.setRestart(checked);
        }
        @Override
        protected boolean isEditable(EditableIoc ioc) {
            return !ioc.inComponent();
        }
    };
    
    private DataboundCellLabelProvider<EditableIoc> remotePvPrefixLabelProvider = new DataboundCellLabelProvider<EditableIoc>(observeProperty("remotePvPrefix")) {
		@Override
		protected String stringFromRow(EditableIoc ioc) {
			return ioc.getRemotePvPrefix();
		}
	};

    /**
     * Standard constructor.
     * 
     * @param parent
     *            The parent composite.
     * @param style
     *            The SWT style.
     * @param tableStyle
     *            The SWT table style.
     */
    public EditableIocsTable(Composite parent, int style, int tableStyle) {
		super(parent, style, tableStyle | SWT.NO_SCROLL | SWT.V_SCROLL);
		
        setSortAction(() -> resetCheckboxLabelProvidersUpdateFlags());
				
        initialise();
	}

    /**
     * Adds columns to the table.
     */
	@Override
	protected void addColumns() {
		name();
		description();
		if (IocModePreferenceHandler.getDisplayRunMode()) {
			simLevel();
		}
		autostart();
		restart();
		remotePvPrefix();
	}

    /**
     * Creates the Name column.
     */
	private void name() {
        createColumn("Name", 1, false, new DecoratedCellLabelProvider<EditableIoc>(
				observeProperty("name"), 
				Arrays.asList(rowDecorator, simulationDecorator)) {
			@Override
			public String stringFromRow(EditableIoc row) {
				return row.getName();
			}
		});
	}

    /**
     * Creates the Description column.
     */
	private void description() {
        createColumn("Description", 2, false, new DecoratedCellLabelProvider<EditableIoc>(
                observeProperty("name"), 
				Arrays.asList(rowDecorator)) {
			@Override
			public String stringFromRow(EditableIoc row) {
				return row.getDescription();
			}
		});	
	}

    /**
     * Creates the Simulation Level column.
     */
    private void simLevel() {
        TableViewerColumn simLevel = createColumn("Sim. level", 1, false, new DecoratedCellLabelProvider<EditableIoc>(observeProperty("simLevel"), Arrays.asList(rowDecorator)) {
                    @Override
					public String stringFromRow(EditableIoc row) {
                        return row.getSimLevel().toString();
                    }
                });
        simLevel.setEditingSupport(new SimLevelEditingSupport(viewer()));
        setColumnToolTipText(simLevel,
        		"By default, the simulation level file is set to NONE, meaning that the IOC will not run in simulation mode.\n"
        		+ "Under normal circumstances, you should not change the default setting.\n"
        		+ "Simulation mode is used for running the IOC without the actual physical device."
        );
    }

    /**
     * Creates the Auto-Start column.
     */
	private void autostart() {
        autoStart = createColumn("Auto-start?", 1, false, autoStartLabelProvider);
        setColumnToolTipText(autoStart, 
        		"If set, the IOC will be started/restarted whenever the configuration is changed.\n"
        		+ "If not set then if the IOC is not running it will remained stopped after config change,\n" 
        		+ "and if it is running it will remain running throughout the config change.\n"
        		+ "\n" 
        		+ "Warning: if not set and the IOC is running, any changes you make (e.g. a macro change)\n" 
        		+ "will not be set on the IOC until you restart it manually."
        );
	}

    /**
     * Creates the Auto-Restart column.
     */
	private void restart() {
        autoRestart = createColumn("Auto-restart?", 1, false, autoRestartLabelProvider);
        setColumnToolTipText(autoRestart, 
        		"If set, the IOC will be automatically restarted if it is terminated unexpectedly.\n"
        		+ "If the IOC is stopped from the client or writing to the appropriate PV then it will not be restarted."
        );
	}
	
	/**
     * Creates the remote pv prefix column.
     */
	private void remotePvPrefix() {
        remotePvPrefix = createColumn("Remote prefix", 1, false, remotePvPrefixLabelProvider);
	}

    /**
     * Fills the table with row data.
     * 
     * @param rows
     *            The rows
     */
    @Override
    public void setRows(Collection<EditableIoc> rows) {
        clear();
        super.setRows(rows);
    }

    /**
     * Clears old check boxes from the table.
     */
    private void clear() {
        autoStart.setLabelProvider(autoStartLabelProvider);
        autoRestart.setLabelProvider(autoRestartLabelProvider);
    }
    
    /**Resets the check box listener update flags of the autoStart and 
     * autoRestart label providers.*/
    private void resetCheckboxLabelProvidersUpdateFlags() {
        autoStartLabelProvider.resetCheckBoxListenerUpdateFlags();
        autoRestartLabelProvider.resetCheckBoxListenerUpdateFlags();
    }
}
