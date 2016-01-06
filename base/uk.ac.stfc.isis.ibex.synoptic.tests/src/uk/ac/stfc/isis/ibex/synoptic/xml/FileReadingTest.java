
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

package uk.ac.stfc.isis.ibex.synoptic.xml;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Before;

public abstract class FileReadingTest {
	
	private String fileContent;
	
	@Before
	public void before() throws Throwable {
		loadFile();
	}
	
	protected abstract URL fileLocation() throws MalformedURLException;

	protected String fileContent() {
		return fileContent;
	}
	
	protected void loadFile() throws IOException {	
	    BufferedReader in = null;
	    InputStream inputStream = null;
	    URL url = fileLocation();
	    try {
		    inputStream = url.openConnection().getInputStream();
		    in = new BufferedReader(new InputStreamReader(inputStream));

		    String inputLine;
		    StringBuilder builder = new StringBuilder();
		    while ((inputLine = in.readLine()) != null) {
		    	builder.append(inputLine);
		    }
		    
		    fileContent = builder.toString();
	    }
	    finally {
		    if (in !=null ) {
		    	in.close(); 
		    }

		    if (inputStream != null) {
		    	inputStream.close();
		    }
	    }
	 
	}
}
