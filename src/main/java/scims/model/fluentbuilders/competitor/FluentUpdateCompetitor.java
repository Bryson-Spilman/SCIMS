package scims.model.fluentbuilders.competitor;

public interface FluentUpdateCompetitor extends FluentCompetitorBuilder {
    FluentUpdateCompetitor withUpdatedName(String name);
    FluentUpdateCompetitor withUpdatedAge(Integer age);
    FluentUpdateCompetitor withUpdatedWeight(Double weight);
}
