package uk.ac.stfc.isis.ibex.epics.pv;

public class Closer implements Closable {

	private final ClosableList<Closable> resources = new ClosableList<>();
	
	protected <T extends Closable> T registerForClose(T toClose) {
		resources.add(toClose);
		return toClose;
	}
	
	@Override
	public void close() {
		resources.close();
	}

}
