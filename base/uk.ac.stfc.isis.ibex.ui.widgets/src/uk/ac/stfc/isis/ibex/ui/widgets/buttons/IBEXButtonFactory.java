/*
 * This file is part of the ISIS IBEX application.
 * Copyright (C) 2012-2023 Science & Technology Facilities Council.
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

package uk.ac.stfc.isis.ibex.ui.widgets.buttons;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Listener;

import uk.ac.stfc.isis.ibex.model.Action;

/**
 * A factory pattern for creating commonly used buttons.
 */
public class IBEXButtonFactory {

	/**
	 * Do not use this constructor.
	 */
	protected IBEXButtonFactory() {
		// Utility class is not meant to be instantiated
		throw new UnsupportedOperationException();
	}

	/**
	 * Creates a button that appears as a checkbox.
	 * 
	 * @param parent          a composite control which will be the parent of the
	 *                        new instance (cannot be null)
	 * @param text            the title of the checkbox
	 * @param tooltip         the tooltip of the button (can be null)
	 * @param onClickListener the event handler for clicks
	 * @return n new button instance
	 */
	public static Button checkbox(Composite parent, String text, String tooltip, Listener onClickListener) {
		int style = SWT.CHECK;
		GridData layoutData = new GridData();

		return create(parent, style, text, tooltip, null, onClickListener, layoutData);
	}

	public static Button checkboxRow(Composite parent, String text, String tooltip, Listener onClickListener) {
		int style = SWT.CHECK;
		RowData layoutData = new RowData();

		return create(parent, style, text, tooltip, null, onClickListener, layoutData);
	}

	public static Button radio(Composite parent, String text, String tooltip, Listener onClickListener) {
		int style = SWT.RADIO;
		RowData layoutData = new RowData();

		return create(parent, style, text, tooltip, null, onClickListener, layoutData);
	}

	/**
	 * Creates a button that fills all available horizontal space.
	 * 
	 * @param parent          a composite control which will be the parent of the
	 *                        new instance (cannot be null)
	 * @param text            the title of the button
	 * @param tooltip         the tooltip of the button (can be null)
	 * @param image           the image of the button displayed before the title
	 *                        (can be null)
	 * @param onClickListener the event handler for clicks
	 * @return a new button instance
	 */
	public static Button expanding(Composite parent, String text, String tooltip, Image image,
			Listener onClickListener) {
		int style = SWT.NONE;
		GridData layoutData = new GridData(SWT.FILL, SWT.FILL, true, true);

		return create(parent, style, text, tooltip, image, onClickListener, layoutData);
	}

	/**
	 * Creates a button takes up only as much horizontal space as its content needs.
	 *
	 * @param parent          a composite control which will be the parent of the
	 *                        new instance (cannot be null)
	 * @param text            the title of the button
	 * @param tooltip         the tooltip of the button (can be null)
	 * @param image           the image of the button displayed before the title
	 *                        (can be null)
	 * @param onClickListener the event handler for clicks
	 * @return a new button instance
	 */
	public static Button compact(Composite parent, String text, String tooltip, Image image, Listener onClickListener) {
		int style = SWT.NONE;
		GridData layoutData = new GridData(SWT.FILL, SWT.FILL, false, false);

		return create(parent, style, text, tooltip, image, onClickListener, layoutData);
	}

	public static Button fitText(Composite parent, String text, String tooltip, Integer widthHint,
			Listener onClickListener) {
		int style = SWT.NONE;
		GridData layoutData = new GridData(SWT.LEFT, SWT.FILL, false, false);
		if (widthHint != null) {
			layoutData.widthHint = widthHint;
		}

		return create(parent, style, text, tooltip, null, onClickListener, layoutData);
	}

	/**
	 * Creates a button using the given parameters.
	 * 
	 * @param parent          the Composite into which the button is placed
	 * @param style
	 * @param text            the title of the button
	 * @param tooltip         the tooltip string for the button (or null)
	 * @param image           the image icon of the button
	 * @param onClickListener the action that happens when button is clicked (click
	 *                        event is propagated)
	 * @param layoutData      the layout data for the button
	 * @return the new button instance
	 */
	public static Button create(Composite parent, int style, String text, String tooltip, Image image,
			Listener onClickListener, Object layoutData) {
		Button btn = new Button(parent, style);
		if (text != null) {
			btn.setText(text);
		}
		btn.setToolTipText(tooltip);
		if (image != null) {
			btn.setImage(image);
		}
		if (onClickListener != null) {
			btn.addListener(SWT.Selection, onClickListener);
		}
		btn.setLayoutData(layoutData);
		return btn;
	}

	public static ActionButton actionButton(Composite parent, Action action, String text, Image image) {
		ActionButton button = new ActionButton(parent, SWT.CENTER, action);

		button.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		button.setText(text);
		button.setImage(image);

		return button;
	}

	public static HelpButton helpButton(Composite parent, String webLink, String desc) {
		return new HelpButton(parent, webLink, desc);
	}

}
