
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

package uk.ac.stfc.isis.ibex.journal.tests;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import uk.ac.stfc.isis.ibex.journal.JournalField;
import uk.ac.stfc.isis.ibex.journal.JournalFieldCategoriser;
import uk.ac.stfc.isis.ibex.journal.JournalFieldCategoriser.JournalFieldCategory;

/**
 * Test the Categorisers for JournalFields.
 */
@SuppressWarnings("checkstyle:methodname")
public class JournalCategoriserTest {

	@Test
	public void test_all_journal_fields_get_categorised_in_map() {
		Map<String, List<JournalField>> catMap = JournalFieldCategoriser.calculateCategoryMap();

		for (final JournalField field : JournalField.values()) {
			boolean exists = catMap.values().stream().anyMatch(list -> list.contains(field));
			assertTrue(exists);
		}
	}

	@Test
	public void test_all_categories_have_title() {
		for (final JournalFieldCategory category : JournalFieldCategory.values()) {
			final String title = category.getCategoryTitle();

			assertNotNull(title);
			assertTrue(title.length() > 0);
		}
	}


	@Test
	public void test_all_categories_have_no_more_than_n_percent_deviation_from_IQR_mean_of_fields_per_category() {
		Map<String, List<JournalField>> catMap = JournalFieldCategoriser.calculateCategoryMap();

		final List<Integer> lengths = new ArrayList<>(0);
		final int numCategories = catMap.size();

		for (final List<JournalField> fieldsList : catMap.values()) {
			lengths.add(fieldsList.size());
		}

		final double iqr_mean_fields_per_category = lengths.subList(numCategories / 4, (3 * numCategories) / 4).stream()
				.mapToDouble(a -> a).average().orElse(0.0);
		


		for (final List<JournalField> fieldsList : catMap.values()) {
			// Test: Is every category +/- n% of the average?
			final double margin = 0.75;
			final int noFields = fieldsList.size();
			
			double lower = iqr_mean_fields_per_category * (1.0 - margin);
			double upper = iqr_mean_fields_per_category * (1.0 + margin);

			assertTrue(noFields >= lower && noFields <= upper);
		}

	}

}
