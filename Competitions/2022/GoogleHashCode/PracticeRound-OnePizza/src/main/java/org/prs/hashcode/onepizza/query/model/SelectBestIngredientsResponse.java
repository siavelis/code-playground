package org.prs.hashcode.onepizza.query.model;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@RequiredArgsConstructor
public class SelectBestIngredientsResponse {

    private final Set<String> bestIngredients;

}
