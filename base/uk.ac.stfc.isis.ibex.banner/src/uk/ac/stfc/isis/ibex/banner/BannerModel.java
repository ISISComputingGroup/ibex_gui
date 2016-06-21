package uk.ac.stfc.isis.ibex.banner;

import java.util.Collection;

import uk.ac.stfc.isis.ibex.configserver.Configurations;
import uk.ac.stfc.isis.ibex.configserver.configuration.BannerItem;
import uk.ac.stfc.isis.ibex.epics.observing.BaseObserver;

public class BannerModel {

	public BannerModel(){
		Configurations.getInstance().variables().bannerDescription.addObserver(descriptionAdapter);
	}
	

    private final BaseObserver<Collection<BannerItem>> descriptionAdapter = new BaseObserver<Collection<BannerItem>>() {

        @Override
        public void onValue(Collection<BannerItem> value) {
            System.out.println(value);
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
}
