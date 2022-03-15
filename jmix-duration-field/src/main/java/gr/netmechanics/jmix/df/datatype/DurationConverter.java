package gr.netmechanics.jmix.df.datatype;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * @author Panos Bariamis (pbaris)
 */
@Converter(autoApply = true)
public class DurationConverter implements AttributeConverter<Duration, Long> {

    @Override
    public Long convertToDatabaseColumn(final Duration attribute) {
        return attribute == null ? null : attribute.toMillis();
    }

    @Override
    public Duration convertToEntityAttribute(final Long dbData) {
        return dbData == null ? null : Duration.of(dbData, ChronoUnit.MILLIS);
    }
}
