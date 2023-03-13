package mapper;

import constants.HTMLElements;
import models.Portfolio;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Mapper {
    public Portfolio toPortfolio(Element element) {
        Elements parentElements = element.select(HTMLElements.DIV_PORTFOLIO_VALUE);
        return Portfolio.builder()
                .minInvestment(parentElements.get(0).text())
                .managementFee(parentElements.get(1).text())
                .build();
    }
}
