package org.prs.hashcode.onepizza.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.prs.hashcode.onepizza.query.model.SelectBestIngredientsResponse;

@Slf4j
@UtilityClass
public class DataWriter {

    public static String compressData(SelectBestIngredientsResponse response) {

        return response.getBestIngredients().size()
                + " "
                + String.join(" ", response.getBestIngredients());
    }
}
