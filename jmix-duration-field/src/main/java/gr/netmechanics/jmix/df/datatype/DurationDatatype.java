package gr.netmechanics.jmix.df.datatype;

import static org.apache.commons.lang3.StringUtils.replaceOnce;
import static org.apache.commons.lang3.time.DurationFormatUtils.formatDuration;

import java.text.ParseException;
import java.time.Duration;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import io.jmix.core.metamodel.annotation.DatatypeDef;
import io.jmix.core.metamodel.annotation.Ddl;
import io.jmix.core.metamodel.datatype.Datatype;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Panos Bariamis (pbaris)
 */
@DatatypeDef(id = "duration", javaClass = Duration.class, defaultForClass = true)
@Ddl("bigint")
public class DurationDatatype implements Datatype<Duration> {

    @Nonnull
    @Override
    public String format(@Nullable final Object value) {
        if (value == null) {
            return "";
        }

        String dhr = formatDuration(((Duration) value).toMillis(), "d'd' H'h' m'm'", false);
        dhr = replaceOnce(dhr, "0d", StringUtils.EMPTY);
        dhr = replaceOnce(dhr, " 0h", StringUtils.EMPTY);
        dhr = replaceOnce(dhr, " 0m", StringUtils.EMPTY);

        return dhr.trim();
    }

    @Nonnull
    @Override
    public String format(@Nullable final Object value, @Nonnull final Locale locale) {
        return format(value);
    }

    @Nullable
    @Override
    public Duration parse(@Nullable final String value) throws ParseException {
        if (StringUtils.isBlank(value)) {
            return null;
        }

        Matcher m = Pattern.compile("\\s*(?:(\\d+)\\s*(?:days?|d))?" +
                "\\s*(?:(\\d+)\\s*(?:hours?|h))?" +
                "\\s*(?:(\\d+)\\s*(?:minutes?|m))?" +
                "\\s*", Pattern.CASE_INSENSITIVE)
            .matcher(value);

        if (!m.matches()) {
            throw new ParseException("Unparseable duration: \"" + value + "\"", 0);
        }

        int days = (m.start(1) == -1 ? 0 : Integer.parseInt(m.group(1)));
        int hours = (m.start(2) == -1 ? 0 : Integer.parseInt(m.group(2)));
        int minutes = (m.start(3) == -1 ? 0 : Integer.parseInt(m.group(3)));

        return Duration.ofMinutes((days * 24L + hours) * 60L + minutes);
    }

    @Nullable
    @Override
    public Duration parse(@Nullable final String value, @Nonnull final Locale locale) throws ParseException {
        return parse(value);
    }
}
