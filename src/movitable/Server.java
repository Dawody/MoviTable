/**
 * Server has List of Movis stored into ArrayList<Movi> . there is no external Data storage for server!
 * Server waits for coming sockets from client
 * As soon as server accept a socket ; server check the operation type
 * If reading operation ,i call thread of FilmReader and continue accepting new sockets
 * If writing operation ,i join all FilmReader threads and start check the writing operation type
 * If writing operation = add row ; then start processing the comming socket in details to extract the new row details
 * If writing operation = delete  time stamp ofrow ; then i start searching for the film that has the same name as the requested film , then i delete it from my List of movis
 * If writing operation = set cell ; then is start to search for the updated columns and update it also in my List of movis
 * If writing operation = delete cell ; then
 * 
 * 
 * Server periodically update his List with master
 */
package movitable;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.ParseException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dawod
 */
public class Server{

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException, ParseException {
        

        List<Thread> readers = new ArrayList<Thread>();
        
        Movi movi = null;
        ServerSocket ss = new ServerSocket(9876);
        FilmDB db = new FilmDB();
        System.out.println("Server start");
        int x =0;
        
        
        //get data from master
//        System.out.println("SERVER ASK MASTER TO SEND LIST OF MOVIS");
        
        
        Socket s_master = new Socket("localhost", 3333);

        //ask omar : how will i ask you to send data ?
        System.out.println("SERVER READY TO RECIEVE LIST OF DATA FROM MASTER");
        
        
        
        
        //then take data from master
            
            x++;
            InputStream is_master = s_master.getInputStream();
            ObjectInputStream ois_master = new ObjectInputStream(is_master);
            db.setList((List<Movi>) ois_master.readObject());
        
        
        
        
        
        
        
        //start accept requests
        System.out.println("SERVER START TO SERVE CLIENTS");
        
        while(true){
            Socket s = ss.accept();
            x++;
            InputStream is = s.getInputStream();
            ObjectInputStream ois = new ObjectInputStream(is);
            movi = (Movi) ois.readObject();
            if(movi.get_operation()==1)
            {
                //read operation
                
                readers.add(new Thread( new FilmReader(db, movi,s)));
                readers.get(readers.size()-1).setName("Reader"+readers.size()+"is working now");
                readers.get(readers.size()-1).start();
                
                
            }
            else
            {
                /**
                 * WRITE OPERATION
                 * 1. join all readers (wait them till finish)
                 * 2. start writing 
                 * 3. socket queue will store the coming requests
                 * 4. when finish come back to accept new sockets and requests
                 */

                
                for(Thread reader : readers)
                {
                    reader.join();
                }
                db.setSocket(s);
                db.setFilm(movi);
                db.filmEditor();

                
                
                
                
                
                
                
            }
            
        }
        
        
        
        
        
        
        
        
    }




}

/**
 * FilmDataBase 
 * this class has he List of Movis that represent the server database
 * this class also is responsible for performing any writing operations
 * @author dawod
 */
class FilmDB{
    List<Movi> movis = new ArrayList<Movi>();
    List<Movi> query = new ArrayList<Movi>();
    Movi movi;
    String row_key;
    Socket s;
    

    public List<Movi> getDB(){
        return this.movis;
    }
    
    public  void setFilm(Movi movi){
        this.movi = movi;
        query.add(movi);
    }
    
    public void setSocket(Socket ss){
        this.s=ss;
    }
    
    
    public  void filmEditor() throws ParseException, IOException{
        switch (movi.get_operation()){
            case 2 :
                movis.add(movi);
                System.out.println("New Film ( "+movi.get_row_key()+" ) is successfully added into server database.");
                break;
            case 3 :
                for (Movi movi1 : movis) {
                    if(movi1.get_row_key().equals(movi.get_row_key()))
                    {
                        movis.remove(movi1);
                        System.out.println("Film ( "+movi.get_row_key()+" ) is successfully deleted from server database.");
                        break;
                    }
                }
                break;
                
            case 4 :
                for (Movi movi1 : movis) {
                    if(movi1.get_row_key().equals(movi.get_row_key()))
                    {   
                        update_set(movi,movi1);
                        System.out.println("Film ( "+movi.get_row_key()+" ) is successfully Updated in server database.");
                        break;
                    }
                }
                break;
                
            case 5 :
                for (Movi movi1 : movis) {
                    if(movi1.get_row_key().equals(movi.get_row_key()))
                    {                        
                        update_delete(movi,movi1);
                        System.out.println("Film ( "+movi.get_row_key()+" ) is successfully Updated in server database.");
                        break;
                    }
                }
            case 7 : //omar must send me operation = 7 to update data periodically
                update_master();
                
                break;
            default :
                break;
                
        }
    } 
    

