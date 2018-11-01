package MacDonaldsVoorbeeld;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

/**
 * Created by coen on 14-10-2018.
 */
public class World {

    public static void main(String[] args) {
        final ActorSystem system = ActorSystem.create("MacDonaldsWorld");

        final ActorRef cashier = system.actorOf(Cashier.props(), "Cashier");

        for (int i=0; i<5; i++){
            system.actorOf(Customer.props(10, "Drink, Burger", cashier, i), "Customer"+i);
        }
    }

}
