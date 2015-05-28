package uk.ac.stfc.isis.ibex.dae.dataacquisition.tests;

import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.net.URL;

import uk.ac.stfc.isis.ibex.dae.dataacquisition.BinaryChoice;
import uk.ac.stfc.isis.ibex.dae.dataacquisition.DaeTimingSource;
import uk.ac.stfc.isis.ibex.dae.dataacquisition.MuonCerenkovPulse;
import uk.ac.stfc.isis.ibex.dae.dataacquisition.XmlBackedDaeSettings;
import uk.ac.stfc.isis.ibex.dae.tests.FileReadingTest;
import org.junit.Before;
import org.junit.Test;

public class Setting_dae_settings_from_xml extends FileReadingTest {
	
	private XmlBackedDaeSettings daeSettings;
	
	@Override
	protected URL fileLocation() throws MalformedURLException {
		return getClass().getResource("/uk/ac/stfc/isis/ibex/dae/dataacquisition/tests/dae_settings.xml");
	}
	
	@Before
	public void setUp() throws Exception {	
		daeSettings = new XmlBackedDaeSettings();
		daeSettings.setXml(fileContent());
	}

	@Test
	public void monitor_spectrum_is_updated() {
		assertEquals("Monitor spectrum not parsed correctly", 1, daeSettings.monitorSpectrum());
	}	

	@Test
	public void to_is_updated() {
		assertEquals("To value not parsed correctly", 99000d, daeSettings.to(), Double.MIN_VALUE);
	}	

	@Test
	public void from_is_updated() {
		assertEquals("To value not parsed correctly", 1000d, daeSettings.from(), Double.MIN_VALUE);
	}
	
	@Test
	public void wiring_table_is_updated() {
		assertEquals("Wiring table not parsed correctly", "C:\\Instrument\\Settings\\tables\\wiring.dat", daeSettings.wiringTable());
	}	
	
	@Test
	public void detector_table_is_updated() {
		assertEquals("Detector table not parsed correctly", "C:\\Instrument\\Settings\\tables\\detector.dat", daeSettings.detectorTable());
	}	
	
	@Test
	public void spectra_table_is_updated() {
		assertEquals("Spectra table not parsed correctly", "C:\\Instrument\\Settings\\tables\\spectra.dat", daeSettings.spectraTable());
	}	
	
	@Test
	public void timing_source_is_updated() {
		assertEquals("Timing source not parsed correctly", DaeTimingSource.INTERNAL_TEST_CLOCK, daeSettings.timingSource());
	}
	
	@Test
	public void smp_chopper_veto_is_updated() {
		assertEquals("SMP chopper veto not parsed correctly", BinaryChoice.NO, daeSettings.smpVeto());
	}
	
	@Test
	public void veto0_is_updated() {
		assertEquals("Veto 0 not parsed correctly", BinaryChoice.YES, daeSettings.veto0());
	}
	
	@Test
	public void veto1_is_updated() {
		assertEquals("Veto 1 not parsed correctly", BinaryChoice.YES, daeSettings.veto1());
	}
	
	@Test
	public void veto2_is_updated() {
		assertEquals("Veto 2 not parsed correctly", BinaryChoice.NO, daeSettings.veto2());
	}
	
	@Test
	public void veto3_is_updated() {
		assertEquals("Veto 3 not parsed correctly", BinaryChoice.NO, daeSettings.veto3());
	}

	@Test
	public void ts2pulseVeto_is_updated() {
		assertEquals("TS2 pulse veto not parsed correctly", BinaryChoice.NO, daeSettings.ts2PulseVeto());
	}
	
	@Test
	public void isis50HzVeto_is_updated() {
		assertEquals("Isis 50Hz Veto not parsed correctly", BinaryChoice.NO, daeSettings.isis50HzVeto());
	}
	
	@Test
	public void fermiChopperVeto_is_updated() {
		assertEquals("Fermi chopper veto not parsed correctly", BinaryChoice.NO, daeSettings.fermiChopperVeto());
	}

	@Test
	public void fcDelay_is_updated() {
		assertEquals("FC delay not parsed correctly", 10, daeSettings.fcDelay(), Double.MIN_VALUE);
	}
	
	@Test
	public void fcWidth_is_updated() {
		assertEquals("FC width not parsed correctly", 11, daeSettings.fcWidth(), Double.MIN_VALUE);
	}
	
	@Test
	public void muonCerenkovPulse_is_updated() {
		assertEquals("Muon Cerenkov Pulse not parsed correctly", MuonCerenkovPulse.FIRST, daeSettings.muonCerenkovPulse());
	}
	
	@Test
	public void xml_not_empty() {
		String xml = daeSettings.xml();
		assertFalse(xml.equals(""));
	}
}
