package uk.ac.stfc.isis.ibex.alerts.tests;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.alerts.AlertsServer;
import uk.ac.stfc.isis.ibex.alerts.AlertsSetter;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;

/**
 * Tests the AlertsSetter class.
 */
@SuppressWarnings("unchecked")
public class AlertsSetterTest {

    private AlertsServer mockAlertsServer;
    private Writable<Double> mockLowLimitSetter;
    private Writable<Double> mockHighLimitSetter;
    private Writable<String> mockEnabledSetter;
    private Writable<Double> mockDelayInSetter;
    private Writable<Double> mockDelayOutSetter;
    private AlertsSetter alertsSetter;

    @Before
    public void setUp() {
        mockAlertsServer = mock(AlertsServer.class);
        mockLowLimitSetter = mock(Writable.class);
        mockHighLimitSetter = mock(Writable.class);
        mockEnabledSetter = mock(Writable.class);
        mockDelayInSetter = mock(Writable.class);
        mockDelayOutSetter = mock(Writable.class);

        when(mockAlertsServer.setLowLimit("testBlock")).thenReturn(mockLowLimitSetter);
        when(mockAlertsServer.setHighLimit("testBlock")).thenReturn(mockHighLimitSetter);
        when(mockAlertsServer.setEnabled("testBlock")).thenReturn(mockEnabledSetter);
        when(mockAlertsServer.setDelayIn("testBlock")).thenReturn(mockDelayInSetter);
        when(mockAlertsServer.setDelayOut("testBlock")).thenReturn(mockDelayOutSetter);

        alertsSetter = new AlertsSetter("testBlock", mockAlertsServer);
    }

    @Test
    public void testSetLowLimit() {
        Double limit = 10.0;
        alertsSetter.setLowLimit(limit);
        verify(mockLowLimitSetter).uncheckedWrite(limit);
    }

    @Test
    public void testSetHighLimit() {
        Double limit = 20.0;
        alertsSetter.setHighLimit(limit);
        verify(mockHighLimitSetter).uncheckedWrite(limit);
    }

    @Test
    public void testSetEnabled() {
        alertsSetter.setEnabled(true);
        verify(mockEnabledSetter).uncheckedWrite("YES");

        alertsSetter.setEnabled(false);
        verify(mockEnabledSetter).uncheckedWrite("NO");
    }

    @Test
    public void testSetDelayIn() {
        Double delayIn = 5.0;
        alertsSetter.setDelayIn(delayIn);
        verify(mockDelayInSetter).uncheckedWrite(delayIn);
    }

    @Test
    public void testSetDelayOut() {
        Double delayOut = 15.0;
        alertsSetter.setDelayOut(delayOut);
        verify(mockDelayOutSetter).uncheckedWrite(delayOut);
    }
}
