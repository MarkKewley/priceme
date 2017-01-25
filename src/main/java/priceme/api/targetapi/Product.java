package priceme.api.targetapi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.base.MoreObjects;

import java.util.List;

/**
 * An external product
 *
 * @author MarkKewley
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Product {

    private List<Item> items;

    public List<Item> getItems() {
        return items;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(Item.class)
                .add("items", items)
                .toString();
    }
}
