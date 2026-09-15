package gr.netmechanics.jmix.df;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Formats a {@link Duration} into a human-readable string and parses strings back into Durations,
 * similar to how Jira displays and handles time.
 *
 * <p><b>Examples — short labels (default):</b></p>
 * <pre>
 * DurationFormatter.format(Duration.ofMillis(500))  &rarr; "500ms"
 * DurationFormatter.format(Duration.ofSeconds(45))   &rarr; "45s"
 * DurationFormatter.format(Duration.ofSeconds(90))   &rarr; "1m 30s"
 * DurationFormatter.format(Duration.ofHours(25))     &rarr; "3d 1h"
 * </pre>
 *
 * <p><b>Examples — long labels:</b></p>
 * <pre>
 * DurationFormatter.format(Duration.ofMillis(500), false) &rarr; "500 milliseconds"
 * DurationFormatter.format(Duration.ofSeconds(45),   false) &rarr; "45 seconds"
 * </pre>
 *
 * <p><b>Time assumptions (matching Jira defaults):</b></p>
 * <ul>
 *   <li>1 minute = 60 seconds</li>
 *   <li>1 hour   = 60 minutes</li>
 *   <li>1 day    = a configurable number of working hours (default 8, see {@code hoursADay})</li>
 *   <li>1 week   = 5 days (working week)</li>
 *   <li>1 month  = 4 weeks</li>
 *   <li>1 year   = 12 months</li>
 * </ul>
 */
public final class DurationFormatter {

    /** Default working hours in a day, used when no {@code hoursADay} is given. */
    public static final double DEFAULT_HOURS_A_DAY = 8.0;

    private static final long DAYS_PER_WEEK = 5L;
    private static final long WEEKS_PER_MONTH = 4L;
    private static final long MONTHS_PER_YEAR = 12L;

    private static final long MILLIS_IN_MILLISECOND = 1L;
    private static final long MILLIS_IN_SECOND = 1000L;
    private static final long MILLIS_IN_MINUTE = 60L * MILLIS_IN_SECOND;
    private static final long MILLIS_IN_HOUR = 60L * MILLIS_IN_MINUTE;

    /** Regex to capture pairs of numbers and unit labels. */
    private static final Pattern DURATION_PATTERN =
        Pattern.compile("(\\d+)\\s*(y|mo|w|d|h|m|s|ms|year|month|week|day|hour|minute|second|millisecond)s?", Pattern.CASE_INSENSITIVE);

    private DurationFormatter() {}

    /**
     * Formats a {@link Duration} using <b>short labels</b> (e.g. "1h 30m 15s") and the default
     * 8-hour working day.
     *
     * @param duration the duration to format; must not be {@code null}
     * @return a human-readable string, or "0ms" for zero / negative durations
     */
    public static String format(final Duration duration) {
        return format(duration, true, DEFAULT_HOURS_A_DAY, null);
    }

    /**
     * Formats a {@link Duration} with a choice of label style, using the default 8-hour working day.
     *
     * @param duration    the duration to format; must not be {@code null}
     * @param shortLabels {@code true} for short labels (y, mo, w, d, h, m, s, ms),
     *                    {@code false} for full words (year(s), month(s), ...)
     * @return a human-readable string, or "0ms" / "0 milliseconds" for
     *         zero / negative durations
     */
    public static String format(final Duration duration, final boolean shortLabels) {
        return format(duration, shortLabels, DEFAULT_HOURS_A_DAY, null);
    }

    /**
     * Formats a {@link Duration} with a choice of label style and a configurable working day length.
     *
     * @param duration    the duration to format; must not be {@code null}
     * @param shortLabels {@code true} for short labels, {@code false} for full words
     * @param hoursADay   working hours in a day (e.g. 7.5); drives the day/week/month/year breakdown
     * @return a human-readable string, or "0ms" / "0 milliseconds" for zero / negative durations
     */
    public static String format(final Duration duration, final boolean shortLabels, final double hoursADay) {
        return format(duration, shortLabels, hoursADay, null);
    }

