package gr.netmechanics.jmix.df.datatype;

import java.time.Duration;
import java.util.Locale;
import java.util.function.Function;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import gr.netmechanics.jmix.df.DurationFormatter;
import gr.netmechanics.jmix.df.DurationUnit;
import io.jmix.core.Messages;
import io.jmix.core.metamodel.annotation.DatatypeDef;
import io.jmix.core.metamodel.annotation.Ddl;
import io.jmix.core.metamodel.datatype.Datatype;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

/**
 * @author Panos Bariamis (pbaris)
 */
@DatatypeDef(id = "duration", javaClass = Duration.class, defaultForClass = true)
@Ddl("bigint")
public class DurationDatatype implements Datatype<Duration> {

    public static final String MESSAGE_GROUP = "gr.netmechanics.jmix.df";

    @Value("${jmix.durationField.shortLabels:true}")
    private boolean shortLabels;

    @Value("${jmix.durationField.hoursADay:8}")
    private double hoursADay;

    @Value("${jmix.durationField.alwaysDisplayIn:}")
    private String alwaysDisplayIn;

    @Autowired(required = false)
    private Messages messages;

    /**
     * @return whether this datatype renders/expects short labels (e.g. "1d") rather than long ones (e.g. "1 day")
     */
    public boolean isShortLabels() {
        return shortLabels;
    }

    /**
     * @return the configured working hours in a day, driving the day/week/month/year breakdown
     */
    public double getHoursADay() {
        return hoursADay;
    }

    /**
     * @return the unit alias durations are always rendered in (e.g. "days"), or blank/{@code null}
     *         when the default multi-part breakdown is used instead
     */
    @Nullable
    public String getAlwaysDisplayIn() {
        return alwaysDisplayIn;
    }

    @Nonnull
    @Override
    public String format(@Nullable final Object value) {
        return format(value, resolver(messages, null));
    }

    @Nonnull
    @Override
    public String format(@Nullable final Object value, @Nonnull final Locale locale) {
        return format(value, resolver(messages, locale));
    }

    @Nullable
    @Override
    public Duration parse(@Nullable final String value) {
        return DurationFormatter.parse(value, hoursADay);
    }

    @Nullable
    @Override
    public Duration parse(@Nullable final String value, @Nonnull final Locale locale) {
        return parse(value);
    }

    @Nonnull
    private String format(@Nullable final Object value, @Nullable final Function<String, String> labelResolver) {
        if (value instanceof Duration duration) {
            return DurationFormatter.format(duration, shortLabels, hoursADay, resolveAlwaysDisplayIn(), labelResolver);
        }

        return "";
    }

    @Nullable
    private DurationUnit resolveAlwaysDisplayIn() {
        return StringUtils.hasText(alwaysDisplayIn) ? DurationUnit.fromAlias(alwaysDisplayIn) : null;
    }

    /**
     * Builds a message-key resolver backed by Jmix {@link Messages}, for use with
     * {@link DurationFormatter}'s resolver-aware {@code format} overload.
     *
     * @param messages the {@link Messages} bean to resolve through; {@code null} yields no resolver
     *                 (falling back to the default English labels)
     * @param locale   an explicit locale to resolve with, or {@code null} to use the current
     *                 session locale
     * @return a resolver function, or {@code null} when {@code messages} is {@code null}
     */
    @Nullable
    public static Function<String, String> resolver(@Nullable final Messages messages, @Nullable final Locale locale) {
        if (messages == null) {
            return null;
        }
        return locale != null
            ? key -> messages.getMessage(MESSAGE_GROUP, key, locale)
            : key -> messages.getMessage(MESSAGE_GROUP, key);
    }
}
