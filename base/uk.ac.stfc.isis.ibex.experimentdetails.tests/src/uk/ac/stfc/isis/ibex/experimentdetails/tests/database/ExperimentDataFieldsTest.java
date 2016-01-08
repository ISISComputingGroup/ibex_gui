
/*
 * This file is part of the ISIS IBEX application. Copyright (C) 2012-2015
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

package uk.ac.stfc.isis.ibex.experimentdetails.tests.database;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import uk.ac.stfc.isis.ibex.experimentdetails.database.ExpDataField;
import uk.ac.stfc.isis.ibex.experimentdetails.database.ExpDataFieldsCreator;
import uk.ac.stfc.isis.ibex.experimentdetails.database.ExpDataFieldsEnum;
import uk.ac.stfc.isis.ibex.experimentdetails.database.ExpDataTablesEnum;

public class ExperimentDataFieldsTest {
	@Test
	public void user_table_contains_name() {
		// Act
		ExpDataField result = ExpDataFieldsCreator.getField(ExpDataTablesEnum.USER_TABLE, ExpDataFieldsEnum.NAME);
				
		//Assert
		assertEquals(result.toString(), "user.name");
	}
	
	@Test
	public void role_table_contains_priority() {
		//Act
		ExpDataField  result = ExpDataFieldsCreator.getField(ExpDataTablesEnum.ROLE_TABLE, ExpDataFieldsEnum.PRIORITY);
		
		//Assert
		assertEquals(result.toString(), "role.priority");		
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void experiment_table_does_not_contain_name() {
		//Act
		ExpDataField  result = ExpDataFieldsCreator.getField(ExpDataTablesEnum.EXPERIMENT_TABLE, ExpDataFieldsEnum.NAME);	
	}
}
