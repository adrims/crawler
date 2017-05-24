package com.marfeel.crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * com.marfeel.crawler
 *
 * @author Adrián Martín Sánchez
 */
@Component
public class CrawlerHttpParser {

    private final Logger logger = Logger.getLogger(CrawlerHttpParser.class.getName());

    /**
     * Returns the document page for a given URL.
     *
     * @param url The given url
     * @return The document page
     */
    public Document getDocument(String url) {
        try {
            return Jsoup.connect(url).get();
        } catch (IOException e) {
            final String error = String.format("Cannot retrieve url: %s", url);
            logger.severe(error);
            throw new IllegalStateException(error);
        }
    }

    /**
     * Returns the page elements for a given CSS query.
     *
     * @param document The given document
     * @param query The given CSS query
     * @return The elemnts list
     */
    public Elements getElements(Document document, String query) {
        return document.select(query);
    }
}
