package org.prs.hashcode.onepizza.query;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.prs.hashcode.onepizza.parser.ClientPreferencesParser;
import org.prs.hashcode.onepizza.query.model.SelectBestIngredientsQuery;
import org.prs.hashcode.onepizza.query.model.SelectBestIngredientsResponse;

import java.io.IOException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.prs.hashcode.onepizza.utils.DataLoader.loadData;
import static org.prs.hashcode.onepizza.utils.DataWriter.compressData;

@Slf4j
class BestIngredientsSelectorTest {

    @Test
    void selectorWithExampleDataReturnsAllIngredients() throws IOException {
        var fileName = "inputs/1. example.txt";
        var expected = Set.of("cheese", "mushrooms", "tomatoes", "peppers");
        checkData(fileName, expected);
    }

    @Test
    void selectorWithBasicDataReturnsAllIngredients() throws IOException {
        var fileName = "inputs/2. basic example.txt";
        var expected = Set.of("akuof", "byyii", "luncl", "dlust", "vxglq", "xveqd");
        checkData(fileName, expected);
    }

    @Test
    void selectorWithCoarseDataReturnsAllIngredients() throws IOException {
        var fileName = "inputs/3. coarse.txt";
        var expected = Set.of("dlust", "luncl", "vxglq", "tfeej");
        checkData(fileName, expected);
    }

    @Test
    void selectorWithDifficultDataReturnsAllIngredients() throws IOException {
        var fileName = "inputs/4. difficult.txt";
        var mapper = new ObjectMapper();
        Set<String> expected = mapper.readValue(
                loadData("output/4. difficult-results.json"),
                mapper.getTypeFactory().constructCollectionType(Set.class, String.class)
        );

        checkData(fileName, expected);
    }

    @Test
    void selectorWithElaborateDataReturnsAllIngredients() throws IOException {
        var fileName = "inputs/5. elaborate.txt";
        var mapper = new ObjectMapper();
        Set<String> expected = mapper.readValue(
                loadData("output/5. elaborate-results.json"),
                mapper.getTypeFactory().constructCollectionType(Set.class, String.class)
        );

        checkData(fileName, expected);
    }

    private void checkData(String fileName, Set<String> expected) throws IOException {

        var data = new ClientPreferencesParser().parseClientPreferences(loadData(fileName));
        var selector = new BestIngredientsSelector();
        var results = selector.selectIngredients(new SelectBestIngredientsQuery(data));

        assertEquals(new SelectBestIngredientsResponse(expected), results);

        System.out.println("Input filename: " + fileName);
        System.out.println("Output:");
        System.out.println(compressData(results));

    }
}