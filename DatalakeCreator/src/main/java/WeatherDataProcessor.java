import com.google.gson.Gson;
import java.util.Timer;
import java.util.TimerTask;

public class WeatherDataProcessor {

    public void startDataProcessing() {
        Timer timer = new Timer();
        timer.schedule(new WeatherDataProcessingTask(), 0, 60 * 60 * 1000); // Run every hour
    }
    private static class WeatherDataProcessingTask extends TimerTask {

        @Override
        public void run() {
            try {
                String data = APIAccessor.getData();
                WeatherData[] weatherDataArray = new Gson().fromJson(data, WeatherData[].class);
                WeatherDataUtils weatherDataUtils = new WeatherDataUtils();
                weatherDataUtils.saveWeatherDataToFile(weatherDataArray);
            } catch (Exception e) {
                System.err.println("Error processing weather data: " + e.getMessage());
            }
        }
    }
}