
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

import java.util.ArrayList;

import org.csstudio.trends.databrowser2.Messages;
import org.csstudio.trends.databrowser2.editor.DataBrowserEditor;
import org.csstudio.trends.databrowser2.model.AxisConfig;
import org.csstudio.trends.databrowser2.model.Model;
import org.csstudio.trends.databrowser2.model.PVItem;
import org.csstudio.trends.databrowser2.preferences.Preferences;
import org.csstudio.ui.util.EmptyEditorInput;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.osgi.util.NLS;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.PlatformUI;

import uk.ac.stfc.isis.ibex.ui.UI;
import uk.ac.stfc.isis.ibex.ui.blocks.presentation.PVHistoryPresenter;

/**
 * The class that is responsible for displaying the log plotter.
 */
public class LogPlotterHistoryPresenter implements PVHistoryPresenter {
	
	/**
	 * @return An arraylist of all the current databrowsers.
	 */
	private ArrayList<DataBrowserEditor> getCurrentDataBrowsers() {
		ArrayList<DataBrowserEditor> dataBrowserEditors = new ArrayList<>();
		
		IEditorReference[] editorRefs = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getEditorReferences();
		
		for (IEditorReference editorRef : editorRefs) {
			IEditorPart editor = editorRef.getEditor(false);
			if (editor instanceof DataBrowserEditor) {
				dataBrowserEditors.add((DataBrowserEditor) editor);
			}
		}
		return dataBrowserEditors;
	}
	
	@Override
	public ArrayList<String> getCurrentDisplays() {
		ArrayList<String> editorNames = new ArrayList<>();
		for (DataBrowserEditor editor : getCurrentDataBrowsers()) {
			editorNames.add(editor.getTitle());
		}
		return editorNames;
	}
	
	private void addPVToEditor(String pvAddress, final String displayName, DataBrowserEditor editor) {
		if (editor == null) {
	        return;
	    }
		Model model = editor.getModel();
		// Add received items
	    final double period = Preferences.getScanPeriod();
	    try {
			final PVItem item = new PVItemWithUnits(displayName, pvAddress, period);
			item.useDefaultArchiveDataSources();
			// Add item to new axes
			AxisConfig axis = model.addAxis(item.getDisplayName());
			axis.setAutoScale(false);
			item.setAxis(axis);
			model.addItem(item);
	    } catch (Exception ex) {
	        MessageDialog.openError(editor.getSite().getShell(),
	                Messages.Error,
	                NLS.bind(Messages.ErrorFmt, ex.getMessage()));
	    }
	}
	
	@Override
	public void newDisplay(String pvAddress, final String displayName) {	
		UI.getDefault().switchPerspective(Perspective.ID);
		
	    // Create new editor
	    final DataBrowserEditor editor = DataBrowserEditor.createInstance(new EmptyEditorInput() {
	    	@Override
	    	public String getName() {
	    		return displayName;
	    	}
	    });
	    
	    addPVToEditor(pvAddress, displayName, editor);
	}

	@Override
	public void addToDisplay(String pvAddress, String display, String presenterName) {
		UI.getDefault().switchPerspective(Perspective.ID);
		
		DataBrowserEditor editor = null;
		for (DataBrowserEditor e : getCurrentDataBrowsers()) {
			if (e.getTitle().equals(presenterName)) {
				editor = e;
			}
		}
		
		if (editor == null) {
			// Can't find the editor to add to, make a new one
			newDisplay(pvAddress, display);
		} else {	
			addPVToEditor(pvAddress, display, editor);
		}
	}

}
