package Scraper;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/*
 * 
 */
public class Scraper {

  private static String[] dataType = new String[] {"1 Year Target Estimate: ", "Average Volume: ", "Fifty Two Week Range: ", "Market Cap: ", "1 Year Forward P/E: ", "EPS: ", "N/A", "Stock Price: ", "Change in Price: "};
  // array containing the labels for the stock data used
  
  /*
   * Formats the name of the stock to be correctly returned
   * 
   * @param the stock's ticker
   * @param an array containing the html data being scraped
   * @return the full name and ticker
   */
  public static String stockName(String name, String[] html) {
    String stockName = html[0];
    String[] placeHolder = new String[] {};
    String string = "";

    placeHolder = stockName.split("\\)");

    stockName = placeHolder[0] + ")";
    
    for (int i = 0; i < 46 - stockName.length(); i++) {
      string = string + " ";
    }
    
    return stockName + string;
  }

  /*
   * Retrieves and properly formats the price of the stock and the percent change
   * 
   * @param an array containing the data from the html site being scraped
   * @return a string with the properly formatted price and percent change
   */
  public static String stockPrice(String[] html) {
    String stockPrice = "";
    String priceChange = "";
    String marketOpen = "";

    for (int i = 1; i < html.length; i++) {
      LocalTime localTime = LocalTime.now();
      String time = localTime.toString().substring(0, 2);
      int numTime = Integer.parseInt(time.trim());
      
      // checks if the market is open or closed to use the correct identifier when finding the data
      if (numTime > 8 && numTime < 16) { // in CST
        marketOpen = ". Market open.";
      }
      else {
        marketOpen = "4:00";
      }
      
      if (html[i].contains(marketOpen)) {
        String[] holder = new String[] {};
        String[] holder2 = new String[] {};
        String rightLine = "";

        rightLine = html[i - 1];

        holder = rightLine.split("\\)");

        // divides price and percent change information based on its value
        if (holder[0].contains("-")) {
          holder2 = holder[0].split("-");
          stockPrice = holder2[0].trim();
          priceChange = "-" + holder2[1] + "-" + holder2[2];
        } else if (holder[0].contains("+")) {
          holder2 = holder[0].split("\\+");
          stockPrice = holder2[0].trim();
          priceChange = "+" + holder2[1] + "+" + holder2[2];
        } else {
          priceChange = "0.0 0.0%";
        }
      }
    }
    
    stockPrice = formatter(dataType[7], " $" + stockPrice);
    priceChange = formatter(dataType[8], " $" + priceChange + ")");
    
    return stockPrice + "&" + priceChange;
  }

  /*
   * Method that returns the current date
   * 
   * @returns a string containing the date
   */
  public static String getDate() {
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    LocalDate localDate = LocalDate.now();
    LocalTime localTime = LocalTime.now();
    String string = "";
    String date = "";
    date = "Date: " + dtf.format(localDate) + "  " + localTime.toString();
    for (int i = 0; i < 46 - date.length() ; i++) {
      string = string + " ";
    }
    
    return date + string;
  }
  
  /*
   * Method that formats the stock data and labels for easy reading
   * 
   * @param a string containing the data label to be formatted
   * @param a string containing the stock data to be formatted
   * @returns a string containing the correctly formatted data
   */
  private static String formatter(String values, String data) {
    String string = "";
    int length = 46;
    length = length - values.length() - data.length();
    for (int i = 0; i < length; i++) {
      string = string + ".";
    }
    string = values + string + data;
    
    return string;
  }
  
  /*
   * Method that takes in a string containing the data to be displayed and returns a string
   * containing the label and the data.
   * 
   * @param a string containing the requested data type
   * @param an array containing the lines of the requested html page
   * @returns a string containing a line with the correct data and label
   */
  public static String getData(String inputs, String[] html) {
    String data = "";
    String[] holder = new String[] {};
    String[] values = new String[] {};
    values = inputs.split("-");
    
    for (int i = 1; i < html.length; i++) {
      if (html[i].contains(values[0])) {

        data = html[i];
        
        holder = data.split(values[1]);
        
        data = holder[1].trim();
      }
    }
    
    if (values[0].equals("1y Target")) {
      return formatter(dataType[0], " $" + data);
    }
    else if (values[0].equals("Avg. Volume")) {
      return formatter(dataType[1], " " + data);
    }
    else if (values[0].equals("52 Week")) {
      return formatter(dataType[2], " $" + data);
    }
    else if (values[0].equals("Market Cap")) {
      return formatter(dataType[3], " " + data).replaceFirst(".......", "") + " shares";
    }
    else if (values[0].equals("PE Ratio (TTM)")) {
      return formatter(dataType[4], " " + data);
    }
    else if (values[0].equals("EPS (TTM)")) {
      return formatter(dataType[5], " " + data);
    }
    else if (values[0].contains("N/A")) {
      return formatter(dataType[6], " " + data);
    }
    else {
      return "invalid input";
    }
  }
}
