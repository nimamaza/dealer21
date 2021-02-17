package no.dealer;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Deck {
    private String[] suits = {"C","D", "H", "S"};
    private String[] values = {"2","3", "4", "5","6","7","8","9","10","J","Q","K","A"};

    public List<String> generateShuffledDeck(){
        StringBuilder stringBuilder = new StringBuilder();
        String separator = "";
        for (String suit : suits){
            for (String value :values){
                stringBuilder.append(separator);
                stringBuilder.append(suit+value);
                separator = ",";
            }
        }

        return shuffle(stringBuilder.toString());
    }

    private List<String> shuffle(String deckStr) {
        List<String> listDeck = Arrays.stream(deckStr.split(","))
                .collect(Collectors.toList());

        Collections.shuffle(listDeck);
        return listDeck;
    }
}
