package com;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/PBO";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static void saveReservation(Reservation reservation) {
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            String sql = "INSERT INTO pemesanan_film (Judul_Film, Jadwal_Tayang, Jumlah_Kursi, Total_Harga) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, reservation.getMovieTitle());
            statement.setString(2, reservation.getSchedule());
            statement.setInt(3, reservation.getSeats());
            statement.setInt(4, reservation.getTotalPrice());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<String> getReservationIds() {
        List<String> reservationIds = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT ID_Reservasi FROM pemesanan_film")) {
            while (resultSet.next()) {
                reservationIds.add(String.valueOf(resultSet.getInt("ID_Reservasi")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservationIds;
    }

    public static Reservation getReservationDetails(String reservationId) {
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM pemesanan_film WHERE ID_Reservasi = ?")) {
            statement.setInt(1, Integer.parseInt(reservationId));
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Reservation(
                        resultSet.getString("Judul_Film"),
                        resultSet.getString("Jadwal_Tayang"),
                        resultSet.getInt("Jumlah_Kursi"),
                        resultSet.getInt("Total_Harga")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void cancelReservation(String reservationId) {
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement("DELETE FROM pemesanan_film WHERE ID_Reservasi = ?")) {
            statement.setInt(1, Integer.parseInt(reservationId));
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

