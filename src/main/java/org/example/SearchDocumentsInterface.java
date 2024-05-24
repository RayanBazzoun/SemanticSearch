package org.example;

import org.apache.tika.exception.TikaException;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.List;

public interface SearchDocumentsInterface {

    public  void searchTextFiles(String directoryPath, List<String> stemmedTokens) throws IOException;

    public void searchPdfFiles(String directoryPath, List<String> stemmedTokens)throws IOException;

    public void searchWordDocuments(String directoryPath, List<String> stemmedTokens) throws IOException, TikaException, SAXException;

}
