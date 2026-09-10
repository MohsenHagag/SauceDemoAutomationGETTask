package dataProviders;

import utils.CsvDataReader;
import org.testng.annotations.DataProvider;

import java.util.List;


public final class DataProviders {

    private DataProviders() {
    }

    @DataProvider(name = "invalidLoginData")
    public static Object[][] invalidLoginData() {
        List<String[]> rows = CsvDataReader.readCsv("testdata/invalid_login_data.csv");
        Object[][] data = new Object[rows.size()][3];
        for (int i = 0; i < rows.size(); i++) {
            data[i][0] = rows.get(i)[0]; // username
            data[i][1] = rows.get(i)[1]; // password
            data[i][2] = rows.get(i)[2]; // expected error message
        }
        return data;
    }

    @DataProvider(name = "checkoutCustomers")
    public static Object[][] checkoutCustomers() {
        List<String[]> rows = CsvDataReader.readCsv("testdata/checkout_customers.csv");
        Object[][] data = new Object[rows.size()][3];
        for (int i = 0; i < rows.size(); i++) {
            data[i][0] = rows.get(i)[0]; // first name
            data[i][1] = rows.get(i)[1]; // last name
            data[i][2] = rows.get(i)[2]; // postal code
        }
        return data;
    }
}

