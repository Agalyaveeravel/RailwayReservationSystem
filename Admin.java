import java.sql.*;
import java.util.Scanner;

public class Admin {
    Scanner sc = new Scanner(System.in);

    public boolean login() {
        System.out.print("Username: ");
        String user = sc.next();
        System.out.print("Password: ");
        String pass = sc.next();

        try (Connection con = DBConnection.getConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM admins WHERE username=? AND password=?");
            ps.setString(1, user);
            ps.setString(2, pass);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void viewTrains() {
        try (Connection con = DBConnection.getConnection()) {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM trains");
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
