package priceme.apiobjects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
}
