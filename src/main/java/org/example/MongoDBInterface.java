package org.example;

import java.util.List;

public interface MongoDBInterface {
    public Boolean isFound(String query);
    public void create(String query, List<String> documents);
    public  void read(String query);
    public void update(String query, String newQuery);
    public void delete(String query);
}
