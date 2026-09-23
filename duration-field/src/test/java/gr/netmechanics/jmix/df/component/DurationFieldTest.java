package gr.netmechanics.jmix.df.component;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Duration;

import gr.netmechanics.jmix.df.datatype.DurationDatatype;
import io.jmix.core.Messages;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class DurationFieldTest {

    private DurationField newField(final boolean globalShortLabels, final double globalHoursADay) {
        DurationField field = new DurationField();
        DurationDatatype global = new DurationDatatype();
        ReflectionTestUtils.setField(global, "shortLabels", globalShortLabels);
        ReflectionTestUtils.setField(global, "hoursADay", globalHoursADay);
        ReflectionTestUtils.setField(field, "durationDatatype", global);
        return field;
    }

    @Test
    void gettersFallBackToGlobalDefaultsWhenNoOverrideSet() {
        DurationField field = newField(true, 8);

        assertTrue(field.isShortLabels());
        assertEquals(8.0, field.getHoursADay());
        assertNull(field.getAlwaysDisplayIn());
    }

    @Test
    void gettersReturnOverrideWhenSet() {
        DurationField field = newField(true, 8);

        field.setShortLabels(false);
        field.setHoursADay(7.5);
        field.setAlwaysDisplayIn("days");

        assertFalse(field.isShortLabels());
        assertEquals(7.5, field.getHoursADay());
        assertEquals("days", field.getAlwaysDisplayIn());
    }

    @Test
    void convertToPresentationUsesGlobalDefaultsWhenNoOverrideSet() {
        DurationField field = newField(true, 8);

        assertEquals("3d 1h", field.convertToPresentation(Duration.ofHours(25)));
    }

    @Test
    void convertToPresentationUsesFieldLevelShortLabelsOverride() {
        DurationField field = newField(true, 8);
        field.setShortLabels(false);

        assertEquals("3 days 1 hour", field.convertToPresentation(Duration.ofHours(25)));
    }

    @Test
    void convertToPresentationUsesFieldLevelHoursADayOverride() {
        DurationField field = newField(true, 8);
        field.setHoursADay(7.5);

        assertEquals("4d", field.convertToPresentation(Duration.ofHours(30)));
    }

    @Test
    void convertToPresentationUsesFieldLevelAlwaysDisplayInOverride() {
        DurationField field = newField(true, 8);
        field.setAlwaysDisplayIn("days");

        assertEquals("3.75d", field.convertToPresentation(Duration.ofHours(30)));
    }

    @Test
    void convertToPresentationReturnsEmptyStringForNullValue() {
        DurationField field = newField(true, 8);

        assertEquals("", field.convertToPresentation(null));
    }

    @Test
    void convertToPresentationUsesMessagesForLocalizedLabelsWhenAvailable() {
        DurationField field = newField(true, 8);
        field.setShortLabels(false);

        Messages messages = mock(Messages.class);
        when(messages.getMessage(DurationDatatype.MESSAGE_GROUP, "durationUnit.DAYS.long.plural")).thenReturn("jours");
        when(messages.getMessage(DurationDatatype.MESSAGE_GROUP, "durationUnit.HOURS.long")).thenReturn("heure");
        ReflectionTestUtils.setField(field, "messages", messages);

        assertEquals("3 jours 1 heure", field.convertToPresentation(Duration.ofHours(25)));
    }

    @Test
    void convertToModelParsesUsingEffectiveHoursADay() {
        DurationField field = newField(true, 8);
        field.setHoursADay(7.5);

        assertEquals(Duration.ofHours(15), field.convertToModel("2d"));
    }

    @Test
    void convertToModelReturnsNullForBlankInput() {
        DurationField field = newField(true, 8);

        assertNull(field.convertToModel(""));
        assertNull(field.convertToModel(null));
    }
}
