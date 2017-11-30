
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

import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.PerspectivesProvider;
import uk.ac.stfc.isis.ibex.ui.UI;

public class LogPlotterDisplay {
	
	public void displayPVHistory(String pvAddress) {	
				
		String targetElementId = "uk.ac.stfc.isis.ibex.client.e4.product.perspective.logplotter";
		
		PerspectivesProvider provider = PerspectivesProvider.getInstance();
		MPerspective element = provider.getPerspective(targetElementId);
	    if (element != null) {
	      	provider.getPartService().switchPerspective(element);
	    }
		else {
			System.out.println("Unable to find perspective part with ID: " + targetElementId);
		}
	    
		try {
			IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			IViewPart view = page.showView(LogPlotterView.ID, null, IWorkbenchPage.VIEW_ACTIVATE);

//			LogPlotterView logplotterView = (LogPlotterView) view;

		} catch (PartInitException e) {
			e.printStackTrace();
		}
	}
}
