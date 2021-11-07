package flashcards;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class CardsModel {
    private final Map<String, String> cardsModel = new LinkedHashMap<>();

    void createCards(int numberOfCards) {
        Scanner sc = new Scanner(System.in);
        for (int i = 0; i < numberOfCards; i++) {
            boolean termOk = false;
            boolean incorrectCard = true;

            System.out.printf("Card #%d:\n", i + 1);
            String term = sc.nextLine();

            while (incorrectCard) {
                if (!termOk) {
                    if (!cardsModel.containsKey(term)) {
                        termOk = true;
                        System.out.printf("The definition for card #%d:\n", i + 1);
                    } else {
                        System.out.printf("The term \"%s\" already exists. Try again:\n", term);
                        term = sc.nextLine();
                    }
                }

                if (termOk) {
                    String definition = sc.nextLine();
                    if (!cardsModel.containsValue(definition)) {
                        incorrectCard = false;
                        cardsModel.put(term, definition);
                    } else {
                        System.out.printf("The definition \"%s\" already exists. Try again:\n", definition);
                    }
                }
            }

        }

    }

    public Map<String, String> getCards() {
        return cardsModel;
    }
}
