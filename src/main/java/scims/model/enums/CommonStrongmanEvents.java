package scims.model.enums;

import scims.model.data.*;
import scims.model.data.scoring.*;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public enum CommonStrongmanEvents {

    MAX_DEADLIFT(new StrengthEventBuilder().withName("Max Barbell Deadlift").withScoring(new WeightScoring()).withNoTimeLimit().build()),
    MAX_AXLE_PRESS(new StrengthEventBuilder().withName("Max Axle Press").withScoring(new WeightScoring()).withNoTimeLimit().build()),
    MAX_LOG_PRESS(new StrengthEventBuilder().withName("Max Log Press").withScoring(new WeightScoring()).withNoTimeLimit().build()),
    MAX_CIRCUS_DUMBBELL(new StrengthEventBuilder().withName("Max Circus Dumbbell").withScoring(new WeightScoring()).withNoTimeLimit().build()),
    MAX_BARBELL_SQUAT(new StrengthEventBuilder().withName("Max Barbell Squat").withScoring(new WeightScoring()).withNoTimeLimit().build()),
    MAX_BENCH_PRESS(new StrengthEventBuilder().withName("Max Barbell Bench Press").withScoring(new WeightScoring()).withNoTimeLimit().build()),
    MAX_KEG_TOSS(new StrengthEventBuilder().withName("Max Keg Toss (fixed height)").withScoring(new WeightScoring()).withNoTimeLimit().build()),
    MAX_SANDBAG_TOSS(new StrengthEventBuilder().withName("Max Sandbag Toss (fixed height)").withScoring(new WeightScoring()).withNoTimeLimit().build()),
    MAX_HUMMER_TIRE_DEADLIFT(new StrengthEventBuilder().withName("Max Hummer Tire Deadlift").withScoring(new WeightScoring()).withNoTimeLimit().build()),
    MAX_SILVER_DOLLAR_DEADLIFT(new StrengthEventBuilder().withName("Max Silver Dollar Deadlift").withScoring(new WeightScoring()).withNoTimeLimit().build()),
    MAX_DISTANCE_FARMERS(new StrengthEventBuilder().withName("Max Distance Farmer's Carry").withScoring(new DistanceScoring()).withTimeLimit(Duration.ofSeconds(60)).build()),
    MAX_DISTANCE_YOKE(new StrengthEventBuilder().withName("Max Distance Yoke Carry").withScoring(new DistanceScoring()).withTimeLimit(Duration.ofSeconds(60)).build()),
    MAX_HEIGHT_KEG_TOSS(new StrengthEventBuilder().withName("Max Height Keg Toss").withScoring(new DistanceScoring()).withNoTimeLimit().build()),
    MAX_HEIGHT_SANDBAG_TOSS(new StrengthEventBuilder().withName("Max Height Sandbag Toss").withScoring(new DistanceScoring()).withNoTimeLimit().build()),
    TRUCK_PULL(new StrengthEventBuilder().withName("Truck Pull").withScoring(new CustomEventScoring<Duration, Double, None>().withPrimaryScoring(new TimeScoring()).withSecondaryScoring(new DistanceScoring())).withTimeLimit(Duration.ofSeconds(60)).build()),
    DEADLIFT_FOR_REPS(new StrengthEventBuilder().withName("Barbell Deadlift for reps").withScoring(new RepsScoring()).withTimeLimit(Duration.ofSeconds(60)).build()),
    ZERCHER_DEADLIFT_FOR_REPS(new StrengthEventBuilder().withName("Zercher Deadlift for reps").withScoring(new RepsScoring()).withTimeLimit(Duration.ofSeconds(60)).build()),
    AXLE_PRESS_FOR_REPS(new StrengthEventBuilder().withName("Axel Press for reps").withScoring(new RepsScoring()).withTimeLimit(Duration.ofSeconds(60)).build()),
    LOG_PRESS_FOR_REPS(new StrengthEventBuilder().withName("Log Press for reps").withScoring(new RepsScoring()).withTimeLimit(Duration.ofSeconds(60)).build()),
    CIRCUS_DUMBBELL_FOR_REPS(new StrengthEventBuilder().withName("Circus Dumbbell for reps").withScoring(new RepsScoring()).withTimeLimit(Duration.ofSeconds(60)).build()),
    LOADING_RACE(new StrengthEventBuilder().withName("Loading Race").withScoring(new CustomEventScoring<Integer, Duration, None>()).withTimeLimit(Duration.ofSeconds(90)).build()),
    HERCULES_HOLD(new StrengthEventBuilder().withName("Hercules Hold").withScoring(new TimeScoring()).withNoTimeLimit().build()),
    LOG_LADDER(new StrengthEventBuilder().withName("Log Press Ladder").withScoring(new CustomEventScoring<Double, Duration, None>().withPrimaryScoring(new WeightScoring()).withSecondaryScoring(new TimeScoring())).withTimeLimit(Duration.ofSeconds(90)).build()),
    AXEL_LADDER(new StrengthEventBuilder().withName("Axel Press Ladder").withScoring(new CustomEventScoring<Double, Duration, None>().withPrimaryScoring(new WeightScoring()).withSecondaryScoring(new TimeScoring())).withTimeLimit(Duration.ofSeconds(90)).build());
    private final StrengthEvent _event;

    CommonStrongmanEvents(StrengthEvent event) {
        _event = event;
    }

    public static List<StrengthEvent> getValues() {
        List<StrengthEvent> retVal = new ArrayList<>();
        for(CommonStrongmanEvents commonEvent : values())
        {
            retVal.add(commonEvent._event);
        }
        return retVal;
    }

    @Override
    public String toString() {
        return _event.getName();
    }
}
