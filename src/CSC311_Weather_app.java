import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Represents a single weather data entry using Java Records.
 *
 * @param date The date of the entry in format YYYY-MM-DD
 * @param temp The recorded temperature in Fahrenheit
 * @param humidity The recorded humidity percentage
 * @param precipitation The recorded precipitation in inches
 */
record WeatherData(String date, double temp, double humidity, double precipitation) {
    /**
     * Checks if this day had rain
     * @return true if precipitation is greater than 0
     */
    boolean isRainy() {
        return precipitation > 0;
    }

    /**
     * Extracts the day part from the date string
     * @return The day in format YYYY-MM
     */
    String getday() {
        return date.substring(0, 7);
    }
}

/**
 * Weather Data Analyzer that uses functional programming concepts
 * to analyze weather data from CSV files.
 */
public class CSC311_Weather_app {

    /**
     * Loads weather data from a CSV file
     *
     * @param filePath path to the CSV file
     * @return List of WeatherData records
     */
    static List<WeatherData> loadData(String filePath) {
        try (var lines = Files.lines(Path.of(filePath))) {
            return lines.skip(1) // Skip header line
                    .map(line -> line.split(","))
                    .filter(parts -> parts.length >= 4) // Basic validation
                    .map(parts -> new WeatherData(
                            parts[0],
                            Double.parseDouble(parts[1]),
                            Double.parseDouble(parts[2]),
                            Double.parseDouble(parts[3])))
                    .toList();
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            return List.of(); // Return empty list instead of null
        }
    }

    /**
     * Calculates the average temperature for a specific day
     *
     * @param data List of weather data
     * @param day day in format YYYY-MM
     * @return Average temperature or NaN if no data
     */
    static double getdaylyAverageTemperature(List<WeatherData> data, String day) {
        return data.stream()
                .filter(d -> d.getday().equals(day))
                .mapToDouble(WeatherData::temp)
                .average()
                .orElse(Double.NaN);
    }

    /**
     * Finds days with temperature above the specified threshold
     *
     * @param data List of weather data
     * @param threshold Temperature threshold in Fahrenheit
     * @return List of dates with temperatures above threshold
     */
    static List<String> getDaysAboveTemperature(List<WeatherData> data, double threshold) {
        return data.stream()
                .filter(d -> d.temp() > threshold)
                .map(WeatherData::date)
                .toList();
    }

    /**
     * Counts the number of rainy days in the data
     *
     * @param data List of weather data
     * @return Count of rainy days
     */
    static long getRainyDaysCount(List<WeatherData> data) {
        return data.stream()
                .filter(WeatherData::isRainy)
                .count();
    }

    /**
     * Categorizes temperature using enhanced switch expression
     *
     * @param temp Temperature in Fahrenheit
     * @return Category as string
     */
    static String categorizeTemperature(double temp) {
        return switch ((int) Math.floor(temp / 20)) {
            case int t when t < 0 -> "Freezing";
            case 0 -> "Very Cold";
            case 1 -> "Cold";
            case 2, 3 -> "Mild";
            case 4 -> "Warm";
            case int t when t >= 5 -> "Hot";
            default -> "Unknown";
        };
    }

    /**
     * Groups data by temperature category and counts occurrences
     *
     * @param data List of weather data
     * @return Map of temperature categories to their counts
     */
    static Map<String, Long> getTemperatureCategoryCounts(List<WeatherData> data) {
        return data.stream()
                .collect(Collectors.groupingBy(
                        wd -> categorizeTemperature(wd.temp()),
                        Collectors.counting()
                ));
    }

    /**
     * Generates a simple summary of the weather data
     *
     * @param data List of weather data
     * @return Formatted summary string
     */
    static String generateSummary(List<WeatherData> data) {
        if (data.isEmpty()) {
            return "No weather data available.";
        }

        /** min and max temperatures */
        var tempStats = data.stream()
                .mapToDouble(WeatherData::temp)
                .summaryStatistics();


        var categoryCountsStr = getTemperatureCategoryCounts(data).entrySet().stream()
                .map(e -> e.getKey() + ": " + e.getValue() + " days")
                .collect(Collectors.joining("\n  ", "  ", ""));

        /**Use text block for nice formatting*/
        return """
               Weather Data Summary
               ===================
               
               Total Records: %d
               Temperature Range: %.1f°F to %.1f°F
               Average Temperature: %.1f°F
               Rainy Days: %d
               
               Temperature Categories:
               %s
               """.formatted(
                data.size(),
                tempStats.getMin(),
                tempStats.getMax(),
                tempStats.getAverage(),
                getRainyDaysCount(data),
                categoryCountsStr
        );
    }

    public static void main(String[] args) {
        /** Taking in weather data*/
        List<WeatherData> weatherData = loadData("src/weather_data.csv");

        if (weatherData.isEmpty()) {
            System.out.println("No data found. Please check the file path: src/weather.csv");
            return;
        }


        System.out.println(generateSummary(weatherData));


        System.out.println("\nAdditional Analysis:");
        System.out.println("-------------------");

        /**Get days above average*/
        var aboveAvgDays = getDaysAboveTemperature(weatherData, 29.0);
        System.out.println("Days above 29°F: " + aboveAvgDays.size());
        if (!aboveAvgDays.isEmpty()) {
            System.out.println("First 5 above average days: " +
                    aboveAvgDays.stream().limit(5).collect(Collectors.joining(", ")));
        }


    }
}