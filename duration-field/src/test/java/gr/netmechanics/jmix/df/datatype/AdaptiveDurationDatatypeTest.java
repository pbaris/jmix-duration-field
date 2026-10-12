package gr.netmechanics.jmix.df.datatype;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Duration;

import gr.netmechanics.jmix.df.annotation.DurationFormat;
import io.jmix.core.Messages;
import org.junit.jupiter.api.Test;

class AdaptiveDurationDatatypeTest {

    private static class Sample {
        @DurationFormat(shortLabels = false, hoursADay = 7.5, alwaysDisplayIn = "days")
        private Duration estimate;
    }

    private DurationFormat annotation() throws NoSuchFieldException {
        return Sample.class.getDeclaredField("estimate").getAnnotation(DurationFormat.class);
    }

    @Test
    void formatUsesAnnotationOptions() throws NoSuchFieldException {
        AdaptiveDurationDatatype datatype = new AdaptiveDurationDatatype(annotation(), null);

        // hoursADay=7.5: 30h / 7.5h-per-day = exactly 4 days
        assertEquals("4 days", datatype.format(Duration.ofHours(30)));
    }

    @Test
    void formatReturnsEmptyStringForNonDurationValue() throws NoSuchFieldException {
        AdaptiveDurationDatatype datatype = new AdaptiveDurationDatatype(annotation(), null);

        assertEquals("", datatype.format("not a duration"));
    }

    @Test
    void formatUsesMessagesForLocalizedLabelsWhenAvailable() throws NoSuchFieldException {
        Messages messages = mock(Messages.class);
        when(messages.getMessage(DurationDatatype.MESSAGE_GROUP, "durationUnit.DAYS.long.plural")).thenReturn("jours");

        AdaptiveDurationDatatype datatype = new AdaptiveDurationDatatype(annotation(), messages);

        assertEquals("4 jours", datatype.format(Duration.ofHours(30)));
    }

    @Test
    void parseUsesAnnotationHoursADay() throws NoSuchFieldException {
        AdaptiveDurationDatatype datatype = new AdaptiveDurationDatatype(annotation(), null);

        assertEquals(Duration.ofHours(15), datatype.parse("2d"));
    }

    @Test
    void getJavaClassReturnsDuration() throws NoSuchFieldException {
        AdaptiveDurationDatatype datatype = new AdaptiveDurationDatatype(annotation(), null);

        assertEquals(Duration.class, datatype.getJavaClass());
    }
}
