package scims.ui.fx;

import javafx.scene.control.TreeItem;
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
        //add columns
        getColumns().add(new CompetitorsColumn());
        List<EventColumn> eventColumns = new ArrayList<>();
        for(Event event : eventsInOrder)
        {
            EventColumn eventColumn = new EventColumn(event);
            eventColumns.add(eventColumn);
            getColumns().add(eventColumn);
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
        welterweightItem.getChildren().add(new TreeItem<>(new CompetitorRow(competitor, eventColumns)));
        root.getChildren().add(welterweightItem);
    }
}
