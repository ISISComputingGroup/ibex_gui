
/*
 * This file is part of the ISIS IBEX application.
 * Copyright (C) 2012-2016 Science & Technology Facilities Council.
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

/**
 * 
 */
package uk.ac.stfc.isis.ibex.ui.weblinks;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

/**
 * 
 */
public class WebLinksPanel extends Composite {

    private final Font defaultFont;

    /**
     * @param parent
     * @param style
     */
    public WebLinksPanel(Composite parent, int style) {
        super(parent, style);

        GridLayout gridLayout = new GridLayout(1, false);
        gridLayout.marginWidth = 15;
        gridLayout.verticalSpacing = 15;
        setLayout(gridLayout);
        
        Label titleLabel = new Label(this, SWT.NONE);
        defaultFont = titleLabel.getFont();
        
        titleLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        titleLabel.setText("Web Links");
        titleLabel.setFont(getResizedFont(defaultFont, 24, SWT.BOLD));
        
        GetWeblinksPage webLinks = new GetWeblinksPage();
        List<String> links = webLinks.getWebLinks();

        for (String link : links) {
            linkCreator(link, parent);
        }

    }

    public Link linkCreator(String message, Composite parent) {
        Link link = new Link(this, SWT.NONE);
        link.setText(message);
        link.setFont(getResizedFont(defaultFont, 16, SWT.NORMAL));

        link.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                System.out.println("You have selected: " + e.text);
                try {
                    // Open default external browser
                    PlatformUI.getWorkbench().getBrowserSupport().getExternalBrowser().openURL(new URL(e.text));
                } catch (PartInitException ex) {
                    // TODO Auto-generated catch block
                    ex.printStackTrace();
                } catch (MalformedURLException ex) {
                    // TODO Auto-generated catch block
                    ex.printStackTrace();
                }
            }
        });

        return link;
    }

    private Font getResizedFont(Font defaultLabelFont, int size, int style) {
        FontData fontData = defaultLabelFont.getFontData()[0];
        fontData.setHeight(size);
        fontData.setStyle(style);
        return new Font(Display.getCurrent(), fontData);
    }

}
