/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package movitable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import jdk.internal.util.xml.impl.Pair;

/**
 *
 * @author dawod
 */



public class Client {
    


    public static void main(String[] args) throws IOException, ParseException {
        
        /**
         * At first client will ask the master for the tablet server number (server port number)
         */
//        Socket soc_master = new Socket("localhost", 5432);
//        PrintWriter pw_master = new PrintWriter(soc_master.getOutputStream(),true);
//        BufferedReader bf_master = new BufferedReader(new InputStreamReader(soc_master.getInputStream()));
        Socket soc_server;
        PrintWriter pw_server;
        BufferedReader bf_server;
        OutputStream os;
        ObjectOutputStream oos;
        int server_port_number = 0;
        
        /**
         * say welcom to user
         * ask him about the film name
         * ask him about the operation (read_row , add_row , delete_row , set_cell , delete_cell)
         */
        Scanner scan = new Scanner(System.in);
        int operation;
        System.out.println("_____________WELCOME TO MOVI TABLE_____________");
        System.out.println("What do you need ?\n"
                + "1. Search about films\n"
                + "2. Add new films\n"
                + "3. Delete film\n"
                + "4. update film\n"
                + "please write your choice number : ");
        operation = scan.nextInt();
        switch(operation){
            case 1 : 
                if(!searchFilm())
                    System.err.println("error in search!");
                break;
            case 2 :
                if(!addFilm())
                    System.err.println("error in add!");
                break;
            case 3 :
                if(!deleteFilm())
                    System.err.println("error in delete!");
                break;
            case 4 :
                if(!updateFilm())
                    System.err.println("error in update!");
                break;
            default :
                System.err.println("Error: Not supported choice, please try again.");
                break;
        }
        
        
        
        
        
//        soc_server = new Socket("localhost", server_port_number);
//        pw_server = new PrintWriter(soc_server.getOutputStream(),true);
//        bf_server = new BufferedReader(new InputStreamReader(soc_server.getInputStream()));

        
    }
    
    
    /**
     * This function perform the following:
     * 1. Ask user for row_key
     * 2. send row_key to master
     * 3. get tablet_server number from master
     * 4. send row_key to server
     * 5. get complete rows[] from server
     * 6. call print films[] function that may print repeated films according to the time stamp
     * @return 
     */
    public static Boolean searchFilm(){
        Scanner scan = new Scanner(System.in);
        String row_key;
        System.out.println("________________________________________________________________________"
                + "Search Operation"
                + "please enter the film name : ");
        row_key = scan.next();
        
        return true;
    }

    /**
     * This function perform the following:
     * 1. ask user for row_key and row_data[][]
     * 2. send row_key to master
     * 3. get tablet server number from master
     * 4. send row_key and data to server
     * 5.
     * 6.
     * 7.
     * @return 
     */
    public static Boolean addFilm() throws ParseException{
        Scanner scan = new Scanner(System.in);
        String title ;
       

        System.out.println("________________________________________________________________________"
                + "Add Operation\n"
                + "please enter the film title : "); //make sure that film is n't existed in the database
        title = scan.nextLine();
        
        Movi movi = new Movi(title);
        
        movi.set_category();
        movi.set_overview();
        movi.set_language();
        movi.set_company();
        movi.set_country();
        movi.set_runtime();
        movi.set_adult();
        movi.set_popularity();
        
        
        
        
        
        return true;
    }
    
    
    public static Boolean deleteFilm(){
        
        return true;
    }
    public static Boolean updateFilm(){
        
        return true;
    }
    
    

    
}
