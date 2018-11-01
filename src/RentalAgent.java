import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;

import java.util.List;

/**
 * Created by Coen Neefjes on 30-10-2018.
 */
public class RentalAgent extends AbstractActor{

    public static Props props(int rentalAgentNr, List<ActorRef> locationAgents) {
        return Props.create(RentalAgent.class, locationAgents);
    }

    private int rentalAgentNr;
    private List<ActorRef> locationAgents;

    public RentalAgent(int rentalAgentNr, List<ActorRef> locationAgents) {
        this.rentalAgentNr = rentalAgentNr;
        this.locationAgents = locationAgents;
    }

    @Override
    public AbstractActor.Receive createReceive() {
        return receiveBuilder()
                .match(Messages.LocationList.class, locationList -> { // Message from superFlex
                    System.out.println(this.toString() + " redirects locationlist");
                    // Redirect this message to the customer
                    locationList.customer.tell(locationList, getSelf());
                })
                .match(Messages.OfficeListRequest.class, officeListRequest -> { // Message from customer
                    System.out.println(this.toString() + " redirects officeListRequest");
                    // Redirect this message to the locationAgent of the specified location
                    locationAgents.get(officeListRequest.location.getLocationNr()).tell(officeListRequest, getSelf());
                })
                .match(Messages.OfficeList.class, officeList -> { // Message from locationAgent
                    System.out.println(this.toString() + " redirects officeList");
                    // Redirect this message to the customer
                    officeList.customer.tell(officeList, getSelf());
                })
                .match(Messages.GetOfficeOnLocationRequest.class, message -> {
                    // Ask for available offices so we can determine if the requested office is available
                    locationAgents.get(message.location.getLocationNr()).tell(new Messages.AvailableOfficeListRequest(message), getSelf());
                })
                .match(Messages.AvailableOfficeList.class, availableOfficeList -> {
                    // Extract the previous message
                    Messages.GetOfficeOnLocationRequest getOfficeOnLocationRequest = availableOfficeList.request;

                    if (availableOfficeList.offices.isEmpty()) {
                        // Notify the customer that the selected location has no free offices
                        getOfficeOnLocationRequest.customer.tell(new Messages.LocationFull(), getSelf());
                    } else if (availableOfficeList.offices.contains(getOfficeOnLocationRequest.office)) {
                        // Notify the customer that the selected office is available
                        getOfficeOnLocationRequest.customer.tell(new Messages.OfficeAvailable(), getSelf());
                    } else {
                        //TODO: send office not available message
                    }
                })
                .build();
    }

    @Override
    public String toString() {
        return "RentalAgent " + rentalAgentNr;
    }
}
