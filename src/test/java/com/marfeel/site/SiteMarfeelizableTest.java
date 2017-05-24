package com.marfeel.site;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

/**
 * Unit test for SiteMarfeelizable
 *
 * @author Adrián Martín Sánchez
 */
@RunWith(MockitoJUnitRunner.class)
public class SiteMarfeelizableTest {

    private SiteMarfeelizable siteMarfeelizable = new SiteMarfeelizable();

    @Test
    public void testIsMarfeelizable() throws Exception {
        String textMarfeelizable = "This is a text marfeelizable beacause has news";
        String textMarfeelizable2 = "This is a text marfeelizable beacause has noticias";
        String textNotMarfeelizable = "This is a text not marfeelizable";

        assertEquals(true,siteMarfeelizable.isMarfeelizable(textMarfeelizable));
        assertEquals(true,siteMarfeelizable.isMarfeelizable(textMarfeelizable2));
        assertEquals(false,siteMarfeelizable.isMarfeelizable(textNotMarfeelizable));
    }
}
