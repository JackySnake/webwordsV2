import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.io.IOUtils;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.util.Triple;

import java.util.List;

public class NERExample {

    public static void main(String[] args) throws Exception {

        String serializedClassifier = "stanford_classifiers/english.all.3class.distsim.crf.ser.gz";

        AbstractSequenceClassifier<CoreLabel> classifier = CRFClassifier.getClassifier(serializedClassifier);

        String fileContents = IOUtils.slurpFile("data/test.txt");
        List<Triple<String, Integer, Integer>> list = classifier.classifyToCharacterOffsets(fileContents);
        for (Triple<String, Integer, Integer> item : list) {
            System.out.println(item.first() + ": " + fileContents.substring(item.second(), item.third()));
        }
    }
}
