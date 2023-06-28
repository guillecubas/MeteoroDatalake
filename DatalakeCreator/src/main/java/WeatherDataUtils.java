import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class WeatherDataUtils {

    private static final String outputDirectory = "datalake";
    private static double minLat = 27.5;
    private static double maxLat = 28.4;
    private static double minLon = -16;
    private static double maxLon = -15;

    void saveWeatherDataToFile(WeatherData[] weatherDataArray) throws IOException {
        createOutputDirectory();
        LocalDateTime datetime = LocalDateTime.parse(weatherDataArray[0].getFint(), DateTimeFormatter.ISO_DATE_TIME);
        String fileNameFirst = getFilename(datetime);
        StringBuilder jsonBuilder = new StringBuilder("[");
        for (WeatherData weatherData : weatherDataArray) {
            if (WeatherDataUtils.isWithinBounds(weatherData)) {
                LocalDateTime WeatherDataTime = LocalDateTime.parse(weatherData.getFint(), DateTimeFormatter.ISO_DATE_TIME);
                String fileNameNext = getFilename(WeatherDataTime);
                if (!fileNameNext.equals(fileNameFirst)) {
                    jsonBuilder = new StringBuilder(jsonBuilder.substring(0, jsonBuilder.length() - 2) + "]");
                    String filepath = String.format("%s/%s.events", outputDirectory, fileNameFirst);
                    writeDataToFile(filepath, jsonBuilder.toString());
                    fileNameFirst = fileNameNext;
                    jsonBuilder = new StringBuilder("[");
                }
                jsonBuilder.append(new GsonBuilder().setPrettyPrinting().create().toJson(weatherData)).append(",\n");
            }
        }
        jsonBuilder = new StringBuilder(jsonBuilder.substring(0, jsonBuilder.length() - 2) + "]");
        String filepath = String.format("%s/%s.events", outputDirectory, fileNameFirst);
        writeDataToFile(filepath, jsonBuilder.toString());
    }
    public static boolean isWithinBounds(WeatherData weatherData) {
        double latitude = weatherData.getLat();
        double longitude = weatherData.getLon();
        return latitude >= minLat && latitude <= maxLat &&
                longitude >= minLon && longitude <= maxLon;
    }

    private void createOutputDirectory() {
        File outputDir = new File(outputDirectory);
        if (!outputDir.exists()) {
            outputDir.mkdir();
        }
    }

    private String getFilename(LocalDateTime datetime) {
        return datetime.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }

    private void writeDataToFile(String filename, String data) throws IOException {
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write(data);
            System.out.println("Data written to file: " + filename);
        }
    }
}