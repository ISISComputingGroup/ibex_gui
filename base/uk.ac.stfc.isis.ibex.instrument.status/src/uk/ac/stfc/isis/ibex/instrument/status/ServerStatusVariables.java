package uk.ac.stfc.isis.ibex.instrument.status;

import java.util.function.Function;

import uk.ac.stfc.isis.ibex.epics.conversion.json.JsonSerialisingConverter;
import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
import uk.ac.stfc.isis.ibex.epics.switching.ObservableFactory;
import uk.ac.stfc.isis.ibex.epics.switching.OnInstrumentSwitch;
import uk.ac.stfc.isis.ibex.epics.switching.WritableFactory;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;
import uk.ac.stfc.isis.ibex.instrument.InstrumentUtils;
import uk.ac.stfc.isis.ibex.instrument.channels.BooleanChannel;
import uk.ac.stfc.isis.ibex.instrument.channels.CompressedCharWaveformChannel;
import uk.ac.stfc.isis.ibex.instrument.channels.DefaultChannel;

/**
 * Collection of PVs related to the IBEX server status.
 */
public final class ServerStatusVariables {

	private static ServerStatusVariables instance;

	/**
	 * The constructor.
	 */
	private ServerStatusVariables() {
	}

	/**
	 * Retrieve the single instance of this class.
	 * 
	 * @return the singleton instance
	 */
	public static ServerStatusVariables getInstance() {
		if (instance == null) {
			instance = new ServerStatusVariables();
		}
		return instance;
	}
    
    /** PV for existence of Runcontrol IOC */
    private static final String RUNCTRL_PV = "CS:RC:INRANGE";
    /** PV for existence of Blockserver */
    private static final String BLOCKSRV_PV = "CS:BLOCKSERVER:SERVER_STATUS";
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
    /** PV for status of Archive INSTETC IOC */
    private static final String ARINST_PV = "CS:PS:ARINST:STATUS";
    /** PV for status of Archive Blockserver IOC */
    private static final String ARBLOCK_PV = "CS:PS:ARBLOCK:STATUS";
    /** PV on the block server to restart a protected server IOC by name */
    private static final String RESTART_SERVICE_IOC_PV = "CS:BLOCKSERVER:RESTART_SERVICE_IOC";
    
    private final ObservableFactory obsFactory = new ObservableFactory(OnInstrumentSwitch.SWITCH);
    private final WritableFactory writeFactory = new WritableFactory(OnInstrumentSwitch.SWITCH);

    private final ForwardingObservable<Boolean> runcontrolPV = 
            obsFactory.getSwitchableObservable(new BooleanChannel(), InstrumentUtils.addPrefix(RUNCTRL_PV));

    private final ForwardingObservable<Boolean> blockServerPV = 
            obsFactory.getSwitchableObservable(new BooleanChannel(), InstrumentUtils.addPrefix(BLOCKSRV_PV));

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

    private final ForwardingObservable<String> archiveInstPV = 
            obsFactory.getSwitchableObservable(new DefaultChannel(), InstrumentUtils.addPrefix(ARINST_PV));

    private final ForwardingObservable<String> archiveBlockserverPV = 
            obsFactory.getSwitchableObservable(new DefaultChannel(), InstrumentUtils.addPrefix(ARBLOCK_PV));

    private final Writable<String> restartServiceIoc = InstrumentUtils.convert(
            writeFactory.getSwitchableWritable(new CompressedCharWaveformChannel(),
                    InstrumentUtils.addPrefix(RESTART_SERVICE_IOC_PV)),
            ((Function<String, String[]>) name -> new String[] {name})
                    .andThen(new JsonSerialisingConverter<String[]>(String[].class)));

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

    /**
     * @return Observable for PV of Archive Inst IOC
     */
    public ForwardingObservable<String> getArchiveInstPV() {
		return archiveInstPV;
	}

    /**
     * @return Observable for PV of Archive Blockserver IOC
     */
    public ForwardingObservable<String> getArchiveBlockserverPV() {
		return archiveBlockserverPV;
	}

    /**
     * @return Writable that asks the block server to restart a service IOC by name
     */
    public Writable<String> getRestartServiceIoc() {
		return restartServiceIoc;
	}


}
