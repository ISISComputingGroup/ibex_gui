
/*
 * This file is part of the ISIS IBEX application. Copyright (C) 2012-2015
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

package uk.ac.stfc.isis.ibex.ui.beamstatus.views;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

/**
 * This page displays the MCR news text. The setText method is used to refresh
 * the MCR news content.
 */
public class McrNewsPanel {
    private static final int FONT_SIZE = 10;

    private static final String MCR_NEWS_PAGE_URL = "https://www.isis.stfc.ac.uk/gallery/beam-status/mcrnews.txt";
    private static final String GET_NEWS_FAILED_MESSAGE =
            "Unable to load MCR news. \nTarget URL: " + MCR_NEWS_PAGE_URL + "\nError: ";

    private static final long TEXT_REFRESH_PERIOD_MS = 30000; // milliseconds
    
    private static final ScheduledExecutorService MCR_NEWS_UPDATE_THREAD = 
    		Executors.newSingleThreadScheduledExecutor(runnable -> new Thread(runnable, "MCR news update worker"));

    private Text newsText;

    /**
     * Creates the MCR News view.
     * 
     * @param parent The parent container obtained via dependency injection
     */
    @PostConstruct
    public void createPartControl(Composite parent) {
        parent.setLayout(new GridLayout(1, false));
        parent.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        
        newsText = new Text(parent, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
        Color backgroundColor = newsText.getBackground();
        newsText.setEditable(false);
        newsText.setBackground(backgroundColor);
        newsText.setText("The MCR news will load shortly. If this message persists, please contact support.");
        newsText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        final String currentFontName = newsText.getFont().getFontData()[0].getName();
        newsText.setFont(SWTResourceManager.getFont(currentFontName, FONT_SIZE, SWT.NORMAL));

        startTimer();
    }

    /**
     * Sets the MCR news text.
     * 
     * @param text A String containing the MCR news.
     */
    public void setText(String text) {
        int topIndex = newsText.getTopIndex();
        Point selection = newsText.getSelection();

        newsText.setText(text);

        newsText.setSelection(selection);
        newsText.setTopIndex(topIndex);
    }

    private void startTimer() {
        MCR_NEWS_UPDATE_THREAD.scheduleWithFixedDelay(this::updateNews, 0, TEXT_REFRESH_PERIOD_MS, TimeUnit.MILLISECONDS);
    }

    private void updateNews() {
    	final String mcrNews = getMCRNewsText();
        Display.getDefault().asyncExec(() -> setText(mcrNews));
    }

    /**
     * Gets the raw text from the MCR News Page.
     * 
     * @return A string containing the MCR news.
     */
    private static String getMCRNewsText() {
        String content = "";
        URLConnection connection = null;
        try {
            connection = new URI(MCR_NEWS_PAGE_URL).toURL().openConnection();
            InputStreamReader connectionWithEncoding = new InputStreamReader(connection.getInputStream(), "UTF-8");
            Scanner scanner = new Scanner(connectionWithEncoding);
            scanner.useDelimiter("\\Z");
            content = scanner.next();
            scanner.close();
        } catch (UnknownHostException ex) {
            content += getNewsFailedMessage("Unknown host", ex);
        } catch (MalformedURLException | URISyntaxException ex) {
            content += getNewsFailedMessage("URL not valid", ex);
        } catch (IOException ex) {
            content += getNewsFailedMessage("Unable to read file", ex);
        }
        return content;
    }

    private static String getNewsFailedMessage(String localMessage, Exception ex) {
        return GET_NEWS_FAILED_MESSAGE + localMessage + ", " + ex.getMessage();
    }
}