    public void update_master() throws IOException{
        
        
        
        System.out.println("SEND QUERIES TO MASTER TO UPDATE DATABASE");
        OutputStream os = s.getOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(os);
        oos.writeObject(query);
        
        oos.flush();
        oos.close();
        os.close();
        
    }
    
    
    public static void update_set(Movi from,Movi to) throws ParseException{
        
        int counter=0;
        for (Date date : from.get_release())
        {
            if(counter>0)
                to.set_release(date);
            counter++;
        }
        
        counter=0;
        for (String category: from.get_category())
        {
            if(counter>0)
                to.set_category(category);
            counter++;
        }
        
        counter=0;
        for (String overview: from.get_overview())
        {
            if(counter>0)
                to.set_overview(overview);
            counter++;
        }
        
        counter=0;
        for (String language: from.get_language())
        {
            if(counter>0)
                to.set_language(language);
            counter++;
        }
        
        counter=0;
        for (String company: from.get_company())
        {
            if(counter>0)
                to.set_company(company);
            counter++;
        }
        
        counter=0;
        for (String country: from.get_country())
        {
            if(counter>0)
                to.set_country(country);
            counter++;
        }
        
        counter=0;
        for (float runtine: from.get_runtime())
        {
            if(counter>0)
                to.set_runtime(runtine);
            counter++;
        }

        counter=0;
        for (boolean adult: from.get_adult())
        {
            if(counter>0)
                to.set_adult(adult);
            counter++;
        }
        
        counter=0;
        for (float popularity: from.get_popularity())
        {
            if(counter>0)
                to.set_popularity(popularity);
            counter++;
        }
        
    }
    
    
    public static void update_delete(Movi from,Movi to) throws ParseException{
        int x,y;
        try{
            x=0;
            for (Date date : from.get_release())
            {
                y=0;
                if(x>0)
                {
                    System.out.println("from.get_release = "+date);
                    for (Date date_ : to.get_release())
                        if(y>0)
                        {
                            System.out.println("to.get_release() = "+date_);                            
                            if(date_ == date)
                                to.delete_release(date);
                        }
                        else
                            y++;
                }
                
                x++;
            }
        }catch(Exception e ){System.err.println("error in date");}
        

        try{
            x=0;
            for (String overview : from.get_overview())
            {
                y=0;
                if(x>0)
                {
//                    System.out.println("from.get_overview = "+overview);
                    for (String overview_ : to.get_overview())
                        if(y>0)
                        {
//                            System.out.println("to.get_overview() = "+overview_);
                            
                            if(overview_.equals(overview))
                            {
                                to.delete_overview(overview);
                                break;
                            }
                            
                        }
                        else
                            y++;
                }
                x++;
            }
        }catch(Exception e ){System.err.println("error in overview");}
        
        
        

        try{
            x=0;
            for (String category : from.get_category())
            {
                y=0;
                if(x>0)
                {
//                    System.out.println("from.get_category() = "+category);
                    for (String category_ : to.get_category())
                        if(y>0)
                        {
  //                          System.out.println("to.get_category() = "+category_);
                            if(category_.equals(category))
                            {try{
                                to.delete_category(category);
                                break;
                            }catch(Exception e){System.err.println("category error!");}
 //                           System.out.println("GOTIT!!!!");
                            
                            }
                        }
                        else
                            y++;
                }
                x++;
            }
        }catch(Exception e ){System.err.println("error in category");}
        
        
        try{
            x=0;
            for (String language : from.get_language())
            {
                y=0;
                if(x>0)
                    for (String language_ : to.get_language())
                        if(y>0)
                        {
                            if(language_.equals(language))
                            {
                                to.delete_language(language);
                                break;
                            }
                        }
                        else
                            y++;
                x++;
            }
        }catch(Exception e ){System.err.println("error in language");}

        
        try{
            x=0;
            for (String company : from.get_company())
            {
                y=0;
                if(x>0)
                    for (String company_ : to.get_company())
                        if(y>0)
                        {
                            if(company_.equals(company))
                            {
                                to.delete_company(company);
                                break;
                            }
                        }
                        else
                            y++;
                x++;
            }
        }catch(Exception e ){System.err.println("error in company");}


        try{
            x=0;
            for (String country : from.get_country())
            {
                y=0;
                if(x>0)
                    for (String country_ : to.get_country())
                        if(y>0)
                        {
                            if(country_.equals(country))
                            {
                                to.delete_country(country);
                                break;
                            }
                        }
                        else
                            y++;
                x++;
            }
        }catch(Exception e ){System.err.println("error in country");}


        try{
            x=0;
            for (Float runtime : from.get_runtime())
            {
                y=0;
                if(x>0)
                    for (Float runtime_ : to.get_runtime())
                        if(y>0)
                        {
                            if(runtime_ == runtime)
                            {
                                to.delete_runtime(runtime);
                                break;
                            }
                        }
                        else
                            y++;
                x++;
            }
        }catch(Exception e ){System.err.println("error in runtime");}



        

        try{
            x=0;
            for (Boolean adult : from.get_adult())
            {
                y=0;
                if(x>0)
                {
                    System.out.println("from.get_adult = "+adult);                    
                    for (Boolean adult_ : to.get_adult())
                        if(y>0)
                        {
                            System.out.println("to.get_adult() = "+adult_);
                            if((adult_ && adult)==false)
                            {                                
                                to.delete_adult(adult);
                                break;
                            }
                        }
                        else
                            y++;
                }
                x++;
            }
        }catch(Exception e ){System.err.println("error in adult");}

        
        try{
            x=0;
            for (Float popularity : from.get_popularity())
            {
                y=0;
                if(x>0)
                {
                    System.out.println("from.get_popularity = "+popularity);                    
                    for (Float popularity_ : to.get_popularity())
                        if(y>0)
                        {
                            System.out.println("to.get_popularity() = "+popularity_);
                            if(popularity_.toString().equals(popularity.toString()) )
                            {
                                to.delete_popularity(popularity);
                                System.out.println("GOTIT in POPULARITY");
                                break;
                            }
                        }
                        else
                            y++;
                }
                x++;
            }
        }catch(Exception e ){System.err.println("error in popularity");}
    
    }

