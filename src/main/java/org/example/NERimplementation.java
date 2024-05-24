package org.example;

import java.util.Properties;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

import java.util.Properties;
public class NERimplementation {




        private static Properties properties;
        private static String propertiesName="tokenize, ssplit, pos, lemma, ner, parse, sentiment";
        private static StanfordCoreNLP stanfordCoreNLP;

        private NERimplementation() {
        }

        static {
            properties= new Properties();
            properties.setProperty("annotators", propertiesName);
        }

        public static StanfordCoreNLP getPipeline(){
            if(stanfordCoreNLP ==null)
            {
                stanfordCoreNLP = new StanfordCoreNLP(properties);
            }
            return stanfordCoreNLP;
        }


}
