package org.example;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;

import java.util.Iterator;
import java.util.List;

public class MongoDBcrud implements MongoDBInterface {

    static MongoClient mongoClient = new MongoClient("localhost", 27017);
    static MongoDatabase db= mongoClient.getDatabase("semantic");
    static MongoCollection<Document> collection = db.getCollection("history");

    public  Boolean isFound(String query)
    {

        Document searchQuery = new Document("query", query); // replace with your search query
        Document document = collection.find(searchQuery).first();

        if (document != null) {
            return true;
        } else {
            return false;
        }
    }

    public  void create(String query, List<String> documents){
        Document doc =new Document("query",query);
        doc.append("Documents", documents);
        collection.insertOne(doc);
        System.out.println("Insert is completed");
    }
    public  void read(String query)
    {
        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put("query", query);
        MongoCursor<Document> cursor = collection.find(searchQuery).iterator();//hon
        while (cursor.hasNext()) {
            System.out.println(cursor.next());
        }
    }

    public  void update(String query, String newQuery){
        collection.updateOne(Filters.eq("query", query), Updates.set("query", newQuery));
        System.out.println("Document updated successfully");
    }

    public  void delete(String query){
        collection.deleteOne(Filters.eq("query", query));
        System.out.println("Document deleted successfully");
    }


}
