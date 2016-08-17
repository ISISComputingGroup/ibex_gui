package uk.ac.stfc.isis.ibex.configserver.configuration;

/**
 * Holds the display information for a banner item state.
 */
public class BannerItemState {
	private String colour;
	private String message;
	
    /**
     * The colour of the banner item status message displayed in the UI.
     * 
     * @return the colour
     */
	public String colour() {
        return this.colour;
	}

    /**
     * The text of the banner item status message displayed in the UI.
     * 
     * @return the message
     */
	public String message() {
		return this.message;
	}
}
