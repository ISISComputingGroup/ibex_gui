
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

package uk.ac.stfc.isis.ibex.scriptgenerator;

import uk.ac.stfc.isis.ibex.model.ModelObject;

/**
 * All settings for the script generator.
 */
public class Settings extends ModelObject {
	private Order order;
	private Integer doSans;
	private Integer doTrans;
	private Boolean loopOver;
	private ApertureSans sansSize;
	private ApertureTrans transSize;
	private SampleGeometry geometry;
	private Integer sampleHeight;
	private Integer sampleWidth;
	private CollectionMode collection;
	private Integer estCountRate;
	private Integer estMoveTime;
	private String estScriptTime;
	
	/**
	 * The default constructor.
	 * @param doSans how many times to do SANS
	 * @param doTrans how many times to do TRANS
	 * @param sampleHeight height of the sample
	 * @param sampleWidth width of the sample
	 * @param estCountRate estimated count rate (in microamp-hours)
	 * @param estMoveTime estimated move time (in seconds)
	 * @param estScriptTime the generated estimated script time
	 */
	public Settings(Integer doSans, Integer doTrans, Integer sampleHeight, Integer sampleWidth, Integer estCountRate, Integer estMoveTime, String estScriptTime) {
		this.doSans = doSans;
		this.doTrans = doTrans;
		this.sampleHeight = sampleHeight;
		this.sampleWidth = sampleWidth;
		this.estCountRate = estCountRate;
		this.estMoveTime = estMoveTime;
		this.estScriptTime = estScriptTime;
	}
	
	/**
	 * Gets the Do SANS.
	 * @return the Do SANS
	 */
	public Integer getDoSans() {
		return doSans;
	}

	/**
	 * Sets the Do SANS.
	 * @param doSans the Do SANS
	 */
	public void setDoSans(Integer doSans) {
		firePropertyChange("doSans", this.doSans, this.doSans = doSans);
	}

	/**
	 * Gets the Do TRANS.
	 * @return the Do TRANS
	 */
	public Integer getDoTrans() {
		return doTrans;
	}

	/**
	 * Sets the Do TRANS.
	 * @param doTrans the doSans
	 */
	public void setDoTrans(Integer doTrans) {
		firePropertyChange("doTrans", this.doTrans, this.doTrans = doTrans);
	}

	/**
	 * Gets the Sample Height.
	 * @return the Sample Height
	 */
	public Integer getSampleHeight() {
		return sampleHeight;
	}

	/**
	 * Sets the Sample Height.
	 * @param sampleHeight the Sample Height
	 */
	public void setSampleHeight(Integer sampleHeight) {
		firePropertyChange("sampleHeight", this.sampleHeight, this.sampleHeight = sampleHeight);
	}

	/**
	 * Gets the Sample Width.
	 * @return the Sample Width value
	 */
	public Integer getSampleWidth() {
		return sampleWidth;
	}

	/**
	 * Sets the SampleWidth.
	 * @param sampleWidth the Sample Width
	 */
	public void setSampleWidth(Integer sampleWidth) {
		firePropertyChange("sampleWidth", this.sampleWidth, this.sampleWidth = sampleWidth);
	}

	/**
	 * Gets the estimated count rate.
	 * @return the estimated count rate
	 */
	public Integer getEstCountRate() {
		return estCountRate;
	}

	/**
	 * Sets the estimated count rate.
	 * @param estCountRate the estimated count rate
	 */
	public void setEstCountRate(Integer estCountRate) {
		firePropertyChange("estCountRate", this.estCountRate, this.estCountRate = estCountRate);
	}

	/**
	 * Gets the estimated move time.
	 * @return the estimated move time
	 */
	public Integer getEstMoveTime() {
		return estMoveTime;
	}

	/**
	 * Sets the estimated move time.
	 * @param estMoveTime the estimated move time
	 */
	public void setEstMoveTime(Integer estMoveTime) {
		firePropertyChange("estMoveTime", this.estMoveTime, this.estMoveTime = estMoveTime);
	}

	/** 
	 * Gets the estimated script time.
	 * @return the estimated script time
	 */
	public String getEstScriptTime() {
		return estScriptTime;
	}

	/**
	 * Sets the estimated script time.
	 * @param estScriptTime the estimated script time
	 */
	public void setEstScriptTime(String estScriptTime) {
		firePropertyChange("estScriptTime", this.estScriptTime, this.estScriptTime = estScriptTime);
	}		
	
	/**
	 * Gets the Order.
	 * @return the order
	 */
	public Order getOrder() {
		return order;
	}

	/** 
	 * Sets the order 
	 * @param order the order
	 */
	public void setOrder(Order order) {
		this.order = order;
	}

	/**
	 * Gets the "Loop over each run?" checkbox value
	 * @return the "Loop over each run?" boolean
	 */
	public Boolean getLoopOver() {
		return loopOver;
	}

	/**
	 * Sets the "Loop over each run?" checkbox value
	 * @param loopOver the "Loop over each run?" boolean
	 */
	public void setLoopOver(Boolean loopOver) {
		this.loopOver = loopOver;
	}

	/**
	 * Gets the SANS aperture size
	 * @return the SANS aperture
	 */
	public ApertureSans getSansSize() {
		return sansSize;
	}

	/**
	 * Sets the SANS aperture size
	 * @param sansSize the SANS aperture size
	 */
	public void setSansSize(ApertureSans sansSize) {
		this.sansSize = sansSize;
	}

	/**
	 * Gets the TRANS aperture size
	 * @return the TRANS aperture size
	 */
	public ApertureTrans getTransSize() {
		return transSize;
	}

	/**
	 * Sets the TRANS aperture size
	 * @param transSize the TRANS aperture size
	 */
	public void setTransSize(ApertureTrans transSize) {
		this.transSize = transSize;
	}

	/**
	 * Gets the Sample Geometry
	 * @return the Sample Geometry
	 */
	public SampleGeometry getGeometry() {
		return geometry;
	}

	/**
	 * Sets the Sample Geometry
	 * @param geometry the Sample Geometry
	 */
	public void setGeometry(SampleGeometry geometry) {
		this.geometry = geometry;
	}

	/**
	 * Gets the Collection Mode 
	 * @return the Collection Mode 
	 */
	public CollectionMode getCollection() {
		return collection;
	}

	/** 
	 * Sets the Collection Mode
	 * @param collection the Collection Mode
	 */
	public void setCollection(CollectionMode collection) {
		this.collection = collection;
	}
}