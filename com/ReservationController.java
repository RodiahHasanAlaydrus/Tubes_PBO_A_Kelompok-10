package com;

import java.util.List;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ReservationController {

    private Stage primaryStage;
    private static final String[] MOVIES = {"The Barbie Movie", "Wicked", "Moana 2", "Avengers: Endgame", "The Dark Knight"};
    private static final String[][] SCHEDULES = {
            {"10:00", "13:00", "17:00"},
            {"11:00", "14:00", "18:00"},
            {"09:00", "12:00", "16:00"},
            {"10:30", "15:00", "19:00"},
            {"11:30", "16:30", "20:30"}
    };

    public ReservationController(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void showMainMenu() {
        VBox mainLayout = new VBox(10); 
        mainLayout.setStyle("-fx-padding: 20px;");
    
        Button searchMoviesButton = new Button("Pencarian Film");
        Button buyTicketsButton = new Button("Booking Tiket");
        Button manageReservationsButton = new Button("Managemen Reservasi");
    
        searchMoviesButton.setOnAction(e -> {
            primaryStage.close();
            showSearchMoviesScreen();
        });
        buyTicketsButton.setOnAction(e -> {
            primaryStage.close();
            showBuyTicketsScreen();
        });
        manageReservationsButton.setOnAction(e -> {
            primaryStage.close();
            showManageReservationsScreen();
        });
    
        mainLayout.getChildren().addAll(searchMoviesButton, buyTicketsButton, manageReservationsButton);
    
        Scene scene = new Scene(mainLayout, 400, 400);
        scene.getStylesheets().add(getClass().getResource("./css/mainScreen.css").toExternalForm());
        mainLayout.setAlignment(javafx.geometry.Pos.CENTER);
        primaryStage.setTitle("Aplikasi Reservasi Bioskop Tixtastic");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }    

    private void showSearchMoviesScreen() {
        Stage searchStage = new Stage();
        VBox layout = new VBox(10);

        TextField movieTitleField = new TextField();
        Button searchButton = new Button("Cari");
        Label resultLabel = new Label();
        Button backButton = new Button("Kembali");

        VBox.setVgrow(resultLabel, javafx.scene.layout.Priority.ALWAYS);

        backButton.setOnAction(e -> {
            searchStage.close();
            primaryStage.show();
        });

        searchButton.setOnAction(e -> {
            String movieTitle = movieTitleField.getText();
            int movieIndex = -1;
            for (int i = 0; i < MOVIES.length; i++) {
                if (MOVIES[i].equalsIgnoreCase(movieTitle)) {
                    movieIndex = i;
                    break;
                }
            }
            if (movieIndex != -1) {
                String[] schedules = SCHEDULES[movieIndex];
                resultLabel.setText("Jadwal: " + String.join(", ", schedules));
            } else {
                resultLabel.setText("Film tidak sedia di Bioskop Tixtastic.");
            }
        });

        layout.getChildren().addAll(new Label("Masukkan Judul Film:"), movieTitleField, searchButton, resultLabel, backButton);

        Scene scene = new Scene(layout, 400, 400);
        layout.setAlignment(javafx.geometry.Pos.CENTER);
        searchStage.setTitle("Pencarian Film");
        searchStage.setScene(scene);
        searchStage.setResizable(false);
        searchStage.show();
    }

    private void showBuyTicketsScreen() {
        Stage buyStage = new Stage();
        VBox layout = new VBox(10);
    
        TextField movieTitleField = new TextField();
        ComboBox<String> scheduleBox = new ComboBox<>();
        TextField seatsField = new TextField();
        Button buyButton = new Button("Reservasi");
        Label resultLabel = new Label();
        Label discountLabel = new Label(); 
        Label ticketPriceLabel = new Label("Harga per tiket: 50,000 IDR");
        Button backButton = new Button("Kembali");
    
        VBox.setVgrow(resultLabel, javafx.scene.layout.Priority.ALWAYS);
        VBox.setVgrow(discountLabel, javafx.scene.layout.Priority.ALWAYS);
    
        backButton.setOnAction(e -> {
            buyStage.close();
            primaryStage.show();
        });
    
        movieTitleField.setPromptText("Masukkan Judul Film");
        seatsField.setPromptText("Masukkan Jumlah Kursi");
    
        movieTitleField.textProperty().addListener((observable, oldValue, newValue) -> {
            int movieIndex = -1;
            for (int i = 0; i < MOVIES.length; i++) {
                if (MOVIES[i].equalsIgnoreCase(newValue)) {
                    movieIndex = i;
                    break;
                }
            }
            if (movieIndex != -1) {
                scheduleBox.getItems().clear();
                scheduleBox.getItems().addAll(SCHEDULES[movieIndex]);
            } else {
                scheduleBox.getItems().clear();
            }
        });
    
        buyButton.setOnAction(e -> {
            String movieTitle = movieTitleField.getText();
            String schedule = scheduleBox.getValue();
            int seats;
            try {
                seats = Integer.parseInt(seatsField.getText());
            } catch (NumberFormatException ex) {
                resultLabel.setText("Jumlah kursi tidak valid.");
                return;
            }
    
            if (movieTitle != null && !movieTitle.isEmpty() && schedule != null) {
                int pricePerSeat = 50000;
                int totalPrice = seats * pricePerSeat;
                if (seats >= 10) {
                    totalPrice *= 0.95; 
                    discountLabel.setText("Diskon 5% diterapkan!");
                    discountLabel.setStyle("-fx-text-fill: #00FF00;");
                } else {
                    discountLabel.setText("");
                }
                Reservation reservation = new Reservation(movieTitle, schedule, seats, totalPrice);
                DatabaseHelper.saveReservation(reservation);
                resultLabel.setText("Booking berhasil! Total Harga: " + totalPrice + " IDR");
            } else {
                resultLabel.setText("Film atau Jadwal tidak valid.");
            }
        });
    
        layout.getChildren().addAll(
            new Label("Masukkan Judul Film:"), movieTitleField,
            new Label("Pilih Jadwal:"), scheduleBox,
            new Label("Masukkan Jumlah Kursi:"), seatsField,
            buyButton, resultLabel, discountLabel, ticketPriceLabel, backButton
        );
    
        Scene scene = new Scene(layout, 400, 400);
        scene.getStylesheets().add(getClass().getResource("./css/bookingTiket.css").toExternalForm());
        buyStage.setTitle("Booking Tiket");
        buyStage.setScene(scene);
        buyStage.setResizable(false);
        buyStage.show();
        layout.setAlignment(javafx.geometry.Pos.CENTER);
    }
    
    private void showManageReservationsScreen() {
        Stage manageStage = new Stage();
        VBox layout = new VBox(10); 
    
        ListView<String> reservationList = new ListView<>();
        List<String> reservationIds = DatabaseHelper.getReservationIds();
        
        for (String reservationId : reservationIds) {
            reservationList.getItems().add("Reservasi # " + reservationId);
        }
    
        Button viewDetailsButton = new Button("Lihat Detail");
        Button cancelButton = new Button("Batalkan Reservasi");
        Button backButton = new Button("Kembali");
    
        TextArea reservationDetailsArea = new TextArea();
        reservationDetailsArea.setEditable(false);
        reservationDetailsArea.setWrapText(true);
        reservationDetailsArea.setPrefHeight(150);
    
        viewDetailsButton.setOnAction(e -> {
            String selectedId = reservationList.getSelectionModel().getSelectedItem();
            if (selectedId != null) {
                String actualReservationId = selectedId.replace("Reservasi # ", "");
                Reservation reservation = DatabaseHelper.getReservationDetails(actualReservationId);
                if (reservation != null) {
                    String reservationInfo = "Reservasi # " + actualReservationId + "\n" +
                                             "Film: " + reservation.getMovieTitle() + "\n" +
                                             "Jadwal: " + reservation.getSchedule() + "\n" +
                                             "Jumlah Kursi: " + reservation.getSeats() + "\n" +
                                             "Total Harga: " + reservation.getTotalPrice() + " IDR";
                    reservationDetailsArea.setText(reservationInfo);
                } else {
                    reservationDetailsArea.setText("Detail reservasi tidak ditemukan.");
                }
            }
        });
    
        cancelButton.setOnAction(e -> {
            String selectedId = reservationList.getSelectionModel().getSelectedItem();
            if (selectedId != null) {
                String actualReservationId = selectedId.replace("Reservasi # ", "");
                DatabaseHelper.cancelReservation(actualReservationId);
                reservationList.getItems().remove(selectedId);
                reservationDetailsArea.clear(); 
            }
        });
    
        backButton.setOnAction(e -> {
            manageStage.close();
            primaryStage.show();
        });
    
        layout.getChildren().addAll(
            new Label("Reservasi:"), reservationList, viewDetailsButton, cancelButton, reservationDetailsArea, backButton
        );
    
        Scene scene = new Scene(layout, 400, 400);
        manageStage.setTitle("Managemen Reservasi");
        manageStage.setScene(scene);
        manageStage.show();
        manageStage.setResizable(false);
        layout.setAlignment(javafx.geometry.Pos.CENTER);
    }
}
