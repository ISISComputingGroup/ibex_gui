//package uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.controls;
//
//import org.eclipse.swt.graphics.Color;
//import org.eclipse.wb.swt.SWTResourceManager;
//
///**
// * A model to provide easy access to the listeners for the interaction with the
// * alarm system; chiefly the number of active alarms.
// */
//public final class AlarmButtonViewModel extends PerspectiveButtonViewModel {
//
//    private static final String ALARM = "Alarms";
//    private static final Color ALARM_COLOR = SWTResourceManager.getColor(250, 150, 150);
//    private static final int MAX_ALARMS = 100;
//
//    private int alarmCount = 0;
//    private final FlashingButton flash;
//    private boolean alarmSeen = false;
//
//    /**
//     * Instantiates a new alarm count view model.
//     *
//     * @param alarmCounter
//     *            the alarm counter model to listen to
//     * @param buttonLabel
//     *            the label to appear on the button
//     */
//    public AlarmButtonViewModel(final AlarmCounter alarmCounter, String buttonLabel) {
//        super(buttonLabel);
//
//        flash = new FlashingButton(this, ALARM_COLOR);
//        flash.setDefaultColour(DEFOCUSSED);
//
//        alarmCounter.addPropertyChangeListener("alarmCount", ignored -> {
//                alarmCount = alarmCounter.getCount();
//                if (!active) {
//                    alarmSeen = false;
//                }
//                update();
//                updateFlashing();
//        });
//        update();
//    }
//
//    /**
//     * Updates the model with the new alarm count.
//     *
//     */
//    protected void update() {
//        setText(ALARM + alarmCountAsText());
//        setColor(chooseColor());
//    }
//
//    /**
//     * Make sure that the button is flashing when required.
//     */
//    protected void updateFlashing() {
//        if (!this.active) {
//            // If there are alarms that the user hasn't seen yet.
//            if (getAlarmCount() != 0 && !alarmSeen) {
//                flash.start();
//            } else {
//                flash.stop();
//            }
//        }
//    }
//
//    private String alarmCountAsText() {
//        String countText;
//        if (alarmCount > 0) {
//            countText = " ("
//                    + (alarmCount > MAX_ALARMS ? Integer.toString(MAX_ALARMS) + "+" : Integer.toString(alarmCount))
//                    + ")";
//        } else {
//            countText = "";
//        }
//        return countText;
//    }
//
//    /**
//     * Choose a colour based on the state of the button.
//     * 
//     * @return colour
//     */
//    @Override
//    protected Color chooseColor() {
//        Color color;
//        if (alarmCount > 0) {
//            color = ALARM_COLOR;
//        } else if (active) {
//            color = ACTIVE;
//        } else if (inFocus) {
//            color = FOCUSSED;
//        } else {
//            color = DEFOCUSSED;
//        }
//        return color;
//    }
//
//    @Override
//    public void setFocus(boolean inFocus) {
//        super.setFocus(inFocus);
//        if (flash != null) {
//            if (inFocus) {
//                flash.stop();
//            } else {
//                updateFlashing();
//            }
//        }
//        update();
//    }
//
//    /**
//     * Get the number of alarms.
//     * 
//     * @return alarmCount
//     */
//    protected int getAlarmCount() {
//        return alarmCount;
//    }
//
//    /**
//     * Calls setActive in PerspectiveButtonViewModel and marks the alarms as
//     * seen.
//     * 
//     * @param newActive
//     *            this perspective now active
//     */
//    @Override
//    public void setActive(boolean newActive) {
//        super.setActive(newActive);
//        if (newActive) {
//            alarmSeen = true;
//        }
//    }
//}
