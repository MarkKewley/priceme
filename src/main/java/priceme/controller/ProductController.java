package priceme.controller;

import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import priceme.exception.NoSuchRecordException;
import priceme.persistence.Price;
import priceme.service.ProductService;

import java.security.InvalidParameterException;
import java.util.*;


/**
 * A controller that handles product information from an external retailers api (e.g. Target)
 */
@RestController
public class ProductController {
    private static final Set<String> REQUIRED_PRICE_FIELDS = Sets.newHashSet("value", "currencyCode");

    @Autowired
    private ProductService productService;

    /**
     * Given an {@code id} this will retrieve pricing information from multiple retailer websites for a given
     * product.
     *
     * @param id {@link String} the id of the product
     * @return {@link Map} the product combined with the price
     * @throws NoSuchRecordException Thrown if no such {@link Price} exists in our database
     */
    @RequestMapping(value = "/products/{id}")
    public Map<String, Object> viewProduct(@PathVariable("id") final String id) throws NoSuchRecordException {
        return productService.retrieveProductAndPricingInformation(id);
    }

    /**
     * Given {@code id} and {@code currentPrice} this will update the {@link Price} object stored within our database.
     *
     * @param id {@link String} id mapped to a Product
     * @param newPriceInformation {@link Map} a valid map with {@link Price} information
     * @return {@link Price} the update object
     * @throws NoSuchRecordException Thrown if no such {@link Price} exists in our database
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/products/{id}")
    public Price updateProduct(@PathVariable("id") final String id, @RequestParam("currentPrice") final Map<String, Object> newPriceInformation) throws NoSuchRecordException {
        validatePriceMap(newPriceInformation);
        return productService.updateProductPricingInformation(id, newPriceInformation);
    }

    /**
     * Checks for valid keys
     * TODO: Check data types as well
     * @param priceMapToCheck {@link Map} the price map we wish to validate from the request
     */
    private void validatePriceMap(final Map<String, Object> priceMapToCheck) {
        if (priceMapToCheck != null) {
            final Set<String> keys = priceMapToCheck.keySet();
            if (Sets.intersection(keys, REQUIRED_PRICE_FIELDS).size() == REQUIRED_PRICE_FIELDS.size()) {
                return;
            }
        }

        throw new InvalidParameterException("currentPrice");
    }

}
