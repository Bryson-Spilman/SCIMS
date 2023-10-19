package scims.model.data.scoring;

import scims.model.data.Competitor;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

public interface EventScoring<T> extends Scoring{
    void setScore(T score);
    T getScore();
    Comparator<Map.Entry<Competitor, T>> getComparator();

    default String getScoreType() {
        String className = this.getClass().getSimpleName();
        String retVal =  className.substring(0, className.indexOf("Scoring"));
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < retVal.length(); i++) {
            char c = retVal.charAt(i);
            if (Character.isUpperCase(c) && i != 0) {
                sb.append(" ");
            }
            sb.append(c);
        }
        retVal = sb.toString();
        return retVal;

    }

    T parseValueForComparing(Object value);

    default Map<Competitor, Double> sortCompetitorScores(Map<Competitor, T> competitorsMap) {
        Map<Competitor, Double> retVal = new LinkedHashMap<>();
        ArrayList<Competitor> sortedList = new ArrayList<>(competitorsMap.entrySet()
                .stream()
                .sorted(getComparator())
                .collect(Collectors.toMap(Map.Entry::getKey, competitorTEntry -> parseValueForComparing(competitorTEntry.getValue()), (v1, v2) -> v1, LinkedHashMap::new))
                .keySet());
        double pointsPossible = sortedList.size();
        int numWithThisScore = 1;
        for(int i=0; i < sortedList.size(); i+= numWithThisScore) {
            Competitor competitor = sortedList.get(i);
            T score = competitorsMap.get(competitor);
            numWithThisScore = getNumberOfCompetitorsWithSameScore(score, sortedList.subList(i+1, sortedList.size()), competitorsMap) + 1;
            double totalPointsForPlacing = pointsPossible;
            double localPointsPossible = pointsPossible -1;
            for(int n=1; n < numWithThisScore; n++) { //start at 1 since we already account for the first person with this score
                totalPointsForPlacing += (localPointsPossible);
                localPointsPossible --;
            }
            double points = totalPointsForPlacing / numWithThisScore;
            if(score == null || score.toString() == null || score.toString().isEmpty()
                || !(score instanceof CustomScore) && isZeroed(score)) {
                points = 0.0;
            }
            else if(score instanceof CustomScore) {
                CustomScore<?,?,?> customScore = (CustomScore<?,?,?>) score;
                if(customScore.getPrimaryScoring() == null || customScore.getPrimaryScoring().getScore() == null
                        || customScore.getPrimaryScoring().getScore().toString().isEmpty()
                        || isZeroed(customScore.getPrimaryScoring().getScore())) {
                    points = 0.0;
                }
            }
            retVal.put(competitor, points);
            for(int j=i+1; j < numWithThisScore; j++) {
                retVal.put(sortedList.get(j), points);
            }
            pointsPossible -= numWithThisScore;
        }
        return retVal;
    }

    default boolean isZeroed(Object score)
    {
        boolean retVal = false;
        if(score != null)
        {
            if(score instanceof Double)
            {
                retVal = ((Double)score) == 0.0;
            }
            else if(score instanceof Duration)
            {
                retVal = ((Duration)score).isZero();
            }
            else if(score instanceof Integer)
            {
                retVal = ((Integer)score) == 0;
            }
            else if(score.toString().replace("0", "").isEmpty())
            {
                retVal = true;
            }
        }
        return retVal;
    }

    default int getNumberOfCompetitorsWithSameScore(T score, List<Competitor> subList, Map<Competitor, T> scoreMap)
    {
        int num = 0;
        for(Competitor competitor : subList) {
            if(scoreMap.get(competitor).equals(score)) {
                num++;
            }
        }
        return num;
    }

}
