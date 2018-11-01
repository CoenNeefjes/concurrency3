import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import model.Location;
import model.Office;

import java.util.List;
import java.util.Random;

/**
 * Created by Coen Neefjes on 30-10-2018.
 */
public class Customer extends AbstractActor{

    private int customerNr;
    private ActorRef rentalAgent;
    private Location chosenLocation;
    private Office chosenOffice;

    public Customer (int customerNr, ActorRef rentalAgent) {
        this.customerNr = customerNr;
        this.rentalAgent = rentalAgent;
    }

    @Override
    public void preStart() throws Exception {
        // Ask for a list of all locations
        rentalAgent.tell(new Messages.LocationListRequest(), getSelf());
    }

    @Override
    public void postStop() throws Exception {
        super.postStop();
    }

    @Override
    public String toString() {
        return "Customer " + customerNr;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Messages.LocationList.class, locationList -> {
                    // Choose one of the returned locations
                    chosenLocation = chooseLocation(locationList.locations);
                    // Aks for a list of offices on chosen location
                    rentalAgent.tell(new Messages.OfficesOfLocationRequest(chosenLocation), getSelf());
                })
                .match(Messages.OfficeList.class, officeList -> {
                    // Choose one of the returned offices
                    chosenOffice = chooseOffice(officeList.offices);
                    // Try to reserve te chosen office on the chosen location
                    rentalAgent.tell(new Messages.GetOfficeOnLocationRequest(chosenOffice, chosenLocation), getSelf());
                })
                .build();
    }

    private Location chooseLocation(List<Location> locations) {
        Random random = new Random();
        return locations.get(random.nextInt(locations.size()));
    }

    private Office chooseOffice(List<Office> offices) {
        Random random = new Random();
        return offices.get(random.nextInt(offices.size()));
    }

}
