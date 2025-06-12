package uk.ac.stfc.isis.ibex.ui.banner.tests;

import org.mockito.Mock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.LinkedList;
import java.util.List;

@RunWith(MockitoJUnitRunner.StrictStubs.class)
public class ServerStatusViewModelTest {
    @Mock private LinkedList<String> mockedList;
    
    @Test
    public void myUnitTest() {
        // stubbing appears before the actual execution
        when(mockedList.get(0)).thenReturn("first");
    
        // the following prints "first“
        System.out.println(mockedList.get(0));
    
        // the following prints "null" because get(999) was not stubbed
        System.out.println(mockedList.get(999));
    }
}