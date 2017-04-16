import com.mongodb.*;
import com.sun.org.apache.xpath.internal.SourceTree;

import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Sentes on 16.04.2017.
 */
public class MongoDBCon {
    private String  host;
    private int port;
    private String database;
    private MongoClient mongoClient;
    private DB db;

    public void init(String host, int port, String database) {
        try {
            mongoClient = new MongoClient( host , port);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            System.out.println("Host not found");
        }
        db = mongoClient.getDB(database);

    }

    public DBCollection GetCollection(String collection) {
        return this.db.getCollection("posts");
    }

    public static void main(String[] args) throws UnknownHostException {
        MongoDBCon mongocon = new MongoDBCon();
        mongocon.init("localhost", 27017, "test");
        mongocon.find("posts");

    }

    public int insert(String collection, Map<Object, Object> pairs){
        BulkWriteOperation builder = GetCollection(collection).initializeOrderedBulkOperation();
        DBObject doc = new BasicDBObject();
        Iterator it = pairs.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            doc.put(pair.getKey().toString(), pair.getValue());
            it.remove(); // avoids a ConcurrentModificationException
        }
        builder.insert(doc);
        BulkWriteResult result = builder.execute();
        return result.getInsertedCount();

    }
    public void find(){

    }
    public void find(String collection ){
        DBCollection coll = GetCollection(collection);
        DBCursor cursor = coll.find();
        System.out.println(coll.getCount());
        try {
            while(cursor.hasNext()) {
                System.out.println(cursor.next());
            }
        } finally {
            cursor.close();
        }
    }
}
