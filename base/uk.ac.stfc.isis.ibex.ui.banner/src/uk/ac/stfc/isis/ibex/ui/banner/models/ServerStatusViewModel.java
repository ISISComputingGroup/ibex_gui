package uk.ac.stfc.isis.ibex.ui.banner.models;

import java.util.ArrayList;
import java.util.List;

import uk.ac.stfc.isis.ibex.epics.observing.BaseObserver;
import uk.ac.stfc.isis.ibex.instrument.status.ServerStatusVariables;
import uk.ac.stfc.isis.ibex.model.ModelObject;

/**
 * Viewmodel for status of the IBEX server.
 */
public class ServerStatusViewModel extends ModelObject {

	private ServerStatus runControlStatus = ServerStatus.UNKNOWN;
	private ServerStatus blockServerStatus = ServerStatus.UNKNOWN;
	private ServerStatus blockGatewayStatus = ServerStatus.UNKNOWN;
	private ServerStatus isisDaeRunning = ServerStatus.UNKNOWN;
	private ServerStatus instetcStatus = ServerStatus.UNKNOWN;
	private ServerStatus dbServerStatus = ServerStatus.UNKNOWN;
	private ServerStatus psControlStatus = ServerStatus.UNKNOWN;
	private ServerStatus arAccessStatus = ServerStatus.UNKNOWN;
	private ServerStatus alarmServerStatus = ServerStatus.UNKNOWN;
	private ServerStatus overallStatus = ServerStatus.UNKNOWN;
	
	private List<ServerStatus> individualStatus = new ArrayList<ServerStatus>();
	
	public ServerStatusViewModel(ServerStatusVariables variables) {
		variables.getRuncontrolPV().subscribe(runControlStatusObserver);
		variables.getBlockServerPV().subscribe(blockServerStatusObserver);
		variables.getBlockGatewayPV().subscribe(blockGatewayStatusObserver);
		variables.getIsisDaePV().subscribe(isisDaeStatusObserver);
		variables.getInstetcPV().subscribe(instetcStatusObserver);
		variables.getDatabaseServerPV().subscribe(dbServerStatusObserver);
		variables.getProcservControlPV().subscribe(psControlStatusObserver);
		variables.getArchiverAccessPV().subscribe(arAccessStatusObserver);
		variables.getAlarmServerPV().subscribe(alarmServerStatusObserver);
		refreshOverallStatus();
	}
	
	private ServerStatus boolToStatus(boolean isConnected) {
		if (isConnected) {
			return ServerStatus.OK;
		} else {
			return ServerStatus.OFF;
		}
	}

	private final BaseObserver<Boolean> runControlStatusObserver = new BaseObserver<Boolean>() {
		@Override
		public void onConnectionStatus(boolean isConnected) {
			setRunControlStatus(boolToStatus(isConnected));
			refreshOverallStatus();
		}
	};

	private final BaseObserver<Boolean> blockServerStatusObserver = new BaseObserver<Boolean>() {
		@Override
		public void onConnectionStatus(boolean isConnected) {
			setBlockServerStatus(boolToStatus(isConnected));
			refreshOverallStatus();
		}
	};

	private final BaseObserver<Boolean> blockGatewayStatusObserver = new BaseObserver<Boolean>() {
		@Override
		public void onConnectionStatus(boolean isConnected) {
			setBlockGatewayStatus(boolToStatus(isConnected));
			refreshOverallStatus();
		}
	};

	private final BaseObserver<Boolean> isisDaeStatusObserver = new BaseObserver<Boolean>() {
		@Override
		public void onConnectionStatus(boolean isConnected) {
			setIsisDaeStatus(boolToStatus(isConnected));
			refreshOverallStatus();
		}
	};

	private final BaseObserver<Boolean> instetcStatusObserver = new BaseObserver<Boolean>() {
		@Override
		public void onConnectionStatus(boolean isConnected) {
			setInstetcStatus(boolToStatus(isConnected));
			refreshOverallStatus();
		}
	};

	private final BaseObserver<Boolean> dbServerStatusObserver = new BaseObserver<Boolean>() {
		@Override
		public void onConnectionStatus(boolean isConnected) {
			setDbServerStatus(boolToStatus(isConnected));
			refreshOverallStatus();
		}
	};

	private final BaseObserver<Boolean> psControlStatusObserver = new BaseObserver<Boolean>() {
		@Override
		public void onConnectionStatus(boolean isConnected) {
			setPsControlStatus(boolToStatus(isConnected));
			refreshOverallStatus();
		}
	};
	
