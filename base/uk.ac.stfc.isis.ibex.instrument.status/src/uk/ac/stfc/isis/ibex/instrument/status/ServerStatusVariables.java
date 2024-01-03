package uk.ac.stfc.isis.ibex.instrument.status;

import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
import uk.ac.stfc.isis.ibex.epics.switching.ObservableFactory;
import uk.ac.stfc.isis.ibex.epics.switching.OnInstrumentSwitch;
import uk.ac.stfc.isis.ibex.instrument.InstrumentUtils;
import uk.ac.stfc.isis.ibex.instrument.channels.BooleanChannel;
import uk.ac.stfc.isis.ibex.instrument.channels.DefaultChannel;

/**
 * Collection of PVs related to the IBEX server status.
 */
public class ServerStatusVariables {

	private static ServerStatusVariables instance;

	/**
	 * The constructor.
	 */
	private ServerStatusVariables() {
	}

	public static ServerStatusVariables getInstance() {
		if (instance == null) {
			instance = new ServerStatusVariables();
		}
		return instance;
	}
    
    /** PV for existence of Runcontrol IOC */
    private static final String RUNCTRL_PV = "CS:RC:INRANGE";
    /** PV for existence of Blockserver */
    private static final String BLOCKSVR_PV = "CS:BLOCKSERVER:SERVER_STATUS";
    /** PV for existence of Block Gateway */
    private static final String BLOCKGW_PV = "CS:GATEWAY:BLOCKSERVER:pvtotal";
    /** PV for existence of ISISDAE IOC */
    private static final String ISISDAE_PV = "DAE:INSTNAME";
    /** PV for existence of INSTETC IOC */
    private static final String INSTETC_PV = "CS:MANAGER";
    /** PV for existence of Database Server */
    private static final String DBSVR_PV = "CS:BLOCKSERVER:IOCS";
    /** PV for existence of Procserv Control IOC */
    private static final String PSCTRL_PV = "CS:PS:PSCTRL_01:IOCNAME";
    /** PV for existence of Alarm Server */
    private static final String ALARMSVR_PV = "CS:PS:ALARM:STATUS";
    
    private final ObservableFactory obsFactory = new ObservableFactory(OnInstrumentSwitch.SWITCH);

    private final ForwardingObservable<Boolean> runcontrolPV = 
            obsFactory.getSwitchableObservable(new BooleanChannel(), InstrumentUtils.addPrefix(RUNCTRL_PV));

    private final ForwardingObservable<Boolean> blockServerPV = 
            obsFactory.getSwitchableObservable(new BooleanChannel(), InstrumentUtils.addPrefix(BLOCKSVR_PV));

    private final ForwardingObservable<Boolean> blockGatewayPV = 
            obsFactory.getSwitchableObservable(new BooleanChannel(), InstrumentUtils.addPrefix(BLOCKGW_PV));

    private final ForwardingObservable<Boolean> isisDaePV = 
            obsFactory.getSwitchableObservable(new BooleanChannel(), InstrumentUtils.addPrefix(ISISDAE_PV));
    
    private final ForwardingObservable<Boolean> instetcPV = 
            obsFactory.getSwitchableObservable(new BooleanChannel(), InstrumentUtils.addPrefix(INSTETC_PV));
    
    private final ForwardingObservable<Boolean> databaseServerPV = 
            obsFactory.getSwitchableObservable(new BooleanChannel(), InstrumentUtils.addPrefix(DBSVR_PV));
    
    private final ForwardingObservable<Boolean> procservControlPV = 
            obsFactory.getSwitchableObservable(new BooleanChannel(), InstrumentUtils.addPrefix(PSCTRL_PV));

    private final ForwardingObservable<String> alarmServerPV = 
            obsFactory.getSwitchableObservable(new DefaultChannel(), InstrumentUtils.addPrefix(ALARMSVR_PV));

    /**
     * @return Observable for PV of Runcontrol IOC
     */
	public ForwardingObservable<Boolean> getRuncontrolPV() {
		return runcontrolPV;
	}
    /**
     * @return Observable for PV of Blockserver
     */
	public ForwardingObservable<Boolean> getBlockServerPV() {
		return blockServerPV;
	}
    /**
     * @return Observable for PV of Block Gateway
     */
	public ForwardingObservable<Boolean> getBlockGatewayPV() {
		return blockGatewayPV;
	}
    /**
     * @return Observable for PV of ISISDAE IOC
     */
	public ForwardingObservable<Boolean> getIsisDaePV() {
		return isisDaePV;
	}
    /**
     * @return Observable for PV of INSTETC IOC
     */
	public ForwardingObservable<Boolean> getInstetcPV() {
		return instetcPV;
	}
    /**
     * @return Observable for PV of Database Server
     */
	public ForwardingObservable<Boolean> getDatabaseServerPV() {
		return databaseServerPV;
	}
	
    /**
     * @return Observable for PV of Procserv Control IOC
     */
	public ForwardingObservable<Boolean> getProcservControlPV() {
		return procservControlPV;
	}
	
    /**
     * @return Observable for PV of Alarmserver
     */
    public ForwardingObservable<String> getAlarmServerPV() {
		return alarmServerPV;
	}


}
