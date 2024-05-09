package gr.netmechanics.jmix.df;

import static org.apache.commons.lang3.StringUtils.replaceOnce;
import static org.apache.commons.lang3.time.DurationFormatUtils.formatDuration;

import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * @author Panos Bariamis (pbaris)
 */
@Component("durfld_DurationTransformations")
public class DurationTransformations {

    public String transformToString(final Duration duration) {
        if (duration == null) {
            return "";
        }

        String dhr = formatDuration(duration.toMillis(), "d'd' H'h' m'm'", false);
        dhr = replaceOnce(dhr, "0d", StringUtils.EMPTY);
        dhr = replaceOnce(dhr, " 0h", StringUtils.EMPTY);
        dhr = replaceOnce(dhr, " 0m", StringUtils.EMPTY);

        return dhr.trim();
    }

    public Duration transformToDuration(final String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        }

        Matcher m = Pattern.compile("\\s*(?:(\\d+)\\s*(?:days?|d))?"
                + "\\s*(?:(\\d+)\\s*(?:hours?|h))?"
                + "\\s*(?:(\\d+)\\s*(?:minutes?|m))?"
                + "\\s*", Pattern.CASE_INSENSITIVE)
            .matcher(value);

        if (!m.matches()) {
            return null;
        }

        int days = (m.start(1) == -1 ? 0 : Integer.parseInt(m.group(1)));
        int hours = (m.start(2) == -1 ? 0 : Integer.parseInt(m.group(2)));
        int minutes = (m.start(3) == -1 ? 0 : Integer.parseInt(m.group(3)));

        return Duration.ofMinutes((days * 24L + hours) * 60L + minutes);
    }
}
