package scims.model.data;

import scims.model.fluentbuilders.competitor.*;

public class StrengthCompetitorBuilder implements FluentWithCompetitorName, FluentFromExistingCompetitor {

    private String _name;
    private Integer _age;
    private Double _weight;

    @Override
    public FluentWithCompetitorAge withCompetitorName(String name) {
        _name = name;
        return new WithCompetitorAge();
    }

    @Override
    public FluentUpdateCompetitor fromExistingCompetitor(Competitor competitor) {
        _name = competitor.getName();
        _age = competitor.getAge();
        _weight = competitor.getWeight();
        return new UpdateCompetitor();
    }

    private class WithCompetitorAge implements FluentWithCompetitorAge {
        @Override
        public FluentWithCompetitorWeight withCompetitorAge(Integer age) {
            _age = age;
            return new WithCompetitorWeight();
        }
    }

    private class WithCompetitorWeight implements FluentWithCompetitorWeight {
        @Override
        public FluentCompetitorBuilder withCompetitorWeight(Double weight) {
            _weight = weight;
            return new CompetitorBuilder();
        }
    }

    private class CompetitorBuilder implements FluentCompetitorBuilder {

        @Override
        public StrengthCompetitor build() {
            return new StrengthCompetitor(_name, _age, _weight);
        }
    }

    private class UpdateCompetitor extends CompetitorBuilder implements FluentUpdateCompetitor {

        @Override
        public FluentUpdateCompetitor withUpdatedName(String name) {
            _name = name;
            return this;
        }

        @Override
        public FluentUpdateCompetitor withUpdatedAge(Integer age) {
            _age = age;
            return this;
        }

        @Override
        public FluentUpdateCompetitor withUpdatedWeight(Double weight) {
            _weight = weight;
            return this;
        }
    }
}
