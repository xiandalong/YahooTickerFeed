package com.example.kelvin.yahootickerfeed;


import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

/**
 * Stock is a class for storing stock information, here we only have three fields, but it is easy to
 * scale to a larger number. Each field is encapsulated and only accessible through get/set methods.
 * Created by Kelvin on 3/24/2016.
 */
public class Stock {
    // define the fields in the Stock Object
    private String stockName;
    private Double lastTradePrice;
    private String dividendPayDate;

    public Stock(String stockName, Double lastTradePrice, String dividendPayDate) {
        this.stockName = stockName;
        this.lastTradePrice = lastTradePrice;
        this.dividendPayDate = dividendPayDate;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public Double getLastTradePrice() {
        return lastTradePrice;
    }

    public void setLastTradePrice(Double lastTradePrice) {
        this.lastTradePrice = lastTradePrice;
    }

    public String getDividendPayDate() {
        return this.dividendPayDate;
    }

    public void setDividendPayDate(String dividendPayDate) {
        this.dividendPayDate = dividendPayDate;
    }
}

/*
All the following are the comparators available for different sorting schemes. Different data type
varies here: for String variables such as stock names, the comparison is lexicographically; for double
number, it is compared directly by the magnitude of the number; for dates, first we check if either
of them is null, if so, the null one is smaller. If they are both not null, we will parse them and
compare them using before() and after() methods for Date Objects.
 */
class AscCompareByStockName implements Comparator<Stock>{

    @Override
    public int compare(Stock lhs, Stock rhs) {
        String lName = lhs.getStockName();
        String rName = rhs.getStockName();
        if( lName.compareTo(rName)<0){
            return -1;
        }else if (lName.compareTo(rName)>0){
            return 1;
        }else{
            return 0;
        }
    }
}

class DesCompareByStockName implements Comparator<Stock>{

    @Override
    public int compare(Stock lhs, Stock rhs) {
        String lName = lhs.getStockName();
        String rName = rhs.getStockName();
        if( lName.compareTo(rName)<0){
            return 1;
        }else if (lName.compareTo(rName)>0){
            return -1;
        }else{
            return 0;
        }
    }
}


class AscCompareByLastTradePrice implements Comparator<Stock>{

    @Override
    public int compare(Stock lhs, Stock rhs) {
        double lPrice = lhs.getLastTradePrice();
        double rPrice = rhs.getLastTradePrice();
        if( lPrice<rPrice){
            return -1;
        }else if (lPrice>rPrice){
            return 1;
        }else{
            return 0;
        }
    }
}

class DesCompareByLastTradePrice implements Comparator<Stock>{

    @Override
    public int compare(Stock lhs, Stock rhs) {
        double lPrice = lhs.getLastTradePrice();
        double rPrice = rhs.getLastTradePrice();
        if( lPrice<rPrice){
            return 1;
        }else if (lPrice>rPrice){
            return -1;
        }else{
            return 0;
        }
    }
}


class AscCompareByDividendPayDate implements Comparator<Stock>{
    SimpleDateFormat dateFormat= new SimpleDateFormat("dd/mm/yyyy");
    @Override
    public int compare(Stock lhs, Stock rhs) {
        String lDate = lhs.getDividendPayDate();
        String rDate = rhs.getDividendPayDate();

        if(lDate.equals("null") && rDate.equals("null")){
            return 0;
        }else if(!lDate.equals("null") && rDate.equals("null")){
            return 1;
        }else if(lDate.equals("null") && !rDate.equals("null")){
            return -1;
        }else{
            try {
                Date ldate = dateFormat.parse(lDate);
                Date rdate = dateFormat.parse(rDate);
                if(ldate.before(rdate)) return -1;
                else if(ldate.after(rdate)) return 1;
                else return 0;
            }catch(Exception e){
                e.printStackTrace();
            }
            return 0;
        }

    }
}

class DesCompareByDividendPayDate implements Comparator<Stock>{
    SimpleDateFormat dateFormat= new SimpleDateFormat("dd/mm/yyyy");
    @Override
    public int compare(Stock lhs, Stock rhs) {
        String lDate = lhs.getDividendPayDate();
        String rDate = rhs.getDividendPayDate();

        if(lDate.equals("null") && rDate.equals("null")){
            return 0;
        }else if(!lDate.equals("null") && rDate.equals("null")){
            return -1;
        }else if(lDate.equals("null") && !rDate.equals("null")){
            return 1;
        }else{
            try {
                Date ldate = dateFormat.parse(lDate);
                Date rdate = dateFormat.parse(rDate);
                if(ldate.before(rdate)) return 1;
                else if(ldate.after(rdate)) return -1;
                else return 0;
            }catch(Exception e){
                e.printStackTrace();
            }
            return 0;
        }

    }
}