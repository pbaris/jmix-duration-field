package gr.netmechanics.jmix.df.component;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.time.Duration;

import gr.netmechanics.jmix.df.annotation.DurationFormat;
import gr.netmechanics.jmix.df.datatype.DurationDatatype;
import io.jmix.core.Messages;
import io.jmix.core.metamodel.model.MetaProperty;
import io.jmix.core.metamodel.model.MetaPropertyPath;
import io.jmix.flowui.component.delegate.TextInputFieldDelegate;
import io.jmix.flowui.data.EntityValueSource;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class DurationFieldTest {

    private static class SampleEntity {
        @DurationFormat(shortLabels = false, hoursADay = 7.5, alwaysDisplayIn = "days")
        private Duration estimate;
    }

    private DurationField newField(final boolean globalShortLabels, final double globalHoursADay) {
        DurationField field = new DurationField();
        DurationDatatype global = new DurationDatatype();
        ReflectionTestUtils.setField(global, "shortLabels", globalShortLabels);
        ReflectionTestUtils.setField(global, "hoursADay", globalHoursADay);
        ReflectionTestUtils.setField(field, "durationDatatype", global);

        // Unbound by default (getValueSource() returns null), matching a field with no
        // dataContainer/property configured; bindToAnnotatedProperty() overrides this.
        TextInputFieldDelegate<DurationField, Duration> delegate = mock(TextInputFieldDelegate.class);
        ReflectionTestUtils.setField(field, "fieldDelegate", delegate);

        return field;
    }

    private void bindToAnnotatedProperty(final DurationField field, final Field annotatedField) {
        MetaProperty metaProperty = mock(MetaProperty.class);
        when(metaProperty.getAnnotatedElement()).thenReturn(annotatedField);

        MetaPropertyPath propertyPath = mock(MetaPropertyPath.class);
        when(propertyPath.getMetaProperty()).thenReturn(metaProperty);

        EntityValueSource<?, Duration> valueSource = mock(EntityValueSource.class);
        when(valueSource.getMetaPropertyPath()).thenReturn(propertyPath);

        TextInputFieldDelegate<DurationField, Duration> delegate = mock(TextInputFieldDelegate.class);
        when(delegate.getValueSource()).thenReturn(valueSource);

        ReflectionTestUtils.setField(field, "fieldDelegate", delegate);
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

    @Test
    void gettersFallBackToEntityAnnotationWhenNoFieldOverrideSet() throws NoSuchFieldException {
        DurationField field = newField(true, 8);
        bindToAnnotatedProperty(field, SampleEntity.class.getDeclaredField("estimate"));

        assertFalse(field.isShortLabels());
        assertEquals(7.5, field.getHoursADay());
        assertEquals("days", field.getAlwaysDisplayIn());
    }

    @Test
    void fieldLevelOverrideTakesPrecedenceOverEntityAnnotation() throws NoSuchFieldException {
        DurationField field = newField(true, 8);
        bindToAnnotatedProperty(field, SampleEntity.class.getDeclaredField("estimate"));
        field.setShortLabels(true);

        assertTrue(field.isShortLabels());
    }

    @Test
    void entityAnnotationTakesPrecedenceOverGlobalDefaultsWhenNoFieldOverride() throws NoSuchFieldException {
        DurationField field = newField(true, 8);
        bindToAnnotatedProperty(field, SampleEntity.class.getDeclaredField("estimate"));

        // annotation's hoursADay=7.5: 30h / 7.5h-per-day = exactly 4 days (vs. 3.75d with the global default of 8)
        assertEquals("4 days", field.convertToPresentation(Duration.ofHours(30)));
    }

    @Test
    void unboundFieldIgnoresEntityAnnotationLookupAndUsesGlobalDefaults() {
        DurationField field = newField(true, 8);

        assertEquals("3d 1h", field.convertToPresentation(Duration.ofHours(25)));
    }
}
