package Scraper;

import java.util.Scanner;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class SingleScraper {
  
  public static String[] inputs = new String[] {"1y Target-Est", "Avg. Volume-Volume",
    "52 Week-nge", "Market Cap-Cap", "PE Ratio (TTM)-\\)", "EPS (TTM)-\\)"};
  // array containing important identifiers to make sure data is labeled accurately

  /*
   * Prints out all of the formatted data
   * 
   * @param a 2D array containing the requested stocks and their data
   * @param the number of stocks in the array
   */
  public static void printer(String[][] stocks, int numStock) {
    for (int j = 0; j < stocks.length - 1; j++) {
      for (int k = 0; k < numStock; k++) {
        System.out.print(stocks[k][j] + "          ");
      }
      System.out.println();
    }
  }

  /*
   * Prints out all of the formatted data
   * 
   * @param the webClient initialized in the main method
   * @param the ticker for the stock
   * @param the complete URL of where to get the data
   */
  public static String[] runScraper(WebClient webClient, String ticker, String url) {
    String[] stockInfo = new String[10];
    try {
      webClient.getOptions().setCssEnabled(false);
      webClient.getOptions().setAppletEnabled(false);
      webClient.getOptions().setJavaScriptEnabled(false);

      HtmlPage page = webClient.getPage(url);
      String html = page.asText();

      System.out.println();

      String[] string = new String[] {};

      string = html.split("\n");

      stockInfo[0] = Scraper.getDate();
      stockInfo[1] = Scraper.stockName(ticker, string);

      String[] array = new String[] {};
      stockInfo[2] = Scraper.stockPrice(string);
      array = stockInfo[2].split("&");
      stockInfo[2] = array[0];
      stockInfo[3] = array[1];

      int j = 0;
      for (int i = 4; i < 10; i++) {
        stockInfo[i] = Scraper.getData(inputs[j], string);
        j++;
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
    return stockInfo;
  }

  /*
   * Entry point for this application
   */
  public static void main(String[] args) {
    String url = "";
    String input = "";
    String[] tickers = new String[4];
    String[][] stocks = new String[10][];
    Scanner scnr = new Scanner(System.in);
    WebClient webClient = new WebClient();
    int i = 0;
   
    while (!input.equals("q")) {
      System.out.print(
        "Enter multiple tickers separated by a comma to display more than one (max 4).\nEnter ticker(s) (ex. vbiv), \"r\" to reset, or \"q\" to quit:");
      input = scnr.nextLine();
      input = input.toUpperCase().trim();
      
      if (input.contains(",")) {
        tickers = input.split(",");
        for (int k = 0; k < tickers.length; k++) {
          tickers[k] = tickers[k].trim();
          url = "https://finance.yahoo.com/quote/" + tickers[k] + "?p=" + tickers[k]
            + "&.tsrc=fin-srch";
          stocks[k] = runScraper(webClient, input, url);
          i++;
        }
        printer(stocks, tickers.length);
      }
      else if (input.toLowerCase().trim().equals("r")) {
        tickers = new String[4];
        stocks = new String[10][];
        i = 0;
      }
      else {
        url = "https://finance.yahoo.com/quote/" + input + "?p=" + input + "&.tsrc=fin-srch";
        stocks[i] = runScraper(webClient, input, url);
        i++;
        printer(stocks, i);
        System.out.println();

      }
      System.out.println();
    }
    webClient.close();
    scnr.close();
  }
}
