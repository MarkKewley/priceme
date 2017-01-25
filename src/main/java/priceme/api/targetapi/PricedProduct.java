package priceme.api.targetapi;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

/**
 * Represents the combined information of a product
 * with it's price.
 *
 * @author MarkKewley
 */
public class PricedProduct {

    private String id;
    private double value;
    private String name;

    @JsonProperty("current_price")
    private Map<String, Object> currentPrice;

    public double getValue() {
        return value;
    }

    public PricedProduct setValue(double value) {
        this.value = value;
        return this;
    }

    public String getId() {
        return id;
    }

    public PricedProduct setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public PricedProduct setName(String name) {
        this.name = name;
        return this;
    }

    public Map<String, Object> getCurrentPrice() {
        return currentPrice;
    }

    public PricedProduct setCurrentPrice(Map<String, Object> currentPrice) {
        this.currentPrice = currentPrice;
        return this;
    }
}
