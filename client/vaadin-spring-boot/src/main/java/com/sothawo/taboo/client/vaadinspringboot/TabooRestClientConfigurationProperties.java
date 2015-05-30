/**
 * Copyright (c) 2015 sothawo
 *
 * http://www.sothawo.com
 */
package com.sothawo.taboo.client.vaadinspringboot;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration for the taboo Rest client.
 *
 * @author P.J. Meisch (pj.meisch@sothawo.com).
 */
@ConfigurationProperties(prefix = "taboo.restclient")
public class TabooRestClientConfigurationProperties {
// ------------------------------ FIELDS ------------------------------

    /** the service host */
    private Host host;

    /** the base context path */
    private String context;

// --------------------- GETTER / SETTER METHODS ---------------------

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public Host getHost() {
        return host;
    }

    public void setHost(
            Host host) {
        this.host = host;
    }

// -------------------------- INNER CLASSES --------------------------

    public static class Host {
// ------------------------------ FIELDS ------------------------------

        /** the connection protocol */
        private String protocol = "http";

        /** the name */
        private String name = "localhost";

        /** the port */
        private Integer port = 8080;

// --------------------- GETTER / SETTER METHODS ---------------------

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getPort() {
            return port;
        }

        public void setPort(Integer port) {
            this.port = port;
        }

        public String getProtocol() {
            return protocol;
        }

        public void setProtocol(String protocol) {
            this.protocol = protocol;
        }
    }
}
