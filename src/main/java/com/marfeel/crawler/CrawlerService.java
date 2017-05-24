package com.marfeel.crawler;

import com.marfeel.site.Site;
import com.marfeel.site.SiteMarfeelizable;
import com.marfeel.site.SiteRepository;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Performe the avaible features to crawl urls
 *
 * @author Adrián Martín Sánchez
 */
@Service
public class CrawlerService {

    @Autowired
    private SiteMarfeelizable siteMarfeelizable;
    @Autowired
    private SiteRepository siteRepository;
    @Autowired
    private CrawlerUrlFormatter crawlerUrlFormatter;
    @Autowired
    private CrawlerHttpParser crawlerHttpParser;

    private String protocol = "http";

    /**
     * Async method, crawls the site and save it
     *
     * @param site The site to check
     */
    @Async
    public void processSite(Site site) {
        Site siteOld = siteRepository.findByUrl(site.getUrl());
        if (siteOld == null) {
            saveSite(site);
        } else {
            saveSite(siteOld);
        }
    }

    /**
     * Crawls the site and save it if it is marfeelizable
     *
     * @param site The site to check
     */
    private void saveSite(Site site) {
        String url = crawlerUrlFormatter.formatUrl(site.getUrl(), protocol);
        Document doc = crawlerHttpParser.getDocument(url);
        Elements elements = crawlerHttpParser.getElements(doc, SiteMarfeelizable.TAG_TO_SEARCH);

        if (siteMarfeelizable.isMarfeelizable(elements.text())) {
            site.setMarfeeliable(true);
        }
        siteRepository.save(site);
    }

    /**
     * Crawls the site and save it
     *
     * @param site The site to check
     * @return the site processed
     */
    public Site processSiteFuture(Site site) {
        Site siteOld = siteRepository.findByUrl(site.getUrl());
        if (siteOld == null) {
            return saveSiteFuture(site);
        } else {
            return saveSiteFuture(siteOld);
        }
    }

    /**
     * Crawls the site check if it is marfeelizable and save it
     *
     * @param site The site to check
     * @return the site processed
     */
    private Site saveSiteFuture(Site site) {
        String url = crawlerUrlFormatter.formatUrl(site.getUrl(), protocol);
        Document doc = crawlerHttpParser.getDocument(url);
        Elements elements = crawlerHttpParser.getElements(doc, SiteMarfeelizable.TAG_TO_SEARCH);

        if (siteMarfeelizable.isMarfeelizable(elements.text())) {
            site.setMarfeeliable(true);
        }
        siteRepository.save(site);
        return site;
    }
}
