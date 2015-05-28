package uk.ac.stfc.isis.ibex.dae.internal;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import uk.ac.stfc.isis.ibex.epics.observing.InitialiseOnSubscribeObservable;
import uk.ac.stfc.isis.ibex.epics.observing.TransformingObservable;

import com.google.common.base.Strings;
import com.google.gson.Gson;

public class FileList extends TransformingObservable<String, Collection<String>> {

	private final Gson gson = new Gson();
	
	public FileList(InitialiseOnSubscribeObservable<String> files) {
		setSource(files);
	}

	@Override
	protected Collection<String> transform(String value) {
		if (Strings.isNullOrEmpty(value) || Strings.isNullOrEmpty(value.trim())) {
			return Collections.emptyList();
		}
		
		return Arrays.asList(gson.fromJson(value, String[].class));
	}
}
