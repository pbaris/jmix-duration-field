package gr.netmechanics.jmix.df;

import java.util.function.Function;
import javax.annotation.Nullable;

/**
 * The working-time units {@link DurationFormatter} formats and parses, from smallest to largest.
 *
 * @author Panos Bariamis (pbaris)
 */
public enum DurationUnit {

    MILLISECONDS("ms", "millisecond"),
    SECONDS("s", "second"),
    MINUTES("m", "minute"),
    HOURS("h", "hour"),
    DAYS("d", "day"),
    WEEKS("w", "week"),
    MONTHS("mo", "month"),
    YEARS("y", "year");

    private final String shortLabel;
    private final String longLabel;

    DurationUnit(final String shortLabel, final String longLabel) {
        this.shortLabel = shortLabel;
        this.longLabel = longLabel;
    }

    /**
     * Resolves a unit from any alias accepted by {@link DurationFormatter} (short or long form,
     * singular or plural, case-insensitive), e.g. "d", "day", "Days" all resolve to {@link #DAYS}.
     *
     * @param alias the alias to resolve; must not be {@code null}
     * @return the matching unit
     * @throws IllegalArgumentException if the alias matches no unit
     */
    public static DurationUnit fromAlias(final String alias) {
        String normalized = alias.trim().toLowerCase();
        String withoutPlural = normalized.endsWith("s") && normalized.length() > 1
            ? normalized.substring(0, normalized.length() - 1)
            : normalized;

        for (DurationUnit unit : values()) {
            if (unit.shortLabel.equals(normalized) || unit.longLabel.equals(normalized) || unit.longLabel.equals(withoutPlural)) {
                return unit;
            }
        }

        throw new IllegalArgumentException("Unknown duration unit: " + alias);
    }

    /**
     * Message key for this unit's short label, e.g. {@code "durationUnit.DAYS.short"}.
     */
    String shortKey() {
        return "durationUnit." + name() + ".short";
    }

    /**
     * Message key for this unit's long label, e.g. {@code "durationUnit.DAYS.long"} /
     * {@code "durationUnit.DAYS.long.plural"}.
     *
     * @param plural whether to return the plural variant of the key
     */
    String longKey(final boolean plural) {
        return "durationUnit." + name() + ".long" + (plural ? ".plural" : "");
    }

    /**
     * Formats a whole number of this unit, e.g. {@code label(2, true)} &rarr; "2d".
     */
    String label(final long value, final boolean shortLabels) {
        return label(value, shortLabels, null);
    }

    /**
     * Formats a whole number of this unit, resolving the label text via {@code resolver} when
     * provided (e.g. a message-bundle lookup), falling back to the built-in English text otherwise.
     *
     * @param value       the numeric value to render
     * @param shortLabels {@code true} for short labels, {@code false} for full words
     * @param resolver    resolves a message key (from {@link #shortKey()} / {@link #longKey(boolean)})
     *                    to localized text; may be {@code null} to use the default English text
     */
    String label(final long value, final boolean shortLabels, @Nullable final Function<String, String> resolver) {
        if (shortLabels) {
            String text = resolver != null ? resolver.apply(shortKey()) : shortLabel;
            return value + text;
        }
        boolean plural = value != 1;
        String text = resolver != null ? resolver.apply(longKey(plural)) : longLabel + (plural ? "s" : "");
        return value + " " + text;
    }

    /**
     * Formats an already-rendered numeric string with this unit's label,
     * e.g. {@code label("3.75", true)} &rarr; "3.75d".
     */
    String label(final String numericValue, final boolean shortLabels) {
        return label(numericValue, shortLabels, null);
    }

    /**
     * Formats an already-rendered numeric string with this unit's label, resolving the label text
     * via {@code resolver} when provided, falling back to the built-in English text otherwise.
     *
     * @param numericValue the pre-rendered numeric value, e.g. "3.75"
     * @param shortLabels  {@code true} for short labels, {@code false} for full words
     * @param resolver     resolves a message key to localized text; may be {@code null} to use the
     *                     default English text
     */
    String label(final String numericValue, final boolean shortLabels, @Nullable final Function<String, String> resolver) {
        if (shortLabels) {
            String text = resolver != null ? resolver.apply(shortKey()) : shortLabel;
            return numericValue + text;
        }
        boolean plural = !"1".equals(numericValue);
        String text = resolver != null ? resolver.apply(longKey(plural)) : longLabel + (plural ? "s" : "");
        return numericValue + " " + text;
    }
}
