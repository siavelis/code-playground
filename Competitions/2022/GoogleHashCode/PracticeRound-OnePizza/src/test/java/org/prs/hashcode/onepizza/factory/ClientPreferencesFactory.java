package org.prs.hashcode.onepizza.factory;

import lombok.experimental.UtilityClass;
import org.prs.hashcode.onepizza.query.model.ClientPreference;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@UtilityClass
public class ClientPreferencesFactory {

    public static List<ClientPreference> createSampleData(){
        return List.of(
                ClientPreference.builder()
                        .desirableIngredients(Set.of("cheese", "peppers"))
                        .unwelcomeIngredients(Collections.emptySet())
                        .build(),
                ClientPreference.builder()
                        .desirableIngredients(Set.of("basil"))
                        .unwelcomeIngredients(Set.of("pineapple"))
                        .build(),
                ClientPreference.builder()
                        .desirableIngredients(Set.of("mushrooms", "tomatoes"))
                        .unwelcomeIngredients(Set.of("basil"))
                        .build()
        );
    }
}
