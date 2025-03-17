CSC311 Weather Data Application - README
Overview:
The CSC311 Weather App is a command-line application designed to analyze and summarize weather data from a CSV file. The app reads weather data, processes it, and provides insights like average temperatures, temperature categories, rainy days, and other useful statistics.

This application uses Java Records, streams, and functional programming concepts to process and analyze the weather data.

Features:
Load Weather Data: Loads weather data from a CSV file into a list of WeatherData records.

Temperature Analysis: Calculates daily average temperatures, identifies days with temperatures above a threshold, and categorizes temperatures.

Rainy Day Count: Counts the number of rainy days in the data.

Weather Summary: Generates a formatted weather data summary including:

Total records

Temperature range (min and max)

Average temperature

Rainy days count

Temperature categories with their counts

Functional Programming: Utilizes Java Streams to filter, map, and group data efficiently.

Data Structure:
The app uses the WeatherData record, which represents a single weather data entry. It includes the following fields:

date: The date of the entry in the format YYYY-MM-DD

temp: The recorded temperature in Fahrenheit

humidity: The recorded humidity percentage

precipitation: The recorded precipitation in inches

Additionally, the WeatherData record has two methods:

isRainy(): Checks if the day had precipitation (rain).

getday(): Extracts the day part of the date in the format YYYY-MM.
