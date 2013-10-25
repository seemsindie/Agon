package net.wachocki.agon.common.util;

/**
 * User: Marty
 * Date: 10/24/13
 * Time: 6:35 PM
 */
public class MathUtils {

    public static float lerp(float x0, float x1, float t) {
        return x0 + (x1 - x0) * t;
    }

}
