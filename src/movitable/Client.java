/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package movitable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author dawod
 */



public class Client {

    public static void main(String[] args) throws IOException {
        
        
//        Socket soc_master = new Socket("localhost", 5432);
//        PrintWriter pw_master = new PrintWriter(soc_master.getOutputStream(),true);
//        BufferedReader bf_master = new BufferedReader(new InputStreamReader(soc_master.getInputStream()));
//        Socket soc_server;
//        PrintWriter pw_server;
//        BufferedReader bf_server;
//        int server_port_number = 0;
        
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
                if(searchFilm())
                    System.err.println("error in search!");
                break;
            case 2 :
                if(addFilm())
                    System.err.println("error in add!");
                break;
            case 3 :
                if(deleteFilm())
                    System.err.println("error in delete!");
                break;
            case 4 :
                if(updateFilm())
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
    
    public static Boolean searchFilm(){
        Scanner scan = new Scanner(System.in);
        String row_key;
        System.out.println("________________________________________________________________________"
                + "Search Operation"
                + "please entre the film name : ");
        row_key = scan.next();
        /**
         * send row_key to master
         * get tablet_server number
         * send row_key to server
         * get complete row from server
         * call print films function that may print repeated films according to the time stamp
         */
        return true;
    }
    public static Boolean addFilm(){
        
        return true;
    }
    public static Boolean deleteFilm(){
        
        return true;
    }
    public static Boolean updateFilm(){
        
        return true;
    }
    
    

    
}
