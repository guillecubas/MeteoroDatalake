package org.example.project1;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.example.APIAccessor;
import org.example.WeatherData;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Timer;
import java.util.TimerTask;

public class WeatherDataProcessor {
    private static final String OUTPUT_DIRECTORY = "datalake";

    public static void main(String[] args) {
        // Create a timer to run the data processing task every hour
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                try {
                    APIAccessor api = new APIAccessor();
                    // Read the JSON data from the API
                    String data = api.getData();
                    // Parse the JSON data into an array of weather data objects
                    WeatherData[] weatherDataArray = new Gson().fromJson(data, WeatherData[].class);
                    // Filter the weather data array by lat and lon and save it to a JSON file
                    saveWeatherDataToFile(weatherDataArray);
                } catch (Exception e) {
                    System.err.println("Error processing weather data: " + e.getMessage());
                }
            }

            private void saveWeatherDataToFile(WeatherData[] weatherDataArray) throws IOException {
                // Create the output directory if it does not exist
                File outputDir = new File(OUTPUT_DIRECTORY);
                if (!outputDir.exists()) {
                    outputDir.mkdir();
                }

                LocalDateTime datetime = LocalDateTime.parse(weatherDataArray[0].getFint(), DateTimeFormatter.ISO_DATE_TIME);
                String filename = datetime.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

                StringBuilder data = new StringBuilder("[");
                // Filter the weather data by lat and lon and concatenate it to a string
                for (WeatherData weatherData : weatherDataArray) {
                    if (weatherData.getLat() >= 27.5
                            && weatherData.getLat() <= 28.4
                            && weatherData.getLon() >= -16
                            && weatherData.getLon() <= -15) {
                        LocalDateTime datetime2 = LocalDateTime.parse(weatherData.getFint(), DateTimeFormatter.ISO_DATE_TIME);
                        String filename2 = datetime2.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
                        if (!filename2.equals(filename)) {
                            data = new StringBuilder(data.substring(0, data.length() - 2) + "]");
                            String filepath = String.format("%s/%s.events", OUTPUT_DIRECTORY, filename);
                            writeDataToFile(filepath, data.toString());
                            filename = filename2;
                            data = new StringBuilder("[");
                        }
                        data.append(new GsonBuilder().setPrettyPrinting().create().toJson(weatherData)).append(",\n");

                    }
                }
                data = new StringBuilder(data.substring(0, data.length() - 2) + "]");
                String filepath = String.format("%s/%s.events", OUTPUT_DIRECTORY, filename);
                writeDataToFile(filepath, data.toString());
            }

            private void writeDataToFile(String filename, String data) throws IOException {
                try (FileWriter writer = new FileWriter(filename)) {
                    writer.write(data);
                    System.out.println("Data written to file: " + filename);
                }
            }
        }, 0, 60*60*1000); // Run every hour
    }
}
