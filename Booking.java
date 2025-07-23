import java.sql.*;
import java.util.Scanner;

public class Booking {
    Scanner sc = new Scanner(System.in);

    public void bookTrain(int userId) {
        try (Connection con = DBConnection.getConnection()) {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM trains");
            System.out.println("Train ID | Name | From -> To | Time | Seats");
            while (rs.next()) {
                System.out.printf("%d | %s | %s -> %s | %s | %d\n",
                        rs.getInt("id"),
                        rs.getString("train_name"),
                        rs.getString("origin"),
                        rs.getString("destination"),
                        rs.getString("departure_time"),
                        rs.getInt("available_seats"));
            }

            System.out.print("Enter Train ID to book: ");
            int trainId = sc.nextInt();
            System.out.print("Enter seat type (Sleeper/AC): ");
            String seatType = sc.next();

            // Generate PNR
            String pnr = "PNR" + System.currentTimeMillis();

            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO bookings (user_id, train_id, seat_type, status, pnr_number) VALUES (?, ?, ?, 'Booked', ?)");
            ps.setInt(1, userId);
            ps.setInt(2, trainId);
            ps.setString(3, seatType);
            ps.setString(4, pnr);
            ps.executeUpdate();

            PreparedStatement update = con.prepareStatement("UPDATE trains SET available_seats = available_seats - 1 WHERE id=?");
            update.setInt(1, trainId);
            update.executeUpdate();

            System.out.println("Booking successful! Your PNR: " + pnr);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cancelBooking(String pnr, int userId) {
        try (Connection con = DBConnection.getConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM bookings WHERE pnr_number=? AND user_id=? AND status='Booked'");
            ps.setString(1, pnr);
            ps.setInt(2, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int trainId = rs.getInt("train_id");

                PreparedStatement cancel = con.prepareStatement("UPDATE bookings SET status='Cancelled' WHERE pnr_number=?");
                cancel.setString(1, pnr);
                cancel.executeUpdate();

                PreparedStatement updateTrain = con.prepareStatement("UPDATE trains SET available_seats = available_seats + 1 WHERE id=?");
                updateTrain.setInt(1, trainId);
                updateTrain.executeUpdate();

                System.out.println("Booking cancelled.");
            } else {
                System.out.println("Invalid or already cancelled PNR.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
