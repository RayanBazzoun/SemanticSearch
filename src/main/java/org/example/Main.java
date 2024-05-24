package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        DictionaryAPI dictionaryAPI = new DictionaryAPI();
        MongoDBcrud mongoDBcrud= new MongoDBcrud();
        SearchDocuments searchDocuments= new SearchDocuments();
  //      "Haidar works at Google"
  //      "who is the current president ?"
  //      "give me types of artists"
  //      "how can i end my gym membership"
  //      "what did messi win in football"
        Scanner scanner =new Scanner(System.in);
        System.out.println("Enter your query");
        String query=scanner.nextLine();


        if(mongoDBcrud.isFound(query))
        {
            System.out.println("Document found! Searching through Database");
            mongoDBcrud.read(query);

        }
        else{

            System.out.println("No document matches the provided query in collection ");
            List<String> tokens= SemanticSearch.processText(query);
        List<String> synonyms= dictionaryAPI.searchSynonymsForWords(tokens);

          //   some synonyms are returned as sentences which need to be split into tokens then added to the tokens List

             List<String> synonymWords= new ArrayList<>();
            for(int i=0;i<synonyms.size();i++)
            {
                String line = synonyms.get(i);
                synonymWords= List.of(line.split(" "));
                tokens.addAll(synonymWords);
            }

            for(int i=0;i<tokens.size();i++)
            {
                System.out.println(tokens.get(i));
            }


            searchDocuments.searchTextFiles("C:\\Users\\LENOVO\\Desktop\\New folder", tokens);
            searchDocuments.searchPdfFiles("C:\\Users\\LENOVO\\Desktop\\New folder",tokens);
            searchDocuments.searchWordDocuments("C:\\Users\\LENOVO\\Desktop\\New folder",tokens);

            if(!SearchDocuments.retunedDocuments.isEmpty())
            {
                mongoDBcrud.create(query,SearchDocuments.retunedDocuments);
            }
            else {
                System.out.println("no match found ");
            }
        }



    }
    }
