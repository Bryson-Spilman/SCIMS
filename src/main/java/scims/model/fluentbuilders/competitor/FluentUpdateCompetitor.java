package scims.model.fluentbuilders.competitor;

public interface FluentUpdateCompetitor extends FluentCompetitorBuilder {
    FluentFromExistingCompetitor withUpdatedName(String name);
    FluentFromExistingCompetitor withUpdatedAge(int age);
    FluentFromExistingCompetitor withUpdatedWeight(double weight);
}
