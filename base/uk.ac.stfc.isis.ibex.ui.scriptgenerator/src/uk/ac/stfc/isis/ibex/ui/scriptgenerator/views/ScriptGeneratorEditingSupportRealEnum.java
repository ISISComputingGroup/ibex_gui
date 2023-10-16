package uk.ac.stfc.isis.ibex.ui.scriptgenerator.views;


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

import org.eclipse.jface.viewers.ColumnViewer;

import uk.ac.stfc.isis.ibex.scriptgenerator.JavaActionParameter;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ScriptGeneratorAction;
import uk.ac.stfc.isis.ibex.ui.widgets.EnumEditingSupport;

/**
 * Editing support for DAE period types.
 */
public class ScriptGeneratorEditingSupportRealEnum extends EnumEditingSupport<ScriptGeneratorAction, TestEnumType> {
	private JavaActionParameter actionParameter;

	/**
	 * Create the editing support.
	 * @param viewer the column viewer
	 * @param rowType the row type to be edited
	 * @param enumType the enum type (cell type) to be edited
	 */
	public ScriptGeneratorEditingSupportRealEnum(ColumnViewer viewer,  Class<ScriptGeneratorAction> rowType, Class<TestEnumType> enumType, JavaActionParameter actionParameter) {
		super(viewer, rowType, enumType);
		this.actionParameter = actionParameter;
	}

	@Override
	protected TestEnumType getEnumValueForRow(ScriptGeneratorAction row) {
		
		return TestEnumType.valueOf(row.getActionParameterValue(actionParameter));
	}

	@Override
	protected void setEnumForRow(ScriptGeneratorAction row, TestEnumType value) {
		row.setActionParameterValue(actionParameter, value.toString());
	}

}
