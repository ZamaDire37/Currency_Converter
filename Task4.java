import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class Task4 {

    private static final String API_KEY = " 785c7d99ae4f8a77e2474c4d"; // Replace with your ExchangeRate-API key

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Step 1: Currency Selection
        System.out.print("Enter the base currency code (e.g., USD): ");
        String baseCurrency = scanner.nextLine().toUpperCase();

        System.out.print("Enter the target currency code (e.g., EUR): ");
        String targetCurrency = scanner.nextLine().toUpperCase();

        // Step 2: Currency Rates (Fetching real-time exchange rates from the API)
        double exchangeRate = getExchangeRate(baseCurrency, targetCurrency);

        // Step 3: Amount Input
        System.out.print("Enter the amount to convert from " + baseCurrency + " to " + targetCurrency + ": ");
        double amountToConvert = scanner.nextDouble();

        // Step 4: Currency Conversion
        double convertedAmount = amountToConvert * exchangeRate;

        // Step 5: Display Result
        System.out.println("Converted amount: " + convertedAmount + " " + targetCurrency);
    }

    private static double getExchangeRate(String baseCurrency, String targetCurrency) {
        try {
            URL url = new URL("https://open.er-api.com/v6/latest/" + baseCurrency + "?apikey=" + API_KEY);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            if (connection.getResponseCode() == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                reader.close();

                // Parse JSON response to get the exchange rate
                String jsonResponse = response.toString();
                int start = jsonResponse.indexOf("\"" + targetCurrency + "\":") + targetCurrency.length() + 3;
                int end = jsonResponse.indexOf(",", start);
                String rateSubstring = jsonResponse.substring(start, end);
                return Double.parseDouble(rateSubstring);
            } else {
                System.out.println("Failed to fetch exchange rates. HTTP Error code: " + connection.getResponseCode());
                System.exit(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        return 0.0;
    }
}
