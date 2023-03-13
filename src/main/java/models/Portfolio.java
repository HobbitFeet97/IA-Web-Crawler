package models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Portfolio {
    private String minInvestment;
    private String managementFee;

    @Override
    public String toString() {
        return String.format("Minimum Investment: %s; Management Fee: %s", this.minInvestment, this.managementFee);
    }
}
