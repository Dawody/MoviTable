/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package movitable;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.Socket;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.AbstractMap;
import java.util.Date;
import java.util.Map;
import java.util.Scanner;
import jdk.internal.util.xml.impl.Pair;
import org.omg.CORBA.TRANSIENT;

/**
 *
 * @author dawod
 */



public class Client{
    


    public static void main(String[] args) throws IOException, ParseException, ClassNotFoundException {
        
        

        String master_ip = "localhost";
        int master_port = 9876;
        
        Scanner scan = new Scanner(System.in);
        int operation;
        String row_key;
        Movi movi;
        System.out.println("_____________WELCOME TO MOVI TABLE_____________");
        
        
        
        
        
        
        while(true){
            System.out.println("=================================================================\n"
                    + "What do you need ?\n"
                    + "1. Search about films\n"
                    + "2. Add new films\n"
                    + "3. Delete film\n"
                    + "4. update film\n"
                    + "please write your choice number : ");
            operation = scan.nextInt();
            switch(operation){
                case 1 :
                    row_key = searchFilm_interface();
                    movi = new Movi(row_key,operation);
                    sendRequest(movi);
                   
                                        
                    
                    break;
                case 2 :
                    movi = addFilm_interface();
                    sendRequest(movi);
                    
                    

                    break;
                case 3 :
                    row_key = deleteFilm_interface();
                    movi = new Movi(row_key , operation);
                    sendRequest(movi);

                    break;
                case 4 :
                    updateFilm_interface();

                    break;
                default :
                    System.err.println("Error: Not supported choice, please try again.");
                    break;
            }
            
            
            
            
            
            
            
        }
    }
    
    
    public static void sendRequest(Movi movi) throws IOException{
        //master//ip_port = get_TabletServer(row_key);
        //test//System.out.println("ip : "+ ip_port.getKey());
        //test//System.out.println("port : " + ip_port.getValue());
        Map.Entry<String,Integer> ip_port = new AbstractMap.SimpleEntry<>("localhost",9876);//just assumption for the output of the master!
        Socket soc_server = new Socket(ip_port.getKey(), ip_port.getValue());
        OutputStream os = soc_server.getOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(os);
        oos.writeObject(movi);
        switch(movi.get_operation()){
            case 1:
                System.out.println("Search request for Film ( "+movi.get_row_key()+" ) is successfully sent to server.");
                break;
            case 2:
                System.out.println("Data of New Film ( "+movi.get_row_key()+" ) is successfully sent to server.");
                break;
            case 3:
                System.out.println("Delete request for Film ( "+movi.get_row_key()+" ) is successfully sent to server.");
                break;
            case 4:
                
                break;
        }
        
        oos.flush();
        oos.close();
        os.close();
        soc_server.close();
        
    }
    
    
    

    
    
    
    public static Map.Entry<String,Integer> get_TabletServer(String row_key) throws IOException, ClassNotFoundException{
        Map.Entry<String,Integer> ip_port;
        Socket soc_master = new Socket("localhost",5432); //for testing purpos only
        OutputStream os = soc_master.getOutputStream();
        InputStream is = soc_master.getInputStream();
        ObjectOutputStream oos = new ObjectOutputStream(os);
        ObjectInputStream ois = new ObjectInputStream(is);
        
        oos.writeObject(row_key);
        ip_port = (Map.Entry<String, Integer>) ois.readObject();
        ois.close();
        is.close();
        oos.close();
        os.close();
        soc_master.close();
        
        return ip_port;
                
    
    }
    
    
    //--------------------------------------------------------------------------------------------------------
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
    public static String searchFilm_interface(){
        Scanner scan = new Scanner(System.in);
        String row_key;
        System.out.println("________________________________________________________________________\n"
                + "Search Operation"
                + "please enter the film name : ");
        row_key = scan.nextLine();
        
        return row_key;
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
    public static Movi addFilm_interface() throws ParseException{
        Scanner scan = new Scanner(System.in);
        String title ;
        System.out.println("________________________________________________________________________"
                + "Add Operation\n"
                + "please enter the film title : ");
        title = scan.nextLine();
        
        
        Movi movi = new Movi(title,2);
        movi.set_release();
        movi.set_category();
        movi.set_overview();
        movi.set_language();
        movi.set_company();
        movi.set_country();
        movi.set_runtime();
        movi.set_adult();
        movi.set_popularity();
                
        return movi;
    }
    
    
    public static String deleteFilm_interface(){
        Scanner scan = new Scanner(System.in);
        String row_key;
        System.out.println("________________________________________________________________________"
                + "Delete Operation"
                + "please enter the film name : ");
        row_key = scan.nextLine();
        
        return row_key;
    }


    public static Boolean updateFilm_interface(){
        
        return true;
    }
    
    
    
    
}