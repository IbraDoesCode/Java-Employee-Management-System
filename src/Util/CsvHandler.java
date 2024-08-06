package Util;

import java.io.*;
import java.util.ArrayList;

import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;


public class CsvHandler {

    private final String filepath;

    public CsvHandler(String filepath) {
        this.filepath = filepath;
    }

    /**
     * retrieves data from csv
     * @return a list of string array, where each array represents a row in the csv file
     **/
    public List<String[]> retrieveCsvData()  {
        List<String[]> data = new ArrayList<>();
        try (CSVReader csvReader = new CSVReader(new FileReader(filepath))) {
            data = csvReader.readAll();
        } catch (FileNotFoundException e) {
            System.out.println("Unable to locate the specified file: " + e.getMessage());
        } catch (IOException | CsvException e) {
            System.out.println("Error retrieving csv date: " + e.getMessage());
        }
        return data;
    }

    /**
     * @param data    list of string array to write to the csv file
     */
    public void writeDataToCsv(List<String[]> data) {
        try (CSVWriter csvWriter = new CSVWriter(new FileWriter(filepath))) {
            csvWriter.writeAll(data);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to locate the specified file: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    /**
    * @param newRecord String Array representing a single row of data
     *
    * */
    public void appendDataToCsv(String[] newRecord) {
        try (CSVWriter csvWriter = new CSVWriter(new FileWriter(filepath, true))) {
            csvWriter.writeNext(newRecord);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to locate the specified file: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }
}

