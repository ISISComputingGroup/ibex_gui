
/*
 * This file is part of the ISIS IBEX application. Copyright (C) 2012-2019
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

package uk.ac.stfc.isis.ibex.journal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Class encapsulating categories and respective Enums and Methods for
 * JournalFields.
 */
public class JournalFieldCategoriser {
	/**
	 * Enum used to categorise all fields in the journal viewer for grouping and
	 * reference in the UI.
	 */
	public enum JournalFieldCategory {

		/**
		 * Field groupings for times and frames.
		 */
		TIME_AND_FRAME("Times And Frames", "Field Groupings for times and frames."),

		/**
		 * Field groupings for names and labels.
		 */
		NAME("Identifiers", "Field Groupings for names and labels."),

		/**
		 * Field groupings for tables.
		 */
		TABLE("Table", "Field Groupings for tables."),

		/**
		 * Field groupings for measurement information.
		 */
		MEASURE("Measurement", "Field Groupings for measurement information."),

		/**
		 * Field groupings for experiment information and proposal.
		 */
		EXP("Experiment information", "Field groupings for experiement info and proposal"),

		/**
		 * Field grouping for instrument configuration and monitoring.
		 */
		INSTRUMENT("Instrument", "Field grouping for instrument configuration and monitoring"),

		/**
		 * Field grouping for samples.
		 */
		SAMPLE("Sample", "Field grouping for samples"),

		/**
		 * Field grouping for texts.
		 */
		TEXT("Text", "Field grouping for texts"),

		/**
		 * Field groupings for numbers.
		 */
		NUMBER("Counts", "Field Groupings for numbers of.");

		private final String title;
		private final String tooltip;

		/**
		 * Initialise a JournalFieldCategory.
		 * 
		 * @param title
		 * @param tooltip
		 */
		JournalFieldCategory(String title, String tooltip) {
			this.title = title;
			this.tooltip = tooltip;
		}

		/**
		 * Gets the friendly title of a category.
		 * 
		 * @return Title.
		 */
		public String getCategoryTitle() {
			return title;
		}

		/**
		 * Gets the descriptive tooltip for a category.
		 * 
		 * @return Tooltip.
		 */
		public String getCategoryTooltip() {
			return tooltip;
		}

	}

	JournalField categoryField;
	JournalFieldCategory category;

	/**
	 * Create a journal sort class.
	 * 
	 * @param sortField The field to sort with
	 * @param direction The direction to sort in
	 */
	public JournalFieldCategoriser(JournalField categoryField, JournalFieldCategory category) {
		this.categoryField = categoryField;
		this.category = category;
	}

	/**
	 * Calculate and returns a TreeMap of JournalFields organised into lists
	 * respective of their category.
	 * 
	 * @return A TreeMap. Category: List of JournalFields in that category.
	 */
	public static Map<String, List<JournalField>> calculateCategoryMap() {
		final Map<String, List<JournalField>> fieldsByCategory = new TreeMap<>();
		for (final JournalField property : JournalField.values()) {
			final String category = property.getCategory().toString();
			if (fieldsByCategory.containsKey(category)) {
				fieldsByCategory.get(category).add(property);
			} else {
				final List<JournalField> catList = new ArrayList<>();
				catList.add(property);
				fieldsByCategory.put(category, catList);
			}
		}
		return fieldsByCategory;
	}

	/**
	 * @return the sortField
	 */
	public JournalField getCategoryField() {
		return categoryField;
	}

	/**
	 * @param rawName The Enum identifier as a string
	 * @return a friendly UI-facing string, i.e. title of the Enum.
	 */
	public static String getFriendlyCategoryName(String rawName) {
		return JournalFieldCategory.valueOf(rawName).getCategoryTitle();
	}

	/**
	 * @return the direction
	 */
	public JournalFieldCategory getCategory() {
		return category;
	}

}
