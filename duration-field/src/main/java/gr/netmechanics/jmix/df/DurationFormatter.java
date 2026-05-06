package playground;

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
 * DurationFormatter.format(Duration.ofHours(25))     &rarr; "1d 1h"
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
 *   <li>1 day    = 8 hours (working day)</li>
 *   <li>1 week   = 5 days (working week)</li>
 *   <li>1 month  = 4 weeks</li>
 *   <li>1 year   = 12 months</li>
 * </ul>
 */
public final class DurationFormatter {

    // Working time constants
    private static final long MILLIS_PER_SECOND = 1000L;
    private static final long SECONDS_PER_MINUTE = 60L;
    private static final long MINUTES_PER_HOUR = 60L;
    private static final long HOURS_PER_DAY = 8L;
    private static final long DAYS_PER_WEEK = 5L;
    private static final long WEEKS_PER_MONTH = 4L;
    private static final long MONTHS_PER_YEAR = 12L;

    private static final long MILLIS_IN_SECOND = MILLIS_PER_SECOND;
    private static final long MILLIS_IN_MINUTE = SECONDS_PER_MINUTE * MILLIS_IN_SECOND;
    private static final long MILLIS_IN_HOUR = MINUTES_PER_HOUR * MILLIS_IN_MINUTE;
    private static final long MILLIS_IN_DAY = HOURS_PER_DAY * MILLIS_IN_HOUR;
    private static final long MILLIS_IN_WEEK = DAYS_PER_WEEK * MILLIS_IN_DAY;
    private static final long MILLIS_IN_MONTH = WEEKS_PER_MONTH * MILLIS_IN_WEEK;
    private static final long MILLIS_IN_YEAR = MONTHS_PER_YEAR * MILLIS_IN_MONTH;

    /** Regex to capture pairs of numbers and unit labels. */
    private static final Pattern DURATION_PATTERN =
        Pattern.compile("(\\d+)\\s*(y|mo|w|d|h|m|s|ms|year|month|week|day|hour|minute|second|millisecond)s?", Pattern.CASE_INSENSITIVE);

    private DurationFormatter() {}

    /**
     * Formats a {@link Duration} using <b>short labels</b> (e.g. "1h 30m 15s").
     *
     * @param duration the duration to format; must not be {@code null}
     * @return a human-readable string, or "0ms" for zero / negative durations
     */
    public static String format(Duration duration) {
        return format(duration, true);
    }

    /**
     * Formats a {@link Duration} with a choice of label style.
     *
     * @param duration    the duration to format; must not be {@code null}
     * @param shortLabels {@code true} for short labels (y, mo, w, d, h, m, s, ms),
     *                    {@code false} for full words (year(s), month(s), ...)
     * @return a human-readable string, or "0ms" / "0 milliseconds" for
     *         zero / negative durations
     * @throws IllegalArgumentException if {@code duration} is {@code null}
     */
    public static String format(Duration duration, boolean shortLabels) {
        if (duration == null) {
            throw new IllegalArgumentException("duration must not be null");
        }

        long totalMillis = duration.toMillis();

        if (totalMillis <= 0) {
            return shortLabels ? "0ms" : "0 milliseconds";
        }

        long years = totalMillis / MILLIS_IN_YEAR;
        totalMillis %= MILLIS_IN_YEAR;

        long months = totalMillis / MILLIS_IN_MONTH;
        totalMillis %= MILLIS_IN_MONTH;

        long weeks = totalMillis / MILLIS_IN_WEEK;
        totalMillis %= MILLIS_IN_WEEK;

        long days = totalMillis / MILLIS_IN_DAY;
        totalMillis %= MILLIS_IN_DAY;

        long hours = totalMillis / MILLIS_IN_HOUR;
        totalMillis %= MILLIS_IN_HOUR;

        long minutes = totalMillis / MILLIS_IN_MINUTE;
        totalMillis %= MILLIS_IN_MINUTE;

        long seconds = totalMillis / MILLIS_IN_SECOND;
        totalMillis %= MILLIS_IN_SECOND;

        long millis = totalMillis;

        List<String> parts = new ArrayList<>();

        if (years > 0) parts.add(label(years, "y", "year", shortLabels));
        if (months > 0) parts.add(label(months, "mo", "month", shortLabels));
        if (weeks > 0) parts.add(label(weeks, "w", "week", shortLabels));
        if (days > 0) parts.add(label(days, "d", "day", shortLabels));
        if (hours > 0) parts.add(label(hours, "h", "hour", shortLabels));
        if (minutes > 0) parts.add(label(minutes, "m", "minute", shortLabels));
        if (seconds > 0) parts.add(label(seconds, "s", "second", shortLabels));
        if (millis > 0) parts.add(label(millis, "ms", "millisecond", shortLabels));

        return String.join(" ", parts);
    }

    /**
     * Parses a human-readable duration string into a {@link Duration} object.
     * Supports both short and long labels (e.g., "1d 2h" or "1 day 2 hours").
     *
     * @param input the string to parse; must not be {@code null}
     * @return the resulting {@link Duration}
     * @throws IllegalArgumentException if the input is null or cannot be parsed
     */
    public static Duration parse(String input) {
        if (input == null || input.trim().isEmpty()) {
            throw new IllegalArgumentException("Input string must not be null or empty");
        }

        long totalMillis = 0;
        Matcher matcher = DURATION_PATTERN.matcher(input.toLowerCase());
        boolean found = false;

        while (matcher.find()) {
            found = true;
            long value = Long.parseLong(matcher.group(1));
            String unit = matcher.group(2);

            switch (unit) {
                case "y":
                case "year":
                    totalMillis += value * MILLIS_IN_YEAR;
                    break;
                case "mo":
                case "month":
                    totalMillis += value * MILLIS_IN_MONTH;
                    break;
                case "w":
                case "week":
                    totalMillis += value * MILLIS_IN_WEEK;
                    break;
                case "d":
                case "day":
                    totalMillis += value * MILLIS_IN_DAY;
                    break;
                case "h":
                case "hour":
                    totalMillis += value * MILLIS_IN_HOUR;
                    break;
                case "m":
                case "minute":
                    totalMillis += value * MILLIS_IN_MINUTE;
                    break;
                case "s":
                case "second":
                    totalMillis += value * MILLIS_IN_SECOND;
                    break;
                case "ms":
                case "millisecond":
                    totalMillis += value;
                    break;
            }
        }

        if (!found) {
            throw new IllegalArgumentException("Unable to parse duration: " + input);
        }

        return Duration.ofMillis(totalMillis);
    }

    /**
     * Builds a single labelled unit string.
     *
     * @param value       the numeric value
     * @param shortForm   short label, e.g. "w"
     * @param longForm    singular long label, e.g. "week"
     * @param shortLabels {@code true} to use the short form
     * @return the formatted label string
     */
    private static String label(long value, String shortForm, String longForm, boolean shortLabels) {
        if (shortLabels) {
            return value + shortForm;
        }
        return value + " " + longForm + (value == 1 ? "" : "s");
    }
}
