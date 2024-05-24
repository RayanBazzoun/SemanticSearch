package org.example;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import opennlp.tools.stemmer.PorterStemmer;
import opennlp.tools.tokenize.SimpleTokenizer;
import org.languagetool.JLanguageTool;
import org.languagetool.language.BritishEnglish;
import org.languagetool.rules.Rule;
import org.languagetool.rules.RuleMatch;

import java.io.IOException;
import java.util.ArrayList;

public class SemanticSearch {
    // Method to process the text data
    public static List<String> processText(String text) throws IOException {
        // Tokenize the text
        SimpleTokenizer tokenizer = SimpleTokenizer.INSTANCE;
        String[] tokens = tokenizer.tokenize(text);

        // Normalize tokens to lowercase
       // tokens = Arrays.stream(tokens).map(String::toLowerCase).toArray(String[]::new);

        // Remove stop words
        List<String> stopWords = List.of("is","what","did","to" ,"I","i","the", "and", "a","this","at","?","give", "can","me","of","who", "how","my"); // Add more stop words as needed
        List<String> filteredTokens = Arrays.stream(tokens)
                .filter(token -> !stopWords.contains(token))
                .collect(Collectors.toList());

        // Stem the tokens
        PorterStemmer stemmer = new PorterStemmer();
        List<String> stemmedTokens = filteredTokens.stream()
                .map(stemmer::stem)
                .collect(Collectors.toList());



       String joinedString = String.join(" ",filteredTokens);

        StanfordCoreNLP stanfordCoreNLP= NERimplementation.getPipeline();
        CoreDocument coreDocument5= new CoreDocument(joinedString);
        stanfordCoreNLP.annotate(coreDocument5);
        List<CoreLabel> coreLabels= coreDocument5.tokens();
        List<String> namedEntities= new ArrayList<>();
        for (CoreLabel coreLabel: coreLabels)
        {
            String ner=coreLabel.get(CoreAnnotations.NamedEntityTagAnnotation.class);
            if(!ner.equals("O")){
                namedEntities.add(ner);
            }
        }

        List<String> enrichedInput = combineTokensAndEntities(stemmedTokens, namedEntities);




        List<String> grammaticallyCorrectTokens= checkAndCorrectTokens(enrichedInput);
        List<String> finalTokens =new ArrayList<>();

        for (String str : grammaticallyCorrectTokens)
        {
            finalTokens.add(str.toLowerCase());
        }
        return finalTokens;





    }




    public static List<String> combineTokensAndEntities(List<String> tokens, List<String> namedEntities)
    {
        // Combine named entities with stemmed tokens
        List<String> combined = new ArrayList<>(tokens);
        combined.addAll(namedEntities);
        return combined;
    }

    public static List<String> checkAndCorrectTokens(List<String> tokens) {
        List<String> correctedTokens = new ArrayList<>();
        JLanguageTool langTool = new JLanguageTool(new BritishEnglish());
        for (Rule rule : langTool.getAllRules()) {
            if (!rule.isDictionaryBasedSpellingRule()) {
                langTool.disableRule(rule.getId());
            }
        }
        for (String token : tokens) {
            List<RuleMatch> matches = null;
            try {
                matches = langTool.check(token);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (matches.size() > 0) {
                List<String> replacements = matches.get(0).getSuggestedReplacements();
                if (replacements.size() > 0) {
                    correctedTokens.add(replacements.get(0));
                } else {
                    correctedTokens.add(token);
                }
            } else {
                correctedTokens.add(token);
            }
        }
        return correctedTokens;
    }




}
