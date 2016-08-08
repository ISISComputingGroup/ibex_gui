package uk.ac.stfc.isis.ibex.banner;

import java.util.Collection;

//import com.google.common.base.Strings;

import uk.ac.stfc.isis.ibex.configserver.Configurations;
import uk.ac.stfc.isis.ibex.configserver.configuration.BannerItem;
import uk.ac.stfc.isis.ibex.epics.observing.BaseObserver;
import uk.ac.stfc.isis.ibex.model.ModelObject;

/**
 * Contains the overall model of the banner in the form of an up-to-date
 * collection of all banner items.
 */
public class BannerModel extends ModelObject {
	
	Collection<BannerItem> bannerItems;

    /**
     * Instantiates the object and starts observing the PV holding the banner's
     * information.
     */
	public BannerModel() {
		Configurations.getInstance().variables().bannerDescription.addObserver(descriptionAdapter);
    }

    private final BaseObserver<Collection<BannerItem>> descriptionAdapter = new BaseObserver<Collection<BannerItem>>() {

        @Override
        public void onValue(Collection<BannerItem> value) {
            setBannerItems(value);
            for (BannerItem item : value) {
            	item.createPVObservable();
            }
        }

        @Override
        public void onError(Exception e) {
            System.out.println("Error");
        }

        @Override
        public void onConnectionStatus(boolean isConnected) {
            if (!isConnected) {
                System.out.println("No description available");
            }
        }
    };
    
    /**
     * Fires a property change when the banner information changes (on
     * instrument switch/restart).
     * 
     * @param value
     */
    private void setBannerItems(Collection<BannerItem> value) {
        firePropertyChange("bannerItems", this.bannerItems, this.bannerItems = value);
    }
    
    /**
     * Returns a collection of all banner items.
     * 
     * @return the banner items.
     */
    public Collection<BannerItem> getBannerItems() {
    	return this.bannerItems;
    }
}
