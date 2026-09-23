package gr.netmechanics.jmix.df.metamodel;

import java.time.Duration;
import javax.annotation.Nullable;

import gr.netmechanics.jmix.df.annotation.DurationFormat;
import gr.netmechanics.jmix.df.datatype.AdaptiveDurationDatatype;
import io.jmix.core.Messages;
import io.jmix.core.Stores;
import io.jmix.core.impl.MetaModelLoader;
import io.jmix.core.metamodel.datatype.Datatype;
import io.jmix.core.metamodel.datatype.DatatypeRegistry;
import io.jmix.core.metamodel.datatype.FormatStringsRegistry;
import io.jmix.core.metamodel.model.MetaProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Extends Jmix's {@link MetaModelLoader} so a {@link Duration} entity attribute annotated with
 * {@link DurationFormat} gets a per-property {@link AdaptiveDurationDatatype} wired into its
 * metamodel range — the same mechanism Jmix uses for {@code @NumberFormat} — so form fields and
 * grid/list columns bound to that attribute render identically without any extra wiring.
 * <p>
 * Registered as a {@code @Primary} bean of type {@link MetaModelLoader} (see
 * {@code DurationFieldConfiguration}) rather than replacing the framework's
 * {@code "core_MetaModelLoader"} bean by name, since the sole injection site
 * ({@code MetadataLoader}) autowires by type.
 *
 * @author Panos Bariamis (pbaris)
 */
public class DurationAwareMetaModelLoader extends MetaModelLoader {

    private static final Logger log = LoggerFactory.getLogger(DurationAwareMetaModelLoader.class);

    public DurationAwareMetaModelLoader(final DatatypeRegistry datatypes, final Stores stores,
                                         final FormatStringsRegistry formatStringsRegistry, final Messages messages) {
        super(datatypes, stores, formatStringsRegistry, messages);
    }

    @Nullable
    @Override
    protected Datatype getAdaptiveDatatype(final MetaProperty metaProperty, final Class<?> type) {
        Datatype datatype = super.getAdaptiveDatatype(metaProperty, type);
        if (datatype != null) {
            return datatype;
        }

        DurationFormat durationFormat = metaProperty.getAnnotatedElement().getAnnotation(DurationFormat.class);
        if (durationFormat != null) {
            if (Duration.class.isAssignableFrom(type)) {
                return new AdaptiveDurationDatatype(durationFormat, messages);
            } else {
                log.warn("DurationFormat annotation is ignored because " + metaProperty + " is not a Duration");
            }
        }
        return null;
    }
}
