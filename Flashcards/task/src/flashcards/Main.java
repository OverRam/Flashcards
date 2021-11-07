package flashcards;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Input the number of cards:");
        int numberOfCards = Integer.parseInt(sc.nextLine());

        Cards cards = new Cards();
        cards.createCards(numberOfCards);

        cards.getCards().forEach((k, v) -> {
            System.out.printf("Print the definition of \"%s\":\n", k);
            String ans = sc.nextLine();
            StringBuilder sb = new StringBuilder();
            if (v.equals(ans)) {
                sb.append("Correct!");
            } else {
                sb.append("Wrong. The right answer is \"").append(v);
                if (cards.getCards().containsValue(ans)) {
                    sb.append("\", but your definition is correct for \"");
                    for (var e : cards.getCards().entrySet()) {
                        if (e.getValue().equals(ans)) {
                            sb.append(e.getKey());
                            break;
                        }
                    }
                }
                sb.append("\".");
            }

            System.out.println(sb);
        });

    }
}
