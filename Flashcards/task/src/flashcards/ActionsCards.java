package flashcards;

public enum ActionsCards {
    ADD("add"),
    REMOVE("remove"),
    IMPORT("import"),
    EXPORT("export"),
    ASK("ask"),
    LOG("log"),
    HARDEST_CARD("hardest card"),
    REST_STATS("reset stats"),
    PRINT_ALL("print all"),
    EXIT("exit");

    private final String action;

    ActionsCards(String action) {
        this.action = action;
    }

    String getAction() {
        return action;
    }
}
