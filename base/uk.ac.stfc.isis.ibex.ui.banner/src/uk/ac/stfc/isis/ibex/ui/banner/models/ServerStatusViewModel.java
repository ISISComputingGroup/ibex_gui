package uk.ac.stfc.isis.ibex.ui.banner.models;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import uk.ac.stfc.isis.ibex.epics.observing.BaseObserver;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;
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
	private ServerStatus archiveInstetcStatus = ServerStatus.UNKNOWN;
	private ServerStatus archiveBlockserverStatus = ServerStatus.UNKNOWN;
	private ServerStatus overallStatus = ServerStatus.UNKNOWN;
	private boolean restartAvailable = false;
	private final Writable<String> restartProtectedIoc;
	
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
		variables.getArchiveInstetcPV().subscribe(archiveInstetcStatusObserver);
		variables.getArchiveBlockserverPV().subscribe(archiveBlockserverStatusObserver);
		restartProtectedIoc = variables.getProtectedRestartIoc();
		refreshOverallStatus();
	}
	
	private ServerStatus boolToStatus(boolean isConnected) {
		if (isConnected) {
			return ServerStatus.UP;
		} else {
			return ServerStatus.DOWN;
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
				setAlarmServerStatus(ServerStatus.DOWN);
			} else if (value.equals("Running")) {
				setAlarmServerStatus(ServerStatus.UP);
			}
		}
		
		@Override
		public void onConnectionStatus(boolean isConnected) {
			if (psControlStatus == ServerStatus.DOWN) {
				setAlarmServerStatus(ServerStatus.UNKNOWN);
			}
		}
	};
	
	private final BaseObserver<String> archiveInstetcStatusObserver = new BaseObserver<String>() {
		@Override
		public void onValue(String value) {
			if (value.equals("Shutdown")) {
				setArchiveInstetcStatus(ServerStatus.DOWN);
			} else if (value.equals("Running")) {
				setArchiveInstetcStatus(ServerStatus.UP);
			}
		}
		
		@Override
		public void onConnectionStatus(boolean isConnected) {
			if (!isConnected) {
				setArchiveInstetcStatus(ServerStatus.UNKNOWN);
			}
		}
	};
	
	private final BaseObserver<String> archiveBlockserverStatusObserver = new BaseObserver<String>() {
		@Override
		public void onValue(String value) {
			if (value.equals("Shutdown")) {
				setArchiveBlockserverStatus(ServerStatus.DOWN);
			} else if (value.equals("Running")) {
				setArchiveBlockserverStatus(ServerStatus.UP);
			}
		}
		
		@Override
		public void onConnectionStatus(boolean isConnected) {
			if (!isConnected) {
				setArchiveBlockserverStatus(ServerStatus.UNKNOWN);
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
		setRestartAvailable(status == ServerStatus.UP);
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
	 * Set the archive INSTETC status and alert listeners.
	 * 
	 * @param status The new status
	 */
	public void setArchiveInstetcStatus(ServerStatus status) {
		firePropertyChange("archiveInstetcStatus", this.archiveInstetcStatus, this.archiveInstetcStatus = status);
		refreshOverallStatus();
	}

	/**
	 * Set the archive blockserver status and alert listeners.
	 * 
	 * @param status The new status
	 */
	public void setArchiveBlockserverStatus(ServerStatus status) {
		firePropertyChange("archiveBlockserverStatus", this.archiveBlockserverStatus, this.archiveBlockserverStatus = status);
		refreshOverallStatus();
	}
	
	/**
	 * Set whether a restart can be requested from the block server and alert listeners.
	 * 
	 * @param available The new availability
	 */
	private void setRestartAvailable(boolean available) {
		firePropertyChange("restartAvailable", this.restartAvailable, this.restartAvailable = available);
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
		return isisDaeRunning;
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
	 * @return The archive INSTETC status
	 */
	public ServerStatus getArchiveInstetcStatus() {
		return archiveInstetcStatus;
	}

	/**
	 * @return The archive blockserver status
	 */
	public ServerStatus getArchiveBlockserverStatus() {
		return archiveBlockserverStatus;
	}

	/**
	 * @return True if a restart can be requested from the block server
	 */
	public boolean getRestartAvailable() {
		return restartAvailable;
	}

	/**
	 * Request the block server to restart the named protected server IOC.
	 * 
	 * @param iocName The logical name of the IOC to restart
	 * @throws IOException if the request could not be written to the block server
	 */
	public void restart(String iocName) throws IOException {
		restartProtectedIoc.write(iocName);
	}

	/**
	 * Update the overall status of the IBEX server and notify listeners.
	 */
	public void refreshOverallStatus() {
		ServerStatus statusToSet = ServerStatus.PARTIAL;
		
		final var individualStatus = List.of(
				runControlStatus, 
				blockServerStatus, 
				blockGatewayStatus, 
				isisDaeRunning, 
				instetcStatus, 
				dbServerStatus, 
				psControlStatus, 
				alarmServerStatus, 
				archiveInstetcStatus, 
				archiveBlockserverStatus);

		if (individualStatus.stream().allMatch(t -> Objects.equals(t, ServerStatus.UP))) {
			statusToSet = ServerStatus.UP;
		} else if (individualStatus.stream().allMatch(t -> Objects.equals(t, ServerStatus.DOWN) || Objects.equals(t, ServerStatus.UNKNOWN))) {
			statusToSet = ServerStatus.DOWN;
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
