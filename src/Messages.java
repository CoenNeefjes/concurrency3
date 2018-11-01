import akka.actor.ActorRef;
import model.Location;
import model.Office;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Coen Neefjes on 30-10-2018.
 */
public class Messages {

    static public class LocationListRequest {} //Customer requests all locations

    static public class LocationList {
        public List<Location> locations = new ArrayList<>();

        public LocationList(List<Location> locations) {
            this.locations = locations;
        }
    } //List of locations

    static public class OfficesOfLocationRequest {
        public Location location;

        public OfficesOfLocationRequest(Location location) {
            this.location = location;
        }
    } //Customer requests all offices on a location

    static public class OfficeListRequest {} //Customer requests all offices a rentalAgent knows

    static public class AvailableOfficeListRequest {
        public ActorRef cumstomer;

        public AvailableOfficeListRequest(ActorRef customer) {
            this.cumstomer = customer;
        }
    }

    static public class OfficeList {
        public List<Office> offices = new ArrayList<>();

        public OfficeList(List<Office> offices) {
            this.offices = offices;
        }
    } //List of offices

    static public class AvailableOfficeList {
        public List<Office> offices;
        public ActorRef customer;

        public AvailableOfficeList(List<Office> offices, ActorRef customer) {
            this.offices = offices;
            this.customer = customer;
        }
    }

    static public class GetOfficeOnLocationRequest {
        public Office office;
        public Location location;

        public GetOfficeOnLocationRequest(Office office, Location location) {
            this.office = office;
            this.location = location;
        }
    } //Customer want to reserve an office on a location

    static public class ReleaseOfficeOnLocation {
        public int officeNr;
        public int locationNr;

        public ReleaseOfficeOnLocation(int officeNr, int locationNr) {
            this.officeNr = officeNr;
            this.locationNr = locationNr;
        }
    } //Customer leaves an office on a location

    static public class DenyOffice {} //Customer decides he doesn't want the available office

    static public class WaitForOffice {} //Customer wants to wait until the requested office becomes available

    static public class OfficeAvailable {} //notify customer the requested office is available

    static public class OfficeNotAvailable {} //notify customer the requested office is not available

    static public class PaymentRequest {} //notify customer he has to pay



}
