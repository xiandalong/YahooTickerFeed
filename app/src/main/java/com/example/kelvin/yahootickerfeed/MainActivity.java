package com.example.kelvin.yahootickerfeed;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;



public class MainActivity extends AppCompatActivity {
    // create a list of Stock Objects for storing JSON data fetched from Yahoo
    List<Stock> stocks = new ArrayList<Stock>();
    // use boolean variables to store the current sorting order and the field used for sorting
    boolean ascSortBySymbol = true;
    boolean ascSortByPrice = false;
    boolean ascSortByDate = false;
    boolean desSortBySymbol = false;
    boolean desSortByPrice = false;
    boolean desSortByDate = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Start the async task when the activity is created
        new RetrieveFeedTask().execute();

        // get the references for the buttons
        Button ascBySymbol = (Button) findViewById(R.id.button1);
        Button ascByPrice = (Button) findViewById(R.id.button2);
        Button ascByDate = (Button) findViewById(R.id.button3);
        Button desBySymbol = (Button) findViewById(R.id.button4);
        Button desByPrice = (Button) findViewById(R.id.button5);
        Button desByDate = (Button) findViewById(R.id.button6);

        /** set up the button actions: after a button is pushed, the corresponding boolean variable
         * is set to be true while others were set to be false, and the list of stock information
         * will be cleared, then the async task will execute again to fetch the refreshed data from
         * Yahoo, and put it in "stocks" in the currently specified order.
         */
        ascBySymbol.setOnClickListener(
            new Button.OnClickListener(){
                @Override
                public void onClick(View v) {
                    ascSortBySymbol = true;
                    ascSortByPrice = false;
                    ascSortByDate = false;
                    desSortBySymbol = false;
                    desSortByPrice = false;
                    desSortByDate = false;
                    stocks.clear();
                    new RetrieveFeedTask().execute();
                }
            }
        );

        ascByPrice.setOnClickListener(
                new Button.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        ascSortBySymbol = false;
                        ascSortByPrice = true;
                        ascSortByDate = false;
                        desSortBySymbol = false;
                        desSortByPrice = false;
                        desSortByDate = false;
                        stocks.clear();
                        new RetrieveFeedTask().execute();
                    }
                }
        );

        ascByDate.setOnClickListener(
                new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ascSortBySymbol = false;
                        ascSortByPrice = false;
                        ascSortByDate = true;
                        desSortBySymbol = false;
                        desSortByPrice = false;
                        desSortByDate = false;
                        stocks.clear();
                        new RetrieveFeedTask().execute();
                    }
                }
        );

        desBySymbol.setOnClickListener(
                new Button.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        ascSortBySymbol = false;
                        ascSortByPrice = false;
                        ascSortByDate = false;
                        desSortBySymbol = true;
                        desSortByPrice = false;
                        desSortByDate = false;
                        stocks.clear();
                        new RetrieveFeedTask().execute();
                    }
                }
        );

        desByPrice.setOnClickListener(
                new Button.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        ascSortBySymbol = false;
                        ascSortByPrice = false;
                        ascSortByDate = false;
                        desSortBySymbol = false;
                        desSortByPrice = true;
                        desSortByDate = false;
                        stocks.clear();
                        new RetrieveFeedTask().execute();
                    }
                }
        );

        desByDate.setOnClickListener(
                new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ascSortBySymbol = false;
                        ascSortByPrice = false;
                        ascSortByDate = false;
                        desSortBySymbol = false;
                        desSortByPrice = false;
                        desSortByDate = true;
                        stocks.clear();
                        new RetrieveFeedTask().execute();
                    }
                }
        );


    }

    // Async Task for fetching data
    private class RetrieveFeedTask extends AsyncTask<Void, Void, Void> {

        String result;
        JSONArray stocklist;

        protected Void doInBackground(Void... params) {
            try {
                // read content from URL as a string
                URL url = new URL("http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.quotes%20where%20symbol%20in%20(%22YHOO%22%2C%22AAPL%22%2C%22GOOG%22%2C%22MSFT%22)%0A%09%09&env=http%3A%2F%2Fdatatables.org%2Falltables.env&format=json");
                BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
                String stringBuffer;
                String stringText = "";
                while((stringBuffer = br.readLine())!=null){
                    stringText += stringBuffer;
                }
                br.close();
                result = stringText;

                // convert string to json object and read symbol, LastTradePriceOnly, and DividendPayDate for each stock
                JSONObject jsonObj = new JSONObject(result);

                stocklist = jsonObj.getJSONObject("query").getJSONObject("results").getJSONArray("quote");

                int listLength = stocklist.length();

                for(int i = 0;i<listLength;i++){
                    JSONObject stock = stocklist.getJSONObject(i);
                    String stockName = stock.getString("symbol");
                    double LastTradePrice = Double.parseDouble(stock.getString("LastTradePriceOnly"));
                    String dividendPayDate = stock.getString("DividendPayDate");
                    stocks.add(new Stock(stockName,LastTradePrice,dividendPayDate ));
                }

                // Sort the stocks based on which field is used for sorting
                if(ascSortBySymbol==true){
                    Collections.sort(stocks,new AscCompareByStockName());
                }else if(ascSortByPrice==true){
                    Collections.sort(stocks,new AscCompareByLastTradePrice());
                }else if(ascSortByDate==true){
                    Collections.sort(stocks,new AscCompareByDividendPayDate());
                }else if(desSortBySymbol==true){
                    Collections.sort(stocks,new DesCompareByStockName());
                }else if(desSortByPrice==true){
                    Collections.sort(stocks,new DesCompareByLastTradePrice());
                }else if(desSortByDate==true){
                    Collections.sort(stocks,new DesCompareByDividendPayDate());
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        // After data is fetched and stored in "stocks", it is displayed by the following method
        protected void onPostExecute(Void res) {
            // TODO: check this.exception
            // TODO: do something with the feed

            List<String> symbols = new ArrayList<String>();
            List<Double> prices = new ArrayList<Double>();
            List<String> dates = new ArrayList<String>();


            //read fields from stocks
            for(int i=0;i<stocks.size();i++){
                symbols.add(stocks.get(i).getStockName());
                prices.add(stocks.get(i).getLastTradePrice());
                dates.add(stocks.get(i).getDividendPayDate());

            }

            // use ListView Adapters to fill the data into ListViews
            ListAdapter symbolAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1,symbols);
            ListAdapter priceAdapter = new ArrayAdapter<Double>(MainActivity.this, android.R.layout.simple_list_item_1,prices);
            ListAdapter dateAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1,dates);

            ListView symbolListView = (ListView) findViewById(R.id.listView);
            ListView priceListView = (ListView) findViewById(R.id.listView2);
            ListView dateListView = (ListView) findViewById(R.id.listView3);

            symbolListView.setAdapter(symbolAdapter);
            priceListView.setAdapter(priceAdapter);
            dateListView.setAdapter(dateAdapter);
//
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // This is for setting up the action for the "Refresh" button on the toolbar
        if (id == R.id.Refresh) {

            // clears the stock list and re-fetch the data
            stocks.clear();
            new RetrieveFeedTask().execute();

        }

        return super.onOptionsItemSelected(item);
    }
}



