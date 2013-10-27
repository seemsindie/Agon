package net.wachocki.agon.client;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.TrueTypeFont;

/**
 * User: Marty
 * Date: 9/17/13
 * Time: 12:48 AM
 */
public class Settings {

    private int scrollSpeed = 15;

    private int minimapSize = 175;

    private int normalFPS = 60;
    private int minimizedFPS = 30;
    private boolean vsync = false;

    private boolean mouseGrabbed = false;

    private int maxChatMessages = 8;
    private int chatIdleLimit = 10000;
    private Font chatFont;


    public void check(GameContainer gameContainer, AppGameContainer app) {
        if (!gameContainer.hasFocus() && app.getTargetFrameRate() != minimizedFPS) {
            app.setTargetFrameRate(getMinimizedFPS());
        } else if (gameContainer.hasFocus() && app.getTargetFrameRate() != normalFPS) {
            app.setTargetFrameRate(getNormalFPS());
        }
        if (vsync != gameContainer.isVSyncRequested()) {
            gameContainer.setVSync(isVSync());
        }
        if (mouseGrabbed != gameContainer.isMouseGrabbed()) {
            gameContainer.setMouseGrabbed(mouseGrabbed);
        }
        setChatFont(new TrueTypeFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 12), true));
    }

    public int getScrollSpeed() {
        return scrollSpeed;
    }

    public void setScrollSpeed(int scrollSpeed) {
        this.scrollSpeed = scrollSpeed;
    }

    public int getMinimapSize() {
        return minimapSize;
    }

    public void setMinimapSize(int minimapSize) {
        this.minimapSize = minimapSize;
    }

    public int getNormalFPS() {
        return normalFPS;
    }

    public void setNormalFPS(int normalFPS) {
        this.normalFPS = normalFPS;
    }

    public int getMinimizedFPS() {
        return minimizedFPS;
    }

    public void setMinimizedFPS(int minimizedFPS) {
        this.minimizedFPS = minimizedFPS;
    }

    public boolean isVSync() {
        return vsync;
    }

    public void setVSync(boolean vSync) {
        this.vsync = vSync;
    }

    public boolean isMouseGrabbed() {
        return mouseGrabbed;
    }

    public void setMouseGrabbed(boolean mouseGrabbed) {
        this.mouseGrabbed = mouseGrabbed;
    }

    public Font getChatFont() {
        return chatFont;
    }

    public void setChatFont(Font chatFont) {
        this.chatFont = chatFont;
    }

    public int getMaxChatMessages() {
        return maxChatMessages;
    }

    public void setMaxChatMessages(int maxChatMessages) {
        this.maxChatMessages = maxChatMessages;
    }

    public int getChatIdleLimit() {
        return chatIdleLimit;
    }

    public void setChatIdleLimit(int chatIdleLimit) {
        this.chatIdleLimit = chatIdleLimit;
    }
}
