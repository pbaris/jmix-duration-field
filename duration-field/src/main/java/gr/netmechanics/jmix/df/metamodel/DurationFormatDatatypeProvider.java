package gr.netmechanics.jmix.df.metamodel;

import java.time.Duration;
import javax.annotation.Nullable;

import gr.netmechanics.jmix.df.annotation.DurationFormat;
import gr.netmechanics.jmix.df.datatype.AdaptiveDurationDatatype;
import io.jmix.core.Messages;
import io.jmix.core.metamodel.datatype.AdaptiveDatatypeProvider;
import io.jmix.core.metamodel.datatype.Datatype;
import io.jmix.core.metamodel.model.MetaProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * {@link AdaptiveDatatypeProvider} that gives a {@link Duration} entity attribute annotated with
 * {@link DurationFormat} a per-property {@link AdaptiveDurationDatatype} in its metamodel range — the same
 * mechanism Jmix uses for {@code @NumberFormat} — so form fields and grid/list columns bound to that
 * attribute render identically without any extra wiring.
 *
 * @author Panos Bariamis (pbaris)
 */
@Component("df_DurationFormatDatatypeProvider")
public class DurationFormatDatatypeProvider implements AdaptiveDatatypeProvider {

    private static final Logger log = LoggerFactory.getLogger(DurationFormatDatatypeProvider.class);

    private final Messages messages;

    public DurationFormatDatatypeProvider(final Messages messages) {
        this.messages = messages;
    }

    @Nullable
    @Override
    public Datatype<?> getAdaptiveDatatype(final MetaProperty metaProperty, final Class<?> type) {
        DurationFormat durationFormat = metaProperty.getAnnotatedElement().getAnnotation(DurationFormat.class);
        if (durationFormat == null) {
            return null;
        }

        if (Duration.class.isAssignableFrom(type)) {
            return new AdaptiveDurationDatatype(durationFormat, messages);
        }

        log.warn("DurationFormat annotation is ignored because {} is not a Duration", metaProperty);
        return null;
    }
}
