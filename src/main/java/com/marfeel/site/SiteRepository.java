package com.marfeel.site;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for Site class
 *
 * @author Adrián Martín Sánchez
 */
public interface SiteRepository extends JpaRepository<Site, Integer> {

    Site findByUrl(String url);
}
