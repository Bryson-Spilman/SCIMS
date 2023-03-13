package scims.model.fluentbuilders.competition;

import scims.model.Competition;

public interface FluentFromExistingCompetition {
    FluentUpdateCompetition fromExistingCompetition(Competition competition);
}
