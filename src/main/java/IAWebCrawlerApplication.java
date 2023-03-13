import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import crawler.Crawler;
import mapper.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utilities.UrlUtility;

import java.util.ArrayList;
import java.util.HashSet;

import static com.typesafe.config.ConfigFactory.defaultOverrides;
import static com.typesafe.config.ConfigFactory.parseResourcesAnySyntax;

public class IAWebCrawlerApplication {
    private static final Logger LOG = LoggerFactory.getLogger(IAWebCrawlerApplication.class);
    public static void main(String[] args) {
        ConfigFactory.invalidateCaches();
        LOG.info("Loading application config.");
        Config config = defaultOverrides()
                .withFallback(parseResourcesAnySyntax("application.conf"))
                .resolve();
        LOG.info("Config loaded {}", config.getConfig("app"));
        UrlUtility urlUtility = new UrlUtility(config);
        Mapper mapper = new Mapper();
        Crawler crawler = new Crawler(config, urlUtility, mapper);
        crawler.crawl(config);
    }
}
