package gr.netmechanics.jmix.df.datatype;

import java.time.Duration;
import java.util.Locale;
import java.util.function.Function;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import gr.netmechanics.jmix.df.DurationFormatter;
import gr.netmechanics.jmix.df.DurationUnit;
import gr.netmechanics.jmix.df.annotation.DurationFormat;
import io.jmix.core.Messages;
import io.jmix.core.metamodel.datatype.Datatype;
import org.springframework.util.StringUtils;

/**
 * A {@link Duration} datatype used for an entity attribute annotated with {@link DurationFormat},
 * closed over that attribute's specific display options instead of the app-wide
 * {@code jmix.durationField.*} config. Mirrors how Jmix's own {@code AdaptiveNumberDatatype} backs
 * {@code @NumberFormat}-annotated attributes.
 *
 * @author Panos Bariamis (pbaris)
 */
public class AdaptiveDurationDatatype implements Datatype<Duration> {

    private final DurationFormat durationFormat;

    @Nullable
    private final Messages messages;

    public AdaptiveDurationDatatype(final DurationFormat durationFormat, @Nullable final Messages messages) {
        this.durationFormat = durationFormat;
        this.messages = messages;
    }

    @Nonnull
    @Override
    public String format(@Nullable final Object value) {
        return format(value, DurationDatatype.resolver(messages, null));
    }

    @Nonnull
    @Override
    public String format(@Nullable final Object value, @Nonnull final Locale locale) {
        return format(value, DurationDatatype.resolver(messages, locale));
    }

    @Nullable
    @Override
    public Duration parse(@Nullable final String value) {
        return DurationFormatter.parse(value, durationFormat.hoursADay());
    }

    @Nullable
    @Override
    public Duration parse(@Nullable final String value, @Nonnull final Locale locale) {
        return parse(value);
    }

    @Override
    public Class<Duration> getJavaClass() {
        return Duration.class;
    }

    @Nonnull
    private String format(@Nullable final Object value, @Nullable final Function<String, String> labelResolver) {
        if (value instanceof Duration duration) {
            return DurationFormatter.format(duration, durationFormat.shortLabels(), durationFormat.hoursADay(),
                resolveAlwaysDisplayIn(), labelResolver);
        }

        return "";
    }

    @Nullable
    private DurationUnit resolveAlwaysDisplayIn() {
        return StringUtils.hasText(durationFormat.alwaysDisplayIn()) ? DurationUnit.fromAlias(durationFormat.alwaysDisplayIn()) : null;
    }
}
