DATABASE PENYAMBUNG DATABASEHELPER.JAVA

### 1. Membuat database PBO (`PBO`)

```sql
CREATE DATABASE PBO;
```

### 2. Membuat table Pemesanan_Film (`Pemesanan_Film`)

```sql
CREATE TABLE Pemesanan_Film (
    ID_Reservasi INT(11) PRIMARY KEY AUTO_INCREMENT,
    Judul_Film VARCHAR(225),
    Jadwal_Tayang VARCHAR(225),
    Jumlah_Kursi INT(11),
    Total_Harga INT(11)
);
```

