package uk.ac.stfc.isis.ibex.ui.banner.models;

import java.util.ArrayList;
import java.util.List;

import uk.ac.stfc.isis.ibex.epics.observing.BaseObserver;
import uk.ac.stfc.isis.ibex.instrument.status.InstrumentStatusVariables;
import uk.ac.stfc.isis.ibex.model.ModelObject;

public class InstrumentStatusViewModel extends ModelObject {

	private boolean runControlRunning;
	private boolean blockServerRunning;
	private boolean blockGatewayRunning;
	private boolean isisDaeRunning;
	private boolean instetcRunning;
	private boolean dbServerRunning;
	private boolean psControlRunning;
	private boolean arAccessRunning;
	private boolean alarmServerRunning;
	private ServerStatus overallStatus;
	
	private List<Boolean> individualStatus = new ArrayList<Boolean>();
	
	public InstrumentStatusViewModel(InstrumentStatusVariables variables) {
		variables.runcontrolPV.subscribe(runControlStatusObserver);
		variables.blockServerPV.subscribe(blockServerStatusObserver);
		variables.blockGatewayPV.subscribe(blockGatewayStatusObserver);
		variables.isisDaePV.subscribe(isisDaeStatusObserver);
		variables.instetcPV.subscribe(instetcStatusObserver);
		variables.databaseServerPV.subscribe(dbServerStatusObserver);
		variables.procservControlPV.subscribe(psControlStatusObserver);
		variables.archiverAccessPV.subscribe(arAccessStatusObserver);
		variables.alarmServerPV.subscribe(alarmServerStatusObserver);
	}

	private final BaseObserver<Boolean> runControlStatusObserver = new BaseObserver<Boolean>() {
		@Override
		public void onConnectionStatus(boolean isConnected) {
			setRunControlRunning(isConnected);
			refreshOverallStatus();
		}
	};

	private final BaseObserver<Boolean> blockServerStatusObserver = new BaseObserver<Boolean>() {
		@Override
		public void onConnectionStatus(boolean isConnected) {
			setBlockServerRunning(isConnected);
			refreshOverallStatus();
		}
	};

	private final BaseObserver<Boolean> blockGatewayStatusObserver = new BaseObserver<Boolean>() {
		@Override
		public void onConnectionStatus(boolean isConnected) {
			setBlockGatewayRunning(isConnected);
			refreshOverallStatus();
		}
	};

	private final BaseObserver<Boolean> isisDaeStatusObserver = new BaseObserver<Boolean>() {
		@Override
		public void onConnectionStatus(boolean isConnected) {
			setIsisDaeRunning(isConnected);
			refreshOverallStatus();
		}
	};

	private final BaseObserver<Boolean> instetcStatusObserver = new BaseObserver<Boolean>() {
		@Override
		public void onConnectionStatus(boolean isConnected) {
			setInstetcRunning(isConnected);
			refreshOverallStatus();
		}
	};

	private final BaseObserver<Boolean> dbServerStatusObserver = new BaseObserver<Boolean>() {
		@Override
		public void onConnectionStatus(boolean isConnected) {
			setDbServerRunning(isConnected);
			refreshOverallStatus();
		}
	};

	private final BaseObserver<Boolean> psControlStatusObserver = new BaseObserver<Boolean>() {
		@Override
		public void onConnectionStatus(boolean isConnected) {
			setPsControlRunning(isConnected);
			refreshOverallStatus();
		}
	};
	
	private final BaseObserver<Boolean> arAccessStatusObserver = new BaseObserver<Boolean>() {
		@Override
		public void onConnectionStatus(boolean isConnected) {
			setArAccessRunning(isConnected);
			refreshOverallStatus();
		}
	};
	
	private final BaseObserver<Boolean> alarmServerStatusObserver = new BaseObserver<Boolean>() {
		@Override
		public void onConnectionStatus(boolean isConnected) {
			setAlarmServerRunning(isConnected);
			refreshOverallStatus();
		}
	};
	
	public void setRunControlRunning(boolean isRunning) {
        firePropertyChange("runControlRunning", this.runControlRunning, this.runControlRunning = isRunning);
		refreshOverallStatus();
	}

	public void setBlockServerRunning(boolean isRunning) {
        firePropertyChange("blockServerRunning", this.blockServerRunning, this.blockServerRunning = isRunning);
		refreshOverallStatus();
	}

	public void setBlockGatewayRunning(boolean isRunning) {
        firePropertyChange("blockGatewayRunning", this.blockGatewayRunning, this.blockGatewayRunning = isRunning);
		refreshOverallStatus();
	}

	public void setIsisDaeRunning(boolean isRunning) {
        firePropertyChange("isisDaeRunning", this.isisDaeRunning, this.isisDaeRunning = isRunning);
		refreshOverallStatus();
	}

	public void setInstetcRunning(boolean isRunning) {
        firePropertyChange("instetcRunning", this.instetcRunning, this.instetcRunning = isRunning);
		refreshOverallStatus();
	}

	public void setDbServerRunning(boolean isRunning) {
        firePropertyChange("dbServerRunning", this.dbServerRunning, this.dbServerRunning = isRunning);
		refreshOverallStatus();
	}

	public void setPsControlRunning(boolean isRunning) {
        firePropertyChange("psControlRunning", this.psControlRunning, this.psControlRunning = isRunning);
		refreshOverallStatus();
	}

	public void setArAccessRunning(boolean isRunning) {
        firePropertyChange("arAccessRunning", this.arAccessRunning, this.arAccessRunning = isRunning);
		refreshOverallStatus();
	}

	public void setAlarmServerRunning(boolean isRunning) {
        firePropertyChange("alarmServerRunning", this.alarmServerRunning, this.alarmServerRunning = isRunning);
		refreshOverallStatus();
	}
	
	public boolean getRunControlRunning() {
		return runControlRunning;
	}

	public boolean getBlockServerRunning() {
		return blockServerRunning;
	}

	public boolean getBlockGatewayRunning() {
		return blockGatewayRunning;
	}

	public boolean getIsisDaeRunning() {
		return runControlRunning;
	}

	public boolean getInstetcRunning() {
		return instetcRunning;
	}

	public boolean getDbServerRunning() {
		return dbServerRunning;
	}

	public boolean getPsControlRunning() {
		return psControlRunning;
	}

	public boolean getArAccessRunning() {
		return arAccessRunning;
	}

	public boolean getAlarmServerRunning() {
		return alarmServerRunning;
	}

	public void refreshOverallStatus() {
		ServerStatus statusToSet = ServerStatus.UNSTABLE;
		
		individualStatus.clear();
		individualStatus.add(runControlRunning);
		individualStatus.add(blockServerRunning);
		individualStatus.add(blockGatewayRunning);
		individualStatus.add(isisDaeRunning);
		individualStatus.add(instetcRunning);
		individualStatus.add(dbServerRunning);
		individualStatus.add(psControlRunning);
		individualStatus.add(arAccessRunning);
		individualStatus.add(alarmServerRunning);
		if (!individualStatus.contains(false)) {
			statusToSet = ServerStatus.OK;
		} else if (!individualStatus.contains(true)) {
			statusToSet = ServerStatus.OFF;
		}
			
        firePropertyChange("overallStatus", this.overallStatus, this.overallStatus = statusToSet);
	}
	
	public ServerStatus getOverallStatus() {
		return overallStatus;
	}
}
