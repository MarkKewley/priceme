package priceme.apiobjects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;

/**
 * An external item
 *
 * @author MarkKewley
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Item {

    @JsonProperty("general_description")
    private String generalDescription;

    Item() {}

    public String getGeneralDescription() {
        return generalDescription;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(Item.class)
                .add("generalDescription", generalDescription)
                .toString();
    }
}
