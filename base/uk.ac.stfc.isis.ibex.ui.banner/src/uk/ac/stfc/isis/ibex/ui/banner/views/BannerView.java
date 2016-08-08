
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

package uk.ac.stfc.isis.ibex.ui.banner.views;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.ISizeProvider;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.SWTResourceManager;

import uk.ac.stfc.isis.ibex.banner.Banner;
import uk.ac.stfc.isis.ibex.configserver.configuration.BannerItem;
import uk.ac.stfc.isis.ibex.epics.observing.BaseObserver;
import uk.ac.stfc.isis.ibex.instrument.baton.Baton;
import uk.ac.stfc.isis.ibex.ui.banner.controls.ControlModel;
import uk.ac.stfc.isis.ibex.ui.banner.indicators.IndicatorModel;
import uk.ac.stfc.isis.ibex.ui.banner.models.BannerItemModel;
import uk.ac.stfc.isis.ibex.ui.banner.models.BatonUserModel;
import uk.ac.stfc.isis.ibex.ui.banner.models.InMotionModel;
import uk.ac.stfc.isis.ibex.ui.banner.models.MotionControlModel;
import uk.ac.stfc.isis.ibex.ui.banner.widgets.Control;
import uk.ac.stfc.isis.ibex.ui.banner.widgets.Indicator;

/**
 * View of the spangle banner containing various instrument status messages.
 */
@SuppressWarnings("checkstyle:magicnumber")
public class BannerView extends ViewPart implements ISizeProvider {

    /**
     * Standard constructor.
     */
    public BannerView() {
    }

    private static final Font ALARM_FONT = SWTResourceManager.getFont("Arial", 10, SWT.BOLD);

    public static final String ID = "uk.ac.stfc.isis.ibex.ui.banner.views.BannerView"; //$NON-NLS-1$
    public static final int FIXED_HEIGHT = 35;

    private final Banner banner = Banner.getInstance();

    private final IndicatorModel batonUserModel = new BatonUserModel(Baton.getInstance().baton());
    private final IndicatorModel inMotionModel = new InMotionModel(banner.observables());
    private final ControlModel motionModel = new MotionControlModel(banner.observables());

    private Composite bannerItemPanel;
    private GridLayout glBannerItemPanel;

    private Indicator batonUser;
    private Indicator inMotion;
    private Control motionControl;
    private Label spacer;


    @Override
    public void createPartControl(Composite parent) {
        GridLayout glParent = new GridLayout(5, false);
        glParent.marginRight = 2;
        glParent.horizontalSpacing = 8;
        glParent.verticalSpacing = 0;
        glParent.marginWidth = 0;
        parent.setLayout(glParent);

        spacer = new Label(parent, SWT.NONE);
        spacer.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));

        bannerItemPanel = new Composite(parent, SWT.RIGHT_TO_LEFT);
        glBannerItemPanel = new GridLayout(1, false);
        glBannerItemPanel.marginHeight = 3;
        glBannerItemPanel.horizontalSpacing = 15;
        bannerItemPanel.setLayout(glBannerItemPanel);
        GridData gdBannerItemPanel = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
        bannerItemPanel.setLayoutData(gdBannerItemPanel);

        banner.observables().bannerItems.addObserver(modelAdapter);

        batonUser = new Indicator(parent, SWT.NONE, batonUserModel, ALARM_FONT);
        GridData gdBatonUser = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
        gdBatonUser.widthHint = 210;
        batonUser.setLayoutData(gdBatonUser);

        inMotion = new Indicator(parent, SWT.NONE, inMotionModel, ALARM_FONT);
        GridData gdInMotion = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
        gdInMotion.widthHint = 170;
        inMotion.setLayoutData(gdInMotion);

        motionControl = new Control(parent, SWT.NONE, motionModel, ALARM_FONT);
        GridData gdMotionControl = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
        gdMotionControl.widthHint = 100;
        motionControl.setLayoutData(gdMotionControl);
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    @Override
    public int getSizeFlags(boolean width) {
        return SWT.MIN | SWT.MAX;
    }

    @Override
    public int computePreferredSize(boolean width, int availableParallel, int availablePerpendicular,
            int preferredResult) {
        return width ? 0 : FIXED_HEIGHT;
    }

    @Override
    public void setFocus() {
    }

    /**
     * Converts a collection of banner item objects (the banner description)
     * into models readable by indicator widgets in the UI.
     * 
     * @param items the banner items
     * @return the indicator models
     */
    private Collection<IndicatorModel> convertBannerItems(Collection<BannerItem> items) {
        Collection<IndicatorModel> convertedItems = new ArrayList<IndicatorModel>();
        if (!(items == null)) {
            for (BannerItem item : items) {
                convertedItems.add(new BannerItemModel(item));
            }
        }
        return convertedItems;
    }

    /**
     * Adds an indicator widget for each banner item.
     * 
     * @param models the banner item indicator models observed by the widget
     */
    private void setBanner(final Collection<IndicatorModel> models) {
        disposeBanner();
        Display.getDefault().asyncExec(new Runnable() {
            @Override
            public void run() {
                GridData gdBannerItem = new GridData(SWT.CENTER, SWT.FILL, false, true, 1, 1);
                gdBannerItem.widthHint = 250;
                GridData gdSep = new GridData(SWT.CENTER, SWT.TOP, false, false, 1, 1);
                gdSep.heightHint = 20;
                for (IndicatorModel model : models) {
                    glBannerItemPanel.numColumns = 2 * models.size();

                    Label sep = new Label(bannerItemPanel, SWT.SEPARATOR | SWT.VERTICAL);
                    sep.setLayoutData(gdSep);

                    Indicator bannerItem = new Indicator(bannerItemPanel, SWT.LEFT_TO_RIGHT, model, ALARM_FONT);
                    bannerItem.setLayoutData(gdBannerItem);
                }
                bannerItemPanel.layout(true);
            }
        });
    }

    /**
     * Removes all indicators for instrument-specific properties from the
     * spangle banner.
     */
    private void disposeBanner() {
        Display.getDefault().asyncExec(new Runnable() {
            @Override
            public void run() {
                for (org.eclipse.swt.widgets.Control item : bannerItemPanel.getChildren()) {
                    item.dispose();
                }
            }
        });
    }

    /**
     * Observes the banner model and forwards changes to the UI.
     */
    private final BaseObserver<Collection<BannerItem>> modelAdapter = new BaseObserver<Collection<BannerItem>>() {
        @Override
        public void onValue(Collection<BannerItem> value) {
            setBanner(convertBannerItems(value));
        }

        @Override
        public void onError(Exception e) {
        }

        @Override
        public void onConnectionStatus(boolean isConnected) {
            if (!isConnected) {
                disposeBanner();
            }
        }
    };

}
