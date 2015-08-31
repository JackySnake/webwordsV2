package uk.ac.shef.dcs.jate.processing;

import net.didion.jwnl.JWNLException;
import uk.ac.shef.dcs.jate.JATEException;
import uk.ac.shef.dcs.jate.core.algorithm.RAKEAlgorithm;
import uk.ac.shef.dcs.jate.core.algorithm.RAKEFeatureWrapper;
import uk.ac.shef.dcs.jate.core.extractor.PhraseExtractor;
import uk.ac.shef.dcs.jate.core.feature.indexer.GlobalIndexBuilderMem;
import uk.ac.shef.dcs.jate.model.CorpusImpl;
import uk.ac.shef.dcs.jate.model.Document;
import uk.ac.shef.dcs.jate.model.Term;
import uk.ac.shef.dcs.jate.util.control.Lemmatizer;
import uk.ac.shef.dcs.jate.util.control.StopList;

import java.io.IOException;
import java.util.List;


public class KeywordExtraction {

    private Document document;

    public KeywordExtraction(Document document) {
        this.document = document;
    }

    public Term[] extractKeywords() throws IOException, JWNLException, JATEException {

        //creates instances of required processors and resources

        //stop word list
        StopList stop = new StopList(true);

        //lemmatiser
        Lemmatizer lemmatizer = new Lemmatizer();

        //noun phrase extractor
        PhraseExtractor npextractor = new PhraseExtractor(stop, lemmatizer);

        GlobalIndexBuilderMem indexbuilder = new GlobalIndexBuilderMem();

        //build the global resource index

//        List<String> candidates = indexbuilder.build(new CorpusImpl(path_to_corpus), npextractor);
        CorpusImpl corpus = new CorpusImpl();
        corpus.add(document);
        List<String> candidates = indexbuilder.build(corpus, npextractor);


        RAKEAlgorithm rakeAlgorithm = new RAKEAlgorithm();
        RAKEFeatureWrapper wrapper = new RAKEFeatureWrapper(candidates);
        Term[] result = rakeAlgorithm.execute(wrapper);

        /*for (Term term : result) {
            System.out.println(term.getConcept() + " " + term.getConfidence());
        }*/

        /*AlgorithmTester tester = new AlgorithmTester();
        tester.registerAlgorithm(new RAKEAlgorithm(), new RAKEFeatureWrapper(candidates));
        tester.execute(path_to_output);*/

//        System.out.println("Ended at: " + new Date());

        return result;
    }
}