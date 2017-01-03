
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

package uk.ac.stfc.isis.ibex.ui.perspectives.switcher;

import java.util.List;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.ISizeProvider;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.SWTResourceManager;

import uk.ac.stfc.isis.ibex.ui.perspectives.Activator;
import uk.ac.stfc.isis.ibex.ui.perspectives.IsisPerspective;

/**
 * Suppress Warnings for the colour and font values.
 */
@SuppressWarnings("checkstyle:magicnumber")
public class PerspectiveSwitcherView extends ViewPart implements ISizeProvider {
    /**
     * Empty constructor for the Perspective Switcher.
     */
	public PerspectiveSwitcherView() {
	}

	private static final Font BUTTON_FONT = SWTResourceManager.getFont("Arial", 12, SWT.NORMAL);
	private static final Color BACKGROUND = SWTResourceManager.getColor(250, 250, 252);
	
    /**
     * The perspective ID.
     */
	public static final String ID = "uk.ac.stfc.isis.ibex.ui.perspectives.PerspectiveSwitcher"; //$NON-NLS-1$

    /**
     * The width of the switcher.
     */
	public static final int FIXED_WIDTH = 200;
	
	/**
	 * Create contents of the view part.
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setBackground(BACKGROUND);
		container.setLayout(new GridLayout(1, false));
				
		List<IsisPerspective> perspectives = Activator.getDefault().perspectives().getVisible();
		
		for (IsisPerspective perspective : perspectives) {	
			PerspectiveButton button = buttonForPerspective(container, perspective);
			configureButton(perspective, button);
		}
	
		createActions();
		initializeToolBar();
		initializeMenu();
	}

    /**
     * Configure the button for display.
     * 
     * @param perspective contains the display information for the perspective
     * @param button is the button to add
     */
    private void configureButton(final IsisPerspective perspective, final PerspectiveButton button) {
        button.setText(perspective.name().replaceAll("&", ""));

        button.setImage(perspective.image());
		button.setAlignment(SWT.LEFT);
		button.setFont(BUTTON_FONT);

		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gd.heightHint = 30;
		gd.minimumHeight = 30;
		
		button.setLayoutData(gd);

        // Adds listeners that will add/remove the mnemonic depending on whether
        // shift/alt are pressed. This is a bit of a fudge to recreate the
        // character underlining behaviour.
        Display.getCurrent().addFilter(SWT.KeyDown, new Listener() {
            @Override
            public void handleEvent(Event event) {
                if ((event.stateMask & (SWT.SHIFT | SWT.ALT)) != 0) {
                    // Add & back into index that it was in originally (used as
                    // the button text keeps changing on alarm/log)
                    int mnemonicIdx = perspective.name().indexOf("&");
                    String buttonText = button.getText();
                    if (buttonText.charAt(mnemonicIdx) != '&') {
                        StringBuilder bldr = new StringBuilder(buttonText);
                        bldr.insert(mnemonicIdx, "&");
                        button.setText(bldr.toString());
                    }
                }
            }
        });
        Display.getCurrent().addFilter(SWT.KeyUp, new Listener() {
            @Override
            public void handleEvent(Event event) {
                if ((event.keyCode == SWT.ALT) || (event.keyCode == SWT.SHIFT)) {
                    button.setText(button.getText().replaceAll("&", ""));
                }
            }
        });
	}

	/**
     * Generate a button for the perspective.
     * 
     * @param container the container the button will be in
     * @param perspective the perspective the button will load
     * @return button to add to the perspective, default is perspective button
     */
	private PerspectiveButton buttonForPerspective(Composite container,
			IsisPerspective perspective) {

		PerspectiveButton buttonToAdd;
		switch (perspective.id()) {
			case "uk.ac.stfc.isis.ibex.ui.log.perspective":
				buttonToAdd = new LogButton(container, perspective.id());
				break;
			case "uk.ac.stfc.isis.ibex.ui.alarm.perspective":
				buttonToAdd = new AlarmButton(container, perspective.id());
				break;
			default:
				buttonToAdd = new PerspectiveButton(container, perspective.id());
				break;
		}

		return buttonToAdd;
	}

    /**
     * Create the actions.
     */
	private void createActions() {
		// Create the actions
	}

	/**
	 * Initialize the toolbar.
	 */
	private void initializeToolBar() {
		@SuppressWarnings("unused")
		IToolBarManager toolbarManager = getViewSite().getActionBars()
				.getToolBarManager();
	}

	/**
	 * Initialize the menu.
	 */
	private void initializeMenu() {
		@SuppressWarnings("unused")
		IMenuManager menuManager = getViewSite().getActionBars()
				.getMenuManager();
	}

	@Override
	public void setFocus() {
		// Set the focus
	}

	@Override
	public int getSizeFlags(boolean width) {
		return width ? SWT.MIN | SWT.MAX : SWT.NONE;
	}

	@Override
	public int computePreferredSize(boolean width, int availableParallel,
			int availablePerpendicular, int preferredResult) {
		return  width ? FIXED_WIDTH : 0;
	}
}
