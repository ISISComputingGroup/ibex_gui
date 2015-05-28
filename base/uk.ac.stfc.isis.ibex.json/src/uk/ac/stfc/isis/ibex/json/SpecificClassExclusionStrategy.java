package uk.ac.stfc.isis.ibex.json;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

public class SpecificClassExclusionStrategy implements ExclusionStrategy {
    private final Class<?> excludedThisClass;

    public SpecificClassExclusionStrategy(Class<?> excludedThisClass) {
        this.excludedThisClass = excludedThisClass;
    }
    @Override
    public boolean shouldSkipClass(Class<?> clazz) {
        return excludedThisClass.equals(clazz);
   }

    @Override
   public boolean shouldSkipField(FieldAttributes f) {
        return excludedThisClass.equals(f.getDeclaredClass());
   }
}
