package scims.model;

import scims.model.fluentbuilders.competition.FluentCompetitionBuilder;
import scims.model.fluentbuilders.competitor.*;

public class StrengthCompetitorBuilder implements FluentWithCompetitorName, FluentFromExistingCompetitor {

    @Override
    public FluentWithCompetitorAge withCompetitorName(String name) {
        return null;
    }

    @Override
    public FluentUpdateCompetitor fromExistingCompetitor(Competitor competitor) {
        return null;
    }

    private class WithCompetitorAge implements FluentWithCompetitorAge {
        @Override
        public FluentWithCompetitorWeight withCompetitorAge(int age) {
            return null;
        }
    }

    private class WithCompetitorWeight implements FluentWithCompetitorWeight {
        @Override
        public FluentCompetitorBuilder withCompetitorWeight(double weight) {
            return null;
        }
    }

    private class CompetitorBuilder implements FluentCompetitorBuilder {

        @Override
        public StrengthCompetitor build() {
            return null;
        }
    }

    private class UpdateCompetitor extends CompetitorBuilder implements FluentUpdateCompetitor {

        @Override
        public FluentFromExistingCompetitor withUpdatedName(String name) {
            return null;
        }

        @Override
        public FluentFromExistingCompetitor withUpdatedAge(int age) {
            return null;
        }

        @Override
        public FluentFromExistingCompetitor withUpdatedWeight(double weight) {
            return null;
        }
    }
}
