package org.example;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;


import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SearchDocuments implements SearchDocumentsInterface {

    public static List<String> retunedDocuments = new ArrayList<>();

    public void searchTextFiles(String directoryPath, List<String> stemmedTokens) throws IOException {
        File directory = new File(directoryPath);
        File[] textFiles = directory.listFiles((dir, name) -> name.toLowerCase().endsWith(".txt"));

        for (File textFile : textFiles) {
            BufferedReader reader = new BufferedReader(new FileReader(textFile));
            String line;
            StringBuilder textBuilder = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                textBuilder.append(line).append("\n");
            }
            reader.close();

            String text = textBuilder.toString();
            text= text.toLowerCase();
            int tokenCount = 0;
            for (String token : stemmedTokens) {
                if (text.contains(token)) {
                    tokenCount++;
                }
            }
//            double percentageMatch = (double) tokenCount / stemmedTokens.size();
//            if (percentageMatch >= 0.25) {

            if(tokenCount>6){
                System.out.println("Match found in: " + textFile.getName());
                // Process the matching text file as needed
                String s= "Match found in: " + textFile.getName();
                retunedDocuments.add(s);
            }
        }
    }


    public void searchPdfFiles(String directoryPath, List<String> stemmedTokens) throws IOException {
        File directory = new File(directoryPath);
        File[] pdfFiles = directory.listFiles((dir, name) -> name.toLowerCase().endsWith(".pdf"));

        for (File pdfFile : pdfFiles) {
            PDDocument document = PDDocument.load(pdfFile);
            PDFTextStripper textStripper = new PDFTextStripper();
            String pdfText = textStripper.getText(document);
            pdfText= pdfText.toLowerCase();
            int tokenCount = 0;
            for (String token : stemmedTokens) {
                if (pdfText.contains(token)) {
                    tokenCount++;
                }
            }
//            double percentageMatch = (double) tokenCount / stemmedTokens.size();
//            if (percentageMatch >= 0.25) {
                if(tokenCount>6){
                System.out.println("Match found in: " + pdfFile.getName());
                // Process the matching PDF file as needed
                String s= "Match found in: " + pdfFile.getName();
                retunedDocuments.add(s);
            }
            document.close();
        }
    }

    public void searchWordDocuments(String directoryPath, List<String> stemmedTokens) throws IOException, TikaException, SAXException, TikaException, SAXException {
        File directory = new File(directoryPath);
        File[] wordFiles = directory.listFiles((dir, name) -> name.toLowerCase().endsWith(".docx"));

        for (File wordFile : wordFiles) {
            Parser parser = new AutoDetectParser();
            StringWriter textBuffer = new StringWriter();
            InputStream input = new FileInputStream(wordFile);
            Metadata md = new Metadata();
            md.set(Metadata.RESOURCE_NAME_KEY, wordFile.getName());
            parser.parse(input, new BodyContentHandler(textBuffer), md, new ParseContext());

            String text = textBuffer.toString().toLowerCase();
            int tokenCount = 0;
            for (String token : stemmedTokens) {
                if (text.contains(token)) {
                    tokenCount++;
                }
            }
//            double percentageMatch = (double) tokenCount / stemmedTokens.size();
//            if (percentageMatch >= 0.25) {
            if(tokenCount>6){
                System.out.println("Match found in: " + wordFile.getName());
                // Process the matching Word document as needed
                String s= "Match found in: " + wordFile.getName();
                retunedDocuments.add(s);
            }

        }
    }
}
