package uk.ac.stfc.isis.ibex.configserver.configuration;

import java.util.Collection;

/**
 * Class which holds banner customisation data.
 */
public class BannerCustom {
    /**
     * Instrument specific information displayed in the banner.
     */
    public Collection<BannerItem> items;
    /**
     * Instrument specific buttons in the banner.
     */
    public Collection<BannerButton> buttons;
}
