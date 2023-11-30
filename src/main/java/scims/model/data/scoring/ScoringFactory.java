package scims.model.data.scoring;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class ScoringFactory {

    private static final Map<String, Class<? extends EventScoring<?>>> SCORING_MAP = new HashMap<>();

    static {
        SCORING_MAP.put("Weight", WeightScoring.class);
        SCORING_MAP.put("Reps", RepsScoring.class);
        SCORING_MAP.put("Distance", DistanceScoring.class);
        SCORING_MAP.put("Time", TimeScoring.class);
    }

    public static EventScoring<?> createScoring(String scoreType) {
        if(scoreType == null)
        {
            return null;
        }
        scoreType = scoreType.replace(" ", "");
        Class<? extends EventScoring<?>> scoringClass = SCORING_MAP.get(scoreType);
        EventScoring<?> scoring;
        if (scoringClass == null) {
            scoring = new CustomEventScoring<>();
            String[] scoreSplit = scoreType.split("->");
            if(scoreSplit.length > 1)
            {
                Class<? extends EventScoring<?>> primaryScoreType = SCORING_MAP.get(scoreSplit[0]);
                Class<? extends EventScoring<?>> secondaryScoreType = SCORING_MAP.get(scoreSplit[1]);
                Class<? extends EventScoring<?>> thirdScoreType = null;
                EventScoring<?> third = null;
                if(scoreSplit.length > 2)
                {
                    thirdScoreType = SCORING_MAP.get(scoreSplit[2]);
                }
                try {
                    EventScoring<?> primary = primaryScoreType.newInstance();
                    EventScoring<?> secondary = secondaryScoreType.newInstance();
                    if(thirdScoreType != null)
                    {
                        third = thirdScoreType.newInstance();
                    }
                    CustomScore<?,?,?> customScore = new CustomScore<>(primary, secondary, third);
                    ((CustomEventScoring)scoring).setScore(customScore);
                } catch (InstantiationException | IllegalAccessException e) {
                    scoring = new CustomEventScoring<>();
                }
            }
        } else {
            try {
                scoring = scoringClass.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                scoring = new CustomEventScoring<>();
            }
        }

        return scoring;
    }

    public static List<EventScoring<?>> createAllScorings()  {
        List<EventScoring<?>> retVal = new ArrayList<>();
        for(Class<? extends EventScoring<?>> scoringClass : SCORING_MAP.values()) {
            EventScoring<?> scoring;
            try {
                scoring = scoringClass.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                scoring = new CustomEventScoring<>();
            }
            retVal.add(scoring);
        }
        return retVal;
    }
}
