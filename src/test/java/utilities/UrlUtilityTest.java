package utilities;

import com.typesafe.config.Config;
import constants.ConfigConstants;
import constants.HTMLElements;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.BDDMockito.given;

class UrlUtilityTest {

    private UrlUtility underTest;

    @Mock
    private Config config;

    @Mock
    private Element testElement = new Element("test_tag");

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        underTest = new UrlUtility(config);
    }

    @Test
    void whenIsElementCrawlable_WithInternalConfig_AndInternalURL_returnTrue() {
        //Given
        String internalUrl = "/something";
        given(config.getBoolean(ConfigConstants.CRAWL_EXTERNAL)).willReturn(false);
        given(testElement.attr(HTMLElements.LINK_URL)).willReturn(internalUrl);
        //When
        boolean actual = underTest.isElementCrawlable(testElement);
        //Then
        boolean expected = true;
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void whenIsElementCrawlable_WithInternalConfig_AndExternalURL_returnFalse() {
        //Given
        String externalUrl = "https://google.com";
        given(config.getBoolean(ConfigConstants.CRAWL_EXTERNAL)).willReturn(false);
        given(testElement.attr(HTMLElements.LINK_URL)).willReturn(externalUrl);
        //When
        boolean actual = underTest.isElementCrawlable(testElement);
        //Then
        boolean expected = false;
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void whenIsElementCrawlable_WithExternalConfig_AndExternalURL_returnTrue() {
        //Given
        String externalUrl = "https://google.com";
        given(config.getBoolean(ConfigConstants.CRAWL_EXTERNAL)).willReturn(true);
        given(testElement.attr(HTMLElements.LINK_URL)).willReturn(externalUrl);
        //When
        boolean actual = underTest.isElementCrawlable(testElement);
        //Then
        boolean expected = true;
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void whenRetrieveElementUrl_WithElementURLPresent_returnUrl() {
        //Given
        String expectedURL = "https://google.com";
        given(testElement.attr(HTMLElements.LINK_URL)).willReturn(expectedURL);
        //When
        String actual = underTest.retrieveElementUrl(testElement);
        //Then
        assertThat(actual).isEqualTo(expectedURL);
    }
}