
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

package uk.ac.stfc.isis.ibex.ui.configserver.editing.blocks.filters;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.jface.viewers.ViewerFilter;

import uk.ac.stfc.isis.ibex.configserver.configuration.PV;
import uk.ac.stfc.isis.ibex.epics.observing.BaseObserver;
import uk.ac.stfc.isis.ibex.epics.observing.InitialisableObserver;
import uk.ac.stfc.isis.ibex.epics.observing.InitialiseOnSubscribeObservable;

public class FilterFromPVList extends PVFilter {
	private FilterViewerFromPVList filter = new FilterViewerFromPVList();
	
	private final InitialisableObserver<Collection<PV>> observer = new BaseObserver<Collection<PV>>() {
		@Override
		public void onValue(Collection<PV> value) {
			if (value != null) {
				updatePVList(value);
				firePropertyChange("refresh", false, true);
			}
		}

		@Override
		public void onError(Exception e) { }

		@Override
		public void onConnectionChanged(boolean isConnected) { }
	};	
	
	public FilterFromPVList(InitialiseOnSubscribeObservable<Collection<PV>> pvList) {
		pvList.subscribe(observer);
	}

	private void updatePVList(Collection<PV> pvs) {
		Collection<String> PVaddresses = new ArrayList<String>();
		
		for (PV p : pvs) {
			PVaddresses.add(p.getAddress());
		}
		filter.setAllowedPVs(PVaddresses);			
	}
	
	@Override
	public ViewerFilter getFilter() {
		return filter;
	}
	
}
