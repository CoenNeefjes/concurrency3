package MacDonaldsVoorbeeld;

import akka.actor.AbstractActorWithTimers;
import akka.actor.Props;

import java.time.Duration;

/**
 * Created by coen on 12-10-2018.
 */
public class Cashier extends AbstractActorWithTimers{

    public static Props props(){
        return Props.create(Cashier.class);
    }

    public Cashier(){

    }

    private void makeDrink(){
        // Wait for 500 milliseconds
        getTimers().startSingleTimer("Cashier makes drink", null, Duration.ofMillis(500));
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Messages.SendOrder.class, order -> {
                    // Received order
                    System.out.println("Cashier received order from " + getSender().toString());
                    // Send payment request to customer
                    getSender().tell(new Messages.PaymentRequest(6), getSelf());
                    // Send burger request to kitchen
                    // TODO: send burger request to burgerflipper
                })
                .match(Messages.NoMoney.class, noMoney -> {
                    // Do nothing since customer cant pay
                })
                .match(Messages.Payment.class, payment -> {
                    // payment received, so make drink for the customer
                    System.out.println("Received payment from " + getSender().toString());
                    makeDrink();
                    // Give the drink
                    getSender().tell(new Messages.OrderedItem("Drink"), getSelf());
                })
                .build();
    }

}
