package uk.ac.shef.dcs.jate;

import net.didion.jwnl.JWNLException;
import uk.ac.shef.dcs.jate.core.algorithm.AlgorithmTester;
import uk.ac.shef.dcs.jate.core.algorithm.RAKEAlgorithm;
import uk.ac.shef.dcs.jate.core.algorithm.RAKEFeatureWrapper;
import uk.ac.shef.dcs.jate.core.extractor.PhraseExtractor;
import uk.ac.shef.dcs.jate.core.feature.indexer.GlobalIndexBuilderMem;
import uk.ac.shef.dcs.jate.model.CorpusImpl;
import uk.ac.shef.dcs.jate.util.control.Lemmatizer;
import uk.ac.shef.dcs.jate.util.control.StopList;

import java.io.IOException;
import java.util.Date;
import java.util.List;


public class TestRAKE {

    public static void main(String[] args) throws IOException, JATEException, JWNLException {


        String path_to_corpus = "/home/joseph/smartdev/bitbucket/test/abc";//args[0];
        String path_to_output = "/home/joseph/smartdev/bitbucket/out/abc";//args[1];

        System.out.println("Started " + TestRAKE.class + "at: " + new Date() + "... For detailed progress see log file jate.log.");

        //creates instances of required processors and resources

        //stop word list
        StopList stop = new StopList(true);

        //lemmatiser
        Lemmatizer lemmatizer = new Lemmatizer();

        //noun phrase extractor
        PhraseExtractor npextractor = new PhraseExtractor(stop, lemmatizer);

        GlobalIndexBuilderMem indexbuilder = new GlobalIndexBuilderMem();

        //build the global resource index

        List<String> candidates = indexbuilder.build(new CorpusImpl(path_to_corpus), npextractor);


        AlgorithmTester tester = new AlgorithmTester();
        tester.registerAlgorithm(new RAKEAlgorithm(), new RAKEFeatureWrapper(candidates));
        tester.execute(path_to_output);
        System.out.println("Ended at: " + new Date());


    }
}