package uk.ac.stfc.isis.ibex.configserver.tests.editing;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.configserver.editing.DefaultName;

public class DefaultNames {
	
	private DefaultName name;
	private static final String DEFAULT_NAME = "NAME";
	private List<String> existing;
	
	@Before
	public void setup() {
		existing = new ArrayList<>();
		name = new DefaultName(DEFAULT_NAME);
	}
	
	@Test
	public void default_name_if_no_others() {
		assertThat(name.getUnique(existing), is(DEFAULT_NAME));
	}
	
	@Test
	public void number_is_appended_if_default_already_taken() {
		existing.add(DEFAULT_NAME);
		assertThat(name.getUnique(existing), is(numberedDefault(1)));
	}

	@Test
	public void next_available_number_is_appended() {
		existing.add(DEFAULT_NAME);
		existing.add(numberedDefault(1));
		assertThat(name.getUnique(existing), is(numberedDefault(2)));
	}
	
	@Test
	public void next_available_number_is_appended_when_nonsequential() {
		existing.add(DEFAULT_NAME);
		existing.add(numberedDefault(2));
		assertThat(name.getUnique(existing), is(numberedDefault(1)));
	}
	
	@Test
	public void default_still_chosen_if_available() {
		existing.add(numberedDefault(1));
		assertThat(name.getUnique(existing), is(DEFAULT_NAME));
	}
	
	private String numberedDefault(Integer number) {
		return DEFAULT_NAME + "(" + number.toString() + ")";
	}
}
