package net.wachocki.agon.client.ui;

import net.wachocki.agon.client.spells.Spell;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;

import java.text.DecimalFormat;

/**
 * User: Marty
 * Date: 9/17/13
 * Time: 1:35 AM
 */
public class ActionBar {

    private Image image;
    private Spell[] spells;
    private boolean hidden;

    public ActionBar(Image image, Spell[] spells) {
        this.image = image;
        this.spells = spells;
    }

    public void render(GameContainer gameContainer) {
        int x = (gameContainer.getWidth() / 2) - (image.getWidth() / 2);
        int y = gameContainer.getHeight() - image.getHeight();
        image.draw(x, y);
        int xPos = 10;
        for (int i = 0; i < spells.length; i++) {
            Image image = spells[i].getImage().getScaledCopy(48, 48);
            if (i == 0) {
                image = image.getScaledCopy(0.70f);
            } else if (i >= 5) {
                image = image.getScaledCopy(0.80f);
            }
            int imageX = x + xPos;
            int imageY = y + ((this.image.getHeight() / 2) - (image.getHeight() / 2));
            image.draw(imageX, imageY);
            if (spells[i].getCooldownLeft() > 0) {
                gameContainer.getGraphics().setColor(new Color(0, 0, 0, 175));
                gameContainer.getGraphics().fillRect(imageX, imageY, image.getWidth(), image.getHeight());
                gameContainer.getGraphics().setColor(Color.white);
                if (spells[i].getCooldownLeft() < 10000) {
                    gameContainer.getGraphics().drawString(new DecimalFormat("0.0").format((double) spells[i].getCooldownLeft() / 1000), imageX, imageY + (image.getHeight() / 2));
                } else if (spells[i].getCooldownLeft() >= 10000 && spells[i].getCooldownLeft() <= 60000) {
                    gameContainer.getGraphics().drawString(String.valueOf((int) Math.ceil(spells[i].getCooldownLeft() / 1000)), imageX, imageY + (image.getHeight() / 2));
                } else if (spells[i].getCooldownLeft() > 60000) {
                    int mins = (int) Math.floor(spells[i].getCooldownLeft() / 1000 / 60);
                    gameContainer.getGraphics().drawString(mins + ":" + ((spells[i].getCooldownLeft() / 1000) - (mins * 60)), imageX, imageY + (image.getHeight() / 2));
                }
            }
            xPos += image.getWidth() + 10;
        }
    }

    public void toggle() {
        hidden = !hidden;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Spell[] getSpells() {
        return spells;
    }

    public void setSpells(Spell[] spells) {
        this.spells = spells;
    }
}
