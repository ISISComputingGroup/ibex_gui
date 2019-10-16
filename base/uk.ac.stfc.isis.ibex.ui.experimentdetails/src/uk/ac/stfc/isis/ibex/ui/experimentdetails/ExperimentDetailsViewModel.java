
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

package uk.ac.stfc.isis.ibex.ui.experimentdetails;

import uk.ac.stfc.isis.ibex.experimentdetails.AbstractExperimentDetailsModel;
import uk.ac.stfc.isis.ibex.experimentdetails.ExperimentDetails;
import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.ui.widgets.observable.StringWritableObservableAdapter;

public class ExperimentDetailsViewModel extends ModelObject {

    private static ExperimentDetailsViewModel instance;
	
	public final AbstractExperimentDetailsModel model = ExperimentDetails.getInstance().model();
	
	public final StringWritableObservableAdapter rbNumber = new StringWritableObservableAdapter(model.rbNumberSetter(), model.rbNumber());
	
    private ExperimentDetailsViewModel() {
        instance = this;
        
        model.addPropertyChangeListener("userDetails", event -> {
            firePropertyChange("userDetailsWarningVisible", !model.isUserDetailsEmpty(),
                    model.isUserDetailsEmpty());
        });
    }

    // TODO: Don't use a singleton here split this into three separate view
    // models instead.
    public static ExperimentDetailsViewModel getInstance() {
        if (instance == null) {
            instance = new ExperimentDetailsViewModel();
        }
        return instance;
	}
    
    public boolean getUserDetailsWarningVisible() {
        return model.isUserDetailsEmpty();
    }
	
}
