package io.mopar.account.aws;

import com.amazonaws.services.simpledb.model.Attribute;
import com.amazonaws.services.simpledb.model.Item;

import java.util.List;

/**
 * Created by hadyn on 7/26/2015.
 */
public class SimpleDBUtil {

    /**
     *
     * @param item
     * @param index
     * @return
     */
    public static String getAttributeValue(Item item, int index) {
        List<Attribute> attributes = item.getAttributes();
        if(index > attributes.size()) {
            throw new ArrayIndexOutOfBoundsException(index);
        }
        return attributes.get(index).getValue();
    }
}
