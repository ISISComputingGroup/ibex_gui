package uk.ac.stfc.isis.ibex.dae.tests;

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
