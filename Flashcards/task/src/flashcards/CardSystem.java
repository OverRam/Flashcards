package flashcards;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CardSystem {
    private final HashMap<String, Card> allCards = new HashMap<>();
    private final Scanner sc = new Scanner(System.in);
    StringBuilder logBuilder = new StringBuilder();
    String exportPatch;

    CardSystem(String[] args) {
        for (int i = 0; i < args.length; i += 2) {
            switch (args[i]) {
                case "-import":
                    importCards(args[i + 1]);
                    break;
                case "-export":
                    exportPatch = args[i + 1];
                    break;
            }
        }
    }

    void run() {
        String availableActions = Stream.of(ActionsCards.values())
                .map(x -> !x.getAction().equals("exit") ? x.getAction() + ", " : x.getAction())
                .collect(Collectors.joining());

        String userAction = "";
        while (!userAction.equals(ActionsCards.EXIT.getAction())) {
            String outPut = String.format("Input the action (%s):\n", availableActions);
            System.out.print(outPut);
            logBuilder.append(outPut);
            userAction = sc.nextLine();
            logBuilder.append(userAction).append("\n");
            menuActions(userAction);
        }
    }

    private void menuActions(String action) {
        if (action.equals(ActionsCards.ADD.getAction())) {
            addCard();
        } else if (action.equals(ActionsCards.ASK.getAction())) {
            ask();
        } else if (action.equals(ActionsCards.EXPORT.getAction())) {
            exportCards(setFileName());
        } else if (action.equals(ActionsCards.IMPORT.getAction())) {
            importCards(setFileName());
        } else if (action.equals(ActionsCards.REMOVE.getAction())) {
            removeCards();
        } else if (action.equals(ActionsCards.LOG.getAction())) {
            saveLogs();
        } else if (action.equals(ActionsCards.HARDEST_CARD.getAction())) {
            printHardestCard();
        } else if (action.equals(ActionsCards.REST_STATS.getAction())) {
            allCards.forEach((k, v) -> v.resetWrongAnswers());
            System.out.println("Card statistics have been reset.");
            logBuilder.append("Card statistics have been reset.\n\n");
        } else if (action.equals(ActionsCards.PRINT_ALL.getAction())) {
            allCards.values()
                    .forEach(x -> System.out.printf("Term is: %s, definition is: %s, wrong answers is %d\n",
                            x.getTerm(), x.getDefinition(), x.getWrongAnswers()));
        } else if (action.equals(ActionsCards.EXIT.getAction())) {
            System.out.println("Bye bye!");
            logBuilder.append("Bye bye!\n");
            if (exportPatch != null) {
                exportCards(exportPatch);
            }
        } else {
            System.out.println("Wrong input action!!");
            logBuilder.append("Wrong input action!!\n");
        }
    }

    private void printHardestCard() {
        int badAnswers = allCards.values()
                .stream()
                .map(Card::getWrongAnswers)
                .max(Comparator.naturalOrder())
                .orElse(0);

        StringBuilder sb = new StringBuilder();

        if (badAnswers > 0) {
            String hardestCards = allCards.values()
                    .stream()
                    .filter(x -> x.getWrongAnswers() >= badAnswers)
                    .map(Card::getTerm)
                    .reduce((x, y) -> "\"" + x + "\", " + "\"" + y + "\"")
                    .orElse(null);

            assert hardestCards != null;
            boolean isMultiCards = hardestCards.split(",").length > 1;
            sb.append("The hardest card")
                    .append(isMultiCards ? "s are " : " is ")
                    .append(isMultiCards ? "" : "\"")
                    .append(hardestCards)
                    .append(isMultiCards ? "" : "\"")
                    .append(". You have ")
                    .append(badAnswers)
                    .append(" errors answering ")
                    .append(isMultiCards ? "them.\n" : "it.\n");
        } else {
            sb.append("There are no cards with errors.");
        }
        logBuilder.append(sb).append("\n");
        System.out.println(sb);
    }

    private void saveLogs() {
        System.out.println("File name:");
        logBuilder.append("File name:\n");
        String fileName = sc.nextLine();
        logBuilder.append(fileName).append("\n");
        try (FileWriter fileWriter = new FileWriter(fileName)) {
            System.out.println("The log has been saved.");
            logBuilder.append("The log has been saved.\n\n");
            fileWriter.write(logBuilder.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void removeCards() {
        System.out.println("Which card?");
        logBuilder.append("Which card?\n");
        String removeCard = sc.nextLine();
        logBuilder.append(removeCard).append("\n");
        String outPut = allCards.containsKey(removeCard) ? "The card has been removed."
                : "Can't remove \"" + removeCard + "\" : there is no such card.";

        System.out.println(outPut);
        logBuilder.append(outPut).append("\n\n");
        allCards.remove(removeCard);
    }

    private void importCards(String fileName) {
        int count = allCards.size();
        logBuilder.append(fileName).append("\n");
        File file = new File(fileName);

        if (file.exists()) {
            try (Scanner scFile = new Scanner(file)) {
                while (scFile.hasNext()) {
                    String[] content = scFile.nextLine().split(":");
                    if (content.length > 1) {
                        if (allCards.containsKey(content[0])) {
                            count--;
                        }
                        allCards.put(content[0], new Card(content[0], content[1], Integer.parseInt(content[2])));
                    }
                }
                String outPut = String.format("%d cards have been loaded.\n", allCards.size() - count);
                logBuilder.append(outPut).append("\n");
                System.out.print(outPut);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("File not found.");
            logBuilder.append("File not found.\n\n");
        }
    }

    private String setFileName() {
        System.out.println("File name:");
        logBuilder.append("File name:\n");
        return sc.nextLine();
    }

    private void exportCards(String fileName) {
        logBuilder.append(fileName).append("\n");
        try (FileWriter fileWriter = new FileWriter(fileName)) {
            allCards.forEach((key, value) -> {
                try {
                    fileWriter.write(key + ":" + value.getDefinition() + ":" + value.getWrongAnswers() + "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            String outPut = String.format("%d cards have been saved.\n", allCards.size());
            System.out.print(outPut);
            logBuilder.append(outPut).append("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void ask() {
        System.out.println("How many times to ask?");
        logBuilder.append("How many times to ask?\n");
        int timeToAsk = Integer.parseInt(sc.nextLine());
        logBuilder.append(timeToAsk).append("\n");
        allCards.entrySet().stream().limit(timeToAsk).forEach(entry -> {
            String key = entry.getKey();
            String def = entry.getValue().getDefinition();
            String out = String.format("Print the definition of \"%s\":\n", key);
            System.out.print(out);
            logBuilder.append(out);

            String ans = sc.nextLine();
            logBuilder.append(ans).append("\n");

            StringBuilder sb = new StringBuilder();
            if (def.equals(ans)) {
                sb.append("Correct!");
            } else {
                entry.getValue().increaseWrongAnswers();
                sb.append("Wrong. The right answer is \"").append(def);
                if (allCards.values().stream().anyMatch(x -> x.getDefinition().equals(ans))) {
                    sb.append("\", but your definition is correct for \"");
                    for (var e : allCards.entrySet()) {
                        if (e.getValue().getDefinition().equals(ans)) {
                            sb.append(e.getKey());
                            break;
                        }
                    }
                }
                sb.append("\".");
            }
            logBuilder.append(sb).append("\n");
            System.out.println(sb);
        });
        logBuilder.append("\n");
    }

    private void addCard() {
        System.out.println("The card:");
        logBuilder.append("The card:\n");
        boolean isOkTerm;
        String definition = "";
        String term = sc.nextLine();
        logBuilder.append(term).append("\n");
        isOkTerm = !allCards.containsKey(term);

        if (isOkTerm) {
            System.out.println("The definition of the card:");
            logBuilder.append("The definition of the card:\n");
            definition = sc.nextLine();
            logBuilder.append(definition).append("\n");
            final String def = definition;
            if (allCards.values().stream().noneMatch(x -> def.equals(x.getDefinition()))) {
                allCards.put(term, new Card(term, definition));
                String out = String.format("The pair (\"%s\":\"%s\") has been added.\n", term, definition);
                System.out.print(out);
                logBuilder.append(out).append("\n");
                return;
            }
        }
        String out = String.format("The %s \"%s\" already exists.\n\n",
                isOkTerm ? "definition" : "card", isOkTerm ? definition : term);
        System.out.print(out);
        logBuilder.append(out);
    }
}
