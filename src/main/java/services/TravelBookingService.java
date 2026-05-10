

public class TravelBookingService {

    public static class BookingResult {

        public final boolean success;
        public final String bookingReference;
        public final String message;

        public BookingResult(boolean success, String bookingReference, String message) {
            this.success = success;
            this.bookingReference = bookingReference;
            this.message = message;
        }
    }

    public static BookingResult bookFlight(String accountNumber, String route,
            String travelClass, String date,
            int passengers, double price) {
        if (price <= 0 || passengers <= 0) {
            return new BookingResult(false, null, "Invalid booking parameters");
        }
        String desc = "Flight booking: " + route + " (" + travelClass + ")";
        if (BalanceManager.withdraw(accountNumber, price, desc)) {
            String ref = "FL" + System.currentTimeMillis() % 1000000;
            return new BookingResult(true, ref, "Flight booked successfully!");
        }
        return new BookingResult(false, null, "Payment failed");
    }

    public static BookingResult bookBusTicket(String accountNumber, String route,
            String busType, String date,
            int seats, double price) {
        if (price <= 0 || seats <= 0) {
            return new BookingResult(false, null, "Invalid booking parameters");
        }
        String desc = "Bus booking: " + route + " (" + busType + ")";
        if (BalanceManager.withdraw(accountNumber, price, desc)) {
            String ref = "BUS" + System.currentTimeMillis() % 1000000;
            return new BookingResult(true, ref, "Ticket booked successfully!");
        }
        return new BookingResult(false, null, "Payment failed");
    }

    public static String[] getFlightRoutes() {
        return new String[]{
            "Addis Ababa - Dubai",
            "Addis Ababa - Nairobi",
            "Addis Ababa - London",
            "Addis Ababa - Frankfurt"
        };
    }

    public static String[] getBusRoutes() {
        return new String[]{
            "Addis Ababa - Bahir Dar",
            "Addis Ababa - Hawassa",
            "Addis Ababa - Dire Dawa",
            "Addis Ababa - Mekelle"
        };
    }
}
