package scims.model.fluentbuilders.competitor;

public interface FluentUpdateCompetitor extends FluentCompetitorBuilder {
    FluentUpdateCompetitor withUpdatedName(String name);
    FluentUpdateCompetitor withUpdatedAge(int age);
    FluentUpdateCompetitor withUpdatedWeight(double weight);
}
