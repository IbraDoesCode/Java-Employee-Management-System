package Util;

import java.io.*;
import java.nio.file.Paths;
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
     *
     * @return a list of string array, where each array represents a row in the csv file
     * @throws IOException  if an I/O error occurs
     * @throws CsvException if a CSV parsing errors occurs
     **/
    public List<String[]> retrieveCsvData(boolean skipHeader) throws IOException {
        List<String[]> data = new ArrayList<>();
        try (CSVReader csvReader = new CSVReader(new FileReader(Paths.get(filepath).toFile()))) {
            String[] row;

            while ((row = csvReader.readNext()) != null) {
                data.add(row);
            }
        } catch (FileNotFoundException e) {
            throw new IOException("File not found " + filepath, e);
        } catch (IOException | CsvException e) {
            throw new IOException("Error reading CSV file " + filepath, e);
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
     * @throws IOException  if I/O error occurs
     * @throws CsvException if a CSV writing error occurs
     */

    public void writeDataToCsv(List<String[]> data, String[] headers) throws IOException, CsvException {
        try (CSVWriter csvWriter = new CSVWriter(new FileWriter(Paths.get(filepath).toFile()))) {
            csvWriter.writeNext(headers);
            csvWriter.writeAll(data);
        } catch (Exception e) {
            throw new IOException("Error writing to CSV file: " + filepath, e);
        }
    }

    /**
     * @return true if file exits otherwise false
     */

    public boolean fileExists() {
        File file = new File(filepath);
        return file.exists();
    }

    public String[] retrieveFieldNames() throws IOException, CsvException {
        try (CSVReader csvReader = new CSVReader(new FileReader(Paths.get(filepath).toFile()))) {
            return csvReader.readNext();
        } catch (IOException | CsvException e) {
            throw new IOException("Error retrieving field names from CSV file: " + filepath, e);
        }
    }

}

