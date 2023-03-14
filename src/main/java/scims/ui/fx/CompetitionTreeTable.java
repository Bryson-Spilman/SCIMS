package scims.ui.fx;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import scims.model.data.*;
import scims.model.enums.EventScoreType;

import java.util.ArrayList;
import java.util.List;

public class CompetitionTreeTable extends TreeTableView<Object> {

    public CompetitionTreeTable() {
        // Create the columns
        List<Event> eventsInOrder = new ArrayList<>();
        eventsInOrder.add(new StrengthEventBuilder()
                .withName("Event 1")
                .withScoreType(EventScoreType.REPS)
                .build());
        List<TreeTableColumn<Object, String>> eventColumns = new ArrayList<>();

        //add columns
        getColumns().add(new CompetitorsColumn());
        for(Event event : eventsInOrder)
        {
            getColumns().add(new EventColumn(event));
        }

        // Set the root item of the table
        setRoot(new TreeItem<>());

        TreeItem<Object> root = getRoot();
        WeightClass testWeightClass = new StrengthWeightClassBuilder()
                .withName("LightWeight")
                .withMaxCompetitorWeight(200.4)
                .withEventsInOrder(eventsInOrder)
                .build();
        Competitor competitor = new StrengthCompetitorBuilder()
                .withCompetitorName("Bryson Spilman")
                .withCompetitorAge(27)
                .withCompetitorWeight(199.8)
                .build();
        testWeightClass.addCompetitor(competitor);
        TreeItem<Object> welterweightItem = new TreeItem<>(new WeightClassRow(testWeightClass));
        root.getChildren().add(welterweightItem);
    }
}
