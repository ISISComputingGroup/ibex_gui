package uk.ac.stfc.isis.ibex.epics.tests.conversion.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import uk.ac.stfc.isis.ibex.epics.conversion.ConversionException;
import uk.ac.stfc.isis.ibex.epics.conversion.json.LowercaseEnumTypeAdapterFactory;

@RunWith(MockitoJUnitRunner.Strict.class)
public class LowercaseEnumTypeAdapterFactoryTest {
	
	public static enum TestEnum {
		IMPLICIT_ONE,
		IMPLICIT_TWO;
	}
	
	public static enum TestEnumWithSerializedName {
		@SerializedName("name1") EXPLICIT_ONE,
		@SerializedName("name2") EXPLICIT_TWO;
	}
	
	public static enum TestEnumMixed {
		MIXED_ONE,
		@SerializedName("explicit_name") MIXED_TWO;
	}
	
	@Test
    public void convert_enum() throws ConversionException {
	    Gson gson = new GsonBuilder()
				.registerTypeAdapterFactory(new LowercaseEnumTypeAdapterFactory())
				.create();
	    
	    assertEquals(TestEnum.IMPLICIT_ONE, gson.fromJson("\"implicit_one\"", TestEnum.class));
	    assertEquals(TestEnum.IMPLICIT_TWO, gson.fromJson("\"implicit_two\"", TestEnum.class));

	    assertEquals(TestEnumWithSerializedName.EXPLICIT_ONE, gson.fromJson("\"name1\"", TestEnumWithSerializedName.class));
	    assertEquals(TestEnumWithSerializedName.EXPLICIT_TWO, gson.fromJson("\"name2\"", TestEnumWithSerializedName.class));

	    assertEquals(TestEnumMixed.MIXED_ONE, gson.fromJson("\"mixed_one\"", TestEnumMixed.class));
	    assertEquals(TestEnumMixed.MIXED_TWO, gson.fromJson("\"explicit_name\"", TestEnumMixed.class));
    }
}
