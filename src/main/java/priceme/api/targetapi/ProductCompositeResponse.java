package priceme.api.targetapi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;

/**
 * An external api product response
 *
 * @author MarkKewley
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductCompositeResponse {

    @JsonProperty("product_composite_response")
    private Product product;

    public Product getProduct() {
        return product;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(Item.class)
                .add("product", product)
                .toString();
    }
}
