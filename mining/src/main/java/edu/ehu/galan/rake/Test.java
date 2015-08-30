package edu.ehu.galan.rake;

//import edu.ehu.galan.rake.model.Token;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by joseph on 27/08/2015.
 */
public class Test {
    public static void main(String[] args) {
        /*List<LinkedList<Token>> tokenizedSentenceList;
        List<String> sentenceList;
        POSTagger tagger = new POSTagger();
        Chunker chunker = new Chunker();
        boolean first = true;
        parser = new PlainToTokenParser(new WordSplitter(new SentenceSplitter(pFile)));
        String sentence = "";
        LinkedList<Token> tokenList = null;
        for (LBJ2.nlp.seg.Token word = (LBJ2.nlp.seg.Token) parser.next(); word != null;
             word = (LBJ2.nlp.seg.Token) parser.next()) {
            String chunked = chunker.discreteValue(word);
            tagger.discreteValue(word);
            if (first) {
                tokenList = new LinkedList<>();
                tokenizedSentenceList.add(tokenList);
                first = false;
            }
            tokenList.add(new Token(word.form, word.partOfSpeech, null, chunked));
            sentence = sentence + " " + (word.form);
            if (word.next == null) {
                sentenceList.add(sentence);
                first = true;
                sentence = "";
            }
        }
        parser.reset();*/
    }
}
