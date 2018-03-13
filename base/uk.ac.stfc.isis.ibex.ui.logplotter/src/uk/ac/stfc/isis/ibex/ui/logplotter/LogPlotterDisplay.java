
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

package uk.ac.stfc.isis.ibex.ui.logplotter;

import org.csstudio.trends.databrowser2.Messages;
import org.csstudio.trends.databrowser2.editor.DataBrowserEditor;
import org.csstudio.trends.databrowser2.model.Model;
import org.csstudio.trends.databrowser2.model.PVItem;
import org.csstudio.trends.databrowser2.preferences.Preferences;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.osgi.util.NLS;

import uk.ac.stfc.isis.ibex.ui.UI;

public class LogPlotterDisplay {
		
	public void displayPVHistory(String pvAddress) {	
				
		UI.getDefault().switchPerspective(Perspective.ID);
		
	    // Create new editor
	    final DataBrowserEditor editor = DataBrowserEditor.createInstance();
	    if (editor == null) {
	        return;
	    }
	    
	    // Add received items
	    final Model model = editor.getModel();
	    final double period = Preferences.getScanPeriod();
	    try {
			final PVItem item = new PVItemWithUnits(pvAddress, period);
			item.useDefaultArchiveDataSources();
			// Add item to new axes
			item.setAxis(model.addAxis(item.getDisplayName()));
			model.addItem(item);
	    } catch (Exception ex) {
	        MessageDialog.openError(editor.getSite().getShell(),
	                Messages.Error,
	                NLS.bind(Messages.ErrorFmt, ex.getMessage()));
	    }
	}
}
