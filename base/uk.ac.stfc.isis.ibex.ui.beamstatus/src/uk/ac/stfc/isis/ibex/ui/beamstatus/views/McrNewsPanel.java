
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

import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

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
public class McrNewsPanel extends Composite {
    private static final int FONT_SIZE = 12;

    private static final String MCR_NEWS_PAGE_URL = "http://www.isis.stfc.ac.uk/files/mcr-news/mcrnews.txt";

    private static final long TEXT_REFRESH_PERIOD_MS = 30000; // milliseconds

    private Text txtTheMcrNews;

    /**
     * Constructor for the MCR news panel.
     * 
     * @param parent
     *            Parent composite
     * @param style
     *            SWT Style
     */
    public McrNewsPanel(Composite parent, int style) {
        super(parent, style);
        setLayout(new GridLayout(1, false));

        txtTheMcrNews = new Text(this, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
        Color backgroundColor = txtTheMcrNews.getBackground();
        txtTheMcrNews.setEditable(false);
        txtTheMcrNews.setBackground(backgroundColor);
        txtTheMcrNews.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        txtTheMcrNews.setText("The MCR news should be shown here.");

        final String currentFontName = txtTheMcrNews.getFont().getFontData()[0].getName();
        txtTheMcrNews.setFont(SWTResourceManager.getFont(currentFontName, FONT_SIZE, SWT.NORMAL));

        updateNews().run();
        startTimer();
    }

    /**
     * Sets the MCR news text.
     * 
     * @param text
     *            A String containing the MCR news.
     */
    public void setText(String text) {
        int topIndex = txtTheMcrNews.getTopIndex();
        Point selection = txtTheMcrNews.getSelection();

        txtTheMcrNews.setText(text);

        txtTheMcrNews.setSelection(selection);
        txtTheMcrNews.setTopIndex(topIndex);
    }

    private void startTimer() {
        Timer timer = new Timer();
        long delay = TEXT_REFRESH_PERIOD_MS;
        timer.scheduleAtFixedRate(updateNews(), delay, TEXT_REFRESH_PERIOD_MS);
    }

    private TimerTask updateNews() {
        return new TimerTask() {
            @Override
            public void run() {
                Display.getDefault().asyncExec(new Runnable() {
                    @Override
                    public void run() {
                        setText(getMCRNewsText());
                    }
                });
            }
        };
    }

    /**
     * Gets the raw text from the MCR News Page.
     * 
     * @return A string containing the MCR news.
     */
    private static String getMCRNewsText() {
        String content = null;
        URLConnection connection = null;
        try {
            connection = new URL(MCR_NEWS_PAGE_URL).openConnection();
            InputStreamReader connectionWithEncoding = new InputStreamReader(connection.getInputStream(), "UTF-8");
            Scanner scanner = new Scanner(connectionWithEncoding);
            scanner.useDelimiter("\\Z");
            content = scanner.next();
            scanner.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return content;
    }
}