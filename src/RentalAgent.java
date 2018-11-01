import akka.actor.AbstractActor;
import akka.actor.ActorRef;

/**
 * Created by Coen Neefjes on 30-10-2018.
 */
public class RentalAgent extends AbstractActor{

    private int rentalAgentNr;
    private int locationNr;
    private ActorRef locationAgent;

    public RentalAgent(int rentalAgentNr, int locationNr, ActorRef locationAgent) {
        this.rentalAgentNr = rentalAgentNr;
        this.locationNr = locationNr;
        this.locationAgent = locationAgent;
    }

    @Override
    public AbstractActor.Receive createReceive() {
        return receiveBuilder()
                .match(Messages.OfficeListRequest.class, officeListRequest -> {
                    // Get available offices from LocationAgent
                    locationAgent.tell(new Messages.AvailableOfficeListRequest(getSender()), getSelf());
                })
                .match(Messages.AvailableOfficeList.class, availableOfficeList -> {
                    // Redirect the available office list to the customer that requested it
                    availableOfficeList.customer.tell(availableOfficeList, getSelf());
                })
                .build();
    }

}
