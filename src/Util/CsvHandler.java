package Util;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;

import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;


public class CsvHandler {

    private final String filepath;

    public CsvHandler(String filepath) {
        this.filepath = filepath;
    }

    /**
     * retrieves data from csv
     * @param skipHeader boolean value to skip first column i.e. header
     * @return a list of string array, where each array represents a row in the csv file
     **/
    public List<String[]> retrieveCsvData(boolean skipHeader)  {
        List<String[]> data = new ArrayList<>();
        try (CSVReader csvReader = new CSVReader(new FileReader(Paths.get(filepath).toFile()))) {
            String[] row;

            while ((row = csvReader.readNext()) != null) {
                data.add(row);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Unable to locate the specified file: " + e.getMessage());
        } catch (IOException | CsvException e) {
            System.out.println("Error retrieving csv date: " + e.getMessage());
        }

        if (skipHeader) {
            return data.subList(1, data.size());
        } else {
            return data;
        }
    }

    /**
     * @param data    list of string array to write to the csv file
     * @param headers the headers for the csv file
     */

    public void writeDataToCsv(List<String[]> data, String[] headers) {
        try (CSVWriter csvWriter = new CSVWriter(new FileWriter(Paths.get(filepath).toFile()))) {
            csvWriter.writeNext(headers);
            csvWriter.writeAll(data);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to locate the specified file: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    /**
     * @return true if file exits otherwise false
     */

    public boolean fileExists() {
        File file = new File(filepath);
        return file.exists();
    }

    public String[] retrieveFieldNames() {
        try (CSVReader csvReader = new CSVReader(new FileReader(Paths.get(filepath).toFile()))) {
            return csvReader.readNext();
        } catch (CsvValidationException | IOException e) {
           throw new RuntimeException("Unable to retrieve file headers: " + e);
        }

    }

}

