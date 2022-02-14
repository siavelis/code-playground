package org.prs.hashcode.onepizza.parser;

import lombok.extern.slf4j.Slf4j;
import org.prs.hashcode.onepizza.query.model.ClientPreference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class ClientPreferencesParser {

    public List<ClientPreference> parseClientPreferences(String input) {
        if (input == null || input.isEmpty() || input.isBlank()) {
            throw new IllegalArgumentException("Malformed input:" + input);
        }
        var lines = input.split("\n");
        if (lines.length < 3 || lines.length % 2 != 1) {
            throw new IllegalArgumentException("Malformed input:" + input);
        }

        int numberOfClients;
        try {
            numberOfClients = Integer.parseInt(lines[0]);
        } catch (NumberFormatException ex) {
            log.error(ex.getMessage(), ex);
            throw new IllegalArgumentException("Malformed first line. Input: " + input);
        }

        List<ClientPreference> clientPreferences = new ArrayList<>(numberOfClients);

        for (int i = 1; i < lines.length; i++) {

            var desirableIngredients = parseLine(lines[i], i);
            i++;
            var unwelcomeIngredients = parseLine(lines[i], i);

            clientPreferences.add(ClientPreference.builder()
                    .desirableIngredients(desirableIngredients)
                    .unwelcomeIngredients(unwelcomeIngredients)
                    .build());
        }


        return clientPreferences;
    }

    private Set<String> parseLine(String line, int lineCnt) {
        var tokens = line.split(" ");
        if (tokens.length < 1) {
            throw new IllegalArgumentException("Malformed data, line cnt: " + lineCnt + " Input line: " + line);
        }

        return Arrays.stream(tokens)
                .skip(1)
                .map(String::toLowerCase)
                .collect(Collectors.toSet());
    }
}
