package uk.ac.stfc.isis.ibex.ui.dashboard.models;

import uk.ac.stfc.isis.ibex.epics.adapters.TextUpdatedObservableAdapter;
import uk.ac.stfc.isis.ibex.epics.adapters.UpdatedObservableAdapter;
import uk.ac.stfc.isis.ibex.epics.observing.InitialiseOnSubscribeObservable;
import uk.ac.stfc.isis.ibex.epics.pv.Closer;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;

public class TitlePanelModel extends Closer {
	
	private final UpdatedObservableAdapter<String> title;
	private final UpdatedObservableAdapter<String> users;
	
	
	public TitlePanelModel(InitialiseOnSubscribeObservable<String> title, InitialiseOnSubscribeObservable<String> users) {
		this.title = registerForClose(new TextUpdatedObservableAdapter(title));
		this.users = registerForClose(new TextUpdatedObservableAdapter(users));
	}

	public UpdatedValue<String> title() {
		return title;
	}

	public UpdatedValue<String> users() {
		return users;
	}
}
