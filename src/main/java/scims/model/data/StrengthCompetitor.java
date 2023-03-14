package scims.model.data;

import java.util.Objects;

public class StrengthCompetitor implements Competitor {

    private final String _name;
    private final int _age;
    private final double _weight;

    StrengthCompetitor(String name, int age, double weight)
    {
        _name = name;
        _age = age;
        _weight = weight;
    }
    @Override
    public int getAge() {
        return _age;
    }

    @Override
    public double getWeight() {
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
        return _age == that._age && Objects.equals(_name, that._name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_name, _age);
    }
}
