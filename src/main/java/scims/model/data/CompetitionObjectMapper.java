package scims.model.data;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import scims.model.data.scoring.*;
import scims.ui.swing.DateTimeTextField;

import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;

public class CompetitionObjectMapper {
    private static final ObjectMapper MAPPER = buildObjectMapper();

    private static ObjectMapper buildObjectMapper() {
        ObjectMapper objectMapper = new XmlMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(Scoring.class, new EventScoringSerializer());
        module.addDeserializer(Scoring.class, new EventScoringDeserializer());
        module.addSerializer(ZonedDateTime.class, new ZonedDateTimeSerializer());
        module.addDeserializer(ZonedDateTime.class, new ZonedDateTimeDeserializer());
        module.addSerializer(Duration.class, new DurationSerializer());
        module.addDeserializer(Duration.class, new DurationDeserializer());
        module.addDeserializer(CompetitorEventScore.class, new CompetitorEventScoreDeserializer());
        objectMapper.registerModule(module);
        return objectMapper;
    }

    public static void serializeCompetition(Competition competition, Path file) throws IOException {
        MAPPER.writeValue(file.toFile(), competition);
    }

    public static Competition deSerializeCompetition(Path file) throws IOException {
        return MAPPER.readValue(file.toFile(), StrengthCompetition.class);
    }

    private static class DurationSerializer extends JsonSerializer<Duration>
    {
        @Override
        public void serialize(Duration value, JsonGenerator gen, SerializerProvider provider) throws IOException
        {
            gen.writeNumber(value.getSeconds());
        }
    }

    private static class DurationDeserializer extends JsonDeserializer<Duration>
    {
        @Override
        public Duration deserialize(JsonParser p, DeserializationContext ctxt) throws IOException
        {
            Duration retVal = null;
            String seconds = p.getText();
            if(seconds != null && !seconds.isEmpty())
            {
                retVal = Duration.ofSeconds(Integer.parseInt(seconds.trim()));
            }
            return retVal;
        }

    }

    private static class CompetitorEventScoreDeserializer extends JsonDeserializer<CompetitorEventScore>
    {
        @Override
        public CompetitorEventScore deserialize(JsonParser p, DeserializationContext ctxt) throws IOException
        {
            JsonNode node = p.getCodec().readTree(p);

            String event = node.get("event").asText();
            String scoreType = node.get("score-type").asText();
            String scoreValue = node.get("score").asText();
            StrengthEvent eventObj = new StrengthEventBuilder().withName(event)
                    .withScoring(ScoringFactory.createScoring(scoreType))
                    .withTimeLimit(null)
                    .build();
            Object convertedScoreValue = null;
            if("Time".equalsIgnoreCase(scoreType) && isDuration(scoreValue)) {
                convertedScoreValue = Duration.parse(scoreValue);
            }
            else if (isInteger(scoreValue)) {
                convertedScoreValue = Integer.parseInt(scoreValue);
            } else if (isDouble(scoreValue)) {
                convertedScoreValue = Double.parseDouble(scoreValue);
            }
            return new CompetitorEventScore(eventObj, ScoringFactory.createScoring(scoreType), convertedScoreValue);
        }

        private boolean isInteger(String value) {
            try {
                Integer.parseInt(value);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }

        private boolean isDouble(String value) {
            try {
                Double.parseDouble(value);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }

        private boolean isDuration(String value) {
            try {
                Duration.parse(value);
                return true;
            } catch (DateTimeParseException e) {
                return false;
            }
        }

    }

    private static class ZonedDateTimeSerializer extends JsonSerializer<ZonedDateTime>
    {
        @Override
        public void serialize(ZonedDateTime value, JsonGenerator gen, SerializerProvider provider) throws IOException
        {
            gen.writeString(value.format(DateTimeTextField.DATE_TIME_FORMATTER));
        }
    }

    private static class ZonedDateTimeDeserializer extends JsonDeserializer<ZonedDateTime>
    {
        @Override
        public ZonedDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException
        {
            ZonedDateTime retVal = null;
            String dateStr = p.getText().trim();
            if(!dateStr.isEmpty())
            {
                retVal = ZonedDateTime.parse(dateStr, DateTimeTextField.DATE_TIME_FORMATTER);
            }
            return retVal;
        }

    }

    private static class EventScoringSerializer extends JsonSerializer<Scoring> {

        @Override
        public void serialize(Scoring value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            if(value instanceof EventScoring) {
                String scoreType = ((EventScoring<?>) value).getScoreType();
                if("Custom".equalsIgnoreCase(scoreType)) {
                    EventScoring<?> primary = ((CustomEventScoring<?, ?>) value).getScore().getPrimaryScoring();
                    EventScoring<?> secondary = ((CustomEventScoring<?, ?>) value).getScore().getSecondaryScoring();
                    String type = primary.getScoreType();
                    if(secondary != null)
                    {
                        type += "->" + secondary.getScoreType();
                    }
                    gen.writeString(type);
                }
                else {
                    gen.writeString(((EventScoring<?>)value).getScoreType());
                }

            } else {
                gen.writeNull();
            }
        }
    }

    private static class EventScoringDeserializer extends JsonDeserializer<Scoring> {
        @Override
        public Scoring deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
            String value = p.getValueAsString();
            return ScoringFactory.createScoring(value);
        }
    }
}
