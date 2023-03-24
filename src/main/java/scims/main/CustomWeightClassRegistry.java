package scims.main;

import scims.model.data.WeightClass;

import java.util.ArrayList;
import java.util.List;

public class CustomWeightClassRegistry {
    private static volatile CustomWeightClassRegistry INSTANCE;
    private List<WeightClass> _weightClassList;

    private CustomWeightClassRegistry() {
        _weightClassList = new ArrayList<>();
    }

    public static CustomWeightClassRegistry getInstance() {
        if (INSTANCE == null) {
            synchronized (CustomWeightClassRegistry.class) {
                if (INSTANCE == null) {
                    INSTANCE = new CustomWeightClassRegistry();
                }
            }
        }
        return INSTANCE;
    }

    public synchronized void registerWeightClass(WeightClass weightClass) {
        _weightClassList.add(weightClass);
    }

    public synchronized List<WeightClass> getWeightClasses() {
        return _weightClassList;
    }
}
