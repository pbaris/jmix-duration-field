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
import io.jmix.core.Stores;
import io.jmix.core.metamodel.annotation.NumberFormat;
import io.jmix.core.metamodel.datatype.Datatype;
import io.jmix.core.metamodel.datatype.DatatypeRegistry;
import io.jmix.core.metamodel.datatype.FormatStringsRegistry;
import io.jmix.core.metamodel.datatype.impl.AdaptiveNumberDatatype;
import io.jmix.core.metamodel.model.MetaProperty;
import org.junit.jupiter.api.Test;

class DurationAwareMetaModelLoaderTest {

    private static class Sample {
        @DurationFormat(shortLabels = false, hoursADay = 7.5, alwaysDisplayIn = "days")
        private Duration estimate;

        @NumberFormat(pattern = "0.00")
        private Double amount;

        private Duration plain;
    }

    private final DurationAwareMetaModelLoader loader = new DurationAwareMetaModelLoader(
        mock(DatatypeRegistry.class), mock(Stores.class), mock(FormatStringsRegistry.class), null);

    private MetaProperty metaPropertyFor(final String fieldName) throws NoSuchFieldException {
        Field field = Sample.class.getDeclaredField(fieldName);
        MetaProperty metaProperty = mock(MetaProperty.class);
        when(metaProperty.getAnnotatedElement()).thenReturn(field);
        return metaProperty;
    }

    @Test
    void returnsAdaptiveDurationDatatypeWhenPropertyIsAnnotated() throws NoSuchFieldException {
        Datatype<?> datatype = loader.getAdaptiveDatatype(metaPropertyFor("estimate"), Duration.class);

        assertInstanceOf(AdaptiveDurationDatatype.class, datatype);
        assertEquals("4 days", datatype.format(Duration.ofHours(30)));
    }

    @Test
    void returnsNullWhenPropertyIsNotAnnotated() throws NoSuchFieldException {
        assertNull(loader.getAdaptiveDatatype(metaPropertyFor("plain"), Duration.class));
    }

    @Test
    void stillHandlesNumberFormatViaSuper() throws NoSuchFieldException {
        Datatype<?> datatype = loader.getAdaptiveDatatype(metaPropertyFor("amount"), Double.class);

        assertInstanceOf(AdaptiveNumberDatatype.class, datatype);
    }
}
