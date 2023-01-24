package uk.ac.stfc.isis.ibex.configserver.tests;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import uk.ac.stfc.isis.ibex.configserver.ConfigServer;
import uk.ac.stfc.isis.ibex.configserver.IocState;
import uk.ac.stfc.isis.ibex.configserver.configuration.ComponentInfo;
import uk.ac.stfc.isis.ibex.configserver.configuration.Configuration;
import uk.ac.stfc.isis.ibex.configserver.configuration.Ioc;
import uk.ac.stfc.isis.ibex.configserver.configuration.SimLevel;

public class IocStateTest {
	private ConfigServer server = Mockito.mock(ConfigServer.class, Mockito.RETURNS_DEEP_STUBS);
	
	private Configuration currentConfig = Mockito.mock(Configuration.class);
	
	private Configuration component = Mockito.mock(Configuration.class);
	private Collection<Configuration> availableComponents = new ArrayList<Configuration>();
	
	private ComponentInfo componentInfo = Mockito.mock(ComponentInfo.class);
	private Collection<ComponentInfo> configComponents = new ArrayList<ComponentInfo>();
	
	private Ioc configIoc = Mockito.mock(Ioc.class);
	private Collection<Ioc> configIocs = new ArrayList<Ioc>();
	
	private Ioc componentIoc = Mockito.mock(Ioc.class);
	private Collection<Ioc> componentIocs = new ArrayList<Ioc>();
	
	private String IOC_NAME = "IOC_01";
	private IocState iocState = new IocState(server, IOC_NAME, true, null);
	
	@Before
    public void setUp() {
		// Setup current configuration.
		Mockito.when(server.currentConfig().getValue()).thenReturn(currentConfig);
		
		// Setup components.
		availableComponents.add(component);
		Mockito.when(server.componentDetails().getValue()).thenReturn(availableComponents);
		configComponents.add(componentInfo);
		Mockito.when(currentConfig.getComponents()).thenReturn(configComponents);
		
		// Setup IOCs.
		configIocs.add(configIoc);
		componentIocs.add(componentIoc);
	}
	
	@Test
	public void GIVEN_ioc_in_config_WHEN_get_sim_level_THEN_sim_level_correct() {
		Mockito.when(currentConfig.getIocs()).thenReturn(configIocs);
		Mockito.when(configIoc.getName()).thenReturn(IOC_NAME);
		Mockito.when(configIoc.getSimLevel()).thenReturn(SimLevel.DEVSIM);
		
		assertEquals(iocState.getSimLevel(), SimLevel.DEVSIM);
	}
	
	@Test
	public void GIVEN_ioc_in_config_component_WHEN_get_sim_level_THEN_sim_level_correct() {
		Mockito.when(component.name()).thenReturn("COMP");
		Mockito.when(componentInfo.getName()).thenReturn("COMP");
		Mockito.when(component.getIocs()).thenReturn(componentIocs);
		
		Mockito.when(componentIoc.getName()).thenReturn(IOC_NAME);
		Mockito.when(componentIoc.getSimLevel()).thenReturn(SimLevel.DEVSIM);
		
		assertEquals(iocState.getSimLevel(), SimLevel.DEVSIM);
	}
}
