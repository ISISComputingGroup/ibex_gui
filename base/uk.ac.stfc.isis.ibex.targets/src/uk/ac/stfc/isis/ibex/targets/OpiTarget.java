
/*
 * This file is part of the ISIS IBEX application. Copyright (C) 2012-2016
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

package uk.ac.stfc.isis.ibex.targets;

/**
 * A target customised for displaying OPIs in the view.
 */
public class OpiTarget extends Target {
	
    /**
     * The OPI name. This is used to look up the OPI from the available list and
     * is notably different from the 'name' field which is used more as a title.
     */
	private final String opiName;
	
    /**
     * @param title - Used as the target's main name
     * @param opiName - Used to look up the OPI with the corresponding name
     */
    public OpiTarget(String title, String opiName) {
        super(title);
		this.opiName = opiName;
	}
	
    /**
     * @return The name of the targeted OPI
     */
	public String opiName() {
		return opiName;
	}
}
