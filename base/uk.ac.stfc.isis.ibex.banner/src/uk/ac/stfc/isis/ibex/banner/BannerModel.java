package uk.ac.stfc.isis.ibex.banner;

import java.util.Collection;

import com.google.common.base.Strings;

import uk.ac.stfc.isis.ibex.configserver.Configurations;
import uk.ac.stfc.isis.ibex.configserver.configuration.BannerItem;
import uk.ac.stfc.isis.ibex.epics.observing.BaseObserver;
import uk.ac.stfc.isis.ibex.model.ModelObject;

public class BannerModel extends ModelObject {
	
	Collection<BannerItem> bannerItems;

	public BannerModel(){
		Configurations.getInstance().variables().bannerDescription.addObserver(descriptionAdapter);
	}
	

    private final BaseObserver<Collection<BannerItem>> descriptionAdapter = new BaseObserver<Collection<BannerItem>>() {

        @Override
        public void onValue(Collection<BannerItem> value) {
            setBannerItems(value);
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
    
    private void setBannerItems(Collection<BannerItem> value) {
        firePropertyChange("bannerItems", this.bannerItems, this.bannerItems = value);
    }
    
    public Collection<BannerItem> getBannerItems(){
    	return this.bannerItems;
    }
}
