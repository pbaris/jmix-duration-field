package gr.netmechanics.jmix.df.datatype;

import java.text.ParseException;
import java.time.Duration;
import java.util.Locale;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import gr.netmechanics.jmix.df.DurationTransformations;
import io.jmix.core.metamodel.annotation.DatatypeDef;
import io.jmix.core.metamodel.annotation.Ddl;
import io.jmix.core.metamodel.datatype.Datatype;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Panos Bariamis (pbaris)
 */
@DatatypeDef(id = "duration", javaClass = Duration.class, defaultForClass = true)
@Ddl("bigint")
public class DurationDatatype implements Datatype<Duration> {

    @Autowired
    private DurationTransformations transformations;

    @Nonnull
    @Override
    public String format(@Nullable final Object value) {
        if (value instanceof Duration duration) {
            return transformations.transformToString(duration);
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
    public Duration parse(@Nullable final String value) throws ParseException {
        return transformations.transformToDuration(value);
    }

    @Nullable
    @Override
    public Duration parse(@Nullable final String value, @Nonnull final Locale locale) throws ParseException {
        return parse(value);
    }
}
