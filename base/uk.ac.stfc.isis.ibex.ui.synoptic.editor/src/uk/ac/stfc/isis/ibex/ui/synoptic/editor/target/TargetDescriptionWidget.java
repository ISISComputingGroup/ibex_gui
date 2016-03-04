
/*
 * This file is part of the ISIS IBEX application. Copyright (C) 2012-2016
 * Science & Technology Facilities Council. All rights reserved.
 *
 * This program is distributed in the hope that it will be useful. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution. EXCEPT AS
 * EXPRESSLY SET FORTH IN THE ECLIPSE PUBLIC LICENSE V1.0, THE PROGRAM AND
 * ACCOMPANYING MATERIALS ARE PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND. See the Eclipse Public License v1.0 for more
 * details.
 *
 * You should have received a copy of the Eclipse Public License v1.0 along with
 * this program; if not, you can obtain a copy from
 * https://www.eclipse.org/org/documents/epl-v10.php or
 * http://opensource.org/licenses/eclipse-1.0.php
 */

package uk.ac.stfc.isis.ibex.ui.synoptic.editor.target;

import java.util.Iterator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import uk.ac.stfc.isis.ibex.opis.desc.MacroInfo;
import uk.ac.stfc.isis.ibex.opis.desc.OpiDescription;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.ComponentDescription;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.model.IInstrumentUpdateListener;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.model.SynopticViewModel;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.model.UpdateTypes;

public class TargetDescriptionWidget extends Composite {

	private Text txtDescription;

	public TargetDescriptionWidget(Composite parent, final SynopticViewModel synopticViewModel) {
		super(parent, SWT.NONE);

		synopticViewModel.addInstrumentUpdateListener(new IInstrumentUpdateListener() {
			@Override
			public void instrumentUpdated(UpdateTypes updateType) {
				ComponentDescription component = synopticViewModel.getFirstSelectedComponent();

				if (component != null) {
					if (component.target() != null && component.target().name() != null) {
                        OpiDescription opi = synopticViewModel.getOpi(component.target().name());
						if (opi != null) {
							txtDescription.setText(generateDescription(opi));
							return;
						}
					}
				}
				txtDescription.setText("");
			}
		});

		createControls(this);
	}

	private void createControls(Composite parent) {
		setLayout(new FillLayout());

        txtDescription = new Text(parent, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.MULTI);
	}

	private String generateDescription(OpiDescription opi) {
		StringBuilder sb = new StringBuilder();
		sb.append(opi.getDescription());

		if (opi.getMacros().size() > 0) {

			sb.append(System.lineSeparator());
			sb.append(System.lineSeparator());
			sb.append("Macros:");
			sb.append(System.lineSeparator());
			
			for (Iterator<MacroInfo> iter = opi.getMacros().iterator(); iter.hasNext();) {
				MacroInfo element = iter.next();
				sb.append("* " + element.getName());
				sb.append(" - ");
				sb.append(element.getDescription());
				sb.append(System.lineSeparator());
			}
		}

		return sb.toString();
	}

}
