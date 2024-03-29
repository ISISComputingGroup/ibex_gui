
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

package uk.ac.stfc.isis.ibex.ui.synoptic.widgets;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import uk.ac.stfc.isis.ibex.synoptic.model.Component;
import uk.ac.stfc.isis.ibex.ui.synoptic.beamline.BeamlineCompositeContainer;
import uk.ac.stfc.isis.ibex.ui.synoptic.beamline.DotDashedLine;
import uk.ac.stfc.isis.ibex.ui.synoptic.beamline.LineDecoration;
import uk.ac.stfc.isis.ibex.ui.synoptic.component.ComponentView;

/**
 * The synoptic's components with an optionally overlaid beamline.
 */
public class SynopticPanel extends Composite {

	private static final int PAGE_INCREMENT = 500;
    private BeamlineCompositeContainer instrumentComposite;
	private LineDecoration beamline;
	private ScrolledComposite scrolledComposite;
	private boolean isPreview;
	
	/**
	 * Constructor for the synoptic panel.
	 * @param parent The parent composite that this panel belongs to.
	 * @param style The SWT style flags for the panel.
	 * @param isPreview True if the panel is part of the editor preview.
	 */
    public SynopticPanel(Composite parent, int style, boolean isPreview) {
		super(parent, style);
		setLayout(new FillLayout(SWT.VERTICAL));
		this.isPreview = isPreview;

		scrolledComposite = new ScrolledComposite(this, SWT.NONE | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);	
		scrolledComposite.getHorizontalBar().setPageIncrement(PAGE_INCREMENT);
		scrolledComposite.addControlListener(new ControlAdapter() {
			@Override
			public void controlResized(ControlEvent arg0) {
				scrolledComposite.getHorizontalBar().setPageIncrement(scrolledComposite.getSize().x);
			}
		});

		reset();
	}

    /**
     * Sets the components for this synoptic.
     * @param components the components
     * @param showBeam true to show beam; false otherwise
     */
	public void setComponents(List<? extends Component> components, Boolean showBeam) {
		reset();
		display(components, showBeam);
		resize();
	}

	private void reset() {
		clearInstrument();
		instrumentComposite = new BeamlineCompositeContainer(scrolledComposite, SWT.NONE, isPreview);
		scrolledComposite.setContent(instrumentComposite);
	}
	
	private void clearInstrument() {
		if (instrumentComposite != null) {
			for (Control child : instrumentComposite.getChildren()) {
				child.dispose();
			}
			
			instrumentComposite.dispose();
		}
	}

	private void display(List<? extends Component> components, Boolean showBeam) {
		if (components.isEmpty()) {
			return;
		}
		
		for (Component component : components) {
			ComponentView.create(instrumentComposite, component, isPreview);
		}
		
		if (showBeam) {
			addBeamline();				
		}
	}

	private void addBeamline() {
        beamline = new LineDecoration(instrumentComposite, new DotDashedLine(1), instrumentComposite.beamLineHeight());
		beamline.addLineTo(instrumentComposite);
	}
	
	private void resize() {
		instrumentComposite.layout();
		instrumentComposite.pack();
		Rectangle bounds = instrumentComposite.getBounds(); 
		scrolledComposite.setMinSize(bounds.width, bounds.height);
	}
}
