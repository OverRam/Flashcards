package flashcards;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;

public class CardController {
    private final CardsModel cardsModel;
    private final Scanner sc = new Scanner(System.in);

    CardController(CardsModel cardsModel) {
        this.cardsModel = cardsModel;
    }

    void run() {
        String availableActions = Arrays.stream(ActionsCards.values())
                .map(x -> !x.getAction()
                        .equals("exit") ? x.getAction() + ", " : x.getAction()).collect(Collectors.joining());
        String userAction = "";
        while (!userAction.equals(ActionsCards.EXIT.getAction())) {
            System.out.printf("Input the action (%s):\n", availableActions);
            userAction = sc.nextLine();
            menuActions(userAction);
        }
    }

    private void menuActions(String action) {
        if (action.equals(ActionsCards.ADD.getAction())) {
            addCard();
        } else if (action.equals(ActionsCards.ASK.getAction())) {
            ask();
        } else if (action.equals(ActionsCards.EXPORT.getAction())) {
            exportCards();
        } else if (action.equals(ActionsCards.IMPORT.getAction())) {
            importCards();
        } else if (action.equals(ActionsCards.REMOVE.getAction())) {
            removeCards();
        } else if (action.equals(ActionsCards.EXIT.getAction())) {
            System.out.println("Bye bye!");
        } else {
            System.out.println("Wrong input action!!");
        }
    }

    private void removeCards() {
        System.out.println("Which card?");
        String removeCard = sc.nextLine();

        System.out.println(cardsModel.getCards().containsKey(removeCard) ? "The card has been removed."
                : "Can't remove \"" + removeCard + "\" : there is no such card.");
        cardsModel.getCards().remove(removeCard);

    }

    private void importCards() {
        int count = 0;
        System.out.println("File name:");
        File file = new File(sc.nextLine());

        if (file.exists()) {
            try (Scanner scFile = new Scanner(file)) {
                while (scFile.hasNext()) {
                    String[] content = scFile.nextLine().split(":");
                    if (content.length > 1) {
                        cardsModel.getCards().put(content[0], content[1]);
                        count++;
                    }
                }
                System.out.printf("%d cards have been loaded.\n", count);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("File not found.");
        }
    }

    private void exportCards() {
        System.out.println("File name:");
        String fileName = sc.nextLine();
//        fileName += ".txt";
        try (FileWriter fileWriter = new FileWriter(fileName)) {
            cardsModel.getCards().forEach((key, value) -> {
                try {
                    fileWriter.write(key + ":" + value + "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            System.out.printf("%d cards have been saved.\n", cardsModel.getCards().size());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void ask() {
        System.out.println("How many times to ask?");
        int timeToAsk = Integer.parseInt(sc.nextLine());
        cardsModel.getCards().entrySet().stream().limit(timeToAsk).forEach((entry) -> {
            String k = entry.getKey();
            String v = entry.getValue();
            System.out.printf("Print the definition of \"%s\":\n", k);
            String ans = sc.nextLine();
            StringBuilder sb = new StringBuilder();
            if (v.equals(ans)) {
                sb.append("Correct!");
            } else {
                sb.append("Wrong. The right answer is \"").append(v);
                if (cardsModel.getCards().containsValue(ans)) {
                    sb.append("\", but your definition is correct for \"");
                    for (var e : cardsModel.getCards().entrySet()) {
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

    private void addCard() {
        System.out.println("The card:");
        boolean isOkTerm;
        String definition = "";
        String term = sc.nextLine();
        isOkTerm = !cardsModel.getCards().containsKey(term);

        if (isOkTerm) {
            System.out.println("The definition of the card:");
            definition = sc.nextLine();
            if (!cardsModel.getCards().containsValue(definition)) {
                cardsModel.getCards().put(term, definition);
                System.out.printf("The pair (\"%s\":\"%s\") has been added.\n\n", term, definition);
                return;
            }
        }
        System.out.printf("The %s \"%s\" already exists.\n\n",
                isOkTerm ? "definition" : "card", isOkTerm ? definition : term);
    }

}
