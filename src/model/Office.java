package model;

import akka.actor.ActorRef;

/**
 * Created by coen on 31-10-2018.
 */
public class Office {

    private boolean isAvailable;
    private ActorRef customer;
    private int officeNr;
    private int locationNr;

    public Office(int locationNr, int officeNr) {
        this.officeNr = officeNr;
        this.locationNr = locationNr;

        isAvailable = true;
        customer = null;
    }

    public void setCustomer(ActorRef customer) {
        assert isAvailable = true;
        this.customer = customer;
        isAvailable = false;
    }

    public ActorRef getCustomer() {
        return customer;
    }

    public void clearOffice() {
        customer = null;
        isAvailable = true;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailability(boolean newAvailability) {
        isAvailable = newAvailability;
    }

    @Override
    public String toString() {
        return "Office " + officeNr + " on location " + locationNr;
    }
}