    /**
     * Formats a {@link Duration}, optionally forcing the whole value into a single unit
     * (e.g. "3.75d") instead of the default multi-part breakdown ("3d 6h").
     *
     * @param duration       the duration to format; must not be {@code null}
     * @param shortLabels    {@code true} for short labels, {@code false} for full words
     * @param hoursADay      working hours in a day (e.g. 7.5); drives the day/week/month/year breakdown
     * @param alwaysDisplayIn when non-{@code null}, the single unit to always render the duration in;
     *                        when {@code null}, the usual multi-part breakdown is used
     * @return a human-readable string, or "0ms" / "0 milliseconds" for zero / negative durations
     */
    public static String format(final Duration duration, final boolean shortLabels, final double hoursADay,
                                 final DurationUnit alwaysDisplayIn) {
        if (duration == null) {
            return "";
        }

        long totalMillis = duration.toMillis();

        if (totalMillis <= 0) {
            return shortLabels ? "0ms" : "0 milliseconds";
        }

        if (alwaysDisplayIn != null) {
            return formatSingleUnit(totalMillis, alwaysDisplayIn, shortLabels, hoursADay);
        }

        return formatBreakdown(totalMillis, shortLabels, hoursADay);
    }

    /**
     * Parses a human-readable duration string into a {@link Duration} object, using the default
     * 8-hour working day.
     * Supports both short and long labels (e.g., "1d 2h" or "1 day 2 hours").
     *
     * @param input the string to parse; must not be {@code null}
     * @return the resulting {@link Duration}
     */
    public static Duration parse(final String input) {
        return parse(input, DEFAULT_HOURS_A_DAY);
    }

    /**
     * Parses a human-readable duration string into a {@link Duration} object, using a
     * configurable working day length.
     *
     * @param input     the string to parse; must not be {@code null}
     * @param hoursADay working hours in a day (e.g. 7.5); drives the day/week/month/year conversion
     * @return the resulting {@link Duration}
     */
    public static Duration parse(final String input, final double hoursADay) {
        if (input == null || input.trim().isEmpty()) {
            return null;
        }

        long totalMillis = 0;
        Matcher matcher = DURATION_PATTERN.matcher(input.toLowerCase());
        boolean found = false;

        while (matcher.find()) {
            found = true;
            long value = Long.parseLong(matcher.group(1));
            DurationUnit unit = DurationUnit.fromAlias(matcher.group(2));
            totalMillis += value * millisPerUnit(unit, hoursADay);
        }

        if (!found) {
            throw new IllegalArgumentException("Unable to parse duration: " + input);
        }

        return Duration.ofMillis(totalMillis);
    }

    /**
     * Builds the default multi-part breakdown, e.g. "1d 1h".
     */
    private static String formatBreakdown(final long totalMillis, final boolean shortLabels, final double hoursADay) {
        List<String> parts = new ArrayList<>();
        long remaining = totalMillis;

        DurationUnit[] units = DurationUnit.values();
        for (int i = units.length - 1; i >= 0; i--) {
            DurationUnit unit = units[i];
            long divisor = millisPerUnit(unit, hoursADay);
            long value = remaining / divisor;
            remaining %= divisor;

            if (value > 0) {
                parts.add(unit.label(value, shortLabels));
            }
        }

        return String.join(" ", parts);
    }

    /**
     * Renders the whole duration as a single decimal number in one unit, e.g. "3.75d".
     * Rounded HALF_UP to 2 decimal places, with trailing zeros stripped.
     */
    private static String formatSingleUnit(final long totalMillis, final DurationUnit unit, final boolean shortLabels,
                                            final double hoursADay) {
        long divisor = millisPerUnit(unit, hoursADay);
        BigDecimal value = BigDecimal.valueOf(totalMillis)
            .divide(BigDecimal.valueOf(divisor), 2, RoundingMode.HALF_UP)
            .stripTrailingZeros();

        String numericValue = value.scale() < 0 ? value.setScale(0).toPlainString() : value.toPlainString();

        return unit.label(numericValue, shortLabels);
    }

    /**
     * Number of milliseconds in one of the given unit, based on the configured working day length.
     */
    private static long millisPerUnit(final DurationUnit unit, final double hoursADay) {
        switch (unit) {
            case MILLISECONDS: return MILLIS_IN_MILLISECOND;
            case SECONDS: return MILLIS_IN_SECOND;
            case MINUTES: return MILLIS_IN_MINUTE;
            case HOURS: return MILLIS_IN_HOUR;
            case DAYS: return Math.round(hoursADay * MILLIS_IN_HOUR);
            case WEEKS: return Math.round(DAYS_PER_WEEK * hoursADay * MILLIS_IN_HOUR);
            case MONTHS: return Math.round(WEEKS_PER_MONTH * DAYS_PER_WEEK * hoursADay * MILLIS_IN_HOUR);
            case YEARS: return Math.round(MONTHS_PER_YEAR * WEEKS_PER_MONTH * DAYS_PER_WEEK * hoursADay * MILLIS_IN_HOUR);
            default: throw new IllegalStateException("Unhandled unit: " + unit);
        }
    }
}
