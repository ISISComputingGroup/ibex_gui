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
import java.util.Date;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.jobs.Job;

import uk.ac.stfc.isis.ibex.log.message.LogMessageFields;
import uk.ac.stfc.isis.ibex.model.ModelObject;

/**
 * The Class SearchControlViewModel.
 */
public class SearchControlViewModel extends ModelObject {

    private ISearchModel searcher;
    private boolean progressIndicatorsVisible;

    private boolean toCheckboxSelected = false;
    private boolean fromCheckboxSelected = false;

    private Date toDate;
    private Date fromDate;
    private Date toTime;
    private Date fromTime;

    private Integer searchFilterItems;
    private Integer searchFilterSeverity;

    private String searchText = "";
    private LogMessageFields field;

    public SearchControlViewModel() {
        final Date now = new Date();
        toDate = now;
        toTime = now;
        fromDate = now;
        fromTime = now;
    }

    /**
     * Requests that the model perform a search for log messages that match the
     * request parameters.
     */
    public void search() {
        if (searcher == null) {
            return;
        }

        Calendar from = fromCheckboxSelected ? getCalendar(fromDate, fromTime) : null;
        Calendar to = toCheckboxSelected ? getCalendar(toDate, toTime) : null;

        runSearchJob(field, searchText, from, to);

    }

    private Calendar getCalendar(Date date, Date time) {
        Calendar resultCalendar = Calendar.getInstance();
        Calendar dateCalendar = Calendar.getInstance();
        Calendar timeCalendar = Calendar.getInstance();

        dateCalendar.setTime(date);
        timeCalendar.setTime(time);

        resultCalendar.set(dateCalendar.get(Calendar.YEAR), dateCalendar.get(Calendar.MONTH),
                dateCalendar.get(Calendar.DAY_OF_MONTH), timeCalendar.get(Calendar.HOUR),
                timeCalendar.get(Calendar.MINUTE), timeCalendar.get(Calendar.SECOND));

        return resultCalendar;
    }

    private void runSearchJob(final LogMessageFields field, final String value, final Calendar from,
            final Calendar to) {

        final Job searchJob = new Job("Searching") {
            @Override
            protected IStatus run(IProgressMonitor monitor) {
                setProgressIndicatorsVisible(true);
                searcher.search(field, value, from, to);
                setProgressIndicatorsVisible(false);
                return Job.ASYNC_FINISH;
            }

        };

        searchJob.schedule();

    }

    private void setProgressIndicatorsVisible(boolean visible) {
        firePropertyChange("progressIndicatorsVisible", this.progressIndicatorsVisible, this.progressIndicatorsVisible = visible);
    }

    /**
     * Gets the progress indicators visible.
     *
     * @return the progress indicators visible
     */
    public boolean getProgressIndicatorsVisible() {
        return progressIndicatorsVisible;
    }

    /**
     * Clear search results.
     */
    public void clearSearchResults() {
        setProgressIndicatorsVisible(false);
        setSearchText("");

        if (searcher != null) {
            searcher.clearSearch();
        }
    }

    /**
     * Sets the searcher.
     *
     * @param searcher
     *            the new searcher
     */
    public void setSearcher(ISearchModel searcher) {
        this.searcher = searcher;
    }

    /**
     * Sets the to checkbox selected.
     *
     * @param selected
     *            the new to checkbox selected
     */
    public void setToCheckboxSelected(boolean selected) {
        firePropertyChange("toCheckboxSelected", this.toCheckboxSelected, this.toCheckboxSelected = selected);
    }

    /**
     * Gets the to checkbox selected.
     *
     * @return the to checkbox selected
     */
    public boolean getToCheckboxSelected() {
        return toCheckboxSelected;
    }

    /**
     * Sets the from checkbox selected.
     *
     * @param selected
     *            the new from checkbox selected
     */
    public void setFromCheckboxSelected(boolean selected) {
        firePropertyChange("fromCheckboxSelected", this.fromCheckboxSelected, this.fromCheckboxSelected = selected);
    }

    /**
     * Gets the from checkbox selected.
     *
     * @return the from checkbox selected
     */
    public boolean getFromCheckboxSelected() {
        return fromCheckboxSelected;
    }

    /**
     * Gets the to date.
     *
     * @return the toDate
     */
    public Date getToDate() {
        return toDate;
    }

    /**
     * Sets the to date.
     *
     * @param toDate
     *            the toDate to set
     */
    public void setToDate(Date toDate) {
        firePropertyChange("toDate", this.toDate, this.toDate = toDate);
    }

    /**
     * Gets the from date.
     *
     * @return the fromDate
     */
    public Date getFromDate() {
        return fromDate;
    }

    /**
     * Sets the from date.
     *
     * @param fromDate
     *            the fromDate to set
     */
    public void setFromDate(Date fromDate) {
        firePropertyChange("fromDate", this.fromDate, this.fromDate = fromDate);
    }

    /**
     * Gets the to time.
     *
     * @return the toTime
     */
    public Date getToTime() {
        return toTime;
    }

    /**
     * Sets the to time.
     *
     * @param toTime
     *            the toTime to set
     */
    public void setToTime(Date toTime) {
        firePropertyChange("toTime", this.toTime, this.toTime = toTime);
    }

    /**
     * Gets the from time.
     *
     * @return the fromTime
     */
    public Date getFromTime() {
        return fromTime;
    }

    /**
     * Sets the from time.
     *
     * @param fromTime
     *            the fromTime to set
     */
    public void setFromTime(Date fromTime) {
        firePropertyChange("fromTime", this.fromTime, this.fromTime = fromTime);
    }

    /**
     * Gets the search filter items.
     *
     * @return the searchFilterItems
     */
    public Integer getSearchFilterItems() {
        return searchFilterItems;
    }

    /**
     * Sets the search filter items.
     *
     * @param searchFilterItems
     *            the searchFilterItems to set
     */
    public void setSearchFilterItems(Integer searchFilterItems) {
        firePropertyChange("searchFilterItems", this.searchFilterItems, this.searchFilterItems = searchFilterItems);
    }

    /**
     * Gets the search filter severity.
     *
     * @return the searchFilterSeverity
     */
    public Integer getSearchFilterSeverity() {
        return searchFilterSeverity;
    }

    /**
     * Sets the search filter severity.
     *
     * @param searchFilterSeverity
     *            the searchFilterSeverity to set
     */
    public void setSearchFilterSeverity(Integer searchFilterSeverity) {
        firePropertyChange("searchFilterSeverity", this.searchFilterSeverity,
                this.searchFilterSeverity = searchFilterSeverity);
    }

    /**
     * @return the searchText
     */
    public String getSearchText() {
        return searchText;
    }

    /**
     * @param searchText the searchText to set
     */
    public void setSearchText(String searchText) {
        firePropertyChange("searchText", this.searchText, this.searchText = searchText);
    }

    /**
     * @return the field
     */
    public LogMessageFields getField() {
        return field;
    }

    /**
     * @param field
     *            the field to set
     */
    public void setField(LogMessageFields field) {
        firePropertyChange("field", this.field, this.field = field);
    }
}
