package net.wachocki.agon.client.spells;

import net.wachocki.agon.client.GameClient;
import net.wachocki.agon.client.entity.LivingEntity;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;

/**
 * User: Marty
 * Date: 10/19/13
 * Time: 7:39 PM
 */
public class Spell {

    public static final int SPELL_PASSIVE = 0;
    public static final int SPELL_Q = 1;
    public static final int SPELL_W = 2;
    public static final int SPELL_E = 3;
    public static final int SPELL_R = 4;
    public static final int SPELL_D = 5;
    public static final int SPELL_F = 6;

    private String name;
    private Image image;
    private int[] cooldowns;
    private int cooldownLeft;
    private int[] manaCosts;
    private int level = 1;
    private boolean stopMovement;

    public Spell(String name, Image image, int[] cooldowns, int[] manaCosts, boolean stopMovement) {
        this.name = name;
        this.image = image;
        this.cooldowns = cooldowns;
        this.manaCosts = manaCosts;
        this.stopMovement = stopMovement;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    private int[] getCooldowns() {
        return cooldowns;
    }

    private int getCooldown(int level) {
        return cooldowns[level - 1];
    }

    private void setCooldown(int level, int cooldown) {
        this.cooldowns[level - 1] = cooldown;
    }

    public int[] getManaCosts() {
        return manaCosts;
    }

    public int getManaCost(int level) {
        return manaCosts[level - 1];
    }

    public boolean isStopMovement() {
        return stopMovement;
    }

    public int getCooldownLeft() {
        return cooldownLeft;
    }

    public void setCooldownLeft(int cooldownLeft) {
        this.cooldownLeft = cooldownLeft;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void cast(GameClient game, GameContainer gameContainer, LivingEntity target) {
        if(stopMovement) {
            //game.getPlayer().getWalkingQueue().clear();
        }
        this.cooldownLeft = cooldowns[level - 1];
    }

}
