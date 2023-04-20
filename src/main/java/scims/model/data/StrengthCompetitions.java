package scims.model.data;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.ArrayList;
import java.util.List;

@JacksonXmlRootElement(localName = "competitions")
public class StrengthCompetitions {
    @JacksonXmlProperty(localName = "competition")
    private final List<StrengthCompetition> _competitions = new ArrayList<>();

    public List<StrengthCompetition> getCompetitions() {
        return _competitions;
    }

    public void setCompetitions(List<Competition> competitions)
    {
        _competitions.clear();
        for(Competition competition : competitions)
        {
            if(competition instanceof StrengthCompetition)
            {
                _competitions.add((StrengthCompetition) competition);
            }
        }
    }
}
