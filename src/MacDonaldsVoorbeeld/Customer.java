package MacDonaldsVoorbeeld;

import akka.actor.AbstractActorWithTimers;
import akka.actor.ActorRef;
import akka.actor.Props;

import java.time.Duration;

/**
 * Created by coen on 12-10-2018.
 */
public class Customer extends AbstractActorWithTimers {

    public static Props props(int balance, String order, ActorRef cashier, int customerNr){
        return Props.create(Customer.class, balance, order, cashier, customerNr);
    }

    private String receivedItems = "";
    private int balance;
    private final String order;
    private final ActorRef cashier;
    private final int customerNr;

    private static final class PlaceOrder {}

    public Customer(int balance, String order, ActorRef cashier, int customerNr){
        this.balance = balance;
        this.order = order;
        this.cashier = cashier;
        this.customerNr = customerNr;
    }

    private void placeOrder(PlaceOrder message) {
        System.out.println("Customer" + customerNr + " sending order to cashier");
        cashier.tell(new Messages.SendOrder(this.order), getSelf());
    }

    @Override
    public void preStart() {
//        getTimers().startSingleTimer("Customer"+customerNr + " placeOrder", new PlaceOrder(), Duration.ofMillis((long)(Math.random()*5000)+500));
        getTimers().startSingleTimer("Customer"+customerNr + " placeOrder", new PlaceOrder(), Duration.ofMillis(0));

    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(PlaceOrder.class, this::placeOrder)
                .match(Messages.PaymentRequest.class, paymentRequest -> {
                    int newBalance = balance - paymentRequest.amount;
                    if (newBalance >= 0){
                        // Update balance
                        this.balance = newBalance;
                        // Send the money to the cashier
                        getSender().tell(new Messages.Payment(paymentRequest.amount), getSelf());
                    } else {
                        cashier.tell(new Messages.NoMoney(), cashier);
                    }
                })
                .match(Messages.OrderedItem.class, orderedItem -> {
                    System.out.println("Customer" + customerNr + " received item: " + orderedItem.item);
                    receivedItems += ", " + orderedItem.item;
                })
                .build();
    }

    @Override
    public String toString(){
        return "Customer" + customerNr;
    }
}
