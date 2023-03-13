package crawler;

import com.typesafe.config.Config;
import constants.ConfigConstants;
import constants.HTMLElements;
import constants.UrlConstants;
import enums.HttpStatus;
import mapper.Mapper;
import models.Portfolio;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utilities.UrlUtility;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class Crawler {
    private static final Logger LOG = LoggerFactory.getLogger(Crawler.class);
    private static Config config;
    private static UrlUtility urlUtil;
    private static Mapper mapper;

    public Crawler(Config config, UrlUtility urlUtil, Mapper mapper) {
        this.config = config;
        this.urlUtil = urlUtil;
        this.mapper = mapper;
    }

    public static void crawl(Config config) {
        boolean isPortfolio = config.getBoolean(ConfigConstants.IS_PORTFOLIO);
        if (isPortfolio) {
            crawl(UrlConstants.PORTFOLIO_URL);
        } else {
            crawl(1, UrlConstants.DEFAULT_URL, new HashSet<>());
        }
    }

    private static void crawl(int level, String url, HashSet<String> visited) {
        //Based on application config or default of 5
        int maxDepth = config.getInt(ConfigConstants.MAX_DEPTH) > 0 ? config.getInt(ConfigConstants.MAX_DEPTH) : 5;
        if (level <= maxDepth) {
            Document document = request(url, visited);
            if (document != null) {
                //Fetch all a tag elements with href attributes
                List<Element> links = document.select(HTMLElements.LINK_ELEMENT_SELECTOR).stream()
                        .filter(urlUtil::isElementCrawlable)
                        .collect(Collectors.toList());
                System.out.println(String.format("Crawlable links on page with URL: %s - ", url) + links.stream().map(urlUtil::retrieveElementUrl).collect(Collectors.toList()));
                for (Element link : links) {
                    //Get absolute URL for subsequent request
                    String nextLink = link.absUrl(HTMLElements.LINK_URL);
                    if (!visited.contains(nextLink)) {
                        crawl(level++, nextLink, visited);
                    }
                }
            }
        }
    }

    private static void crawl(String url) {
        Document document = request(url, new HashSet<>());
        if (document != null) {
            Elements portfolios = document.select(HTMLElements.DIV_PORTFOLIO_INFO);
            List<Portfolio> portfolioInformation = portfolios.stream()
                    .map(mapper::toPortfolio)
                    .collect(Collectors.toList());
            System.out.println(portfolioInformation);
        }
    }

    private static Document request(String url, HashSet<String> visited) {
        try {
            Connection con = Jsoup.connect(url);
            Document document = con.get();
            if (con.response().statusCode() == HttpStatus.OK.code()) {
                visited.add(url);
                return document;
            }
            LOG.error("Error reaching URL : {} with status code : {}", url, con.response().statusCode());
            //If we fail to connect, add to visited to ensure we don't make same request twice
            visited.add(url);
        } catch (IOException e) {
            LOG.error("Error reaching URL : {} with exception : {}", url, e);
            //If we fail to connect, add to visited to ensure we don't make same request twice
            visited.add(url);
        }
        return null;
    }
}
