package MacDonaldsVoorbeeld;

/**
 * Created by coen on 14-10-2018.
 */
public class Messages {

    static public class PaymentRequest {
        public final int amount;

        public PaymentRequest(int amount) {
            this.amount = amount;
        }
    }

    static public class Payment {
        public final int amount;

        public Payment(int amount) { this.amount = amount; }
    }

    static public class OrderedItem {
        public final String item;

        public OrderedItem(String item){
            this.item = item;
        }
    }

    static public class NoMoney {
        public NoMoney(){
        }
    }

    static public class SendOrder {
        public String order;

        public SendOrder(String order){ this.order = order; }
    }

}
