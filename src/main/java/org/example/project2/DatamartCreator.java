package org.example.project2;
import com.google.gson.Gson;
import org.example.SQLiteDBHelper;
import org.example.WeatherData;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;





public class Main {
        public static void main(String[] args) throws IOException, SQLException {
        // Read the JSON data from  all files in datalake
        File dir = new File("datalake");
        File[] files = dir.listFiles();
        Gson gson = new Gson();
        SQLiteDBHelper db = new SQLiteDBHelper();
        // Borrar las tablas si existen
        db.dropTable("Tmax");
        db.dropTable("Tmin");
        // Crear una base de datos con las temperaturas minimas y maximas de cada dia
        db.createTable("Tmax", "place TEXT, date DATE, time  TEXT, value REAL, station TEXT");
        db.createTable("Tmin", "place TEXT, date DATE, time  TEXT, value REAL, station TEXT");

        assert files != null;

        for (File file : files) {
            // Read the JSON data from file
            String json = new String(java.nio.file.Files.readAllBytes(file.toPath()));
            // Parse the JSON data into an array of weather data objects
            WeatherData[] meteorologicos = gson.fromJson(json, WeatherData[].class);
            int indiceTmax = 0;
            int indiceTmin = 0;
            for (int i = 0; i < meteorologicos.length - 1; i++) {
                if (meteorologicos[i].getLat() >= 27.5
                        && meteorologicos[i].getLat() <= 28.4
                        && meteorologicos[i].getLon() >= -16
                        && meteorologicos[i].getLon() <= -15) {
                    // Encontrar el indice de la temperatura maxima y minima del dia
                    if (meteorologicos[i].getTa() > meteorologicos[indiceTmax].getTa()) {
                        indiceTmax = i;
                    }
                    if (meteorologicos[i].getTa() < meteorologicos[indiceTmin].getTa()) {
                        indiceTmin = i;
                    }
                }
            }

            db.insertData(
                    "Tmax", "place, date, time, value, station",
                    "'" + meteorologicos[indiceTmax].getUbi()
                            + "', '" + meteorologicos[indiceTmax].getFint().substring(0, 10) +
                            "', '" + meteorologicos[indiceTmax].getFint().substring(11, 19) +
                            "', " + meteorologicos[indiceTmax].getTa() +
                            ", '" + meteorologicos[indiceTmax].getIdema() + "'");
            db.insertData(
                    "Tmin", "place, date, time, value, station",
                    "'" + meteorologicos[indiceTmin].getUbi()
                            + "', '" + meteorologicos[indiceTmin].getFint().substring(0, 10) +
                            "', '" + meteorologicos[indiceTmin].getFint().substring(11, 19) +
                            "', " + meteorologicos[indiceTmin].getTa() +
                            ", '" + meteorologicos[indiceTmin].getIdema() + "'");



        }
    }
}
