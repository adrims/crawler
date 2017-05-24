package com.marfeel.crawler;

import com.marfeel.config.*;
import com.marfeel.site.Site;
import com.marfeel.site.SiteRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


import java.util.Date;


/**
 * Integration test for the CrawlerService
 *
 * @author Adrián Martín Sánchez
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={TestConfiguration.class, JpaConfiguration.class, CrawlerInitializer.class})
@WebAppConfiguration
public class CrawlerServiceIT {

    @Autowired
    private CrawlerService crawlerService;
    @Autowired
    private SiteRepository siteRepository;

    private final String urlMarfeelizable = "noticias24.com";
    private final String urlNotMarffelizable = "marfeel.com";

    @Before
    public void clearDatabase() throws Exception {
        siteRepository.deleteAll();
    }

    @Test
    public void processSiteTrueTest() throws Exception {
        Site site = new Site.Builder()
                .url(urlMarfeelizable)
                .isMarfeelizable(false)
                .lastStatusDate(new Date())
                .build();

        crawlerService.processSite(site);
        assertTrue(site.isMarfeelizable());
    }

    @Test
    public void processSiteFalseTest() throws Exception {
        Site site = new Site.Builder()
                .url(urlNotMarffelizable)
                .isMarfeelizable(false)
                .lastStatusDate(new Date())
                .build();

        crawlerService.processSite(site);
        assertFalse(site.isMarfeelizable());
    }

    @Test
    public void processSiteFutureTrueTest() throws Exception {
        Site site = new Site.Builder()
                .url(urlMarfeelizable)
                .isMarfeelizable(false)
                .lastStatusDate(new Date())
                .build();

        Site siteReturn = crawlerService.processSiteFuture(site);
        assertTrue(siteReturn.isMarfeelizable());
    }

    @Test
    public void processSiteFutureFalseTest() throws Exception {
        Site site = new Site.Builder()
                .url(urlNotMarffelizable)
                .isMarfeelizable(false)
                .lastStatusDate(new Date())
                .build();

        Site siteReturn = crawlerService.processSiteFuture(site);
        assertFalse(siteReturn.isMarfeelizable());
    }
}
