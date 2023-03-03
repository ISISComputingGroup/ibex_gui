
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

package uk.ac.stfc.isis.ibex.ui.synoptic.beamline;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Layout;

/**
 * Composite for storing beamline components.
 */
public class BeamlineCompositeContainer extends BeamlineComposite {

	private final List<BeamlineComposite> targets = new ArrayList<>();
	private final GridLayout grid = new GridLayout(1, false);
	
	private int targetLineHeight;
	private boolean isPreview;
	
	/**
	 * Composite for storing beamline components.
	 * 
	 * @param parent
	 *             The parent this composite belongs too.
	 * @param style
	 *             The SWT style display.
	 * @param isPreview
	 *             True if this composite was created in the synoptic preview.
	 */
	public BeamlineCompositeContainer(Composite parent, int style, boolean isPreview) {
		super(parent, style);
		
		super.setLayout(grid);
		this.isPreview = isPreview;
		grid.marginHeight = 0;
		grid.verticalSpacing = 0;
		setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
	}
	
	@Override
	public void setLayout(Layout layout) {
		// Do nothing, layout is fixed
	}

	/**
	 * Registers a target with this beamline view.
	 * @param target the target to add
	 */
	public void registerBeamlineTarget(BeamlineComposite target) {
	    target.setIsPreview(isPreview);
		targets.add(target);		
		setTargetSize(target);
		grid.numColumns = targets.size();
		
		targetLineHeight = Math.max(targetLineHeight, target.beamLineHeight());
		alignVerticalHeights();
	}

	@Override
	public int beamLineHeight() {
		return targetLineHeight;
	}
	
	private static void setTargetSize(Composite composite) {
		composite.pack();
		GridData gd = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
		gd.minimumHeight = composite.getSize().y;
		gd.heightHint = gd.minimumHeight;
		composite.setLayoutData(gd);
	}
	
	private void alignVerticalHeights() {
		for (BeamlineComposite target : targets) {
			alignVerticalHeight(target);
		}
	}

	private void alignVerticalHeight(BeamlineComposite target) {
		GridData gd = (GridData) target.getLayoutData();
		gd.verticalIndent = targetLineHeight - target.beamLineHeight();
	}
	
	/**
     * Overrides standard getBounds() method, to ensure only extent of children
     * is used.
     * 
     * @return Width and height give maximum extent of container
     */
	@Override
	public Rectangle getBounds() {
		Control[] controls = getChildren();
		int maxX = 0, maxY = 0;
		
		for (Control control : controls) {
			Rectangle bounds = control.getBounds();
			maxX = Math.max(bounds.width + bounds.x, maxX);
			maxY = Math.max(bounds.height + bounds.y, maxY);
		}
		
		// Add the margins
		maxX += grid.marginWidth;
		maxY += grid.marginHeight;
		
		// Rectangle(x, y, width, height) - assume starts at 0
		return new Rectangle(0, 0, maxX, maxY);
	}
}
