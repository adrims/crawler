package com.marfeel.site;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import java.util.Date;

/**
 * Class for Site entity
 *
 * @author Adrián Martín Sánchez
 */
@Entity
public class Site {

    @JsonIgnore
    @Id
    @GeneratedValue
    private Integer id;

    @Column(unique = true)
    private String url;

    private boolean isMarfeelizable;

    private Date lastStatusDate;

    public Site(){
        this.lastStatusDate = new Date();
    }

    public Site(Builder builder) {
        this.url = builder.url;
        this.isMarfeelizable = builder.isMarfeelizable;
        this.lastStatusDate = builder.lastStatusDate;
    }

    public Integer getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public boolean isMarfeelizable(){
        return isMarfeelizable;
    }

    public void setMarfeeliable(boolean marfeelizable) {
        isMarfeelizable = marfeelizable;
    }

    public Date getLastStatusDate() {
        return lastStatusDate;
    }

    @Override
    public boolean equals(Object object) {

        if (object == null) {
            return false;
        }
        if (object == this) {
            return true;
        }
        if (!(object instanceof Site)) {
            return false;
        }
        Site site = (Site)object;
        return (this.url.equals(site.getUrl()));
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = url.hashCode();
        result = (int) (temp ^ (temp >>> 32));
        return result;
    }

    public static class Builder {
        private String url;
        private boolean isMarfeelizable;
        private Date lastStatusDate;

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder isMarfeelizable(Boolean isMarfeelizable) {
            this.isMarfeelizable = isMarfeelizable;
            return this;
        }

        public Builder lastStatusDate(Date lastStatusDate) {
            this.lastStatusDate = lastStatusDate;
            return this;
        }

        public Site build() {
            return new Site(this);
        }
    }

}
