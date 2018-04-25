/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package movitable;

import java.io.Serializable;
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
public class Movi implements Serializable{
    private int operation; //it is not colum . it's to detect the request type only
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
    
    
    private String temp = "";
    private float tempf = 0;
    private boolean tempb = true;
    private String date = "";
    private Date tempd = new Date();
    private DateFormat formatter;
    private Float ff = new Float(0);
            
    public Movi(String title , int operation){
        this.title=title;
        this.operation = operation;
        
        //
        this.release.add(new Date());
        this.category.add("null");
        this.overview.add("null");
        this.language.add("null");
        this.company.add("null");
        this.country.add("null");
        this.runtime.add(ff);
        this.popularity.add(ff);
        this.adult.add(true);
        //
        
    }
    
//
////--------------------------------------------------------------------------------
    public void set_release() throws ParseException{
        Scanner scan = new Scanner(System.in);
        System.out.println("please type film release date[yyyy-mm-dd]:");
        date = scan.next();
        formatter = new SimpleDateFormat("yyyy-MM-dd");
        tempd = formatter.parse(date);
        release.add(tempd);

    }


    public void set_category(){
        Scanner scan = new Scanner(System.in);
        System.out.println("please type film category:");
        temp = scan.nextLine();
        category.add(temp);
    }
    
    public void set_overview(){
        Scanner scan = new Scanner(System.in);
        System.out.println("please type film overview:");
        temp = scan.nextLine();
        overview.add(temp);
    }
    
    public void set_language(){
        Scanner scan = new Scanner(System.in);
        System.out.println("please type film language:");
        temp = scan.nextLine();
        language.add(temp);
    }
    
    public void set_company(){
        Scanner scan = new Scanner(System.in);
        System.out.println("please type film company:");
        temp = scan.nextLine();
        company.add(temp);
    }
    
    public void set_country(){
        Scanner scan = new Scanner(System.in);
        System.out.println("please type film country:");
        temp = scan.nextLine();
        country.add(temp);
    }
    
    public void set_runtime(){
        Scanner scan = new Scanner(System.in);
        System.out.println("please type film runtime:");
        tempf = scan.nextFloat();
        runtime.add(tempf);
    }

    public void set_adult(){
        Scanner scan = new Scanner(System.in);
        System.out.println("is this film for adults?[true or false]");
        tempb = scan.nextBoolean();
        adult.add(tempb);
        
    }

    public void set_popularity(){
        Scanner scan = new Scanner(System.in);
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
    
    public  int get_operation(){
        return this.operation;
    }
////-------------------------------------------------------------------------------    
    
    
    
}
