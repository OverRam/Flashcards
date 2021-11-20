package flashcards;

public enum ActionsCards {
    ADD("add"),
    REMOVE("remove"),
    IMPORT("import"),
    EXPORT("export"),
    ASK("ask"),
    EXIT("exit");

    private final String action;

    ActionsCards(String action) {
        this.action = action;
    }

    String getAction() {
        return action;
    }
}
