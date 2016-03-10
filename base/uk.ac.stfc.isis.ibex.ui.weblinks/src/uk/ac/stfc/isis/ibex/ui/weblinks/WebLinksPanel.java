
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
import org.eclipse.ui.browser.IWebBrowser;

import uk.ac.stfc.isis.ibex.instrument.Instrument;

/**
 * Provides a UI panel showing a title and a list of links.
 */
public class WebLinksPanel extends Composite {

    private static final String URL_REPLACEMENT_STRING = "INSTNAME";

    private final Font defaultFont;

    /**
     * The UI panel showing a list of links.
     * 
     * @param parent
     *            The parent composite
     * @param style
     *            An SWT style type
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
        
        List<String> links = GetWeblinksPage.getWebLinks();

        for (String link : links) {
            linkCreator(link, parent);
        }

    }

    /**
     * Creates a new Link object based on the string passed in. The string
     * should be a HTML element, e.g. <a href="http://www.stfc.ac.uk/">STFC</a>.
     * 
     * @param linkHtml
     *            A string with the HTML for the link
     * @param parent
     *            The parent composite
     * @return
     */
    private Link linkCreator(String linkHtml, Composite parent) {
        Link link = new Link(this, SWT.NONE);
        link.setText(linkHtml);
        link.setFont(getResizedFont(defaultFont, 16, SWT.NORMAL));

        link.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                URL url = null;
                String instrumentSpecificUrl = e.text.replaceAll(URL_REPLACEMENT_STRING,
                        Instrument.getInstance().currentInstrument().name());

                try {
                    url = new URL(instrumentSpecificUrl);
                } catch (MalformedURLException ex) {
                    ex.printStackTrace();
                    return;
                }

                try {
                    // Open default external browser
                    IWebBrowser browser = PlatformUI.getWorkbench().getBrowserSupport().getExternalBrowser();
                    browser.openURL(url);
                } catch (PartInitException ex) {
                    ex.printStackTrace();
                }
            }
        });

        return link;
    }

    /**
     * Returns a resized version of a font.
     * 
     * @param font
     *            The font to start with
     * @param size
     *            The new font size
     * @param style
     *            The new font style (e.g. SWT.BOLD)
     * @return A resized and restyled font
     */
    private static Font getResizedFont(Font font, int size, int style) {
        FontData fontData = font.getFontData()[0];
        fontData.setHeight(size);
        fontData.setStyle(style);
        return new Font(Display.getCurrent(), fontData);
    }

}