    void setList(List<Movi> list) {
        this.movis = list;
        System.out.println("NOW SERVER HAS LIST OF MOVIS");
    }
    

    
    
    
    
    
    
    
}


/**
 * Film Reader is responsible for seraching for the requqested row and send it back to the client
 * 
 * @author dawod
 */
class FilmReader implements Runnable{
    Movi movi;
    List<Movi> db ;
    Socket s;
    public FilmReader(FilmDB filmdb ,Movi movi,Socket s) {
        this.movi = movi;
        this.db = filmdb.getDB();
        this.s =s;
    }
    
    
    public void filmReader() throws IOException{
        boolean flag = true;
        for (Movi movi1 : db) {
            if(movi1.get_row_key().equals(movi.get_row_key()))
            {
                System.out.println("Send movi1 to client");
                OutputStream os = s.getOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(os);
                oos.writeObject(movi1);
                
                flag = false;
                oos.flush();
                oos.close();
                os.close();
                
            }
            
        }
        
        if(flag)
        {
            Movi nullMovi = new Movi("nullMovi", 0);            
            System.out.println("Send nullMovi1 to client");
            OutputStream os = s.getOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(os);
            oos.writeObject(nullMovi);
            
            oos.flush();
            oos.close();
            os.close();
            
        }
        
        
    }

    @Override
    public void run() {
        try {
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            filmReader();
        } catch (IOException ex) {
            Logger.getLogger(FilmReader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


}