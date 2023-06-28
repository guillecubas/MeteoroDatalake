import com.google.gson.Gson;

import java.io.File;

public class DatamartHelper {
    public static File[] readFilesFromDirectory(String directoryPath) {
        File dir = new File(directoryPath);
        return dir.listFiles();
    }

    public static WeatherData[] parseJSONData(String jsonData) {
        Gson gson = new Gson();
        return gson.fromJson(jsonData, WeatherData[].class);
    }


    public static int findMaxTemperatureIndex(WeatherData[] meteorologicalData) {
        int maxIndex = 0;
        for (int i = 0; i < meteorologicalData.length - 1; i++) {
            if (meteorologicalData[i].getTa() > meteorologicalData[maxIndex].getTa()) {
                maxIndex = i;
            }
        }
        return maxIndex;
    }

    public static int findMinTemperatureIndex(WeatherData[] meteorologicalData) {
        int minIndex = 0;
        for (int i = 0; i < meteorologicalData.length - 1; i++) {
            if (meteorologicalData[i].getTa() < meteorologicalData[minIndex].getTa()) {
                minIndex = i;
            }
        }
        return minIndex;
    }



    public static void insertTemperatureData(SQLiteDBHelper db,WeatherData[] meteorologicalData, int indiceTmax, int indiceTmin) {
        db.insertData(
                "Tmax", "place, date, time, value, station",
                "'" + meteorologicalData[indiceTmax].getUbi()
                        + "', '" + meteorologicalData[indiceTmax].getFint().substring(0, 10) +
                        "', '" + meteorologicalData[indiceTmax].getFint().substring(11, 19) +
                        "', " + meteorologicalData[indiceTmax].getTa() +
                        ", '" + meteorologicalData[indiceTmax].getIdema() + "'");
        db.insertData(
                "Tmin", "place, date, time, value, station",
                "'" + meteorologicalData[indiceTmin].getUbi()
                        + "', '" + meteorologicalData[indiceTmin].getFint().substring(0, 10) +
                        "', '" + meteorologicalData[indiceTmin].getFint().substring(11, 19) +
                        "', " + meteorologicalData[indiceTmin].getTa() +
                        ", '" + meteorologicalData[indiceTmin].getIdema() + "'");

    }
}

