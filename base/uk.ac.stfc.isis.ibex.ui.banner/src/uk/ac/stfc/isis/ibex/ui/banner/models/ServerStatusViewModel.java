package uk.ac.stfc.isis.ibex.ui.banner.models;

import java.util.ArrayList;
import java.util.List;

import uk.ac.stfc.isis.ibex.epics.observing.BaseObserver;
import uk.ac.stfc.isis.ibex.instrument.status.ServerStatusVariables;
import uk.ac.stfc.isis.ibex.model.ModelObject;

/**
 * Viewmodel for status of the IBEX server and its individual components.
 */
public class ServerStatusViewModel extends ModelObject {

	private ServerStatus runControlStatus = ServerStatus.UNKNOWN;
	private ServerStatus blockServerStatus = ServerStatus.UNKNOWN;
	private ServerStatus blockGatewayStatus = ServerStatus.UNKNOWN;
	private ServerStatus isisDaeRunning = ServerStatus.UNKNOWN;
	private ServerStatus instetcStatus = ServerStatus.UNKNOWN;
	private ServerStatus dbServerStatus = ServerStatus.UNKNOWN;
	private ServerStatus psControlStatus = ServerStatus.UNKNOWN;
	private ServerStatus alarmServerStatus = ServerStatus.UNKNOWN;
	private ServerStatus overallStatus = ServerStatus.UNKNOWN;
	
	private List<ServerStatus> individualStatus = new ArrayList<ServerStatus>();
	
	/**
	 * The constructor.
	 * 
	 * @param variables The class holding PVs for accessing the status
	 */
	public ServerStatusViewModel(ServerStatusVariables variables) {
		variables.getRuncontrolPV().subscribe(runControlStatusObserver);
		variables.getBlockServerPV().subscribe(blockServerStatusObserver);
		variables.getBlockGatewayPV().subscribe(blockGatewayStatusObserver);
		variables.getIsisDaePV().subscribe(isisDaeStatusObserver);
		variables.getInstetcPV().subscribe(instetcStatusObserver);
		variables.getDatabaseServerPV().subscribe(dbServerStatusObserver);
		variables.getProcservControlPV().subscribe(psControlStatusObserver);
		variables.getAlarmServerPV().subscribe(alarmServerStatusObserver);
		refreshOverallStatus();
	}
	
	private ServerStatus boolToStatus(boolean isConnected) {
		if (isConnected) {
			return ServerStatus.RUNNING;
		} else {
			return ServerStatus.NOT_RUNNING;
		}
	}

	private final BaseObserver<Boolean> runControlStatusObserver = new BaseObserver<Boolean>() {
		@Override
		public void onConnectionStatus(boolean isConnected) {
			setRunControlStatus(boolToStatus(isConnected));
		}
	};

	private final BaseObserver<Boolean> blockServerStatusObserver = new BaseObserver<Boolean>() {
		@Override
		public void onConnectionStatus(boolean isConnected) {
			setBlockServerStatus(boolToStatus(isConnected));
		}
	};

	private final BaseObserver<Boolean> blockGatewayStatusObserver = new BaseObserver<Boolean>() {
		@Override
		public void onConnectionStatus(boolean isConnected) {
			setBlockGatewayStatus(boolToStatus(isConnected));
		}
	};
	
	
	private final BaseObserver<Boolean> isisDaeStatusObserver = new BaseObserver<Boolean>() {
		@Override
		public void onConnectionStatus(boolean isConnected) {
			setIsisDaeStatus(boolToStatus(isConnected));
		}
	};

	private final BaseObserver<Boolean> instetcStatusObserver = new BaseObserver<Boolean>() {
		@Override
		public void onConnectionStatus(boolean isConnected) {
			setInstetcStatus(boolToStatus(isConnected));
		}
	};

	private final BaseObserver<Boolean> dbServerStatusObserver = new BaseObserver<Boolean>() {
		@Override
		public void onConnectionStatus(boolean isConnected) {
			setDbServerStatus(boolToStatus(isConnected));
		}
	};

	private final BaseObserver<Boolean> psControlStatusObserver = new BaseObserver<Boolean>() {
		@Override
		public void onConnectionStatus(boolean isConnected) {
			setPsControlStatus(boolToStatus(isConnected));
		}
	};
	
	private final BaseObserver<String> alarmServerStatusObserver = new BaseObserver<String>() {
		@Override
		public void onValue(String value) {
			if (value.equals("Shutdown")) {
				setAlarmServerStatus(ServerStatus.NOT_RUNNING);
			} else if (value.equals("Running")) {
				setAlarmServerStatus(ServerStatus.RUNNING);
			}
		}
		
		@Override
		public void onConnectionStatus(boolean isConnected) {
			if (psControlStatus == ServerStatus.NOT_RUNNING) {
				setAlarmServerStatus(ServerStatus.UNKNOWN);
			}
		}
	};
	
