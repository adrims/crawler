package com.marfeel.crawler;

import com.marfeel.site.Site;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Logger;

import static java.util.concurrent.CompletableFuture.supplyAsync;
import static java.util.stream.Collectors.toList;


/**
 * Controller class for application
 *
 * @author Adrián Martín Sánchez
 */

@RestController
public class CrawlerController {

    @Autowired
    private CrawlerService crawlerService;

    private final Logger logger = Logger.getLogger(CrawlerUrlFormatter.class.getName());

    /**
     * Mapping for use async method
     *
     * @param siteList The list of sites to process
     * @return ResponseEntity with httpStatus Accepted (202)
     */
    @RequestMapping(value = "/crawl", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<String> crawl(@RequestBody List<Site> siteList){
        getMarfeelizableSites(getSetFromList(siteList));
        return new ResponseEntity<>("In progress", HttpStatus.ACCEPTED);
    }

    /**
     * Mapping for use async servlet
     *
     * @param siteList The list of sites to process
     * @return List of sites processed (Future)
     */
    @RequestMapping(value = "/crawlFuture", method = RequestMethod.POST, consumes = "application/json")
    public Future<List<Site>> crawlFuture(@RequestBody List<Site> siteList){
       return getOutput(siteList);
    }

    /**
     * Create a new future to gather results once all of the previous futures complete.
     *
     * @param futures The list of CompletableFuture
     * @return new future with all results of previous futures
     */
    private CompletableFuture<Void> getJobsDone ( List<CompletableFuture<Site>> futures) {
        // Restructure as arr because CompletableFuture.allOf requires it.
        CompletableFuture<?>[] futuresAsVarArgs =
                futures.toArray(new CompletableFuture[futures.size()]);

        CompletableFuture<Void> jobsDone =
                CompletableFuture.allOf(futuresAsVarArgs);
        return jobsDone;
    }

    /**
     * Once all of the futures have completed, build out the result string from the return values of the crawlerService.processSiteFuture calls.
     *
     * @param siteList The list of sites
     * @return list of sites processed
     */
    private CompletableFuture<List<Site>> getOutput (List<Site> siteList) {
        CompletableFuture<List<Site>> siteListFuture = new CompletableFuture<>();
        List<CompletableFuture<Site>> futures = getFutures(siteList);
        CompletableFuture<Void> jobsDone = getJobsDone(futures);

        jobsDone.thenAccept(i -> {
            List<Site> sites = new ArrayList<>();
            futures.forEach(future -> {
                try {
                    Site siteToAdd = future.get();
                    if (siteToAdd != null) {
                        sites.add(siteToAdd);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (ExecutionException e) {
                     Thread.currentThread().interrupt();
                }
            });
            siteListFuture.complete(sites);
        });

        return siteListFuture;
    }

    /**
     * Create list of futures
     *
     * @param siteList The list of sites
     * @return list of futures
     */
    private List<CompletableFuture<Site>> getFutures(List<Site> siteList) {
        List<CompletableFuture<Site>> futures =
                getSetFromList(siteList).stream()
                        .map(site -> supplyAsync(() -> crawlerService.processSiteFuture(site))
                                .exceptionally(f -> null))
                        .collect(toList());
        return futures;
    }

    /**
     * Process the sites list
     *
     * @param siteSet The list of sites to check
     */
    private void getMarfeelizableSites(Set<Site> siteSet) {
        for (Site site: siteSet) {
            crawlerService.processSite(site);
        }
    }

    /**
     * Transfor the List to Set for delete duplicates
     *
     * @param siteList The list of sites to transform
     */
    private Set<Site> getSetFromList(List<Site> siteList) {
        return (siteList!=null) ? new LinkedHashSet<>(siteList) : new LinkedHashSet<>();
    }
}
