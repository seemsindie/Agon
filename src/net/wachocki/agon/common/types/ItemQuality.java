package net.wachocki.agon.common.types;

import org.newdawn.slick.Color;

/**
 * User: Marty
 * Date: 10/27/13
 * Time: 4:14 PM
 */
public enum  ItemQuality {
    COMMON(0, Color.white), UNCOMMON(1, Color.green), RARE(2, Color.blue), EPIC(3, Color.purple), LEGENDARY(4, Color.orange);

    public int value;
    public Color color;

    ItemQuality(int value, Color color) {
        this.value = value;
        this.color = color;
    }

}
