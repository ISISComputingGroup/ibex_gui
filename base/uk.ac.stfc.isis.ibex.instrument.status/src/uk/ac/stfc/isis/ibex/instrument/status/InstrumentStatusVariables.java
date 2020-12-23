package uk.ac.stfc.isis.ibex.instrument.status;

import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
import uk.ac.stfc.isis.ibex.epics.switching.ObservableFactory;
import uk.ac.stfc.isis.ibex.epics.switching.OnInstrumentSwitch;
import uk.ac.stfc.isis.ibex.instrument.InstrumentUtils;
import uk.ac.stfc.isis.ibex.instrument.channels.BooleanChannel;

public class InstrumentStatusVariables {

    /** PV ending for server status. */
    private static final String RUNCTRL_PV = "CS:RC:INRANGE";
    private static final String BLOCKSVR_PV = "CS:BLOCKSERVER:SERVER_STATUS";
    private static final String BLOCKGW_PV = "CS:GATEWAY:BLOCKSERVER:pvtotal";
    private static final String ISISDAE_PV = "DAE:INSTNAME";
    private static final String INSTETC_PV = "CS:MANAGER";
    private static final String DBSVR_PV = "CS:BLOCKSERVER:IOCS";
    private static final String PSCTRL_PV = "CS:PS:PSCTRL_01:IOCNAME";
    private static final String ARACCESS_PV = "CS:PS:ARACCESS:STATUS";
    private static final String ALARMSVR_PV = "CS:PS:ALARM:STATUS";
    
    private final ObservableFactory obsFactory = new ObservableFactory(OnInstrumentSwitch.SWITCH);

    public InstrumentStatusVariables() {
    }
    
    public final ForwardingObservable<Boolean> runcontrolPV = 
            obsFactory.getSwitchableObservable(new BooleanChannel(), InstrumentUtils.addPrefix(RUNCTRL_PV));

    public final ForwardingObservable<Boolean> blockServerPV = 
            obsFactory.getSwitchableObservable(new BooleanChannel(), InstrumentUtils.addPrefix(BLOCKSVR_PV));
    
    public final ForwardingObservable<Boolean> blockGatewayPV = 
            obsFactory.getSwitchableObservable(new BooleanChannel(), InstrumentUtils.addPrefix(BLOCKGW_PV));
    
    public final ForwardingObservable<Boolean> isisDaePV = 
            obsFactory.getSwitchableObservable(new BooleanChannel(), InstrumentUtils.addPrefix(ISISDAE_PV));
    
    public final ForwardingObservable<Boolean> instetcPV = 
            obsFactory.getSwitchableObservable(new BooleanChannel(), InstrumentUtils.addPrefix(INSTETC_PV));
    
    public final ForwardingObservable<Boolean> databaseServerPV = 
            obsFactory.getSwitchableObservable(new BooleanChannel(), InstrumentUtils.addPrefix(DBSVR_PV));
    
    public final ForwardingObservable<Boolean> procservControlPV = 
            obsFactory.getSwitchableObservable(new BooleanChannel(), InstrumentUtils.addPrefix(PSCTRL_PV));
    
    public final ForwardingObservable<Boolean> archiverAccessPV = 
            obsFactory.getSwitchableObservable(new BooleanChannel(), InstrumentUtils.addPrefix(ARACCESS_PV));
    
    public final ForwardingObservable<Boolean> alarmServerPV = 
            obsFactory.getSwitchableObservable(new BooleanChannel(), InstrumentUtils.addPrefix(ALARMSVR_PV));

}
