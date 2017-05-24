package com.marfeel.crawler;

import com.marfeel.config.CrawlerInitializer;
import com.marfeel.config.JpaConfiguration;
import com.marfeel.config.TestConfiguration;
import com.marfeel.config.WebMvcConfig;
import com.marfeel.site.SiteRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import javax.servlet.ServletContext;
import java.util.List;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration test for the application
 *
 * @author Adrián Martín Sánchez
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestConfiguration.class, JpaConfiguration.class, CrawlerInitializer.class, WebMvcConfig.class})
@WebAppConfiguration
public class CrawlerIT {

    @Autowired
    private WebApplicationContext wac;
    @Autowired
    SiteRepository siteRepository;

    private MockMvc mockMvc;

    private final String urlNotMarfeelizable = "marfeel.com";
    private final String urlMarfeelizable = "noticias24.com";
    private final String urlWithoutResponse = "asasdfasd.com";
    private final String json = "[{\"url\":\"".concat(urlNotMarfeelizable).concat("\"},{\"url\":\"").concat(urlMarfeelizable).concat("\"}]");
    private final String jsonRepeated = "[{\"url\":\"".concat(urlNotMarfeelizable).concat("\"},{\"url\":\"").concat(urlMarfeelizable).concat("\"},{\"url\":\"").concat(urlNotMarfeelizable).concat("\"}]");
    private final String jsonWithoutResponse = "[{\"url\":\"".concat(urlNotMarfeelizable).concat("\"},{\"url\":\"").concat(urlMarfeelizable).concat("\"},{\"url\":\"").concat(urlWithoutResponse).concat("\"}]");


    @Before
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        siteRepository.deleteAll();
    }

    @Test
    public void crawlerControllerTest() throws Exception {
        ServletContext servletContext = wac.getServletContext();

        assertNotNull(servletContext);
        assertTrue(servletContext instanceof MockServletContext);
        assertNotNull(wac.getBean("crawlerController"));
    }

    @Test
    public void mappingCrawlTest() throws Exception {

        this.mockMvc.perform(post("/crawl").content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andExpect(content().string("In progress"));
    }

    @Test
    public void mappingCrawlFutureTest() throws Exception {

        MvcResult mvcResult = this.mockMvc.perform(post("/crawlFuture").content(json).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(request().asyncStarted())
                .andExpect(request().asyncResult(instanceOf(List.class)))
                .andReturn();

        this.mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[?(@.url=='".concat(urlNotMarfeelizable).concat("')].marfeelizable")).value(false))
                .andExpect(jsonPath("$[?(@.url=='".concat(urlMarfeelizable).concat("')].marfeelizable")).value(true));
    }

    @Test
    public void mappingCrawlFutureRepeatedSitesTest() throws Exception {

        MvcResult mvcResult = this.mockMvc.perform(post("/crawlFuture").content(jsonRepeated).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(request().asyncStarted())
                .andExpect(request().asyncResult(instanceOf(List.class)))
                .andReturn();

        this.mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[?(@.url=='".concat(urlNotMarfeelizable).concat("')].marfeelizable")).value(false))
                .andExpect(jsonPath("$[?(@.url=='".concat(urlMarfeelizable).concat("')].marfeelizable")).value(true));
    }

    @Test
    public void mappingCrawlFutureWithoutResponseTest() throws Exception {

        MvcResult mvcResult = this.mockMvc.perform(post("/crawlFuture").content(jsonWithoutResponse).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(request().asyncStarted())
                .andExpect(request().asyncResult(instanceOf(List.class)))
                .andReturn();

        this.mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[?(@.url=='".concat(urlNotMarfeelizable).concat("')].marfeelizable")).value(false))
                .andExpect(jsonPath("$[?(@.url=='".concat(urlMarfeelizable).concat("')].marfeelizable")).value(true));
    }
}
