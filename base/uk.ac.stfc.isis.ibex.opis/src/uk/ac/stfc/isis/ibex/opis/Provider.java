package uk.ac.stfc.isis.ibex.opis;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.logging.log4j.Logger;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.FileLocator;

import uk.ac.stfc.isis.ibex.logger.IsisLog;

public class Provider {
	
	private static final Logger LOG = IsisLog.getLogger(Provider.class);

	public Collection<String> getOPIList() {
		Collection<String> relativeFilePaths = new ArrayList<String>();
		Path root = pathToFileResource("/resources/");
		Iterator<File> itr = FileUtils.iterateFiles(root.toFile(), new SuffixFileFilter(".opi"), TrueFileFilter.INSTANCE);
		
		while(itr.hasNext()) {
			Path path = new Path(itr.next().getAbsolutePath());
			relativeFilePaths.add(path.makeRelativeTo(root).toString());
		}
		
		return relativeFilePaths;
	}
	
	public Path pathFromName(String name) {
		return pathToFileResource("/resources/" + name);
	}
	
	private final Path pathToFileResource(String relativePath) {	
		Path path = null;
		try {
			URL url = getClass().getResource(relativePath);
			String filePath = FileLocator.resolve(url).getPath();			
			path = new Path(filePath);
		} catch (IOException e) {
			LOG.catching(e);
		}
		
		return path;
	}
}
