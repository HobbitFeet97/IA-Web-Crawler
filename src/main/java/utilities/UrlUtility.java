package utilities;

import com.typesafe.config.Config;
import constants.ConfigConstants;
import constants.HTMLElements;
import org.jsoup.nodes.Element;

public final class UrlUtility {
    private static Config config;

    public UrlUtility(Config config) {
        UrlUtility.config = config;
    }

    public boolean isElementCrawlable(Element element) {
        boolean crawlExternal = config.getBoolean(ConfigConstants.CRAWL_EXTERNAL);
        if (!crawlExternal) {
            //Presuming for the minute that any "href" element with leading / is an "internal" url
            return element.attr(HTMLElements.LINK_URL).startsWith("/");
        }
        return true;
    }

    public String retrieveElementUrl(Element element) {
        return element.attr(HTMLElements.LINK_URL);
    }
}
