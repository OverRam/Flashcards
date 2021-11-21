package flashcards;

import java.util.HashMap;
import java.util.Map;

public class CardsModel {
    private final Map<String, String> cardsModel = new HashMap<>();

    Map<String, String> getCards() {
        return cardsModel;
    }
}
