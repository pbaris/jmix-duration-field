package gr.netmechanics.jmix.df.datatype;

import java.time.Duration;
import java.util.Locale;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import gr.netmechanics.jmix.df.DurationFormatter;
import gr.netmechanics.jmix.df.DurationUnit;
import io.jmix.core.metamodel.annotation.DatatypeDef;
import io.jmix.core.metamodel.annotation.Ddl;
import io.jmix.core.metamodel.datatype.Datatype;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

/**
 * @author Panos Bariamis (pbaris)
 */
@DatatypeDef(id = "duration", javaClass = Duration.class, defaultForClass = true)
@Ddl("bigint")
public class DurationDatatype implements Datatype<Duration> {

    @Value("${jmix.durationField.shortLabels:true}")
    private boolean shortLabels;

    @Value("${jmix.durationField.hoursADay:8}")
    private double hoursADay;

    @Value("${jmix.durationField.alwaysDisplayIn:}")
    private String alwaysDisplayIn;

    @Nonnull
    @Override
    public String format(@Nullable final Object value) {
        if (value instanceof Duration duration) {
            return DurationFormatter.format(duration, shortLabels, hoursADay, resolveAlwaysDisplayIn());
        }

        return "";
    }

    @Nonnull
    @Override
    public String format(@Nullable final Object value, @Nonnull final Locale locale) {
        return format(value);
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

    @Nullable
    private DurationUnit resolveAlwaysDisplayIn() {
        return StringUtils.hasText(alwaysDisplayIn) ? DurationUnit.fromAlias(alwaysDisplayIn) : null;
    }
}
