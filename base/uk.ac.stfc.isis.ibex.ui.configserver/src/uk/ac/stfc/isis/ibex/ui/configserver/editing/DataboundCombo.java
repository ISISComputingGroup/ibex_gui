
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

package uk.ac.stfc.isis.ibex.ui.configserver.editing;

import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.jface.databinding.viewers.IViewerObservableValue;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.databinding.viewers.ObservableMapLabelProvider;
import org.eclipse.jface.databinding.viewers.ViewerProperties;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

public class DataboundCombo<T> extends ComboViewer {
	
	private final IViewerObservableValue selected;
	
	public DataboundCombo(Composite parent, int style, String observedProperty) {
		super(parent, style | SWT.DROP_DOWN);
		
		ObservableListContentProvider contentProvider = new ObservableListContentProvider();
		setContentProvider(contentProvider);
		setLabelProvider(
				new ObservableMapLabelProvider(BeansObservables.observeMaps(contentProvider.getKnownElements(), new String[] { observedProperty } )));	
		
		selected = ViewerProperties.singleSelection().observe(this);
	}

	public IViewerObservableValue selected() {
		return selected;
	}
}
