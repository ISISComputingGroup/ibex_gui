
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

import javax.annotation.PostConstruct;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
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
import uk.ac.stfc.isis.ibex.ui.banner.models.ManagerModeBannerModel;
import uk.ac.stfc.isis.ibex.ui.banner.models.MotionControlModel;
import uk.ac.stfc.isis.ibex.ui.banner.widgets.Control;
import uk.ac.stfc.isis.ibex.ui.banner.widgets.Indicator;

/**
 * View of the spangle banner containing various instrument status messages.
 */
@SuppressWarnings("checkstyle:magicnumber")
public class BannerView {

    private static final Font ALARM_FONT = SWTResourceManager.getFont("Arial", 10, SWT.BOLD);

    /**
     * View ID.
     */
    public static final String ID = "uk.ac.stfc.isis.ibex.ui.banner.views.BannerView";
    private static final int ITEM_WIDTH = 250;

    private final Banner banner = Banner.getInstance();

    private final IndicatorModel managerModeModel = new ManagerModeBannerModel();
    private final IndicatorModel batonUserModel = new BatonUserModel(Baton.getInstance().baton());
    private final IndicatorModel inMotionModel = new InMotionModel(banner.observables());
    private final ControlModel motionModel = new MotionControlModel(banner.observables());

    private Composite bannerItemPanel;
    private GridLayout glBannerItemPanel;

    private Indicator managerMode;
    private Indicator batonUser;
    private Indicator inMotion;
    private Control motionControl;

    @PostConstruct
    public void draw(Composite parent) {
        parent.setBackground(new Color(Display.getCurrent(), 255, 0, 0));
//        
        GridLayout layout = new GridLayout(10, false);
        layout.verticalSpacing = 0;
        layout.marginTop = 0;
        layout.marginBottom = 0;
        layout.marginHeight = 0;
        parent.setLayout(layout);

        Composite myComposite = new Composite(parent, SWT.NONE);

        myComposite.setLayout(new GridLayout(20, false));
        myComposite.setLayoutData(new GridData(SWT.END, SWT.FILL, false, true));
        myComposite.setBackground(new Color(Display.getCurrent(), 0, 255, 0));

        parent.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));

//        GridLayout layout = new GridLayout(1, false);
//        layout.verticalSpacing = 0;
//        layout.marginHeight = 0;
//        layout.marginBottom = 0;
//        layout.marginTop = 0;
//        myComposite.setLayout(layout);
//        myComposite.setLayoutData(new GridData(SWT.END, SWT.FILL, true, false));

        bannerItemPanel = new Composite(myComposite, SWT.END);
        bannerItemPanel.setLayout(glBannerItemPanel = new GridLayout(5, false));
        bannerItemPanel.setLayoutData(new GridData(SWT.END, SWT.BEGINNING, false, false, 1, 1));
        glBannerItemPanel.marginHeight = 0;
        glBannerItemPanel.horizontalSpacing = 15;
        bannerItemPanel.setLayout(glBannerItemPanel);

        banner.observables().bannerDescription.addObserver(modelAdapter);

        managerMode = new Indicator(bannerItemPanel, SWT.NONE, managerModeModel, ALARM_FONT);
        GridData gdManagerMode = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
        gdManagerMode.widthHint = 210;
        managerMode.setLayoutData(gdManagerMode);

        batonUser = new Indicator(bannerItemPanel, SWT.NONE, batonUserModel, ALARM_FONT);
        GridData gdBatonUser = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
        gdBatonUser.widthHint = 210;
        batonUser.setLayoutData(gdBatonUser);

        inMotion = new Indicator(bannerItemPanel, SWT.NONE, inMotionModel, ALARM_FONT);
        GridData gdInMotion = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
        gdInMotion.widthHint = 170;
        inMotion.setLayoutData(gdInMotion);

        motionControl = new Control(bannerItemPanel, SWT.NONE, motionModel, ALARM_FONT);
        GridData gdMotionControl = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
        gdMotionControl.widthHint = 100;
        motionControl.setLayoutData(gdMotionControl);
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
                GridData gdBannerItem = new GridData(SWT.CENTER, SWT.FILL, false, false, 1, 1);
                gdBannerItem.widthHint = ITEM_WIDTH;

                for (IndicatorModel model : models) {
                    glBannerItemPanel.numColumns = 2 * models.size();

                    addSeparator(bannerItemPanel);

                    Indicator bannerItem = new Indicator(bannerItemPanel, SWT.LEFT_TO_RIGHT, model, ALARM_FONT);
                    bannerItem.setLayoutData(gdBannerItem);
                }
                bannerItemPanel.layout(true);
            }
        });
    }

    private void addSeparator(Composite parent) {
        Label sep = new Label(parent, SWT.SEPARATOR | SWT.VERTICAL);
        sep.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, false, false, 1, 1));
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
        public void onConnectionStatus(boolean isConnected) {
            if (!isConnected) {
                disposeBanner();
            }
        }
    };

}
