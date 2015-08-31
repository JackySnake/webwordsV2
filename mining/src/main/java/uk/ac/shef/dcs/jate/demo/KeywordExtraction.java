package uk.ac.shef.dcs.jate.demo;

import net.didion.jwnl.JWNLException;
import uk.ac.shef.dcs.jate.JATEException;
import uk.ac.shef.dcs.jate.core.algorithm.RAKEAlgorithm;
import uk.ac.shef.dcs.jate.core.algorithm.RAKEFeatureWrapper;
import uk.ac.shef.dcs.jate.core.extractor.PhraseExtractor;
import uk.ac.shef.dcs.jate.core.feature.indexer.GlobalIndexBuilderMem;
import uk.ac.shef.dcs.jate.model.CorpusImpl;
import uk.ac.shef.dcs.jate.model.Document;
import uk.ac.shef.dcs.jate.model.InMemoryDocument;
import uk.ac.shef.dcs.jate.model.Term;
import uk.ac.shef.dcs.jate.util.control.Lemmatizer;
import uk.ac.shef.dcs.jate.util.control.StopList;

import java.io.IOException;
import java.util.Date;
import java.util.List;


public class KeywordExtraction {

    public static void main(String[] args) throws IOException, JATEException, JWNLException {


//        String path_to_corpus = "/home/joseph/smartdev/bitbucket/test/abc";//args[0];
        String path_to_output = "/home/joseph/smartdev/bitbucket/out/abc";//args[1];

        System.out.println("Started " + KeywordExtraction.class + " at: " + new Date() + "... For detailed progress see log file jate.log.");

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
        Document document = new InMemoryDocument("While the brain sleeps, it clears out harmful toxins, a process that may reduce the risk of Alzheimer's, researchers say.\n" +
                "During sleep, the flow of cerebrospinal fluid in the brain increases dramatically, washing away harmful waste proteins that build up between brain cells during waking hours, a study of mice found.\n" +
                "\"It's like a dishwasher,\" says Dr. Maiken Nedergaard , a professor of neurosurgery at the University of Rochester and an author of the study in Science.\n" +
                "The results appear to offer the best explanation yet of why animals and people need sleep. If this proves to be true in humans as well, it could help explain a mysterious association between sleep disorders and brain diseases, including Alzheimer's.\n" +
                "Nedergaard and a team of scientists discovered the cleaning process while studying the brains of sleeping mice.\n" +
                "The scientists noticed that during sleep, the system that circulates cerebrospinal fluid through the brain and nervous system was \"pumping fluid into the brain and removing fluid from the brain in a very rapid pace,\" Nedergaard says.\n" +
                "The team discovered that this increased flow was possible in part because when mice went to sleep, their brain cells actually shrank, making it easier for fluid to circulate. When an animal woke up, the brain cells enlarged again and the flow between cells slowed to a trickle. \"It's almost like opening and closing a faucet,\" Nedergaard says. \"It's that dramatic.\"\n" +
                "Nedergaard's team, which is funded by the National Institute of Neurological Disorders and Stroke , had previously shown that this fluid was carrying away waste products that build up in the spaces between brain cells.\n" +
                "The process is important because what's getting washed away during sleep are waste proteins that are toxic to brain cells, Nedergaard says. This could explain why we don't think clearly after a sleepless night and why a prolonged lack of sleep can actually kill an animal or a person, she says.\n" +
                "So why doesn't the brain do this sort of housekeeping all the time? Nedergaard thinks it's because cleaning takes a lot of energy. \"It's probably not possible for the brain to both clean itself and at the same time [be] aware of the surroundings and talk and move and so on,\" she says.\n" +
                "The brain-cleaning process has been observed in rats and baboons, but not yet in humans, Nedergaard says. Even so, it could offer a new way of understanding human brain diseases including Alzheimer's. That's because one of the waste products removed from the brain during sleep is beta amyloid, the substance that forms sticky plaques associated with the disease.\n" +
                "That's probably not a coincidence, Nedergaard says. \"Isn't it interesting that Alzheimer's and all other diseases associated with dementia, they are linked to sleep disorders,\" she says.\n" +
                "Researchers who study Alzheimer's say Nedergaard's research could help explain a number of recent findings related to sleep. One of these involves how sleep affects levels of beta amyloid, says Randall Bateman , a professor of neurology Washington University in St. Louis who wasn't involved in the study.\n" +
                "\"Beta amyloid concentrations continue to increase while a person is awake,\" Bateman says. \"And then after people go to sleep that concentration of beta amyloid decreases. This report provides a beautiful mechanism by which this may be happening.\"\n" +
                "The report also offers a tantalizing hint of a new approach to Alzheimer's prevention, Bateman says. \"It does raise the possibility that one might be able to actually control sleep in a way to improve the clearance of beta amyloid and help prevent amyloidosis that we think can lead to Alzheimer's disease.\"\n");
        corpus.add(document);
        List<String> candidates = indexbuilder.build(corpus, npextractor);


        RAKEAlgorithm rakeAlgorithm = new RAKEAlgorithm();
        RAKEFeatureWrapper wrapper = new RAKEFeatureWrapper(candidates);
        Term[] result = rakeAlgorithm.execute(wrapper);

        for (Term term : result) {
            System.out.println(term.getConcept() + " " + term.getConfidence());
        }

        /*AlgorithmTester tester = new AlgorithmTester();
        tester.registerAlgorithm(new RAKEAlgorithm(), new RAKEFeatureWrapper(candidates));
        tester.execute(path_to_output);*/

        System.out.println("Ended at: " + new Date());


    }
}