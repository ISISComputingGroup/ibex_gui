package uk.ac.stfc.isis.ibex.configserver.tests.displaying;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.configserver.configuration.Block;
import uk.ac.stfc.isis.ibex.configserver.displaying.DisplayBlock;
import uk.ac.stfc.isis.ibex.epics.observing.CachingObservable;
import uk.ac.stfc.isis.ibex.epics.observing.InitialiseOnSubscribeObservable;

@SuppressWarnings({ "unchecked", "checkstyle:methodname" })
public class DisplayBlockTest {
	
	TestableIOSObservable<String> inRangeObservable;
	
	DisplayBlock displayBlock;
	
	@Before
	public void setUp() {
		// Arrange
		
		inRangeObservable = new TestableIOSObservable<>(mock(CachingObservable.class));
		
		displayBlock = new DisplayBlock(
				mock(Block.class),                            // block
				mock(InitialiseOnSubscribeObservable.class),  // value
				mock(InitialiseOnSubscribeObservable.class),  // description
				inRangeObservable,                            // inRange
				mock(InitialiseOnSubscribeObservable.class),  // lowLimit
				mock(InitialiseOnSubscribeObservable.class),  // highLimit
				mock(InitialiseOnSubscribeObservable.class),  // enabled
				"");		
	}
	
	@Test
	public void if_in_range_unset_then_text_color_is_black() {
		// Act - do nothing
		
		// Assert
		assertEquals(displayBlock.getTextColor(), DisplayBlock.BLACK);
	}
	
	@Test
	public void if_in_range_unset_then_background_color_is_white() {
		// Act - do nothing
		
		// Assert
		assertEquals(displayBlock.getBackgroundColor(), DisplayBlock.WHITE);
	}
	
	@Test
	public void setting_in_range_to_true_sets_text_color_to_black() {
		// Act
		inRangeObservable.setValue("YES");
		
		// Assert
		assertEquals(displayBlock.getTextColor(), DisplayBlock.BLACK);
	}
	
	@Test
	public void setting_in_range_to_true_sets_background_color_to_white() {
		// Act
		inRangeObservable.setValue("YES");
		
		// Assert
		assertEquals(displayBlock.getBackgroundColor(), DisplayBlock.WHITE);
	}
	
	@Test
	public void setting_in_range_to_false_sets_text_color_to_white() {
		// Act
		inRangeObservable.setValue("NO");
		
		// Assert
		assertEquals(displayBlock.getTextColor(), DisplayBlock.WHITE);
	}
	
	@Test
	public void setting_in_range_to_false_sets_background_color_to_dark_red() {
		// Act
		inRangeObservable.setValue("NO");
		
		// Assert
		assertEquals(displayBlock.getBackgroundColor(), DisplayBlock.DARK_RED);
	}
	
	@Test
	public void setting_in_range_to_false_then_true_sets_text_color_to_black() {
		// Act
		inRangeObservable.setValue("NO");
		inRangeObservable.setValue("YES");
		
		// Assert
		assertEquals(displayBlock.getTextColor(), DisplayBlock.BLACK);
	}
	
	@Test
	public void setting_in_range_to_false_then_true_sets_background_color_to_white() {
		// Act
		inRangeObservable.setValue("NO");
		inRangeObservable.setValue("YES");
		
		// Assert
		assertEquals(displayBlock.getBackgroundColor(), DisplayBlock.WHITE);
	}

	@Test
	public void setting_in_range_to_true_then_false_sets_text_color_to_white() {
		// Act
		inRangeObservable.setValue("YES");
		inRangeObservable.setValue("NO");
		
		// Assert
		assertEquals(displayBlock.getTextColor(), DisplayBlock.WHITE);
	}
	
	@Test
	public void setting_in_range_to_true_then_false_sets_background_color_to_black() {
		// Act
		inRangeObservable.setValue("YES");
		inRangeObservable.setValue("NO");
		
		// Assert
		assertEquals(displayBlock.getBackgroundColor(), DisplayBlock.DARK_RED);
	}
	
	@Test
	public void setting_in_range_to_false_then_nonsense_sets_text_color_to_black() {
		// Act
		inRangeObservable.setValue("NO");
		inRangeObservable.setValue("maybe");
		
		// Assert
		assertEquals(displayBlock.getTextColor(), DisplayBlock.BLACK);
	}
	
	@Test
	public void setting_in_range_to_false_then_nonsense_sets_background_color_to_white() {
		// Act		
		inRangeObservable.setValue("NO");
		inRangeObservable.setValue("maybe");
		
		// Assert
		assertEquals(displayBlock.getBackgroundColor(), DisplayBlock.WHITE);
	}
}
