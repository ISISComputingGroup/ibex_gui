
/*
* This file is part of the ISIS IBEX application.
* Copyright (C) 2012-2015 Science & Technology Facilities Council.
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

package uk.ac.stfc.isis.ibex.ui.configserver.editing.iocs;

import java.util.Arrays;

import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import uk.ac.stfc.isis.ibex.configserver.configuration.Ioc;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableIoc;
import uk.ac.stfc.isis.ibex.ui.configserver.CheckboxLabelProvider;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.CellDecorator;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.DecoratedCellLabelProvider;
import uk.ac.stfc.isis.ibex.ui.tables.DataboundTable;

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

	private final CellDecorator<EditableIoc> rowDecorator = new IocRowCellDecorator();
	private final CellDecorator<EditableIoc> simulationDecorator = new IocSimulationCellDecorator();
	
	public EditableIocsTable(Composite parent, int style, int tableStyle) {
		super(parent, style, EditableIoc.class, tableStyle | SWT.NO_SCROLL | SWT.V_SCROLL);
				
		initialise();		
	}
	
	@Override
	protected void addColumns() {
		name();
		description();
		simLevel();
		autostart();
		restart();
	}

	private void name() {
		TableViewerColumn name = createColumn("Name", 2, false);
		name.setLabelProvider(new DecoratedCellLabelProvider<EditableIoc>(
				observeProperty("name"), 
				Arrays.asList(rowDecorator, simulationDecorator)) {
			@Override
			protected String valueFromRow(EditableIoc row) {
				return row.getName();
			}
		});
	}
	
	private void description() {
		TableViewerColumn desc = createColumn("Description", 2, false);
		desc.setLabelProvider(new DecoratedCellLabelProvider<EditableIoc>(
				observeProperty("name"), 
				Arrays.asList(rowDecorator)) {
			@Override
			protected String valueFromRow(EditableIoc row) {
				return row.getDescription();
			}
		});	
	}
	
	private void autostart() {
		TableViewerColumn enabled = createColumn("Auto-start?", 1, false);
		IObservableMap[] stateProperties = {observeProperty("autostart")};
		enabled.setLabelProvider(new CheckboxLabelProvider<Ioc>(stateProperties) {	
			@Override
			protected boolean checked(Ioc ioc) {
				return ioc.getAutostart();
			}
			
			@Override
			protected void setChecked(Ioc ioc, boolean checked) {
				ioc.setAutostart(checked);
			}
			
			@Override
			protected boolean isEditable(Ioc ioc) {
				return !ioc.hasComponent();
			}
		});	
	}
	
	private void restart() {
		TableViewerColumn enabled = createColumn("Auto-restart?", 1, false);
		IObservableMap[] stateProperties = {observeProperty("restart")};
		enabled.setLabelProvider(new CheckboxLabelProvider<Ioc>(stateProperties) {	
			@Override
			protected boolean checked(Ioc ioc) {
				return ioc.getRestart();
			}
			
			@Override
			protected void setChecked(Ioc ioc, boolean checked) {
				ioc.setRestart(checked);
			}
			
			@Override
			protected boolean isEditable(Ioc ioc) {
				return !ioc.hasComponent();
			}
		});	
	}
	
	private void simLevel() {
		TableViewerColumn simLevel = createColumn("Sim. level", 1, false);
		simLevel.setLabelProvider(new DecoratedCellLabelProvider<EditableIoc>(
				observeProperty("simLevel"), 
				Arrays.asList(rowDecorator)) {
			@Override
			protected String valueFromRow(EditableIoc row) {
				return row.getSimLevel().toString();
			}
		});	
		simLevel.setEditingSupport(new SimLevelEditingSupport(viewer()));
	}
}
