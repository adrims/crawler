package com.marfeel.crawler;

import com.marfeel.site.Site;
import com.marfeel.site.SiteMarfeelizable;
import com.marfeel.site.SiteRepository;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import java.util.Date;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

/**
 * Unit test for CrawlerService
 *
 * @author Adrián Martín Sánchez
 */
@RunWith(MockitoJUnitRunner.class)
public class CrawlerServiceTest {


    @InjectMocks
    CrawlerService crawlerService;
    @Mock
    private SiteMarfeelizable siteMarfeelizable;
    @Mock
    private SiteRepository siteRepository;
    @Mock
    private CrawlerUrlFormatter crawlerUrlFormatter;
    @Mock
    private CrawlerHttpParser crawlerHttpParser;
    @Captor
    private ArgumentCaptor<Site> siteArgumentCaptor;

    private Document document = mock(Document.class);
    private Elements elements = mock(Elements.class);
    private String protocol = "http";

    @Test
    public void processSiteFalseTest() throws Exception {

        Site site = new Site.Builder()
                .url("")
                .isMarfeelizable(false)
                .lastStatusDate(new Date())
                .build();

        when(siteRepository.findByUrl(site.getUrl())).thenReturn(null);
        when(crawlerUrlFormatter.formatUrl(site.getUrl(),protocol)).thenReturn(site.getUrl());
        when(crawlerHttpParser.getDocument(site.getUrl())).thenReturn(document);
        when(crawlerHttpParser.getElements(document, SiteMarfeelizable.TAG_TO_SEARCH)).thenReturn(elements);
        when(siteMarfeelizable.isMarfeelizable(elements.text())).thenReturn(false);

        crawlerService.processSite(site);

        verify(siteRepository).save(siteArgumentCaptor.capture());
        assertFalse(siteArgumentCaptor.getValue().isMarfeelizable());

    }

    @Test
    public void processSiteTrueTest() throws Exception {

        Site site = new Site.Builder()
                .url("")
                .isMarfeelizable(false)
                .lastStatusDate(new Date())
                .build();

        when(siteRepository.findByUrl(site.getUrl())).thenReturn(null);
        when(crawlerUrlFormatter.formatUrl(site.getUrl(),protocol)).thenReturn(site.getUrl());
        when(crawlerHttpParser.getDocument(site.getUrl())).thenReturn(document);
        when(crawlerHttpParser.getElements(document, SiteMarfeelizable.TAG_TO_SEARCH)).thenReturn(elements);
        when(siteMarfeelizable.isMarfeelizable(elements.text())).thenReturn(true);

        crawlerService.processSite(site);

        verify(siteRepository).save(siteArgumentCaptor.capture());
        assertTrue(siteArgumentCaptor.getValue().isMarfeelizable());

    }

    @Test
    public void processSiteFutureFalseTest() throws Exception {

        Site site = new Site.Builder()
                .url("")
                .isMarfeelizable(false)
                .lastStatusDate(new Date())
                .build();

        when(siteRepository.findByUrl(site.getUrl())).thenReturn(null);
        when(crawlerUrlFormatter.formatUrl(site.getUrl(),protocol)).thenReturn(site.getUrl());
        when(crawlerHttpParser.getDocument(site.getUrl())).thenReturn(document);
        when(crawlerHttpParser.getElements(document, SiteMarfeelizable.TAG_TO_SEARCH)).thenReturn(elements);
        when(siteMarfeelizable.isMarfeelizable(elements.text())).thenReturn(false);

        Site siteReturn = crawlerService.processSiteFuture(site);

        verify(siteRepository).save(siteArgumentCaptor.capture());
        assertFalse(siteArgumentCaptor.getValue().isMarfeelizable());
        assertFalse(siteReturn.isMarfeelizable());

    }

    @Test
    public void processSiteFutureTrueTest() throws Exception {

        Site site = new Site.Builder()
                .url("")
                .isMarfeelizable(false)
                .lastStatusDate(new Date())
                .build();

        when(siteRepository.findByUrl(site.getUrl())).thenReturn(null);
        when(crawlerUrlFormatter.formatUrl(site.getUrl(),protocol)).thenReturn(site.getUrl());
        when(crawlerHttpParser.getDocument(site.getUrl())).thenReturn(document);
        when(crawlerHttpParser.getElements(document, SiteMarfeelizable.TAG_TO_SEARCH)).thenReturn(elements);
        when(siteMarfeelizable.isMarfeelizable(elements.text())).thenReturn(true);

        Site siteReturn = crawlerService.processSiteFuture(site);

        verify(siteRepository).save(siteArgumentCaptor.capture());
        assertTrue(siteArgumentCaptor.getValue().isMarfeelizable());
        assertTrue(siteReturn.isMarfeelizable());

    }
}
