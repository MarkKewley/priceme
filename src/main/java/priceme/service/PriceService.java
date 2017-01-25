package priceme.service;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import priceme.api.ExternalApiUtils;
import priceme.api.targetapi.Item;
import priceme.api.targetapi.PricedProduct;
import priceme.api.targetapi.Product;
import priceme.api.targetapi.ProductCompositeResponse;
import priceme.exception.NoSuchRecordException;
import priceme.persistence.Price;
import priceme.persistence.PriceRepository;

import java.security.InvalidParameterException;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;

/**
 * Handles all logic regarding {@link Price}
 */
@Service
public class PriceService {

    @Autowired
    private PriceRepository priceRepository;
    @Autowired
    private RestTemplate restTemplate;

    /**
     * Combines a product with an {@link Price}
     *
     * @param productId {@link String} a valid product id
     * @return {@link Map}
     * @throws NoSuchRecordException Thrown if no such {@link Price} exists in our database
     */
    public Map<String, List> combineProductAndPricingInformation(final String productId) throws NoSuchRecordException {
        //TODO: Do a regex match for what a valid id is - Kewley
        if (Strings.isNullOrEmpty(productId)) {
            throw new InvalidParameterException("productId");
        }

        ProductCompositeResponse productCompositeResponse = restTemplate.getForObject(ExternalApiUtils.getApiEndpoint(ExternalApiUtils.ExternalApiProvider.TARGET, productId), ProductCompositeResponse.class);
        if (productCompositeResponse.getProduct() != null) {

            final Product product = productCompositeResponse.getProduct();
            final Map<String, List> productsMap = new HashMap<>();
            final List<PricedProduct> pricedProducts = new ArrayList<>();

            if (product.getItems() != null) {
                for (Item item : product.getItems()) {

                    final Price price = priceRepository.findByProductId(productId);
                    if (price == null) {
                        throw new NoSuchRecordException();
                    }

                    pricedProducts.add(new PricedProduct()
                            .setId(productId)
                            .setName(item.getGeneralDescription())
                            .setCurrentPrice(ImmutableMap.of(
                                    "value", price.getValue(),
                                    "currency_code", price.getCurrencyCode()
                            ))
                    );

                }
            }

            productsMap.put("products", pricedProducts);
            return productsMap;
        }

        return null;
    }

    /**
     * Updates {@link Price} data
     * @param productId {@link String} a valid id for querying {@link Price} records
     * @param newPriceInformation {@link Map} new pricing information
     * @return {@link Price} the updated price
     * @throws NoSuchRecordException Thrown if no such {@link Price} exists in our database
     */
    public Price updateProductPricingInformation(final String productId, double value, final String currencyCode) throws NoSuchRecordException {
        //TODO: Do a regex match for what a valid id is - Kewley
        if (Strings.isNullOrEmpty(productId)) {
            throw new InvalidParameterException("productId");
        }

        Price price = priceRepository.findByProductId(productId);
        if (price == null) {
            throw new NoSuchRecordException();
        }

        price.setCurrencyCode(currencyCode).setValue(value);

        price = priceRepository.save(price);

        return price;
    }
}
