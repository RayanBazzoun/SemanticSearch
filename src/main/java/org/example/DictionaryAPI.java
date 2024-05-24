package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


public class DictionaryAPI {
    private final String USER_AGENT = "Mozilla/5.0";
    public List searchSynonymsForWords(List<String> wordsArrayList) throws Exception {
        List<String> highScoreSynonyms = new ArrayList<>(); // New ArrayList to store high-score synonyms
        String[] wordsArray = wordsArrayList.stream().toArray(String[]::new);
        for (String word : wordsArray) {
            System.out.println("Searching synonyms for " + word);
            String url = "https://api.datamuse.com/words?ml=" + word;
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", USER_AGENT);
            int responseCode = con.getResponseCode();
            System.out.println("JSON Response: " + responseCode);

            // Read the response
            StringBuilder response;
            try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
                String inputLine;
                response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
            }

            // Convert JSON array to ArrayList of words
            ObjectMapper mapper = new ObjectMapper();
            try {
                ArrayList<Word> synonyms = mapper.readValue(response.toString(),
                        mapper.getTypeFactory().constructCollectionType(ArrayList.class, Word.class));

                if (synonyms.size() > 0) {
                    for (Word synonym : synonyms) {
                        if (synonym.getScore() > 15000000) {
                            highScoreSynonyms.add(synonym.getWord()); // Add high-score synonyms to the new ArrayList
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return highScoreSynonyms;
    }

    // Word and score attributes are from DataMuse API
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class Word {
        private String word;
        private int score;

        public String getWord() {
            return this.word;
        }

        public int getScore() {
            return this.score;
        }


    }
}
