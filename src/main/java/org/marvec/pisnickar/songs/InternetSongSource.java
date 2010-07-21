package org.marvec.pisnickar.songs;

import java.io.IOException;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author marvec
 */
public class InternetSongSource implements SongSource, Serializable {
    private String server = "http://www.marvec.org/songs/";
    private boolean enabled = true;
    private int count = -1;

    public void open(String id, String name) throws IOException {
        this.server = name;

        try {
            Document d = parseURL("get.php");
            Node n = d.getDocumentElement();
            count = Integer.parseInt(n.getTextContent());
        } catch (Exception ex) {
            throw new IOException(ex);
        }
    }

    public String getId() {
        return "Písničkář";
    }

    public String getType() {
        return "Internet";
    }

    public Song getSong(String id) throws IOException {
        Song s = new Song();

        try {
            Document d = parseURL("get.php?s=" + URLEncoder.encode(id, "UTF-8"));
            Node n = d.getDocumentElement();
            s.setText(n.getTextContent());
            s.setAuthorMusic(n.getAttributes().getNamedItem("music").getNodeValue());
            s.setAuthorText(n.getAttributes().getNamedItem("text").getNodeValue());
            s.setTitle(n.getAttributes().getNamedItem("name").getNodeValue());
            s.setTags(n.getAttributes().getNamedItem("tags").getNodeValue().split(" "));
        } catch (Exception ex) {
            throw new IOException(ex);
        }

        return s;
    }

    public String storeSong(String id, Song song) throws IOException {
        throw new UnsupportedOperationException("Read-only resource.");
    }

    public List<SearchResult> search(String query) throws IOException {
        query = query.replaceAll("\\*", "%");

        List<SearchResult> res = new LinkedList<SearchResult>();
        try {
            Document d = parseURL("search.php?q=" + URLEncoder.encode(query, "UTF-8"));

            NodeList nl = d.getDocumentElement().getChildNodes();
            for (int i = 0; i < nl.getLength(); i++) {
                Node n = nl.item(i);
                if ("song".equals(n.getNodeName())) {
                    SearchResult sr = new SearchResult(this, n.getAttributes().getNamedItem("id").getNodeValue());
                    res.add(sr);
                }
            }
        } catch (Exception ex) {
            throw new IOException(ex);
        }

        return res;
    }

    private HttpURLConnection connect(String query) throws MalformedURLException, IOException {
        HttpURLConnection c = (HttpURLConnection) (new URL(server + query)).openConnection();
        return c;
    }

    private Document parseURL(String query) throws IOException {
        try {
            HttpURLConnection c = connect(query);
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            try {
                c.connect();
            } catch (IOException e) {
                enabled = false;
            }
            Document d = db.parse(c.getInputStream());
            c.disconnect();
            return d;
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    public int getCount() {
        return count;
    }

    public void flush() throws IOException {
        // nothing we can de
    }

    public void close() throws IOException {
        // nothing we can do
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isReadOnly() {
        return true;
    }

    public boolean isDirty() {
        return false;
    }

}
