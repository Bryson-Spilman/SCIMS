package scims.ui.fx;

import scims.model.data.scoring.*;

class ScoringColumnFactory {
    private ScoringColumnFactory() {
        throw new AssertionError("Factory Class");
    }

    static <T extends EventScoring<S>, S>ScoringColumn<T, S> buildScoringColumn(EventColumn eventColumn, T scoring) {
        return new ScoringColumn<>(eventColumn, scoring);
    }
}
