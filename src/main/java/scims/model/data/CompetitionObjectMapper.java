package scims.model.data;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import scims.model.data.scoring.EventScoring;
import scims.model.data.scoring.Scoring;
import scims.model.data.scoring.ScoringFactory;
import scims.ui.swing.DateTimeTextField;

import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;
import java.time.ZonedDateTime;

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
                gen.writeString(((EventScoring<?>)value).getScoreType());
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
