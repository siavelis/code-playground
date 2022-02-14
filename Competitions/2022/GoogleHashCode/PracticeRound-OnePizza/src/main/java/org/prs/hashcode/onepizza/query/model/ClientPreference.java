package org.prs.hashcode.onepizza.query.model;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class ClientPreference {

    private final Set<String> desirableIngredients;
    private final Set<String> unwelcomeIngredients;

}
