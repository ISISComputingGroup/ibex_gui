package uk.ac.stfc.isis.ibex.journal;

import java.util.Calendar;
import java.util.Optional;

/**
 * Encapsulates journal search parameters.
 */
public class JournalParameters {
    public JournalField field = JournalField.RUN_NUMBER;
    public Optional<String> searchString = Optional.empty();
    public Optional<Integer> fromNumber = Optional.empty();
    public Optional<Integer> toNumber = Optional.empty();
    public Optional<Calendar> fromTime = Optional.empty();
    public Optional<Calendar> toTime = Optional.empty();
    
    /**
     * @return Returns true if any of the optional parameters are not empty, false otherwise.
     */
    public boolean hasOptionalParameters() {
        return searchString.isPresent() || fromNumber.isPresent() || toNumber.isPresent() || fromTime.isPresent() || toTime.isPresent();
    }
}
