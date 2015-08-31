package uk.ac.shef.dcs.jate.model;


import java.net.URL;

/**
 * @author <a href="mailto:z.zhang@dcs.shef.ac.uk">Ziqi Zhang</a>
 */


public class InMemoryDocument implements Document {

    protected String _text;

    public InMemoryDocument(String text) {
        _text = text;
    }

    @Override
    public URL getUrl() {
        return null;
    }

    @Override
    public String getContent() {
        return _text;
    }
}
