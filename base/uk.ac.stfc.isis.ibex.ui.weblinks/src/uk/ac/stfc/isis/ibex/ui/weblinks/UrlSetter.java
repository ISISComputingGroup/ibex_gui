
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

package uk.ac.stfc.isis.ibex.ui.weblinks;

import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import uk.ac.stfc.isis.ibex.instrument.Instrument;
import uk.ac.stfc.isis.ibex.instrument.InstrumentInfo;
import uk.ac.stfc.isis.ibex.instrument.InstrumentInfoReceiver;

public class UrlSetter implements InstrumentInfoReceiver {
	
	private static final String SECI_LINKS_URL = "http://dataweb.isis.rl.ac.uk/SeciLinks/default.htm?Instrument=";
	
	public static String getUrl() {
		String instname = Instrument.getInstance().currentInstrument().name();
		return SECI_LINKS_URL + instname;
	}

	@Override
	public void setInstrument(InstrumentInfo instrument) {
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		
		if (page == null) {
			return;
		}
		
		IViewPart view = page.findView(WebLinksView.ID);
		
		if (view == null) {
			return;
		}
		
		if (view instanceof WebLinksView) {
			WebLinksView webview = (WebLinksView) view;
			webview.setUrl(getUrl());
		}

	}

}
