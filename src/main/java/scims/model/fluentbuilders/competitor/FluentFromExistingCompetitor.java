package scims.model.fluentbuilders.competitor;

import scims.model.Competitor;

public interface FluentFromExistingCompetitor {
    FluentUpdateCompetitor fromExistingCompetitor(Competitor competitor);
}
