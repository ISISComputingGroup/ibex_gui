
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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.mihalis.opal.breadcrumb.Breadcrumb;
import org.mihalis.opal.breadcrumb.BreadcrumbItem;

import uk.ac.stfc.isis.ibex.ui.synoptic.Activator;
import uk.ac.stfc.isis.ibex.ui.synoptic.SynopticPresenter;

public class SynoptictBreadCrumb extends Composite {
	
	private static final String TWO_SPACES = "  ";
	private static final String THREE_SPACES = "   ";
	private static final Display DISPLAY = Display.getCurrent();
	
	private final Breadcrumb trail;
	private final List<BreadcrumbItem> crumbs = new ArrayList<>(); 
	
	private final SynopticPresenter presenter;
	
	public SynoptictBreadCrumb(Composite parent, int style) {
		super(parent, style);
		GridLayout gridLayout = new GridLayout(2, false);
		gridLayout.verticalSpacing = 0;
		gridLayout.marginWidth = 0;
		gridLayout.marginHeight = 0;
		setLayout(gridLayout);

		trail = new Breadcrumb(this, SWT.BORDER);
		trail.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		presenter = Activator.getDefault().presenter();
		
		setComponents(presenter.currentTrail());
		presenter.addPropertyChangeListener("currentTrail", new PropertyChangeListener() {		
			@Override
			public void propertyChange(PropertyChangeEvent e) {
				updateTrail(presenter.currentTrail());
			}
		});
	}
	
	@Override
	public void setBackground(Color colour) {
		super.setBackground(colour);
	}

	private void updateTrail(final List<String> items) {
		DISPLAY.asyncExec(new Runnable() {
			@Override
			public void run() {
				setComponents(items);

				// Must redraw here to ensure text is updated. 
				trail.redraw();
				getParent().layout();
			}
		});
	}
	
	public void setComponents(List<String> items) {
		cleanupCrumbs();
		
		if (items.isEmpty() || items.size() == 1) {
			return;
		} else {
			configureMultipleCrumbTrail(items);			
		}

		selectLastCrumb();
	}
	
	private void configureMultipleCrumbTrail(List<String> items) {
		int lastIndex = items.size() - 1;
		String head = items.get(0);
		List<String> middle = items.subList(1, lastIndex);
		String tail = items.get(lastIndex);
		
		addHeadCrumb(head, false);
		addMiddleCrumbs(middle);
		addTailCrumb(tail);		
	}

	private void addHeadCrumb(String item, boolean isTail) {
		BreadcrumbItem head = isTail ? new AlwaysSelectedBreadcrumbItem(trail, SWT.TOGGLE) 
									: new BreadcrumbItem(trail, SWT.PUSH);

		// Hack to correct text positioning
		head.setText(TWO_SPACES + item);
		registerCrumb(head, item);
	}
	
	private void addMiddleCrumbs(List<String> items) {
		for (String item : items) {
			addMiddleCrumb(item);
		}
	}
	
	private void addMiddleCrumb(String item) {
		BreadcrumbItem crumb = new BreadcrumbItem(trail, SWT.PUSH);
		crumb.setText(item);
		registerCrumb(crumb, item);
	}

	private void addTailCrumb(String item) {
		BreadcrumbItem tail = new AlwaysSelectedBreadcrumbItem(trail, SWT.TOGGLE);
		// Hack to correct text positioning
		tail.setText(THREE_SPACES + item);
		registerCrumb(tail, item);
	}

	private void registerCrumb(BreadcrumbItem crumb, final String item) {
		crumbs.add(crumb);
		crumb.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				presenter.navigateTo(item);
			}
		});		
	}
	
	private void selectLastCrumb() {
		crumbs.get(crumbs.size() - 1).setSelection(true);
	}
	
	private void cleanupCrumbs() {
		for (BreadcrumbItem crumb : crumbs) {
			trail.removeItem(crumb);
			crumb.dispose();
		}
		crumbs.clear();
	}
		
	private class AlwaysSelectedBreadcrumbItem extends BreadcrumbItem {
        AlwaysSelectedBreadcrumbItem(Breadcrumb arg0, int arg1) {
			super(arg0, arg1);
		}
		
		@Override
		public void setSelection(boolean selected) {
			super.setSelection(true);
		}
		
		@Override
		public void addSelectionListener(SelectionListener arg0) {
			// Do nothing
		}
	}
}
