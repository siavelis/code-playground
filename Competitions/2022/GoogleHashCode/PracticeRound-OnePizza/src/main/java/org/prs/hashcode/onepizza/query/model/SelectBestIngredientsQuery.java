package org.prs.hashcode.onepizza.query.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class SelectBestIngredientsQuery {

    private final List<ClientPreference> clientPreferences;

}
