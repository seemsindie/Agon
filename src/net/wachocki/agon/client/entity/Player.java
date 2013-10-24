package net.wachocki.agon.client.entity;

/**
 * User: Marty
 * Date: 10/24/13
 * Time: 2:49 AM
 */
public class Player extends Entity {

    public enum Specialization {
        ARCHER;
    }

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
