package priceme.apiobjects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

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
}
