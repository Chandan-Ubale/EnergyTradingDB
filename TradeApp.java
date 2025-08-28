// Source code for TradeApp with Class.forName added
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class TradeApp {
  static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=EnergyTradingDB;encrypt=false;integratedSecurity=true;";

    static final String USER = ""; // If using SQL Auth, put username here
    static final String PASS = ""; // If using SQL Auth, put password here

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            // Load SQL Server JDBC Driver
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            // Establish connection
            Connection conn = DriverManager.getConnection(URL, USER, PASS);
            try {
                System.out.println("Connected to Database!");

                while (true) {
                    System.out.println("\n--- Trade Management Menu ---");
                    System.out.println("1. Add a Trade");
                    System.out.println("2. View All Trades");
                    System.out.println("3. Update Trade");
                    System.out.println("4. Delete Trade");
                    System.out.println("5. Search Trades by Counterparty/Commodity");
                    System.out.println("6. Exit");
                    System.out.print("Choose an option: ");

                    int choice = scanner.nextInt();
                    scanner.nextLine(); // consume newline

                    switch (choice) {
                        case 1:
                            addTrade(conn, scanner);
                            break;
                        case 2:
                            viewTrades(conn);
                            break;
                        case 3:
                            updateTrade(conn, scanner);
                            break;
                        case 4:
                            deleteTrade(conn, scanner);
                            break;
                        case 5:
                            searchTrades(conn, scanner);
                            break;
                        case 6:
                            System.out.println("Exiting...");
                            return;
                        default:
                            System.out.println("Invalid choice!");
                    }
                }
            } finally {
                if (conn != null) conn.close();
            }

        } catch (ClassNotFoundException e) {
            System.out.println("SQL Server JDBC Driver not found! Add sqljdbc jar to classpath.");
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void addTrade(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter TradeDate (YYYY-MM-DD): ");
        String date = scanner.nextLine();
        System.out.print("Enter Counterparty: ");
        String cp = scanner.nextLine();
        System.out.print("Enter Commodity: ");
        String cmd = scanner.nextLine();
        System.out.print("Enter Volume: ");
        double vol = scanner.nextDouble();
        System.out.print("Enter Price: ");
        double price = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Enter TradeType (BUY/SELL): ");
        String type = scanner.nextLine();

        String sql = "INSERT INTO Trades (TradeDate, Counterparty, Commodity, Volume, Price, TradeType) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, date);
        ps.setString(2, cp);
        ps.setString(3, cmd);
        ps.setDouble(4, vol);
        ps.setDouble(5, price);
        ps.setString(6, type);

        ps.executeUpdate();
        ps.close();
        System.out.println("Trade added successfully!");
    }

    private static void viewTrades(Connection conn) throws SQLException {
        String sql = "SELECT * FROM Trades";
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);

        System.out.println("\n--- Trades ---");
        while (rs.next()) {
            System.out.printf("ID:%d | Date:%s | CP:%s | Cmd:%s | Vol:%.2f | Price:%.2f | Type:%s%n",
                    rs.getInt("TradeID"),
                    rs.getDate("TradeDate"),
                    rs.getString("Counterparty"),
                    rs.getString("Commodity"),
                    rs.getDouble("Volume"),
                    rs.getDouble("Price"),
                    rs.getString("TradeType"));
        }
        rs.close();
        st.close();
    }

    private static void updateTrade(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter TradeID to update: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter new Price: ");
        double price = scanner.nextDouble();
        System.out.print("Enter new Volume: ");
        double vol = scanner.nextDouble();

        String sql = "UPDATE Trades SET Price=?, Volume=? WHERE TradeID=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setDouble(1, price);
        ps.setDouble(2, vol);
        ps.setInt(3, id);

        int rows = ps.executeUpdate();
        ps.close();

        if (rows > 0) System.out.println("Trade updated!");
        else System.out.println("Trade not found!");
    }

    private static void deleteTrade(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter TradeID to delete: ");
        int id = scanner.nextInt();

        String sql = "DELETE FROM Trades WHERE TradeID=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);

        int rows = ps.executeUpdate();
        ps.close();

        if (rows > 0) System.out.println("Trade deleted!");
        else System.out.println("Trade not found!");
    }

    private static void searchTrades(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Search by (1=Counterparty, 2=Commodity): ");
        int choice = scanner.nextInt();
        scanner.nextLine();
        String column = choice == 1 ? "Counterparty" : "Commodity";

        System.out.print("Enter value: ");
        String value = scanner.nextLine();

        String sql = "SELECT * FROM Trades WHERE " + column + "=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, value);
        ResultSet rs = ps.executeQuery();

        System.out.println("\n--- Search Results ---");
        while (rs.next()) {
            System.out.printf("ID:%d | Date:%s | CP:%s | Cmd:%s | Vol:%.2f | Price:%.2f | Type:%s%n",
                    rs.getInt("TradeID"),
                    rs.getDate("TradeDate"),
                    rs.getString("Counterparty"),
                    rs.getString("Commodity"),
                    rs.getDouble("Volume"),
                    rs.getDouble("Price"),
                    rs.getString("TradeType"));
        }

        rs.close();
        ps.close();
    }
}
