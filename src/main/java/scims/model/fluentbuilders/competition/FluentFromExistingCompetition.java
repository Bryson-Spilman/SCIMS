package scims.model.fluentbuilders.competition;

import scims.model.data.Competition;

public interface FluentFromExistingCompetition {
    FluentUpdateCompetition fromExistingCompetition(Competition competition);
}
