package uk.ac.shef.dcs.jate;

public class TestJustesonKatz {
    /*public static void main(String[] args) throws IOException, JATEException, JWNLException {


        String path_to_corpus = args[0];
        String path_to_output = args[1];

        //String path_to_corpus= JATEProperties.getInstance().getCorpusPath();
        //String path_to_output= JATEProperties.getInstance().getResultPath();

        System.out.println("Started " + TestJustesonKatz.class + "at: " + new Date() + "... For detailed progress see log file jate.log.");

        //creates instances of required processors and resources

        //stop word list
        StopList stop = new StopList(true);
        //lemmatiser
        Lemmatizer lemmatizer = new Lemmatizer();

        //noun phrase extractor
        CandidateTermExtractor npextractor = new NounPhraseExtractorOpenNLP(stop, lemmatizer);
        //CandidateTermExtractor npextractor = new NGramExtractor(stop, lemmatizer);
        //counters
        TermFreqCounter npcounter = new TermFreqCounter();
        WordCounter wordcounter = new WordCounter();

        //create global resource index builder, which indexes global resources, such as documents and terms and their
        //relations
        GlobalIndexBuilderMem builder = new GlobalIndexBuilderMem();
        //build the global resource index
        GlobalIndexMem termDocIndex = builder.build(new CorpusImpl(path_to_corpus), npextractor);

		*//*newly added for improving frequency count calculation: begins*//*


        TermVariantsUpdater update = new TermVariantsUpdater(termDocIndex, stop, lemmatizer);


        GlobalIndexMem termIndex = update.updateVariants();

        FeatureCorpusTermFrequency termCorpusFreq =
                new FeatureBuilderCorpusTermFrequency(npcounter, wordcounter, lemmatizer).build(termIndex);
			
		
		*//*newly added for improving frequency count calculation: ends*//*

        //build a feature store required by the tfidf algorithm, using the processors instantiated above
        //		FeatureCorpusTermFrequency termCorpusFreq =
        //			new FeatureBuilderCorpusTermFrequency(npcounter, wordcounter, lemmatizer).build(termDocIndex);


        AlgorithmTester tester = new AlgorithmTester();
        tester.registerAlgorithm(new JustesonAlgorithm(), new FrequencyFeatureWrapper(termCorpusFreq));

        tester.execute(termDocIndex, path_to_output);
        System.out.println("Ended at: " + new Date());
    }*/
}