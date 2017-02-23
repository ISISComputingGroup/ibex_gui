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
package uk.ac.stfc.isis.ibex.ui.log.widgets;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.swt.widgets.DateTime;

import uk.ac.stfc.isis.ibex.log.message.LogMessageFields;
import uk.ac.stfc.isis.ibex.model.ModelObject;

/**
 *
 */
public class SearchControlViewModel extends ModelObject {

    private static final LogMessageFields[] FIELDS =
            { LogMessageFields.CONTENTS, LogMessageFields.CLIENT_NAME, LogMessageFields.CLIENT_HOST,
                    LogMessageFields.SEVERITY, LogMessageFields.TYPE, LogMessageFields.APPLICATION_ID };

    private static final String[] FIELD_NAMES;

    static {
        FIELD_NAMES = new String[FIELDS.length];
        for (int f = 0; f < FIELDS.length; ++f) {
            FIELD_NAMES[f] = FIELDS[f].getDisplayName();
        }
    }

    private ISearchModel searcher;
    private boolean progressIndicatorsVisible;

    private boolean toCheckboxSelected = false;
    private boolean fromCheckboxSelected = false;

    private DateTime toDate;
    private DateTime fromDate;
    private DateTime toTime;
    private DateTime fromTime;

    private Integer searchFilterItems;
    private Integer searchFilterSeverity;

    public void setSearcher(ISearchModel searcher) {
        this.searcher = searcher;
    }

    public void setToCheckboxSelected(boolean selected) {
        firePropertyChange("toCheckboxSelected", this.toCheckboxSelected, this.toCheckboxSelected = selected);
    }

    public boolean getToCheckboxSelected() {
        return toCheckboxSelected;
    }

    public void setFromCheckboxSelected(boolean selected) {
        firePropertyChange("fromCheckboxSelected", this.fromCheckboxSelected, this.fromCheckboxSelected = selected);
    }

    public boolean getFromCheckboxSelected() {
        return fromCheckboxSelected;
    }

    /**
     * Requests that the model perform a search for log messages that match the
     * request parameters.
     */
    public void search() {
        if (searcher != null) {
            int fieldIndex = 0;

            if (fieldIndex != -1) {
                final LogMessageFields field = FIELDS[fieldIndex];
                final String value = "a";

                final Calendar from = fromCheckboxSelected
                        ? new GregorianCalendar(fromDate.getYear(), fromDate.getMonth(), fromDate.getDay(),
                                fromTime.getHours(), fromTime.getMinutes(), fromTime.getSeconds())
                        : null;
                final Calendar to = toCheckboxSelected ? new GregorianCalendar(toDate.getYear(), toDate.getMonth(),
                        toDate.getDay(), toTime.getHours(), toTime.getMinutes(), toTime.getSeconds()) : null;

                runSearchJob(field, value, from, to);

            }
        }
    }

    private void runSearchJob(final LogMessageFields field, final String value, final Calendar from,
            final Calendar to) {

        final Job searchJob = new Job("Searching") {
            @Override
            protected IStatus run(IProgressMonitor monitor) {
                searcher.search(field, value, from, to);
                return Job.ASYNC_FINISH;
            }

        };

        searchJob.addJobChangeListener(new JobChangeAdapter() {

            @Override
            public void scheduled(IJobChangeEvent event) {
                setProgressIndicatorsVisible(true);
            }

            @Override
            public void done(IJobChangeEvent event) {
                setProgressIndicatorsVisible(false);
            }

        });

        searchJob.schedule();

    }

    private void setProgressIndicatorsVisible(final boolean visible) {
        firePropertyChange("progressIndicatorsVisible", this.progressIndicatorsVisible, this.progressIndicatorsVisible = visible);
    }

    public boolean getProgressIndicatorsVisible() {
        return progressIndicatorsVisible;
    }

    public void clearSearchResults() {
//        txtValue.setText("");
//
//        if (searcher != null) {
//            searcher.clearSearch();
//        }
    }

    /**
     * @return the toDate
     */
    public DateTime getToDate() {
        return toDate;
    }

    /**
     * @param toDate the toDate to set
     */
    public void setToDate(DateTime toDate) {
        firePropertyChange("toDate", this.toDate, this.toDate = toDate);
    }

    /**
     * @return the fromDate
     */
    public DateTime getFromDate() {
        return fromDate;
    }

    /**
     * @param fromDate the fromDate to set
     */
    public void setFromDate(DateTime fromDate) {
        firePropertyChange("fromDate", this.fromDate, this.fromDate = fromDate);
    }

    /**
     * @return the toTime
     */
    public DateTime getToTime() {
        return toTime;
    }

    /**
     * @param toTime the toTime to set
     */
    public void setToTime(DateTime toTime) {
        firePropertyChange("toTime", this.toTime, this.toTime = toTime);
    }

    /**
     * @return the fromTime
     */
    public DateTime getFromTime() {
        return fromTime;
    }

    /**
     * @param fromTime the fromTime to set
     */
    public void setFromTime(DateTime fromTime) {
        firePropertyChange("fromTime", this.fromTime, this.fromTime = fromTime);
    }

    /**
     * @return the searchFilterItems
     */
    public Integer getSearchFilterItems() {
        return searchFilterItems;
    }

    /**
     * @param searchFilterItems the searchFilterItems to set
     */
    public void setSearchFilterItems(Integer searchFilterItems) {
        firePropertyChange("searchFilterItems", this.searchFilterItems, this.searchFilterItems = searchFilterItems);
    }

    /**
     * @return the searchFilterSeverity
     */
    public Integer getSearchFilterSeverity() {
        return searchFilterSeverity;
    }

    /**
     * @param searchFilterSeverity the searchFilterSeverity to set
     */
    public void setSearchFilterSeverity(Integer searchFilterSeverity) {
        firePropertyChange("searchFilterSeverity", this.searchFilterSeverity,
                this.searchFilterSeverity = searchFilterSeverity);
    }
}
