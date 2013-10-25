package net.wachocki.agon.client.entity;

import net.wachocki.agon.common.types.Specialization;

/**
 * User: Marty
 * Date: 10/24/13
 * Time: 2:49 AM
 */
public class Player extends Entity {

    private Specialization specialization = Specialization.ARCHER;

    public Player(String name) {
        super(name);
    }

    public Specialization getSpecialization() {
        return specialization;
    }

    public void setSpecialization(Specialization specialization) {
        this.specialization = specialization;
    }
}
