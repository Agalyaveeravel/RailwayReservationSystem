import java.sql.*;
import java.util.Scanner;

public class User {
    Scanner sc = new Scanner(System.in);

    public int loginOrRegister() {
        System.out.println("1. Login\n2. Register");
        int choice = sc.nextInt();
        if (choice == 1) return login();
        else return register();
    }

    public int register() {
        try (Connection con = DBConnection.getConnection()) {
            System.out.print("Name: ");
            String name = sc.next();
            System.out.print("Age: ");
            int age = sc.nextInt();
            System.out.print("Phone: ");
            String phone = sc.next();
            System.out.print("Gender: ");
            String gender = sc.next();
            System.out.print("Username: ");
            String username = sc.next();
            System.out.print("Password: ");
            String password = sc.next();

            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO users (name, age, phone, gender, username, password) VALUES (?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, name);
            ps.setInt(2, age);
            ps.setString(3, phone);
            ps.setString(4, gender);
            ps.setString(5, username);
            ps.setString(6, password);
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                System.out.println("Registered successfully!");
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int login() {
        try (Connection con = DBConnection.getConnection()) {
            System.out.print("Username: ");
            String user = sc.next();
            System.out.print("Password: ");
            String pass = sc.next();

            PreparedStatement ps = con.prepareStatement("SELECT * FROM users WHERE username=? AND password=?");
            ps.setString(1, user);
            ps.setString(2, pass);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                System.out.println("Login successful!");
                return rs.getInt("id");
            } else {
                System.out.println("Invalid credentials.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
}
