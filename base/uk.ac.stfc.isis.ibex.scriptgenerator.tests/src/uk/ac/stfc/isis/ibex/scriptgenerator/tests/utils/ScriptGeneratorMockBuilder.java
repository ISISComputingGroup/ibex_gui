package uk.ac.stfc.isis.ibex.scriptgenerator.tests.utils;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.AssumptionViolatedException;

import uk.ac.stfc.isis.ibex.nicos.NicosErrorState;
import uk.ac.stfc.isis.ibex.nicos.NicosModel;
import uk.ac.stfc.isis.ibex.scriptgenerator.NoScriptDefinitionSelectedException;
import uk.ac.stfc.isis.ibex.scriptgenerator.ScriptGeneratorSingleton;
import uk.ac.stfc.isis.ibex.scriptgenerator.generation.InvalidParamsException;
import uk.ac.stfc.isis.ibex.scriptgenerator.generation.UnsupportedLanguageException;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ScriptGeneratorAction;

public class ScriptGeneratorMockBuilder {
	
	private ScriptGeneratorSingleton mockScriptGeneratorModel;
	private NicosModel nicosMock;
	private List<ScriptGeneratorAction> mockScriptGeneratorActions;
	private InvalidParamsException invalidParamsException = new InvalidParamsException("Invalid params");
	private UnsupportedLanguageException unsupportedLanguageException = new UnsupportedLanguageException("Unsupported language");
	private NoScriptDefinitionSelectedException noScriptDefSelectedException = new NoScriptDefinitionSelectedException("No script definition selected");
	
	public ScriptGeneratorMockBuilder() {
		mockScriptGeneratorModel = mock(ScriptGeneratorSingleton.class);
		nicosMock = mock(NicosModel.class);
		mockScriptGeneratorActions = List.of(mock(ScriptGeneratorAction.class), mock(ScriptGeneratorAction.class), mock(ScriptGeneratorAction.class));
		setUpMockActions();
		setUpMockNicosModel();
	}
	
	private void setUpMockActions() {
		when(mockScriptGeneratorModel.getActions()).thenReturn(mockScriptGeneratorActions);
		Integer numberOfActions = mockScriptGeneratorActions.size();
		for (int i = 0; i < numberOfActions ; i++) {
			when(mockScriptGeneratorModel.getAction(i)).thenReturn(Optional.of(mockScriptGeneratorActions.get(i)));
		}
		for (int j = -1; j > -5; j--) {
			when(mockScriptGeneratorModel.getAction(j)).thenReturn(Optional.empty());
		}
		for (int k = numberOfActions + 1; k < numberOfActions + 5; k++) {
			when(mockScriptGeneratorModel.getAction(k)).thenReturn(Optional.empty());
		}
	}
	
	private void setUpMockNicosModel() {
		when(nicosMock.getError()).thenReturn(NicosErrorState.NO_ERROR);
	}
	
	public ScriptGeneratorSingleton getMockScriptGeneratorModel() {
		return mockScriptGeneratorModel;
	}
	
	public NicosModel getMockNicosModel() {
		return nicosMock;
	}
	
	public List<ScriptGeneratorAction> getMockScriptGeneratorActions() {
		return mockScriptGeneratorActions;
	}
	
	public Optional<ScriptGeneratorAction> getMockScriptGeneratorAction(Integer actionIndex) {
		if (actionIndex >= 0 && actionIndex < mockScriptGeneratorActions.size()) {
			return Optional.of(mockScriptGeneratorActions.get(actionIndex));
		} else {
			return Optional.empty();
		}
	}
	
	public InvalidParamsException getinvalidParamsException() {
		return invalidParamsException;
	}
	
	public UnsupportedLanguageException getUnsupportedLanguageException() {
		return unsupportedLanguageException;
	}
	
	public NoScriptDefinitionSelectedException getNoScriptDefSelectedException() {
		return noScriptDefSelectedException;
	}
	
	public List<Integer> arrangeCorrectScriptId() {
		try {
			List<Integer> scriptIds = new ArrayList<Integer>();
			Integer numberOfActions = mockScriptGeneratorActions.size();
			for (int i = 0; i < numberOfActions ; i++) {
				when(mockScriptGeneratorModel.refreshGeneratedScript(mockScriptGeneratorActions.get(i))).thenReturn(Optional.of(i));
				scriptIds.add(i);
			}
			return scriptIds;
		} catch (InvalidParamsException | UnsupportedLanguageException | NoScriptDefinitionSelectedException e) {
			throw new AssumptionViolatedException("Assumed we could create mock and failed");
		}
	}
	
	public void arrangeRefreshScriptThrows(Exception e) {
		try {
			Integer numberOfActions = mockScriptGeneratorActions.size();
			for (int i = 0; i < numberOfActions ; i++) {
				when(mockScriptGeneratorModel.refreshGeneratedScript(mockScriptGeneratorActions.get(i))).thenThrow(e);
			}
		} catch (InvalidParamsException | UnsupportedLanguageException | NoScriptDefinitionSelectedException e1) {
			throw new AssumptionViolatedException("Assumed we could create mock and failed");
		}
	}

}
