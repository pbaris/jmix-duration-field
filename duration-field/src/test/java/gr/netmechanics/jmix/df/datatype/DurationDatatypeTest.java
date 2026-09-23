package gr.netmechanics.jmix.df.datatype;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.util.Locale;

import io.jmix.core.Messages;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class DurationDatatypeTest {

    @Test
    void exposesConfiguredValuesViaGetters() {
        DurationDatatype datatype = new DurationDatatype();
        ReflectionTestUtils.setField(datatype, "shortLabels", false);
        ReflectionTestUtils.setField(datatype, "hoursADay", 7.5);
        ReflectionTestUtils.setField(datatype, "alwaysDisplayIn", "days");

        assertFalse(datatype.isShortLabels());
        assertEquals(7.5, datatype.getHoursADay());
        assertEquals("days", datatype.getAlwaysDisplayIn());
    }

    @Test
    void formatFallsBackToDefaultEnglishLabelsWhenMessagesNotAvailable() {
        DurationDatatype datatype = new DurationDatatype();
        ReflectionTestUtils.setField(datatype, "shortLabels", false);
        ReflectionTestUtils.setField(datatype, "hoursADay", 8.0);

        assertEquals("3 days 1 hour", datatype.format(Duration.ofHours(25)));
    }

    @Test
    void formatUsesMessagesForLocalizedLabelsWhenAvailable() {
        DurationDatatype datatype = new DurationDatatype();
        ReflectionTestUtils.setField(datatype, "shortLabels", false);
        ReflectionTestUtils.setField(datatype, "hoursADay", 8.0);

        Messages messages = mock(Messages.class);
        when(messages.getMessage(DurationDatatype.MESSAGE_GROUP, "durationUnit.DAYS.long.plural")).thenReturn("jours");
        when(messages.getMessage(DurationDatatype.MESSAGE_GROUP, "durationUnit.HOURS.long")).thenReturn("heure");
        ReflectionTestUtils.setField(datatype, "messages", messages);

        assertEquals("3 jours 1 heure", datatype.format(Duration.ofHours(25)));
    }

    @Test
    void formatSingleUnitUsesMessagesForLocalizedLabel() {
        DurationDatatype datatype = new DurationDatatype();
        ReflectionTestUtils.setField(datatype, "shortLabels", false);
        ReflectionTestUtils.setField(datatype, "hoursADay", 8.0);
        ReflectionTestUtils.setField(datatype, "alwaysDisplayIn", "days");

        Messages messages = mock(Messages.class);
        when(messages.getMessage(DurationDatatype.MESSAGE_GROUP, "durationUnit.DAYS.long.plural")).thenReturn("jours");
        ReflectionTestUtils.setField(datatype, "messages", messages);

        assertEquals("3.75 jours", datatype.format(Duration.ofHours(30)));
    }

    @Test
    void formatWithExplicitLocaleUsesLocaleAwareMessagesLookup() {
        DurationDatatype datatype = new DurationDatatype();
        ReflectionTestUtils.setField(datatype, "shortLabels", false);
        ReflectionTestUtils.setField(datatype, "hoursADay", 8.0);

        Messages messages = mock(Messages.class);
        when(messages.getMessage(DurationDatatype.MESSAGE_GROUP, "durationUnit.DAYS.long", Locale.FRENCH)).thenReturn("jour");
        ReflectionTestUtils.setField(datatype, "messages", messages);

        assertEquals("1 jour", datatype.format(Duration.ofHours(8), Locale.FRENCH));
    }

    @Test
    void formatUsesConfiguredHoursADayAndAlwaysDisplayIn() {
        DurationDatatype datatype = new DurationDatatype();
        ReflectionTestUtils.setField(datatype, "shortLabels", true);
        ReflectionTestUtils.setField(datatype, "hoursADay", 7.5);
        ReflectionTestUtils.setField(datatype, "alwaysDisplayIn", "days");

        assertEquals("4d", datatype.format(Duration.ofHours(30)));
    }

    @Test
    void formatFallsBackToBreakdownWhenAlwaysDisplayInIsBlank() {
        DurationDatatype datatype = new DurationDatatype();
        ReflectionTestUtils.setField(datatype, "shortLabels", true);
        ReflectionTestUtils.setField(datatype, "hoursADay", 8.0);
        ReflectionTestUtils.setField(datatype, "alwaysDisplayIn", "");

        assertEquals("3d 1h", datatype.format(Duration.ofHours(25)));
    }

    @Test
    void parseUsesConfiguredHoursADay() {
        DurationDatatype datatype = new DurationDatatype();
        ReflectionTestUtils.setField(datatype, "hoursADay", 7.5);

        assertEquals(Duration.ofHours(15), datatype.parse("2d"));
    }
}
