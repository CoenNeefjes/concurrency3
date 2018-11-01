import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import model.Location;
import model.Office;

import java.util.List;
import java.util.Random;

public class Customer extends AbstractActor{

    public static Props props(int customerNr, ActorRef superFlex) {
        return Props.create(Customer.class, customerNr, superFlex);
    }

    private int customerNr;
    private ActorRef superFlex;
    private Location chosenLocation;
    private Office chosenOffice;

    public Customer (int customerNr, ActorRef superFlex) {
        this.customerNr = customerNr;
        this.superFlex = superFlex;
    }

    @Override
    public void preStart() throws Exception {
        // Ask for a list of all locations from superFlex
        superFlex.tell(new Messages.LocationListRequest(), getSelf());
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
                    assert chosenLocation == null && chosenOffice == null;
                    System.out.println(this.toString() + " received locationList");
                    // Choose location
                    chosenLocation = chooseLocation(locationList.locations);
                    // Tell rentalAgent we want an office on this location
                    getSender().tell(new Messages.OfficeListRequest(chosenLocation, getSelf()), getSelf());
                })
                .match(Messages.OfficeList.class, officeList -> {
                    assert chosenLocation != null && chosenOffice == null;
                    System.out.println(this.toString() + " received officeList");
                    // Choose office
                    chosenOffice = chooseOffice(officeList.offices);
                    // Tell the rentalAgent we want this office
                    getSender().tell(new Messages.GetOfficeOnLocationRequest(chosenOffice, chosenLocation, getSelf()), getSelf());
                })
                .match(Messages.LocationFull.class, locationFull -> {
                    assert chosenLocation != null && chosenOffice != null;
                    System.out.println(this.toString() + " received location full message");
                    //TODO: start over
                })
                .match(Messages.OfficeAvailable.class, officeAvailable -> {

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

    public int getCustomerNr() {
        return this.customerNr;
    }

}
