package scims.model.data;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.Objects;

public class StrengthCompetitor implements Competitor {

    @JacksonXmlProperty(isAttribute = true, localName = "name")
    private final String _name;
    @JacksonXmlProperty(isAttribute = true, localName = "age")
    private final Integer _age;
    @JacksonXmlProperty(isAttribute = true, localName = "weight")
    private final Double _weight;

    public StrengthCompetitor(String name, Integer age, Double weight)
    {
        _name = name;
        _age = age;
        _weight = weight;
    }

    public StrengthCompetitor()
    {
        this(null, null, null);
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
