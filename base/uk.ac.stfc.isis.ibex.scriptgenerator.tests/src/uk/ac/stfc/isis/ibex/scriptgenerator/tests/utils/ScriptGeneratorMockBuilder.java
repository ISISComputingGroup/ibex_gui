package uk.ac.stfc.isis.ibex.scriptgenerator.tests.utils;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.junit.AssumptionViolatedException;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import uk.ac.stfc.isis.ibex.nicos.NicosErrorState;
import uk.ac.stfc.isis.ibex.nicos.NicosModel;
import uk.ac.stfc.isis.ibex.nicos.ScriptStatus;
import uk.ac.stfc.isis.ibex.scriptgenerator.NoScriptDefinitionSelectedException;
import uk.ac.stfc.isis.ibex.scriptgenerator.ScriptGeneratorSingleton;
import uk.ac.stfc.isis.ibex.scriptgenerator.generation.InvalidParamsException;
import uk.ac.stfc.isis.ibex.scriptgenerator.generation.UnsupportedLanguageException;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ScriptGeneratorAction;

/**
 * A test utility class for mocking out the script generator model and nicos model.
 */
public class ScriptGeneratorMockBuilder {
	
	private ScriptGeneratorSingleton mockScriptGeneratorModel;
	private NicosModel nicosMock;
	private List<ScriptGeneratorAction> mockScriptGeneratorActions;
	private InvalidParamsException invalidParamsException = new InvalidParamsException("Invalid params");
	private UnsupportedLanguageException unsupportedLanguageException = new UnsupportedLanguageException("Unsupported language");
	private NoScriptDefinitionSelectedException noScriptDefSelectedException = new NoScriptDefinitionSelectedException("No script definition selected");
	
	/**
	 * Create the mock models and set them up.
	 */
	public ScriptGeneratorMockBuilder() {
		mockScriptGeneratorModel = mock(ScriptGeneratorSingleton.class);
		nicosMock = mock(NicosModel.class);
		arrangeNumberOfActions(3);
		setUpMockNicosModel();
	}
	
	private void setUpMockActions() {
		when(mockScriptGeneratorModel.getActions()).thenReturn(mockScriptGeneratorActions);
		Integer numberOfActions = mockScriptGeneratorActions.size();
		// Set up the actions
		for (int i = 0; i < numberOfActions ; i++) {
			when(mockScriptGeneratorModel.getAction(i)).thenReturn(Optional.of(mockScriptGeneratorActions.get(i)));
		}
		// Ensure action queries near expected range return empty
		for (int j = -1; j > -5; j--) {
			when(mockScriptGeneratorModel.getAction(j)).thenReturn(Optional.empty());
		}
		for (int k = numberOfActions; k < numberOfActions + 5; k++) {
			when(mockScriptGeneratorModel.getAction(k)).thenReturn(Optional.empty());
		}
	}
	
	private void setUpMockNicosModel() {
		when(nicosMock.getError()).thenReturn(NicosErrorState.NO_ERROR);
		when(nicosMock.getScriptStatus()).thenReturn(ScriptStatus.IDLE);
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
	
	/**
	 * Get the action at the given index or an empty option if it does not exist.
	 * 
	 * @param actionIndex
	 * @return
	 */
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
	
	/**
	 * For the actions currently arranged, ensure the refreshing them as a generated script in the model, returns an expected script ID.
	 * 
	 * @return The script IDs.
	 */
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
	
	/**
	 * For all the currently arranged actions, ensure that refreshing a generated script with them throws the given exception.
	 * @param e
	 */
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
	
	/**
	 * Arrange nicos model to be in error.
	 */
	public void arrangeNicosError() {
		when(nicosMock.getError()).thenReturn(NicosErrorState.FAILED_LOGIN);
	}
	
	/**
	 * Arrange nicos model to fail to send a script.
	 */
	public void arrangeNicosSendScriptFail() {
		Mockito.doAnswer(new Answer<Object>() {

			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				when(nicosMock.getError()).thenReturn(NicosErrorState.SCRIPT_SEND_FAIL);
				return null;
			}
			
		}).when(nicosMock).sendScript(any());
	}
	
	/**
	 * Arrange the given number of actions to be gettable from the script generator model, 
	 * including getting correct script IDs for them.
	 * 
	 * @param numberOfActions The number of actions to arrange.
	 */
	public void arrangeNumberOfActions(Integer numberOfActions) {
		mockScriptGeneratorActions = new ArrayList<>();
		for (int i = 0; i < numberOfActions; i++) {
			mockScriptGeneratorActions.add(new ScriptGeneratorAction(new HashMap<>()));
		}
		setUpMockActions();
		arrangeCorrectScriptId();
	}
	
	/**
	 * Arrange the script generator model such that when refreshing a generated script at the given index, then given exception is thrown.
	 * 
	 * @param actionIndex The action to throw the exception for.
	 * @param exceptionToThrow The exception to throw.
	 * @return The action the exception will be thrown for.
	 */
	public ScriptGeneratorAction arrangeExceptionToThrowForAction(Integer actionIndex, Class<? extends Throwable> exceptionToThrow) {
		ScriptGeneratorAction action = getMockScriptGeneratorAction(actionIndex).get();
		try {
			when(mockScriptGeneratorModel.refreshGeneratedScript(action)).thenThrow(exceptionToThrow);
			return action;
		} catch (InvalidParamsException | UnsupportedLanguageException | NoScriptDefinitionSelectedException e) {
			throw new AssumptionViolatedException("Assumed we could create mock and failed");
		}
	}

}
