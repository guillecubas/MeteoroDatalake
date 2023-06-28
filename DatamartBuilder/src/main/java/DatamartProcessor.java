import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;


public class DatamartProcessor {
    public void startDataProcessing() {
        Timer timer = new Timer();
        timer.schedule(new DatamartProcessingTask(), 0, 60 * 60 * 1000); // Run every hour
    }
    private class DatamartProcessingTask extends TimerTask{
        @Override
        public void run() {
            try {
                File[] files = DatamartHelper.readFilesFromDirectory("datalake");
                SQLiteDBHelper db = new SQLiteDBHelper();
                db.dropTable("Tmax");
                db.dropTable("Tmin");
                db.createTable("Tmax", "place TEXT, date DATE, time  TEXT, value REAL, station TEXT");
                db.createTable("Tmin", "place TEXT, date DATE, time  TEXT, value REAL, station TEXT");

                for (File file : files) {
                    String json = new String(java.nio.file.Files.readAllBytes(file.toPath()));
                    WeatherData[] meteorologicalData = DatamartHelper.parseJSONData(json);
                    int maxTemperatureIndex = DatamartHelper.findMaxTemperatureIndex(meteorologicalData);
                    int minTemperatureIndex = DatamartHelper.findMinTemperatureIndex(meteorologicalData);
                    DatamartHelper.insertTemperatureData(db, meteorologicalData, maxTemperatureIndex, minTemperatureIndex);
                }
                System.out.println("Datamart actualizado\n");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

