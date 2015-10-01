
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

package uk.ac.stfc.isis.ibex.users.ui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import uk.ac.stfc.isis.ibex.users.Director;
import uk.ac.stfc.isis.ibex.users.Users;

public class SwitchUserDialog extends Dialog {
	
	private final Director director = Users.getInstance().director();
	private final UserSwitcherModel model = new UserSwitcherModel(director);
	
	public SwitchUserDialog(Shell parent) {
		super(parent);
	}
          
	@Override
    protected void configureShell(Shell newShell) { 
        super.configureShell(newShell); 
        newShell.setText("Switch User"); 
        newShell.setSize(250, 170); 
    } 

	@Override
	protected Control createDialogArea(Composite parent) { 
	    Composite composite = (Composite) super.createDialogArea(parent); 
	    UserSwitcher switcher = new UserSwitcher(composite, SWT.NONE);
	    switcher.setModel(model);
	    
	    updateButtons(false);
	    model.addPropertyChangeListener("isValid", new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent e) {
				updateButtons(model.getIsValid());
			}
		});
	    
	    return composite;
	}
	
    @Override
    protected void createButtonsForButtonBar(Composite parent) { 
        super.createButtonsForButtonBar(parent); 
    } 
	    
    @Override
    protected void okPressed() {
    	super.okPressed();	
    	director.switchUser(model.selectedUserName());
    }
    
    private void updateButtons(boolean isValid) { 
        Button okButton = getButton(IDialogConstants.OK_ID); 
        if (okButton != null) { 
            okButton.setEnabled(isValid); 
        }
    }
}
