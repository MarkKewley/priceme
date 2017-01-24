package priceme.persistence;

import com.google.common.base.MoreObjects;
import org.springframework.data.annotation.Id;

/**
 * Represents a price object tied to a product
 */
public class Price {

    @Id
    private String id;

    /**
     * The product id the price is tied to (e.g. 13860428)
     */
    private String productId;

    /**
     * The value of the price (e.g. 12.34)
     */
    private double value;

    /**
     * The currency code (e.g. USD)
     */
    private String currencyCode;


    public Price(String productId, double value, String currencyCode) {
        this.productId = productId;
        this.value = value;
        this.currencyCode = currencyCode;
    }

    public Price setProductId(String productId) {
        this.productId = productId;
        return this;
    }

    public String getProductId() {
        return productId;
    }

    public Price setValue(double value) {
        this.value = value;
        return this;
    }

    public double getValue() {
        return value;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public Price setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
        return this;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(Price.class)
                .add("productId", productId)
                .add("value", value)
                .add("currencyCode", currencyCode)
                .toString();
    }
}
