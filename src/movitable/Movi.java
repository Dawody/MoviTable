/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package movitable;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


/**
 *
 * @author dawod
 */
public class Movi {
    private String title;
    private List<Date> release = new ArrayList<Date>();
    private List<String> category = new ArrayList<String>();
    private List<String> overview = new ArrayList<String>();
    private List<String> language = new ArrayList<String>();
    private List<String> company = new ArrayList<String>();
    private List<String> country = new ArrayList<String>();
    private List<Float> runtime = new ArrayList<Float>();
    private List<Float> popularity = new ArrayList<Float>();
    private List<Boolean> adult = new ArrayList<Boolean>();
    
    private Scanner scan = new Scanner(System.in);
    private String temp;
    private float tempf;
    private boolean tempb;
    private String date;
    private Date tempd;
    private DateFormat formatter;
            
    public Movi(String title){
        this.title=title;
    }
    

//--------------------------------------------------------------------------------
    public void set_release() throws ParseException{
        System.out.println("please type film release date[yyyy-mm-dd]:");
        date = scan.next();
        formatter = new SimpleDateFormat("yyyy-MM-dd");
        tempd = formatter.parse(date);
        release.add(tempd);

    }


    public void set_category(){
        System.out.println("please type film category:");
        temp = scan.nextLine();
        category.add(temp);
    }
    
    public void set_overview(){
        System.out.println("please type film overview:");
        temp = scan.nextLine();
        overview.add(temp);
    }
    
    public void set_language(){
        System.out.println("please type film language:");
        temp = scan.nextLine();
        language.add(temp);
    }
    
    public void set_company(){
        System.out.println("please type film company:");
        temp = scan.nextLine();
        company.add(temp);
    }
    
    public void set_country(){
        System.out.println("please type film country:");
        temp = scan.nextLine();
        country.add(temp);
    }
    
    public void set_runtime(){
        System.out.println("please type film runtime:");
        tempf = scan.nextFloat();
        runtime.add(tempf);
    }

    public void set_adult(){
        System.out.println("is this film for adults?[true or false]");
        tempb = scan.nextBoolean();
        adult.add(tempb);
        
    }

    public void set_popularity(){
        System.out.println("please type film popularity:");
        tempf = scan.nextFloat();
        popularity.add(tempf);
    }
//-------------------------------------------------------------------------------    
    public String get_row_key(){
        return this.title;
    }
    
    public List<Date> get_release(){
        return this.release;
    }
    
    public List<String> get_category(){
        return this.category;
    }

    public List<String> get_overview(){
        return this.overview;
    }

    public List<String> get_language(){
        return this.language;
    }

    public List<String> get_company(){
        return this.company;
    }

    public List<String> get_country(){
        return this.country;
    }
    
    public List<Float> get_runtime(){
        return this.runtime;
    }

    public List<Boolean> get_adult(){
        return this.adult;        
    }

    public List<Float> get_popularity(){
        return this.popularity;
    }
//-------------------------------------------------------------------------------    
    
    
    
}
