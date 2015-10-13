
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

package uk.ac.stfc.isis.ibex.instrument.pv.tests;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;

import uk.ac.stfc.isis.ibex.epics.observing.BaseCachingObservable;
import uk.ac.stfc.isis.ibex.instrument.pv.PVAddressBook;
import uk.ac.stfc.isis.ibex.instrument.pv.PVType;

/**
 * This class is responsible for testing the Address Book of PVs
 *
 */
@SuppressWarnings({ "checkstyle:magicnumber", "checkstyle:methodname" })
public class PVAddressBookTest {

	/**
	 * Test method for {@link uk.ac.stfc.isis.ibex.instrument.pv.PVAddressBook#resolvePV}.
	 */
	@Test
	public final void resolve_pv_string_suffix_only() {
		// Arrange
		String prefix = "Prefix";
		String suffix = "Suffix";
		PVAddressBook addressBook = new PVAddressBook(prefix);
		// Act
		BaseCachingObservable<String> actual = addressBook.resolvePV(suffix);
		// Assert
		assertEquals(prefix+suffix, actual.toString());
	}
	
	/**
	 * Test method for {@link uk.ac.stfc.isis.ibex.instrument.pv.PVAddressBook#resolvePV}.
	 * The pv is already in the book.
	 */
	@Test
	public final void resolve_pv_string_suffix_only_pre_exists() {
		// Arrange
		String prefix = "Prefix";
		String suffix = "Suffix";
		PVAddressBook addressBook = new PVAddressBook(prefix);
		// Act
		addressBook.resolvePV(suffix);
		BaseCachingObservable<String> actual = addressBook.resolvePV(suffix);
		// Assert
		assertEquals(prefix+suffix, actual.toString());
	}

	/**
	 * Test method for {@link uk.ac.stfc.isis.ibex.instrument.pv.PVAddressBook#resolvePV}.
	 */
	@Test
	public final void resolve_pv_string_suffix_local() {
		// Arrange
		String prefix = "Prefix";
		String suffix = "Suffix";
		PVType type = PVType.LOCAL_PV;
		PVAddressBook addressBook = new PVAddressBook(prefix);
		// Act
		BaseCachingObservable<String> actual = addressBook.resolvePV(suffix, type);
		// Assert
		assertEquals(prefix+suffix, actual.toString());
	}
	
	/**
	 * Test method for {@link uk.ac.stfc.isis.ibex.instrument.pv.PVAddressBook#resolvePV}.
	 * The pv is already in the book.
	 */
	@Test
	public final void resolve_pv_string_suffix_local_pre_exists() {
		// Arrange
		String prefix = "Prefix";
		String suffix = "Suffix";
		PVType type = PVType.LOCAL_PV;
		PVAddressBook addressBook = new PVAddressBook(prefix);
		// Act
		addressBook.resolvePV(suffix, type);
		BaseCachingObservable<String> actual = addressBook.resolvePV(suffix, type);
		// Assert
		assertEquals(prefix+suffix, actual.toString());
	}
	
	/**
	 * Test method for {@link uk.ac.stfc.isis.ibex.instrument.pv.PVAddressBook#resolvePV}.
	 */
	@Test
	public final void resolve_pv_string_suffix_remote() {
		// Arrange
		String prefix = "Prefix";
		String suffix = "Suffix";
		PVType type = PVType.REMOTE_PV;
		PVAddressBook addressBook = new PVAddressBook(prefix);
		// Act
		BaseCachingObservable<String> actual = addressBook.resolvePV(suffix, type);
		// Assert
		assertEquals(suffix, actual.toString());
	}
	
	/**
	 * Test method for {@link uk.ac.stfc.isis.ibex.instrument.pv.PVAddressBook#resolvePV}.
	 * The pv is already in the book.
	 */
	@Test
	public final void resolve_pv_string_suffix_remote_pre_exists() {
		// Arrange
		String prefix = "Prefix";
		String suffix = "Suffix";
		PVType type = PVType.REMOTE_PV;
		PVAddressBook addressBook = new PVAddressBook(prefix);
		// Act
		addressBook.resolvePV(suffix, type);
		BaseCachingObservable<String> actual = addressBook.resolvePV(suffix, type);
		// Assert
		assertEquals(suffix, actual.toString());
	}

	/**
	 * Test method for {@link uk.ac.stfc.isis.ibex.instrument.pv.PVAddressBook#setPrefix(java.lang.String)}.
	 */
	@Test
	public final void set_new_prefix() {
		// Arrange
		String prefix = "Prefix";
		String suffix = "Suffix";
		String newPrefix = "NewPrefix";
		PVAddressBook addressBook = new PVAddressBook(prefix);
		// Act
		addressBook.resolvePV(suffix);
		addressBook.setPrefix(newPrefix);
		BaseCachingObservable<String> actual = addressBook.resolvePV(suffix);
		// Assert
		assertEquals(newPrefix+suffix, actual.toString());
	}

}
