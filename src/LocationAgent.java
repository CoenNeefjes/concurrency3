import akka.actor.AbstractActor;
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

    @Override
    public AbstractActor.Receive createReceive() {
        return receiveBuilder()
                .match(Messages.AvailableOfficeListRequest.class, availableOfficeListRequest -> {
                    // Return the list of available offices on this location
                    getSender().tell(new Messages.AvailableOfficeList(getAvailableOffices(), availableOfficeListRequest.cumstomer), getSelf());
                })
                .build();
    }

    @Override
    public String toString() {
        return "LocationAgent " + locationAgentNr;
    }
}
