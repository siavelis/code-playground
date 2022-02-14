package org.prs.hashcode.onepizza.query;

import org.prs.hashcode.onepizza.query.model.ClientPreference;
import org.prs.hashcode.onepizza.query.model.SelectBestIngredientsQuery;
import org.prs.hashcode.onepizza.query.model.SelectBestIngredientsResponse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;

public class BestIngredientsSelector implements IngredientsSelector<SelectBestIngredientsQuery, SelectBestIngredientsResponse> {

    @Override
    public SelectBestIngredientsResponse selectIngredients(SelectBestIngredientsQuery data) {

        var allUnwelcomeIngredients = data.getClientPreferences()
                .stream()
                .map(ClientPreference::getUnwelcomeIngredients)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());

        var allDesirableIngredients = data.getClientPreferences()
                .stream()
                .map(ClientPreference::getDesirableIngredients)
                .flatMap(Collection::stream)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        allDesirableIngredients.removeAll(allUnwelcomeIngredients);

        return new SelectBestIngredientsResponse(allDesirableIngredients);
    }
}
