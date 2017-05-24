package com.marfeel.crawler;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;

/**
 * Unit test for CrawlerUrlFormatter
 *
 * @author Adrián Martín Sánchez
 */
@RunWith(MockitoJUnitRunner.class)
public class CrawlerUrlFormatterTest {

    private CrawlerUrlFormatter crawlerUrlFormatter = new CrawlerUrlFormatter();

    final String protocol = "https";

    @Test
    public void testFormatUrlWithProtocol() throws Exception {

        final String url = "https://www.marfeel.com";

        assertEquals(url, crawlerUrlFormatter.formatUrl(url, protocol));
    }

    @Test
    public void testFormatUrlWithoutProtocol() throws Exception {

        final String url = "www.marfeel.com";

        assertEquals(protocol.concat("://").concat(url), crawlerUrlFormatter.formatUrl(url, protocol));
    }

    @Test
    public void testFormatUrlWithProtocolEncoded() throws Exception {

        final String urlEncoded = "https://www.marfeel.com/%28encoded-test%29";
        final String urlExpected = "https://www.marfeel.com/(encoded-test)";

        assertEquals(urlExpected, crawlerUrlFormatter.formatUrl(urlEncoded, protocol));
    }

    @Test
    public void testFormatUrlWithoutProtocolEncoded() throws Exception {

        final String urlEncoded = "www.marfeel.com/%28encoded-test%29";
        final String urlExpected = "https://www.marfeel.com/(encoded-test)";

        assertEquals(urlExpected, crawlerUrlFormatter.formatUrl(urlEncoded, protocol));
    }
}

