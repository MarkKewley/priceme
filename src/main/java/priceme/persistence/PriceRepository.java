package priceme.persistence;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

/**
 * Interface which will allow you to perform various operations involving
 * {@link Price} objects.
 *
 * @author MarkKewley
 */
public interface PriceRepository extends MongoRepository<Price, String> {
    Price findByProductId(String productId);

    /**
     * Given an {@link List<String>} of {@link Price#productId}s this will
     * find all {@link Price} objects that have those {@code productIds}.
     *
     * @param productIds {@link List<String>} a collection of productIds to query for
     * @return {@link List< Price >} a {@link List} of {@link Price} objects, empty {@link List} if no such
     * objects are found
     */
    @Query("{ productId: { $in: ?0 } }")
    List<Price> findByProductIds(List<String> productIds);
}