	private final BaseObserver<String> arAccessStatusObserver = new BaseObserver<String>() {
		@Override
		public void onValue(String value) {
			if (value.equals("Shutdown")) {
				setArAccessStatus(ServerStatus.OFF);
			} else {
				setArAccessStatus(ServerStatus.OK);
			}
		}
		
		@Override
		public void onConnectionStatus(boolean isConnected) {
			if (psControlStatus == ServerStatus.OFF) {
				setArAccessStatus(ServerStatus.UNKNOWN);
			}
		}
	};
	
	private final BaseObserver<String> alarmServerStatusObserver = new BaseObserver<String>() {
		@Override
		public void onValue(String value) {
			if (value.equals("Shutdown")) {
				setAlarmServerStatus(ServerStatus.OFF);
			} else {
				setAlarmServerStatus(ServerStatus.OK);
			}
		}
		
		@Override
		public void onConnectionStatus(boolean isConnected) {
			if (psControlStatus == ServerStatus.OFF) {
				setAlarmServerStatus(ServerStatus.UNKNOWN);
			}
		}
	};
	
	public void setRunControlStatus(ServerStatus isRunning) {
        firePropertyChange("runControlStatus", this.runControlStatus, this.runControlStatus = isRunning);
		refreshOverallStatus();
	}

	public void setBlockServerStatus(ServerStatus isRunning) {
        firePropertyChange("blockServerStatus", this.blockServerStatus, this.blockServerStatus = isRunning);
		refreshOverallStatus();
	}

	public void setBlockGatewayStatus(ServerStatus isRunning) {
        firePropertyChange("blockGatewayStatus", this.blockGatewayStatus, this.blockGatewayStatus = isRunning);
		refreshOverallStatus();
	}

	public void setIsisDaeStatus(ServerStatus isRunning) {
        firePropertyChange("isisDaeStatus", this.isisDaeRunning, this.isisDaeRunning = isRunning);
		refreshOverallStatus();
	}

	public void setInstetcStatus(ServerStatus isRunning) {
        firePropertyChange("instetcStatus", this.instetcStatus, this.instetcStatus = isRunning);
		refreshOverallStatus();
	}

	public void setDbServerStatus(ServerStatus isRunning) {
        firePropertyChange("dbServerStatus", this.dbServerStatus, this.dbServerStatus = isRunning);
		refreshOverallStatus();
	}

	public void setPsControlStatus(ServerStatus isRunning) {
        firePropertyChange("psControlStatus", this.psControlStatus, this.psControlStatus = isRunning);
		refreshOverallStatus();
	}

	public void setArAccessStatus(ServerStatus isRunning) {
        firePropertyChange("arAccessStatus", this.arAccessStatus, this.arAccessStatus = isRunning);
		refreshOverallStatus();
	}

	public void setAlarmServerStatus(ServerStatus isRunning) {
        firePropertyChange("alarmServerStatus", this.alarmServerStatus, this.alarmServerStatus = isRunning);
		refreshOverallStatus();
	}
	
	public ServerStatus getRunControlStatus() {
		return runControlStatus;
	}

	public ServerStatus getBlockServerStatus() {
		return blockServerStatus;
	}

	public ServerStatus getBlockGatewayStatus() {
		return blockGatewayStatus;
	}

	public ServerStatus getIsisDaeStatus() {
		return runControlStatus;
	}

	public ServerStatus getInstetcStatus() {
		return instetcStatus;
	}

	public ServerStatus getDbServerStatus() {
		return dbServerStatus;
	}

	public ServerStatus getPsControlStatus() {
		return psControlStatus;
	}

	public ServerStatus getArAccessStatus() {
		return arAccessStatus;
	}

	public ServerStatus getAlarmServerStatus() {
		return alarmServerStatus;
	}

	public void refreshOverallStatus() {
		ServerStatus statusToSet = ServerStatus.UNSTABLE;
		
		individualStatus.clear();
		individualStatus.add(runControlStatus);
		individualStatus.add(blockServerStatus);
		individualStatus.add(blockGatewayStatus);
		individualStatus.add(isisDaeRunning);
		individualStatus.add(instetcStatus);
		individualStatus.add(dbServerStatus);
		individualStatus.add(psControlStatus);
		individualStatus.add(arAccessStatus);
		individualStatus.add(alarmServerStatus);
		if (individualStatus.stream().allMatch(t -> t.equals(ServerStatus.OK))) {
			statusToSet = ServerStatus.OK;
		} else if (individualStatus.stream().allMatch(t -> t.equals(ServerStatus.OFF) || t.equals(ServerStatus.UNKNOWN))) {
			statusToSet = ServerStatus.OFF;
		} else {
			statusToSet = ServerStatus.UNSTABLE;
		}
			
        firePropertyChange("overallStatus", this.overallStatus, this.overallStatus = statusToSet);
	}
	
	public ServerStatus getOverallStatus() {
		return overallStatus;
	}
}