	/**
	 * Set the run control status and alert listeners.
	 * 
	 * @param status The new status
	 */
	public void setRunControlStatus(ServerStatus status) {
        firePropertyChange("runControlStatus", this.runControlStatus, this.runControlStatus = status);
		refreshOverallStatus();
	}

	/**
	 * Set the block server status and alert listeners.
	 * 
	 * @param status The new status
	 */
	public void setBlockServerStatus(ServerStatus status) {
        firePropertyChange("blockServerStatus", this.blockServerStatus, this.blockServerStatus = status);
		refreshOverallStatus();
	}

	/**
	 * Set the block gateway status and alert listeners.
	 * 
	 * @param status The new status
	 */
	public void setBlockGatewayStatus(ServerStatus status) {
        firePropertyChange("blockGatewayStatus", this.blockGatewayStatus, this.blockGatewayStatus = status);
		refreshOverallStatus();
	}

	/**
	 * Set the ISIS DAE status and alert listeners.
	 * 
	 * @param status The new status
	 */
	public void setIsisDaeStatus(ServerStatus status) {
        firePropertyChange("isisDaeStatus", this.isisDaeRunning, this.isisDaeRunning = status);
		refreshOverallStatus();
	}

	/**
	 * Set the INSTETC status and alert listeners.
	 * 
	 * @param status The new status
	 */
	public void setInstetcStatus(ServerStatus status) {
        firePropertyChange("instetcStatus", this.instetcStatus, this.instetcStatus = status);
		refreshOverallStatus();
	}

	/**
	 * Set the database server status and alert listeners.
	 * 
	 * @param status The new status
	 */
	public void setDbServerStatus(ServerStatus status) {
        firePropertyChange("dbServerStatus", this.dbServerStatus, this.dbServerStatus = status);
		refreshOverallStatus();
	}

	/**
	 * Set the procserv control status and alert listeners.
	 * 
	 * @param status The new status
	 */
	public void setPsControlStatus(ServerStatus status) {
        firePropertyChange("psControlStatus", this.psControlStatus, this.psControlStatus = status);
		refreshOverallStatus();
	}

	/**
	 * Set the alarm server status and alert listeners.
	 * 
	 * @param status The new status
	 */
	public void setAlarmServerStatus(ServerStatus status) {
        firePropertyChange("alarmServerStatus", this.alarmServerStatus, this.alarmServerStatus = status);
		refreshOverallStatus();
	}
	
	/**
	 * @return The run control status
	 */
	public ServerStatus getRunControlStatus() {
		return runControlStatus;
	}

	/**
	 * @return The block server status
	 */
	public ServerStatus getBlockServerStatus() {
		return blockServerStatus;
	}

	/**
	 * @return The block gateway status
	 */
	public ServerStatus getBlockGatewayStatus() {
		return blockGatewayStatus;
	}

	/**
	 * @return The ISIS DAE status status
	 */
	public ServerStatus getIsisDaeStatus() {
		return runControlStatus;
	}

	/**
	 * @return The INSTETC status
	 */
	public ServerStatus getInstetcStatus() {
		return instetcStatus;
	}

	/**
	 * @return The database server status
	 */
	public ServerStatus getDbServerStatus() {
		return dbServerStatus;
	}

	/**
	 * @return The procserv control status
	 */
	public ServerStatus getPsControlStatus() {
		return psControlStatus;
	}

	/**
	 * @return The alarm server status
	 */
	public ServerStatus getAlarmServerStatus() {
		return alarmServerStatus;
	}

	/**
	 * Update the overall status of the IBEX server and notify listeners.
	 */
	public void refreshOverallStatus() {
		ServerStatus statusToSet = ServerStatus.PARTIAL;
		
		individualStatus.clear();
		individualStatus.add(runControlStatus);
		individualStatus.add(blockServerStatus);
		individualStatus.add(blockGatewayStatus);
		individualStatus.add(isisDaeRunning);
		individualStatus.add(instetcStatus);
		individualStatus.add(dbServerStatus);
		individualStatus.add(psControlStatus);
		individualStatus.add(alarmServerStatus);
		if (individualStatus.stream().allMatch(t -> t.equals(ServerStatus.RUNNING))) {
			statusToSet = ServerStatus.RUNNING;
		} else if (individualStatus.stream().allMatch(t -> t.equals(ServerStatus.NOT_RUNNING) || t.equals(ServerStatus.UNKNOWN))) {
			statusToSet = ServerStatus.NOT_RUNNING;
		} else {
			statusToSet = ServerStatus.PARTIAL;
		}
			
        firePropertyChange("overallStatus", this.overallStatus, this.overallStatus = statusToSet);
	}

	/**
	 * @return The overall server status
	 */
	public ServerStatus getOverallStatus() {
		return overallStatus;
	}
}
