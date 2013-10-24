package net.wachocki.agon.client.spells;

import org.newdawn.slick.Image;

/**
 * User: Marty
 * Date: 10/20/13
 * Time: 2:49 PM
 */
public class TogglableSpell extends Spell {

    private boolean activated;

    public TogglableSpell(String name, Image image, int[] cooldowns, int[] manaCosts) {
        super(name, image, cooldowns, manaCosts, false);
    }

    public boolean isActivated() {
        return activated;
    }

    public void toggle() {
        activated = !activated;
    }

}
