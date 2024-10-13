package nz.ac.wgtn.swen225.lc.app;

/**
 * Domain need to be able to notify App certain events,
 * such as the game being won or lost.
 * when chap picked up the key/ treasure.
 *
 *
 *
 * @author Nagare Negishi
 * @studentID 300653779
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
