package gr.netmechanics.jmix.df.metamodel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.time.Duration;

import gr.netmechanics.jmix.df.annotation.DurationFormat;
import gr.netmechanics.jmix.df.datatype.AdaptiveDurationDatatype;
import io.jmix.core.metamodel.datatype.Datatype;
import io.jmix.core.metamodel.model.MetaProperty;
import org.junit.jupiter.api.Test;

class DurationFormatDatatypeProviderTest {

    private static class Sample {
        @DurationFormat(shortLabels = false, hoursADay = 7.5, alwaysDisplayIn = "days")
        private Duration estimate;

        @DurationFormat
        private String misannotated;

        private Duration plain;
    }

    private final DurationFormatDatatypeProvider provider = new DurationFormatDatatypeProvider(null);

    private MetaProperty metaPropertyFor(final String fieldName) throws NoSuchFieldException {
        Field field = Sample.class.getDeclaredField(fieldName);
        MetaProperty metaProperty = mock(MetaProperty.class);
        when(metaProperty.getAnnotatedElement()).thenReturn(field);
        return metaProperty;
    }

    @Test
    void returnsAdaptiveDurationDatatypeWhenPropertyIsAnnotated() throws NoSuchFieldException {
        Datatype<?> datatype = provider.getAdaptiveDatatype(metaPropertyFor("estimate"), Duration.class);

        assertInstanceOf(AdaptiveDurationDatatype.class, datatype);
        assertEquals("4 days", datatype.format(Duration.ofHours(30)));
    }

    @Test
    void returnsNullWhenPropertyIsNotAnnotated() throws NoSuchFieldException {
        assertNull(provider.getAdaptiveDatatype(metaPropertyFor("plain"), Duration.class));
    }

    @Test
    void returnsNullWhenAnnotatedPropertyIsNotADuration() throws NoSuchFieldException {
        assertNull(provider.getAdaptiveDatatype(metaPropertyFor("misannotated"), String.class));
    }
}
