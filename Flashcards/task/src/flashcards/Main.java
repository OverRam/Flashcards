package flashcards;

public class Main {

    public static void main(String[] args) {
        CardController cardController = new CardController(new CardsModel());
        cardController.run();

    }
}
