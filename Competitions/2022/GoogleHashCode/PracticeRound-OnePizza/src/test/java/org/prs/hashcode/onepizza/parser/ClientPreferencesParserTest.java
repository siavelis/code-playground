package org.prs.hashcode.onepizza.parser;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static java.lang.System.lineSeparator;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.prs.hashcode.onepizza.factory.ClientPreferencesFactory.createSampleData;
import static org.prs.hashcode.onepizza.utils.DataLoader.loadData;

class ClientPreferencesParserTest {

    @Test
    void givenInvalidInputs_throwException() {
        var parser = new ClientPreferencesParser();
        assertThrows(IllegalArgumentException.class, () -> parser.parseClientPreferences(null));
        assertThrows(IllegalArgumentException.class, () -> parser.parseClientPreferences(""));
        assertThrows(IllegalArgumentException.class, () -> parser.parseClientPreferences("   "));
        assertThrows(IllegalArgumentException.class, () -> parser.parseClientPreferences(lineSeparator() + lineSeparator()));
    }

    @Test
    void givenValidInput_parseData() throws IOException {
        var inputData = loadData("inputs/1. example.txt");
        var result = new ClientPreferencesParser().parseClientPreferences(inputData);

        assertEquals(createSampleData(), result);
    }
}