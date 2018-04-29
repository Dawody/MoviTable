import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.*;

//class addData extends Thread {
//    Socket s1;
//
//    public addData(Socket s1) {
//        this.s1 = s1;
//    }
//
//    private void update(String z, Object w, String title) {
//        Bson scores;
//        scores = pushEach(z, (List) w);
//        UpdateResult updateResult = Main.collection.updateMany(eq("original_title", title), scores);
//    }
//
//    private void delete(String z, Object w, String title) {
//        Bson scores;
//        scores = pullAll(z, (List) w);
//        UpdateResult updateResult = Main.collection.updateMany(eq("original_title", title), scores);
//    }
//
//    public void run() {
//        try {
//            ObjectInput in = new ObjectInputStream(s1.getInputStream());
//            List<Movi> movies = (ArrayList<Movi>)in.readObject();
//            for(Movi movi : movies)
//            {
//                switch (movi.get_operation()) {
//                    case 2:
//                        Document doc = new Document("original_title", movi.get_row_key());
//                        Main.collection.insertOne(doc);
//                        // add
////                    break;
//                    case 4:
//                        // update
//                        update("adult", movi.get_adult(), movi.get_row_key());
//                        update("runtime", movi.get_runtime(), movi.get_row_key());
//                        update("genres", movi.get_category(), movi.get_row_key());
//                        update("original_language", movi.get_language(), movi.get_row_key());
//                        update("production_companies", movi.get_company(), movi.get_row_key());
//                        update("production_countries", movi.get_country(), movi.get_row_key());
//                        update("release_date", movi.get_release(), movi.get_row_key());
//                        update("overview", movi.get_overview(), movi.get_row_key());
//                        break;
//                    case 3:
//                        // update
//                        Main.collection.deleteOne(eq("original_title", movi.get_row_key()));
//                        break;
//                    case 5:
//                        // delete cell
//                        delete("adult", movi.get_adult(), movi.get_row_key());
//                        delete("runtime", movi.get_runtime(), movi.get_row_key());
//                        delete("genres", movi.get_category(), movi.get_row_key());
//                        delete("original_language", movi.get_language(), movi.get_row_key());
//                        delete("production_companies", movi.get_company(), movi.get_row_key());
//                        delete("production_countries", movi.get_country(), movi.get_row_key());
//                        delete("release_date", movi.get_release(), movi.get_row_key());
//                        delete("overview", movi.get_overview(), movi.get_row_key());
//                        break;
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//    }
//}

class Metadata extends Thread {
    Socket s1;

    public Metadata(Socket s1) {
        this.s1 = s1;
    }

