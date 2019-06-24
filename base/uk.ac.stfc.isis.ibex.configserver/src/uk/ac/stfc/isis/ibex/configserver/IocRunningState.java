package uk.ac.stfc.isis.ibex.configserver;

import com.google.gson.annotations.SerializedName;

public enum IocRunningState {
	@SerializedName("STOPPED")
    STOPPED,
    @SerializedName("RUNNING_LOCALLY")
    RUNNING_LOCALLY,
    @SerializedName("RUNNING_REMOTELY")
    RUNNING_REMOTELY;
}
