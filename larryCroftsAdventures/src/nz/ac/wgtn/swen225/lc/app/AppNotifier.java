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

    /**
     * Notify the App that the game has been won.
     */
    void onGameWin();
    /**
     * Notify the App that the game has been lost.
     */
    void onGameLose();
    /**
     * Notify the App that the chap has picked up a key.
     * @param keyCount
     */
    void onKeyPickup(int keyCount);
    /**
     * Notify the App that the chap has picked up a treasure.
     * @param treasureCount the number of treasures picked up so far
     *
     * Note: treasureCount is not used in the current version of the game.
     * The parameter was prepared to be flexible.
     * Making it without the parameter make sense to App module,
     * However it will require the change in other modules.
     */
    void onTreasurePickup(int treasureCount);

    /**
     * onPickup may need String version of the key/treasure
     * if we want to desplay image instead of number.
     */

}
