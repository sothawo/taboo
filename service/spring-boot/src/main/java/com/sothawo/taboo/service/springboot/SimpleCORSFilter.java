/**
 * Copyright (c) 2015 sothawo
 *
 * http://www.sothawo.com
 */
package com.sothawo.taboo.service.springboot;

import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Filter that sets CORS headers in the response.
 * TODO: configure allowed origins
 *
 * @author P.J. Meisch (pj.meisch@sothawo.com).
 */
@Component
public class SimpleCORSFilter implements Filter {

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException,
            ServletException {
        Collection<String> allowedOrigins = Arrays.asList("localhost", "127.0.0.1");

        HttpServletRequest request = (HttpServletRequest) req;
        String origin = Optional.ofNullable(request.getHeader("Origin")).orElse("");

        if (!origin.isEmpty()) {
            try {
                URL url = new URL(origin);
                String host = url.getHost();
                if (allowedOrigins.contains(host)) {
                    HttpServletResponse response = (HttpServletResponse) res;
                    response.setHeader("Access-Control-Allow-Origin", origin);
                    response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, OPTIONS, DELETE");
                    response.setHeader("Access-Control-Max-Age", "3600");
                    response.setHeader("Access-Control-Allow-Headers", "x-requested-with, Content-Type");
                    response.setHeader("Vary", "Origin");
                }
            } catch (MalformedURLException ignored) {
                // ignored
            }
        }

        chain.doFilter(req, res);
    }

    public void init(FilterConfig filterConfig) {
    }

    public void destroy() {
    }

}
