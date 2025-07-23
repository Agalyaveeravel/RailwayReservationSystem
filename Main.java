import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Admin admin = new Admin();
        User user = new User();
        Booking booking = new Booking();

        while (true) {
            System.out.println("\n--- Railway Reservation System ---");
            System.out.println("1. Admin Login");
            System.out.println("2. User Login/Register");
            System.out.println("3. Exit");
            System.out.print("Choose option: ");
            int ch = sc.nextInt();

            if (ch == 1) {
                if (admin.login()) {
                    System.out.println("Admin Logged in!");
                    admin.viewTrains();
                } else {
                    System.out.println("Invalid admin credentials.");
                }

            } else if (ch == 2) {
                int userId = user.loginOrRegister();
                if (userId > 0) {
                    while (true) {
                        System.out.println("\n1. Book Train\n2. Cancel Booking\n3. Logout");
                        int op = sc.nextInt();
                        if (op == 1) booking.bookTrain(userId);
                        else if (op == 2) {
                            System.out.print("Enter PNR: ");
                            String pnr = sc.next();
                            booking.cancelBooking(pnr, userId);
                        } else break;
                    }
                }
            } else {
                System.out.println("Exiting...");
                break;
            }
        }

        sc.close();
    }
}
