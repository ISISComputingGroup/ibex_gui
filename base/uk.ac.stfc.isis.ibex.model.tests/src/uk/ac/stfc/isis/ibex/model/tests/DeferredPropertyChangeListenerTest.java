package uk.ac.stfc.isis.ibex.model.tests;

import static org.junit.Assert.assertEquals;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import uk.ac.stfc.isis.ibex.model.DeferredPropertyChangeListener;

@RunWith(MockitoJUnitRunner.class)
public class DeferredPropertyChangeListenerTest {
	
	private int oldValue = 0;
	private int newValue = 0;
	

	@Captor private ArgumentCaptor<Runnable> runnableCaptor;
	
	@Mock private ScheduledThreadPoolExecutor executor;
	
	private PropertyChangeListener listener;
	
	@Before
	public void setUp() {
		oldValue = 0;
		newValue = 0;
		
		listener = new DeferredPropertyChangeListener(new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				oldValue = (int) evt.getOldValue();
				newValue = (int) evt.getNewValue();
			}
		}, 0, TimeUnit.SECONDS, executor);
        
        Mockito.verify(executor).scheduleWithFixedDelay(runnableCaptor.capture(), Mockito.anyLong(), Mockito.anyLong(), Mockito.any());
	}
	
	@Test
    public void GIVEN_two_events_with_same_source_and_property_name_THEN_batched() {
        var source = new Object();
        
        // Simulate two events, 1->2 and then 2->3
        listener.propertyChange(new PropertyChangeEvent(source, "ABC", 1, 2));
        listener.propertyChange(new PropertyChangeEvent(source, "ABC", 2, 3));
        
        runnableCaptor.getValue().run();
        
        // Assert that the two events above got "batched" into a single event, 1->3
        assertEquals(oldValue, 1);
        assertEquals(newValue, 3);
    }
	
	@Test
    public void GIVEN_two_events_with_different_source_THEN_not_batched() {
        var source1 = new Object();
        var source2 = new Object();
        
        // Simulate two events, 1->2 and then 2->3
        listener.propertyChange(new PropertyChangeEvent(source1, "ABC", 1, 2));
        listener.propertyChange(new PropertyChangeEvent(source2, "ABC", 2, 3));
        
        runnableCaptor.getValue().run();
        
        // Assert that the two events above didn't get batched. They are both updating a single variable
        // and the order is undefined, so check new - old == 1 (which would not be the case if events got accidentally batched)
        assertEquals(newValue - oldValue, 1);
    }
	
	@Test
    public void GIVEN_two_events_with_different_property_names_THEN_not_batched() {
        var source = new Object();
        
        // Simulate two events, 1->2 and then 2->3
        listener.propertyChange(new PropertyChangeEvent(source, "ABC", 1, 2));
        listener.propertyChange(new PropertyChangeEvent(source, "CBA", 2, 3));
        
        runnableCaptor.getValue().run();
        
        // Assert that the two events above didn't get batched. They are both updating a single variable
        // and the order is undefined, so check new - old == 1 (which would not be the case if events got accidentally batched)
        assertEquals(newValue - oldValue, 1);
    }
	
	@Test
    public void GIVEN_two_time_separated_events_THEN_not_batched_and_new_and_old_values_correct() {
        var source = new Object();
        
        // Simulate two events, 1->2 and then 2->3
        listener.propertyChange(new PropertyChangeEvent(source, "ABC", 1, 2));

        runnableCaptor.getValue().run();
        assertEquals(oldValue, 1);
        assertEquals(newValue, 2);
        
        listener.propertyChange(new PropertyChangeEvent(source, "ABC", 2, 3));

        runnableCaptor.getValue().run();
        assertEquals(oldValue, 2);
        assertEquals(newValue, 3);
    }
}
