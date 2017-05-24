package com.marfeel.site;

import org.springframework.stereotype.Service;

/**
 * Service for siteMarfeelizable
 *
 * @author Adrián Martín Sánchez
 */
@Service
public class SiteMarfeelizable implements Marfeelizable {

    public final static String TAG_TO_SEARCH = "title";
    private final String[] keyWordsList = {"news", "noticias"};

    @Override
    public boolean isMarfeelizable(String text) {
        for (String key: keyWordsList) {
            if (text.toLowerCase().contains(key)) {
                return true;
            }
        }
        return false;
    }
}
