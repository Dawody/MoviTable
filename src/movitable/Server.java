/**
 * 
 */
package movitable;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.AbstractMap;
import java.util.ArrayList;
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

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        

        List<Thread> readers = new ArrayList<Thread>();
        FilmDB db = new FilmDB();
        Movi movi = null;
        ServerSocket ss = new ServerSocket(9876);
        System.out.println("Server start");
        int x =0;
        
        
        while(true){
            Socket s = ss.accept();
            x++;
            InputStream is = s.getInputStream();
            ObjectInputStream ois = new ObjectInputStream(is);
            movi = (Movi) ois.readObject();
            if(movi.get_operation()==1)
            {
                //read operation
                
                readers.add(new Thread( new FilmReader(db, movi)));
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
                db.setFilm(movi);
                db.filmEditor();

                
                
                
                
                
                
                
            }
            
        }
        
        
        
        
        
        
        
        
    }




}


class FilmDB{
    List<Movi> movis = new ArrayList<Movi>();
    Movi movi;
    String row_key;
    

    public List<Movi> getDB(){
        return this.movis;
    }
    
    public  void setFilm(Movi movi){
        this.movi = movi;
    }
    
    
    public  void filmEditor(){
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
                
                break;
            default :
                break;
                
        }
    }    
    
}

class FilmReader implements Runnable{
    Movi movi;
    List<Movi> db ;
    public FilmReader(FilmDB filmdb ,Movi movi) {
        this.movi = movi;
        this.db = filmdb.getDB();
    }
    
    
    public void filmReader(){
        for (Movi movi1 : db) {
            if(movi1.get_row_key().equals(movi.get_row_key()))
            {
                System.out.println("Send movi1 to client");
            }
            
        }

    }

    @Override
    public void run() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        filmReader();
    }


}