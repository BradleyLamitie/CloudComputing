package application;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

/**
 * Creator: Bradley Lamitie
 * Date: 4/26/2019
 * Purpose: To use an HTTP GET method to retrieve the number of countries in a JSON file that have the
 *          substring s in it's name and a population over p.
 * NOTE: I am relying heavily on documentation in order to give this a shot. I do not have much experience in working with JSON.
 */
public class hey {
  /*
   * Complete the function below.
   * https://jsonmock.hackerrank.com/api/countries/search?name=
   */

  static int getCountries(String s, int p) throws Exception {
    URL url = new URL("https://jsonmocl.hackerrank.com/api/countries/search?name=" + s);
    HttpURLConnection con = (HttpURLConnection) url.openConnection();
    con.setRequestMethod("GET");
    BufferedReader reply = new BufferedReader(new InputStreamReader(con.getInputStream()));
    int responseCode = con.getResponseCode();
    System.out.println("\nSending 'GET' request to URL : " + url);
    System.out.println("Response Code : " + responseCode);
    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
    String inputLine;
    StringBuffer response = new StringBuffer();
    while ((inputLine = in.readLine()) != null) {
      response.append(inputLine);
    }
    in.close();
    // print in String
    System.out.println(response.toString());
    JSONObject myResponse = new JSONObject(response.toString());
    System.out.println("result after Reading JSON Response");

    return 0;
  }


  public static void main(String[] args) throws IOException{
//    Scanner in = new Scanner(System.in);
//    final String fileName = System.getenv("OUTPUT_PATH");
//    BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));
//    int res;
//    String _s;
//    try {
//      _s = in.nextLine();
//    } catch (Exception e) {
//      _s = null;
//    }
//
//    int _p;
//    _p = Integer.parseInt(in.nextLine().trim());
    int res;
    String _s = "un";
    int _p = 100090;
    try {
      res = getCountries(_s, _p);
    } catch (Exception e) {
      res = 0;
    }
//
    System.out.println(res);
  }
}