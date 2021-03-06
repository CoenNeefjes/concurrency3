import akka.actor.AbstractActor;
import akka.actor.Props;
import model.Location;
import model.Office;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Coen Neefjes on 30-10-2018.
 *
 * Controls a locations
 */
public class LocationAgent extends AbstractActor{

    public static Props props(int locationAgentNr, Location location) {
        return Props.create(LocationAgent.class, locationAgentNr, location);
    }

    private int locationAgentNr;
    private Location location;

    public LocationAgent(int locationAgentNr, Location location) {
        this.locationAgentNr = locationAgentNr;
        this.location = location;
    }

    private List<Office> getAvailableOffices() {
        List<Office> availableOffices = new ArrayList<>();
        for (Office office : location.getOffices()) {
            if (office.isAvailable()) {
                availableOffices.add(office);
            }
        }
        return availableOffices;
    }

    public int getLocationNr() {
        return location.getLocationNr();
    }

    @Override
    public AbstractActor.Receive createReceive() {
        return receiveBuilder()
                .match(Messages.OfficeListRequest.class, officeListRequest -> {
                    System.out.println(this.toString() + " received officeListRequest");
                    // Return the list of offices on this location
                    getSender().tell(new Messages.OfficeList(location.getOffices(), officeListRequest.customer), getSelf());
                })
                .match(Messages.GetOfficeOnLocationRequest.class, message -> {
                    if (getAvailableOffices().isEmpty()) {
                        // The location has no available offices
                        getSender().tell(new Messages.LocationFull(message.customer),getSelf());
                    } else if (getAvailableOffices().contains(message.office)) {
                        // Office is available

                        // Set the office as reserved
                        List<Office> offices = location.getOffices();
                        int officeIndex = offices.indexOf(message.office);
                        offices.get(officeIndex).setReservedBy(message.customer);

                        // Tell the locationAgent the office is available
                        getSender().tell(new Messages.OfficeAvailable(message.customer), getSelf());
                    } else {
                        // Office isn't available

                        //TODO: implement this
                    }
                })
                .build();
    }

    @Override
    public String toString() {
        return "LocationAgent " + locationAgentNr;
    }
}
