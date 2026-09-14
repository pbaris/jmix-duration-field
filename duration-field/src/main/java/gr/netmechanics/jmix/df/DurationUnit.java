package gr.netmechanics.jmix.df;

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
     * Formats a whole number of this unit, e.g. {@code label(2, true)} &rarr; "2d".
     */
    String label(final long value, final boolean shortLabels) {
        if (shortLabels) {
            return value + shortLabel;
        }
        return value + " " + longLabel + (value == 1 ? "" : "s");
    }

    /**
     * Formats an already-rendered numeric string with this unit's label,
     * e.g. {@code label("3.75", true)} &rarr; "3.75d".
     */
    String label(final String numericValue, final boolean shortLabels) {
        if (shortLabels) {
            return numericValue + shortLabel;
        }
        return numericValue + " " + longLabel + ("1".equals(numericValue) ? "" : "s");
    }
}
