package uk.ac.stfc.isis.ibex.ui.configserver.editing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.jface.viewers.ViewerCell;

import uk.ac.stfc.isis.ibex.ui.tables.DataboundCellLabelProvider;

public abstract class DecoratedCellLabelProvider<TRow> extends DataboundCellLabelProvider<TRow> {

	private final List<CellDecorator<TRow>> decorators = new ArrayList<>();
			
	public DecoratedCellLabelProvider(
			IObservableMap attributeMap, 
			Collection<? extends CellDecorator<TRow>> decorators) {
		super(attributeMap);
		
		this.decorators.addAll(decorators);
	}
	
    @Override
    public void update(ViewerCell cell) {
    	super.update(cell);
    	
    	for (CellDecorator<TRow> decorator : decorators) {
    		decorator.applyDecoration(cell);
    	}
    }
}
