package org.playground.sphinx4;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;
import edu.cmu.sphinx.api.SpeechResult;
import edu.cmu.sphinx.result.WordResult;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {

        Configuration configuration = new Configuration();

        configuration.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
        configuration.setDictionaryPath("resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict");
        configuration.setLanguageModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us.lm.bin");

        LiveSpeechRecognizer recognizer = new LiveSpeechRecognizer(configuration);
        // Start recognition process pruning previously cached data.
        recognizer.startRecognition(true);
        while (true) {
            SpeechResult result = recognizer.getResult();
            System.out.println("Hypothesis: " + result.getHypothesis());

            // Get individual words and their times.
            for (WordResult r : result.getWords()) {
                System.out.println("Word: " + r);
            }

            if ("stop".equalsIgnoreCase(result.getHypothesis())) {
                break;
            }

        }
        // Pause recognition process. It can be resumed then with startRecognition(false).
        System.out.println("shutting down recognition process");
        recognizer.stopRecognition();
    }
}