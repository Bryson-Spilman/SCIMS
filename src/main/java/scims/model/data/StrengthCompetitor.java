package scims.model.data;

import java.util.Objects;

public class StrengthCompetitor implements Competitor {

    private final String _name;
    private final Integer _age;
    private final Double _weight;

    StrengthCompetitor(String name, Integer age, Double weight)
    {
        _name = name;
        _age = age;
        _weight = weight;
    }
    @Override
    public Integer getAge() {
        return _age;
    }

    @Override
    public Double getWeight() {
        return _weight;
    }

    @Override
    public String getName() {
        return _name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StrengthCompetitor that = (StrengthCompetitor) o;
        return Objects.equals(_age, that._age) && Objects.equals(_name, that._name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_name, _age);
    }

    @Override
    public String toString() {
        return getName();
    }
}
