package priceme.controller;

import com.google.common.base.Strings;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import priceme.apiobjects.Item;
import priceme.apiobjects.Product;
import priceme.apiobjects.ProductCompositeResponse;
import priceme.exception.NoSuchRecordException;
import priceme.persistence.Price;
import priceme.persistence.PriceRepository;

import java.security.InvalidParameterException;
import java.util.*;


/**
 * A controller that handles product information from an external retailers api (e.g. Target)
 */
@Controller
public class ProductController {
    private static final String TARGET_API_FORMAT = "https://api.target.com/products/v3/%s?fields=descriptions&id_type=TCIN&key=43cJWpLjH8Z8oR18KdrZDBKAgLLQKJjz";
    private static final Set<String> REQUIRED_PRICE_FIELDS = Sets.newHashSet("value", "currencyCode");

    @Autowired
    private PriceRepository priceRepository;
    @Autowired
    private RestTemplate restTemplate;

    /**
     * Given an {@code id} this will retrieve pricing information from multiple retailer websites for a given
     * product.
     *
     * @param id {@link String} the id of the product
     * @return {@link Map} the product combined with the price
     * @throws NoSuchRecordException Thrown if no such {@link Price} exists in our database
     */
    @RequestMapping(value = "/products/{id}")
    @ResponseBody
    public Map<String, Object> viewProduct(@PathVariable("id") final String id) throws NoSuchRecordException {
        if (Strings.isNullOrEmpty(id)) {
            throw new InvalidParameterException("id");
        }

        ProductCompositeResponse productCompositeResponse = restTemplate.getForObject(String.format(TARGET_API_FORMAT, id), ProductCompositeResponse.class);
        if (productCompositeResponse.getProduct() != null) {
            final Product product = productCompositeResponse.getProduct();
            final Map<String, Object> productsMap = new HashMap<>();
            final List<Map<String, Object>> products = new ArrayList<>();
            if (product.getItems() != null) {
                for (Item item : product.getItems()) {

                    final Price price = priceRepository.findByProductId(id);
                    if (price == null) {
                        throw new NoSuchRecordException();
                    }

                    final Map<String, Object> priceMap = new HashMap<>();
                    priceMap.put("value", price.getValue());
                    priceMap.put("currency_code", price.getCurrencyCode());

                    final Map<String, Object> productMap = new HashMap<>();
                    productMap.put("id", id);
                    productMap.put("name", item.getGeneralDescription());
                    productMap.put("current_price", priceMap);
                    products.add(productMap);

                }
            }
            productsMap.put("products", products);
            return productsMap;
        }

        return null;
    }

    /**
     * Given {@code id} and {@code currentPrice} this will update the {@link Price} object stored within our database.
     *
     * @param id {@link String} id mapped to a Product
     * @param currentPrice {@link Map} a valid map with {@link Price} information
     * @return {@link Price} the update object
     * @throws NoSuchRecordException Thrown if no such {@link Price} exists in our database
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/products/{id}")
    @ResponseBody
    public Price updateProduct(@PathVariable("id") final String id, @RequestParam("currentPrice") final Map<String, Object> currentPrice) throws NoSuchRecordException {
        if (Strings.isNullOrEmpty(id)) {
            throw new InvalidParameterException("id");
        }

        validatePriceMap(currentPrice);

        Price price = priceRepository.findByProductId(id);
        if (price == null) {
            throw new NoSuchRecordException();
        }

        price.setCurrencyCode((String) currentPrice.get("currencyCode")).setValue((double) currentPrice.get("value"));

        price = priceRepository.save(price);

        return price;
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
