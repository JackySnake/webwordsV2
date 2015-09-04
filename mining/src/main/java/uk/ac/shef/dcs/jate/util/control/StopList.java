package uk.ac.shef.dcs.jate.util.control;

import java.io.*;
import java.util.HashSet;

/**
 * Represents a stop word list. These are the words which usually should not occur in a valid term
 *
 * @author <a href="mailto:z.zhang@dcs.shef.ac.uk">Ziqi Zhang</a>
 */


public class StopList extends HashSet<String> {

    private boolean _caseSensitive;

    /**
     * Creates an instance of stop word list
     *
     * @param caseSensitive whether the list should ignore cases
     * @throws IOException
     */
    public StopList(final boolean caseSensitive) throws IOException {
        super();
        _caseSensitive = caseSensitive;
        /*loadStopList(new File(
                JATEProperties.getInstance().getNLPPath() + "/stoplist.txt"), _caseSensitive);*/
        loadStopList(getClass().getResourceAsStream("/stoplist.txt"), _caseSensitive);
    }

    /**
     * @param word
     * @return true if the word is a stop word, false if otherwise
     */
    public boolean isStopWord(String word) {
        if (_caseSensitive) return contains(word.toLowerCase());
        return contains(word);
    }



    private void loadStopList(final File stopListFile, final boolean lowercase) throws IOException {
        final BufferedReader reader = new BufferedReader(new FileReader(stopListFile));
        String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.equals("") || line.startsWith("//")) continue;
            if (lowercase) this.add(line.toLowerCase());
            else this.add(line);
        }
    }

    private void loadStopList(final InputStream stopListFile, final boolean lowercase) throws IOException {
        final BufferedReader reader = new BufferedReader(new InputStreamReader(stopListFile, "UTF-8"));
        String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.equals("") || line.startsWith("//")) continue;
            if (lowercase) this.add(line.toLowerCase());
            else this.add(line);
        }
    }

}
