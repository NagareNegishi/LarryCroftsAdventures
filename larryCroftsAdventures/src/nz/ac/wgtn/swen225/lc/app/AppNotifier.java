package nz.ac.wgtn.swen225.lc.app;

/**
 * Domain need to be able to notify the app of certain events,
 * such as the game being won or lost.
 * when chap picked up the key/ treasure.
 * This interface should be a feild in the model.
 * Do the model can use it to notify the app.
 */
public interface AppNotifier {

    void onGameWin();
    void onGameLose();
    void onKeyPickup(int keyCount);
    void onTreasurePickup(int treasureCount);

    /**
     * onPickup may need String version of the key/treasure
     * if we want to desplay image instead of number.
     */

}
