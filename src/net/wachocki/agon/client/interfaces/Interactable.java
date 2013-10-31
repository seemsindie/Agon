package net.wachocki.agon.client.interfaces;

import net.wachocki.agon.client.GameClient;

/**
 * User: Marty
 * Date: 10/28/13
 * Time: 10:27 AM
 */
public interface Interactable {

    public String[] getActions();

    public void doAction(GameClient game, int action);

}
