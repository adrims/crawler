package com.marfeel.site;

/**
 * Interface for marfeelizable classes
 *
 * @author Adrián Martín Sánchez
 */
public interface Marfeelizable {

    /**
     * Method for check if the site is marfeelizable
     *
     * @param text The site to check
     * @return true if the text is marfeelizable (contains "news" or "noticias"), false otherwise
     */
    boolean isMarfeelizable(String text);
}
