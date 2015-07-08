
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

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public abstract class XmlNode<T> {

	private final XPath xpath = XPathFactory.newInstance().newXPath();
	private final String expression;
	
	private Node node;
	
	public XmlNode(String xPathExpression) {
		expression = xPathExpression;
	}
	
	public void setDoc(Document doc) throws XPathExpressionException {
		setNode(doc);
	}

	public abstract T value();
	
	public abstract void setValue(T value);
	
	protected Node node() {
		return node;
	}
	
	private void setNode(Document doc) throws XPathExpressionException {
		NodeList nodes = (NodeList) xpath.compile(expression).evaluate(doc, XPathConstants.NODESET);	
		node =  nodes.item(0);
	}
}
