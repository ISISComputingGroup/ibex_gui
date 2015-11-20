
/**
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

package uk.ac.stfc.isis.ibex.ui.scripting;

import org.eclipse.core.runtime.IProgressMonitor;
import org.python.pydev.ui.pythonpathconf.IInterpreterInfoBuilder;
import org.python.pydev.ui.pythonpathconf.InterpreterInfo;

/**
 * DO NOT DELETE THIS CLASS!
 * 
 * PyDev requires something to extend the pydev.pydev_interpreter_info_builder
 * extension point otherwise it gets a null pointer exception on the first time
 * it is run after building. This class takes care of that - it doesn't actually
 * do anything but it keeps PyDev happy.
 *
 */
public class InterpreterInfoBuilderParticipant implements IInterpreterInfoBuilder {
    @Override
    public BuilderResult syncInfoToPythonPath(IProgressMonitor arg0, InterpreterInfo arg1) {
        // Do nothing
        return null;
    }

}
