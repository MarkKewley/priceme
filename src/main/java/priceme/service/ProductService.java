package priceme.service;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import priceme.api.ExternalApiUtils;
import priceme.api.apiobject.Item;
import priceme.api.apiobject.Product;
import priceme.api.apiobject.ProductCompositeResponse;
import priceme.exception.NoSuchRecordException;
import priceme.persistence.Price;
import priceme.persistence.PriceRepository;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by markkewley on 1/24/17.
 */
@Service
public class ProductService {

    @Autowired
    private PriceRepository priceRepository;
    @Autowired
    private RestTemplate restTemplate;

    public Map<String, Object> retrieveProductAndPricingInformation(final String productId) throws NoSuchRecordException {
        if (Strings.isNullOrEmpty(productId)) {
            throw new InvalidParameterException("productId");
        }

        ProductCompositeResponse productCompositeResponse = restTemplate.getForObject(ExternalApiUtils.getApiEndpoint(ExternalApiUtils.ExternalApiProvider.TARGET, productId), ProductCompositeResponse.class);
        if (productCompositeResponse.getProduct() != null) {
            final Product product = productCompositeResponse.getProduct();
            final Map<String, Object> productsMap = new HashMap<>();
            final List<Map<String, Object>> products = new ArrayList<>();
            if (product.getItems() != null) {
                for (Item item : product.getItems()) {

                    final Price price = priceRepository.findByProductId(productId);
                    if (price == null) {
                        throw new NoSuchRecordException();
                    }

                    final Map<String, Object> priceMap = new HashMap<>();
                    priceMap.put("value", price.getValue());
                    priceMap.put("currency_code", price.getCurrencyCode());

                    final Map<String, Object> productMap = new HashMap<>();
                    productMap.put("id", productId);
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

    public Price updateProductPricingInformation(final String productId, final Map<String, Object> newPriceInformation) throws NoSuchRecordException {
        if (Strings.isNullOrEmpty(productId)) {
            throw new InvalidParameterException("productId");
        }

        Price price = priceRepository.findByProductId(productId);
        if (price == null) {
            throw new NoSuchRecordException();
        }

        price.setCurrencyCode((String) newPriceInformation.get("currencyCode")).setValue((double) newPriceInformation.get("value"));

        price = priceRepository.save(price);

        return price;
    }
}
