
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

package uk.ac.stfc.isis.ibex.dae.xml;

/**
 * An XML node representing an Enum type.
 *
 * @param <E> the enum type
 */
public class EnumNode<E extends Enum<E>> extends XmlNode<E> {

	private Class<E> enumType;
	
	/**
	 * Creates a new XML node representing the provided enum type.
	 * @param xPathExpression the XPATH expression
	 * @param enumType the enum type
	 */
	public EnumNode(String xPathExpression, Class<E> enumType) {
		super(xPathExpression);
		this.enumType = enumType;
	}

	@Override
	public E value() {
		if (node() == null) {
			return null;
		}
		
        int index = Integer.parseInt(node().getTextContent());
        try {
            return enumType.getEnumConstants()[index];
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
	}

	@Override
	public void setValue(E value) {
		if (node() != null) {
			String text = String.format("%d", value.ordinal());
			node().setTextContent(text);
		}
	}


}
