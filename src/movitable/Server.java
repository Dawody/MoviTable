/**
 * 
 */
package movitable;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dawod
 */
public class Server {
   
    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(9697);
        Socket s = ss.accept();
        
        
        
        
        
    }
}
