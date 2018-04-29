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
    

    public static String server_ip= "localhost";//"172.28.107.227";
    static String master_ip = "localhost";
    static int master_port = 9876;

    public static void main(String[] args) throws IOException, ParseException, ClassNotFoundException {
        
        

        
        
        
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
                    movi = sendSearchRequest(movi);
                    if(movi.get_operation()==0)
                        System.out.println("Film Not Found");
                    else
                    {
                        movi.print_overview();
                        movi.print_release();
                        movi.print_category();
                        movi.print_country();
                        movi.print_company();
                        movi.print_runtime();
                        movi.print_popularity();
                        movi.print_adult();
                        movi.print_language();                        
                    }
                   
                                        
                    
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
                    movi  = updateFilm_interface();
                    sendRequest(movi);

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
        Map.Entry<String,Integer> ip_port = new AbstractMap.SimpleEntry<>(server_ip,9876);//just assumption for the output of the master!
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
                System.out.println("Add cell update request for Film ( "+movi.get_row_key()+" ) is successfully sent to server.");
            case 5:
                System.out.println("Delete cell update request for Film ( "+movi.get_row_key()+" ) is successfully sent to server.");
                
                break;
        }
        
        oos.flush();
        oos.close();
        os.close();
        //soc_server.close();
        
    }
    
    
    
    public static Movi sendSearchRequest(Movi movi) throws IOException, ClassNotFoundException{
        //master//ip_port = get_TabletServer(row_key);
        //test//System.out.println("ip : "+ ip_port.getKey());
        //test//System.out.println("port : " + ip_port.getValue());
        Map.Entry<String,Integer> ip_port = new AbstractMap.SimpleEntry<>(server_ip,9876);//just assumption for the output of the master!
        Socket soc_server = new Socket(ip_port.getKey(), ip_port.getValue());
        OutputStream os = soc_server.getOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(os);
        oos.writeObject(movi);
        System.out.println("Search request for Film ( "+movi.get_row_key()+" ) is successfully sent to server.");

        oos.flush();
        
        InputStream is = soc_server.getInputStream();
        ObjectInputStream ois = new ObjectInputStream(is);
        movi = (Movi) ois.readObject();

        return movi;

        //oos.close();
        //os.close();
        //soc_server.close();
        
    }
    
    
    

    
    
    
    public static Map.Entry<String,Integer> get_TabletServer(String row_key) throws IOException, ClassNotFoundException{
        Map.Entry<String,Integer> ip_port;
        Socket soc_master = new Socket(master_ip,master_port);
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
        System.out.println("________________________________________________________________________\n"
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
        System.out.println("________________________________________________________________________\n"
                + "Delete Operation"
                + "please enter the film name : ");
        row_key = scan.nextLine();
        
        return row_key;
    }

    
    public static Movi updateFilm_interface() throws ParseException{
        Scanner scan = new Scanner(System.in);
        String title ;
        int operation;
        boolean flag = true;
        Movi movi = null;
        System.out.println("________________________________________________________________________\n"
                + "Update Operation\n"
                + "please enter the film title : ");
        title = scan.nextLine();
        System.out.println("choose the update operation : \n"
                + "1. set information\n"
                + "2. delete information\n");
        while(flag){
            operation = scan.nextInt();
            switch(operation){
                case 1:
                    //set cell
                    movi = new Movi(title,4);
                    updateFilm_set_interface(movi);
                    
                    flag=false;
                    break;
                case 2:
                    //delete cell
                    movi = new Movi(title,5);
                    updateFilm_delete_interface(movi);
                    
                    flag=false;
                    break;
                default:
                    System.err.println("Not supported option. please try again.");
            }
            
        }
        
        
        
        return movi;
    }
    
    
    public static void updateFilm_set_interface(Movi movi) throws ParseException{
        Scanner scan = new Scanner(System.in);
        int q = 0;
        
        System.out.println("Do you need to add Release Date ?[0,1]");
        q=scan.nextInt();
        if(q==1)
            movi.set_release();

        System.out.println("Do you need to add Category ?[0,1]");
        q=scan.nextInt();
        if(q==1)            
            movi.set_category();
        
        System.out.println("Do you need to add Overview ?[0,1]");
        q=scan.nextInt();
        if(q==1)
            movi.set_overview();
        
        System.out.println("Do you need to add Language ?[0,1]");
        q=scan.nextInt();
        if(q==1)
            movi.set_language();
        
        System.out.println("Do you need to add Production Company ?[0,1]");
        q=scan.nextInt();
        if(q==1)
            movi.set_company();
        
        System.out.println("Do you need to add Pruction Country ?[0,1]");
        q=scan.nextInt();
        if(q==1)
            movi.set_country();
        
        System.out.println("Do you need to add Runtime ?[0,1]");
        q=scan.nextInt();
        if(q==1)
            movi.set_runtime();
        
        System.out.println("Do you need to add Adults Contrain ?[0,1]");
        q=scan.nextInt();
        if(q==1)
            movi.set_adult();
        
        System.out.println("Do you need to add Popularity ?[0,1]");
        q=scan.nextInt();
        if(q==1)
            movi.set_popularity();
        
    }
    

    
    
    public static void updateFilm_delete_interface(Movi movi) throws ParseException{
        Scanner scan = new Scanner(System.in);
        int q = 0;
        
        System.out.println("Do you need to delete Release Date ?[0,1]");
        q=scan.nextInt();
        if(q==1)
            movi.set_release();

        System.out.println("Do you need to delete Category ?[0,1]");
        q=scan.nextInt();
        if(q==1)            
            movi.set_category();
        
        System.out.println("Do you need to delete Overview ?[0,1]");
        q=scan.nextInt();
        if(q==1)
            movi.set_overview();
        
        System.out.println("Do you need to delete Language ?[0,1]");
        q=scan.nextInt();
        if(q==1)
            movi.set_language();
        
        System.out.println("Do you need to delete Production Company ?[0,1]");
        q=scan.nextInt();
        if(q==1)
            movi.set_company();
        
        System.out.println("Do you need to delete Pruction Country ?[0,1]");
        q=scan.nextInt();
        if(q==1)
            movi.set_country();
        
        System.out.println("Do you need to delete Runtime ?[0,1]");
        q=scan.nextInt();
        if(q==1)
            movi.set_runtime();
        
        System.out.println("Do you need to delete Adults Contrain ?[0,1]");
        q=scan.nextInt();
        if(q==1)
            movi.set_adult();
        
        System.out.println("Do you need to delete Popularity ?[0,1]");
        q=scan.nextInt();
        if(q==1)
            movi.set_popularity();
        
    }
    
    
}