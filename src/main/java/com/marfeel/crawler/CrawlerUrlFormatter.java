package com.marfeel.crawler;

import org.springframework.stereotype.Component;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * com.marfeel.util
 *
 * @author Adrián Martín Sánchez
 */
@Component
public class CrawlerUrlFormatter {

    private final Logger logger = Logger.getLogger(CrawlerUrlFormatter.class.getName());


    /**
     * Check the url format and returns url with good format
     *
     * @param url The site url to check
     * @param protocol Protocol for check
     * @return Formatted url
     */
    public String formatUrl(String url, String protocol) {
        return decodeUrl(addProtocol(url, protocol));
    }

    /**
     * Check the url protocol and returns url with protocol
     *
     * @param url The site url to check
     * @param protocol Protocol for check
     * @return Url with protocol
     */
    private String addProtocol(String url, String protocol) {
        String protocolPattern = "^".concat(protocol).concat(":\\/\\/");
        Pattern pattern = Pattern.compile(protocolPattern, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(url);

        return matcher.find() ? url : protocol.concat("://").concat(url);
    }

    /**
     * Decodes an url.
     *
     * @param url The URL to decode
     * @return The url already decoded
     */
    private String decodeUrl(String url) {
        try {
            return URLDecoder.decode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            final String error = String.format("Cannot decode url: %s", url);
            logger.severe(error);
            throw new IllegalStateException(error);
        }
    }

}
