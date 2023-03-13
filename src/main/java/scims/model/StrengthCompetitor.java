package scims.model;

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
}
