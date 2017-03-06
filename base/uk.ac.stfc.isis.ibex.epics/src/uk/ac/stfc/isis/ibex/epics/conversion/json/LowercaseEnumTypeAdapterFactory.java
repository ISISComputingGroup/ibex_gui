
/*
* This file is part of the ISIS IBEX application.
* Copyright (C) 2012-2015 Science & Technology Facilities Council.
* All rights reserved.
*
* This program is distributed in the hope that it will be useful.
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License v1.0 which accompanies this distribution.
* EXCEPT AS EXPRESSLY SET FORTH IN THE ECLIPSE PUBLIC LICENSE V1.0, THE PROGRAM 
* AND ACCOMPANYING MATERIALS ARE PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES 
* OR CONDITIONS OF ANY KIND.  See the Eclipse Public License v1.0 for more details.
*
* You should have received a copy of the Eclipse Public License v1.0
* along with this program; if not, you can obtain a copy from
* https://www.eclipse.org/org/documents/epl-v10.php or 
* http://opensource.org/licenses/eclipse-1.0.php
*/

package uk.ac.stfc.isis.ibex.epics.conversion.json;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

public class LowercaseEnumTypeAdapterFactory implements TypeAdapterFactory {
	
	public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
		@SuppressWarnings("unchecked")
		Class<T> rawType = (Class<T>) type.getRawType();
		if (!rawType.isEnum()) {
			return null;
		}

	    final Map<String, T> lowercaseToConstant = new HashMap<String, T>();
	    for (T constant : rawType.getEnumConstants()) {
	    	lowercaseToConstant.put(toLowercase(constant), constant);
	    }

	    return new TypeAdapter<T>() {
	    	public void write(JsonWriter out, T value) throws IOException {
	    		if (value == null) {
	    			out.nullValue();
	    		} else {
	    			out.value(toLowercase(value));
	    		}
	      }
	
	      public T read(JsonReader reader) throws IOException {
	    	  if (reader.peek() == JsonToken.NULL) {
	    		  reader.nextNull();
	    		  return null;
	    	  } else {
	    		  return lowercaseToConstant.get(reader.nextString());
	    	  }
	      }
	    };
	}

	private String toLowercase(Object o) {
		return o.toString().toLowerCase(Locale.UK);
	}
}
