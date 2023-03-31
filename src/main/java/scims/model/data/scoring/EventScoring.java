package scims.model.data.scoring;

import scims.model.data.Competitor;

import java.util.*;
import java.util.stream.Collectors;

public interface EventScoring<T> {
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

    default List<Competitor> sortCompetitorScores(Map<Competitor, T> competitorsMap) {
        return new ArrayList<>(competitorsMap.entrySet()
                .stream()
                .sorted(getComparator())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (v1, v2) -> v1, LinkedHashMap::new))
                .keySet());
    }

}
