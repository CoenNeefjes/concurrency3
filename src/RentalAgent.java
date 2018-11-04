import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import model.Office;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RentalAgent extends AbstractActor{

    public static Props props(int rentalAgentNr, List<ActorRef> locationAgents) {
        return Props.create(RentalAgent.class, locationAgents);
    }

    private int rentalAgentNr;
    private List<ActorRef> locationAgents;
    private Map<ActorRef, Office> reservations;

    public RentalAgent(int rentalAgentNr, List<ActorRef> locationAgents) {
        this.rentalAgentNr = rentalAgentNr;
        this.locationAgents = locationAgents;

        reservations = new HashMap<>();
    }

    @Override
    public AbstractActor.Receive createReceive() {
        return receiveBuilder()
                .match(Messages.LocationList.class, locationList -> { // Message from superFlex
//                    System.out.println(this.toString() + " redirects locationList");
                    // Redirect this message to the customer
                    locationList.customer.tell(locationList, getSelf());
                })
                .match(Messages.OfficeListRequest.class, officeListRequest -> { // Message from customer
//                    System.out.println(this.toString() + " redirects officeListRequest");
                    // Redirect this message to the locationAgent of the specified location
                    locationAgents.get(officeListRequest.location.getLocationNr()).tell(officeListRequest, getSelf());
                })
                .match(Messages.OfficeList.class, officeList -> { // Message from locationAgent
//                    System.out.println(this.toString() + " redirects officeList");
                    // Redirect this message to the customer
                    officeList.customer.tell(officeList, getSelf());
                })
                .match(Messages.GetOfficeOnLocationRequest.class, message -> {
                    // Save request locally
                    reservations.put(getSender(), message.office);
                    // Redirect message to locationAgent
                    locationAgents.get(message.location.getLocationNr()).tell(message, getSelf());
                })
                .match(Messages.LocationFull.class, locationFull -> {
                    assert reservations.containsKey(locationFull.customer);
                    // Redirect this message to the customer
                    locationFull.customer.tell(locationFull, getSelf());
                })
                .match(Messages.OfficeAvailable.class, officeAvailable -> {
                    assert reservations.containsKey(officeAvailable.customer);
                    // Redirect this message to the customer
                    officeAvailable.customer.tell(officeAvailable, getSelf());
                })
                .match(Messages.AcceptOffice.class, acceptOffice -> {
                    System.out.println(this.toString() + " received acceptOffice message");
                    //TODO: implement
                })
                .build();
    }

    @Override
    public String toString() {
        return "RentalAgent " + rentalAgentNr;
    }
}