    public void run() {
        try {
            ObjectInput in = new ObjectInputStream(s1.getInputStream());
            String name = (String) in.readObject();
            ObjectOutput out = new ObjectOutputStream(s1.getOutputStream());
//            if (name.toLowerCase().charAt(0) <= 'a' - 13) {
//                out.writeObject(1);
//            } else if (name.toLowerCase().charAt(0) >= 'a' - 18) {
//                out.writeObject(3);
//            } else {
//                out.writeObject(2);
//            }
            if (name.toLowerCase().charAt(0) - 'a' <= 12) {
                out.writeObject(1);
            } else {
                out.writeObject(2);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

class SendData extends Thread {
    Socket s1;

    public SendData(Socket s1) {
        this.s1 = s1;
    }

    private List cast_list(List<Object> list) {
        List<String> cusList = new ArrayList<String>();

        for (Object o : list) {
            cusList.add(o.toString());
        }

        return cusList;
    }

    public void run() {
        try {
            List<Movi> movies = new ArrayList<Movi>();
            boolean first = true, first2 = true;
            if (first) {
                first2 = true;
                Main.first = false;
            } else {
                first2 = false;
            }
            for (Document cur : Main.collection.find()) {
                //            cur.get
                String x = cur.getString("original_title");
                if ((first2 && (x.toLowerCase().charAt(0) - 'a' <= 12)) || (!first2 && (x.toLowerCase().charAt(0) - 'a' > 12))) {
                    Movi movie = new Movi(x, 1);
                    movie.setOperation(1);
                    movie.setTitle(x);
//                    java.lang.Class<ArrayList>/
                    movie.setAdult((List) cur.get("adult", new ArrayList<>()));
                    movie.setRuntime((ArrayList<Float>) (List) cur.get("runtime", new ArrayList<>()));
                    movie.setCategory(cast_list((List) cur.get("genres", new ArrayList<>())));
                    movie.setLanguage(cast_list((List) cur.get("spoken_languages", new ArrayList<>())));
                    movie.setCompany(cast_list((List) cur.get("production_companies", new ArrayList<>())));
                    movie.setCountry(cast_list((List) cur.get("production_countries", new ArrayList<>())));
                    movie.setRelease((List) cur.get("release_date", new ArrayList<Float>()));
                    movie.setOverview(cast_list((List) cur.get("overview", new ArrayList<>())));
                    movies.add(movie);
                }

//                System.out.println( cur.getString("original_title"));
            }
            ObjectOutput out = new ObjectOutputStream(s1.getOutputStream());
//            for(int i=0;i<movies.size();i++)
//            {
//                System.out.println(i);
//                out.writeObject(movies.get(i));
//            }
            out.writeObject(movies);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


class threader extends Thread {
    ServerSocket ss1;
    int port;

    public threader(int port) throws Exception {
        this.port = port;
        this.ss1 = new ServerSocket(port);
    }

    public void run() {

        try {
            while (true) {
                Socket s1 = ss1.accept();
                if (this.port == 1111) {
                    new Metadata(s1).start();
                } else {
                    new SendData(s1).start();
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}


class forUpdating extends Thread {


    private void update(String z, Object w, String title) {
        Bson scores;
        scores = pushEach(z, (List) w);
        UpdateResult updateResult = Main.collection.updateMany(eq("original_title", title), scores);
    }


    private void delete(String z, Object w, String title) {
        Bson scores;
        scores = pullAll(z, (List) w);
        UpdateResult updateResult = Main.collection.updateMany(eq("original_title", title), scores);
    }

    public forUpdating() {

    }

    public void run() {


        try {
            while (true) {
                Thread.sleep(30 *   // minutes to sleep
                        60 *   // seconds to a minute
                        1000); // milliseconds to a second
                Socket s1 = new Socket("localhost", 9999);
                ObjectInput in = new ObjectInputStream(s1.getInputStream());
                List<Movi> movies = (ArrayList<Movi>) in.readObject();
                for (Movi movi : movies) {
                    switch (movi.get_operation()) {
                        case 2:
                            Document doc = new Document("original_title", movi.get_row_key());
                            Main.collection.insertOne(doc);
                            // add
//                    break;
                        case 4:
                            // update
                            update("adult", movi.get_adult(), movi.get_row_key());
                            update("runtime", movi.get_runtime(), movi.get_row_key());
                            update("genres", movi.get_category(), movi.get_row_key());
                            update("original_language", movi.get_language(), movi.get_row_key());
                            update("production_companies", movi.get_company(), movi.get_row_key());
                            update("production_countries", movi.get_country(), movi.get_row_key());
                            update("release_date", movi.get_release(), movi.get_row_key());
                            update("overview", movi.get_overview(), movi.get_row_key());
                            break;
                        case 3:
                            // update
                            Main.collection.deleteOne(eq("original_title", movi.get_row_key()));
                            break;
                        case 5:
                            // delete cell
                            delete("adult", movi.get_adult(), movi.get_row_key());
                            delete("runtime", movi.get_runtime(), movi.get_row_key());
                            delete("genres", movi.get_category(), movi.get_row_key());
                            delete("original_language", movi.get_language(), movi.get_row_key());
                            delete("production_companies", movi.get_company(), movi.get_row_key());
                            delete("production_countries", movi.get_country(), movi.get_row_key());
                            delete("release_date", movi.get_release(), movi.get_row_key());
                            delete("overview", movi.get_overview(), movi.get_row_key());
                            break;
                    }
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

}


public class Main {
    public static MongoCollection<Document> collection;
    public static boolean first = true;

    public static void upd(String z, Object w, String title) {
        Bson scores;
        scores = pushEach(z, (List) w);
        UpdateResult updateResult = Main.collection.updateMany(eq("original_title", title), scores);
    }

    private static List cast_list(List<Object> list) {
        List<String> cusList = new ArrayList<String>();

        for (Object o : list) {
            cusList.add(o.toString());
        }

        return cusList;
    }


    public static void main(String args[]) throws Exception {

        // Creating a Mongo client
        MongoClient mongoClient = new MongoClient("localhost", 27017);
        MongoDatabase myMongoDb = mongoClient.getDatabase("mp2");
        collection = myMongoDb.getCollection("data");
        new threader(1111).start();
//        new threader(2222).start();
        new forUpdating().start();
        new threader(3333).start();
    }
}