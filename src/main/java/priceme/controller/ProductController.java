package priceme.controller;

import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import priceme.exception.NoSuchRecordException;
import priceme.persistence.Price;
import priceme.service.PriceService;

import java.security.InvalidParameterException;
import java.util.*;


/**
 * A controller that handles product information from an external retailers api (e.g. Target)
 */
@RestController
public class ProductController {
    private static final String PRICE_VALUE_FIELD = "value";
    private static final String PRICE_CURRENT_PRICE_FIELD = "current_price";

    @Autowired
    private PriceService priceService;

    /**
     * Given an {@code id} this will retrieve pricing information from multiple retailer websites for a given
     * product.
     *
     * @param id {@link String} the id of the product
     * @return {@link Map} the product combined with the price
     * @throws NoSuchRecordException Thrown if no such {@link Price} exists in our database
     */
    @RequestMapping(value = "/products/{id}")
    public Map<String, List> viewProduct(@PathVariable("id") final String id) throws NoSuchRecordException {
        return priceService.combineProductAndPricingInformation(id);
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
    public Price updateProduct(@PathVariable("id") final String id, @RequestParam("current_price") final Map<String, Object> newPriceInformation) throws NoSuchRecordException {
        validatePriceMap(newPriceInformation);
        return priceService.updateProductPricingInformation(id, (Double) newPriceInformation.get(PRICE_VALUE_FIELD), (String) newPriceInformation.get(PRICE_CURRENT_PRICE_FIELD));
    }

    /**
     * Checks for valid keys
     * TODO: Check data types as well
     * @param priceMapToCheck {@link Map} the price map we wish to validate from the request
     */
    private void validatePriceMap(final Map<String, Object> priceMapToCheck) {
        boolean isInvalid = true;
        if (priceMapToCheck != null) {
            isInvalid &= priceMapToCheck.containsKey(PRICE_VALUE_FIELD)
                    && priceMapToCheck.containsKey(PRICE_CURRENT_PRICE_FIELD)
                    && priceMapToCheck.get(PRICE_VALUE_FIELD) instanceof Double
                    && priceMapToCheck.get(PRICE_CURRENT_PRICE_FIELD) instanceof String;
        }

        if (isInvalid) {
            throw new InvalidParameterException("currentPrice");
        }
    }

}
