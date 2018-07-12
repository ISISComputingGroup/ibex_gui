
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

package uk.ac.stfc.isis.ibex.ui.graphing;

import javax.inject.Inject;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

/**
 * The main view for displaying a graph.
 */
@SuppressWarnings("checkstyle:magicnumber")
public class GraphView {
	
	/**
	 * The public ID of this class.
	 */
	public static final String ID = "uk.ac.stfc.isis.ibex.ui.graphing.GraphView";
    
	private final Canvas canvas;
	private final GC graphicsContext;

	/**
	 * Create the main view for the graph.
	 * @param parent the parent composite injected by SWT.
	 */
	@Inject
	public GraphView(Composite parent) {
		canvas = new Canvas(parent, SWT.BORDER); 
		graphicsContext = new GC(canvas);
        
        canvas.setLayoutData(new FillLayout());
        // canvas.drawBackground(graphicsContext, x, y, width, height);
        canvas.setBackground(new Color(Display.getCurrent(), 255, 0, 0));
    }

}
