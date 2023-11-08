package projectt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

public class CurrencyConverter {
    private static Set<String> favoriteCurrency = new HashSet<>();

    private static void addfavoriteCurrency() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Enter the currency to add favorites: ");
        try {
            String currencycode = reader.readLine().toUpperCase();
            favoriteCurrency.add(currencycode);
            System.out.println(currencycode + " has been added to your favorites.");
        } catch (IOException e) {
            System.out.println("error reading input: " + e.getMessage());
        }
    }

    private static void viewfavoriteCurrencies() {
        System.out.println("Favorite Currency:");
        for (String currencycode : favoriteCurrency) {
            System.out.println(currencycode);
        }
    }

    private static void convertCurrency() throws IOException {
        HashMap<Integer, String> currencycodes = new HashMap<>();
        currencycodes.put(1, "AUD");
        currencycodes.put(2, "EUR");
        currencycodes.put(3, "USD");
        currencycodes.put(4, "INR");
        currencycodes.put(5, "BOB");

        String fromCode, toCode;
        double amount;
        Scanner sc = new Scanner(System.in);

        System.out.println("welcome to the Currency Converterr!");
        System.out.println("Currency Converting From?");
        System.out.println(
                "\n 1: USD(US Dollar)\t\n 2: AUD(Australlia Dollar)\t\n 3: EUR(Euro)\t\n 4: BOB (Bolivia Dollar)\t\n 5. INR (Indian Rupees)");
        int fromCurrencyChoice = sc.nextInt();
        fromCode = currencycodes.get(fromCurrencyChoice);

        System.out.println("Currency Converting To?");
        System.out.println(
                "1: USD(US Dollar)\t\n 2: AUD(Australlia Dollar)\t\n 3: EUR(Euro)\t\n 4: BOB (Bolivia Dollar)\t\n 5. INR (Indian Rupees)");
        int toCurrencyChoice = sc.nextInt();
        toCode = currencycodes.get(toCurrencyChoice);

        System.out.println("Amount that you wish to convert?");
        amount = sc.nextDouble();

        sendHttpGetRequest(fromCode, toCode, amount);

        System.out.println("Thank you for using the Currency Converter!");
    }

    private static void sendHttpGetRequest(String fromCode, String toCode, double amount) throws IOException {

        String GET_URL = "http://api.exchangerate.host/convert?access_key=ff8b13597d7e9dd892c944f57cd59f66&from="
                + fromCode + "&to=" + toCode + "&amount=" + amount;
        URL url = new URL(GET_URL);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("GET");
        int rs = httpURLConnection.getResponseCode();

        if (rs == HttpURLConnection.HTTP_OK) { 
            try (BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()))) {
                String inputLine;
                StringBuilder resp = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    resp.append(inputLine);
                }

             
                JSONObject obj = new JSONObject(resp.toString());
                if (obj.has("result")) {
                    double convertedAmount = obj.getDouble("result");
                    System.out.println(amount + " " + fromCode + " = " + convertedAmount + " " + toCode);
                } else {
                    System.out.println("Unable to find converted amount in response.");
                }
            } catch (JSONException e) {
                System.out.println("JSON parsing error: " + e.getMessage());
            }
        } else {
            System.out.println("GET request failed!");
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            System.out.println("Currency Converter Menu:");
            System.out.println("1. Convert Currency");
            System.out.println("2. Add Favorite Currency");
            System.out.println("3. View Favorite Currencies");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");

            int choice = Integer.parseInt(reader.readLine());

            switch (choice) {
                case 1:
                    convertCurrency();
                    break;
                case 2:
                    addfavoriteCurrency();
                    break;
                case 3:
                    viewfavoriteCurrencies();
                    break;
                case 4:
                    System.out.println("Goodbye!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
            }
        }
    }
}
