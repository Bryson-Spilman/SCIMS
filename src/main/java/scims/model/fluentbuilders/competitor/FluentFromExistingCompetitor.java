package scims.model.fluentbuilders.competitor;

import scims.model.data.Competitor;

public interface FluentFromExistingCompetitor {
    FluentUpdateCompetitor fromExistingCompetitor(Competitor competitor);
}
