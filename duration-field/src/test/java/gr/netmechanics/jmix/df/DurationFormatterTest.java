package gr.netmechanics.jmix.df;

import java.time.Duration;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DurationFormatterTest {

    @Test
    void formatUsesConfigurableHoursPerDayForDayBreakdown() {
        // 30h with a 7.5h working day = exactly 4 days, no remainder
        assertEquals("4d", DurationFormatter.format(Duration.ofHours(30), true, 7.5));
    }

    @Test
    void formatUsesConfigurableHoursPerDayWithRemainder() {
        // 32h with a 7h working day = 4 days + 4 hours
        assertEquals("4d 4h", DurationFormatter.format(Duration.ofHours(32), true, 7));
    }

    @Test
    void formatAlwaysDisplayInRendersDecimalDays() {
        // 30h with an 8h working day = 3.75 days
        assertEquals("3.75d", DurationFormatter.format(Duration.ofHours(30), true, 8, DurationUnit.DAYS));
    }

    @Test
    void formatAlwaysDisplayInStripsTrailingZerosForWholeUnits() {
        assertEquals("4d", DurationFormatter.format(Duration.ofHours(32), true, 8, DurationUnit.DAYS));
    }

    @Test
    void formatAlwaysDisplayInRoundsHalfUpToTwoDecimals() {
        // 30h with a 7h working day = 4.2857... days -> rounds to 4.29
        assertEquals("4.29d", DurationFormatter.format(Duration.ofHours(30), true, 7, DurationUnit.DAYS));
    }

    @Test
    void formatAlwaysDisplayInUsesLongLabelsWhenRequested() {
        assertEquals("3.75 days", DurationFormatter.format(Duration.ofHours(30), false, 8, DurationUnit.DAYS));
    }

    @Test
    void formatDefaultOverloadsPreserveEightHourWorkingDay() {
        assertEquals("500ms", DurationFormatter.format(Duration.ofMillis(500)));
        assertEquals("1m 30s", DurationFormatter.format(Duration.ofSeconds(90)));
        assertEquals("3d 1h", DurationFormatter.format(Duration.ofHours(25)));
        assertEquals("45 seconds", DurationFormatter.format(Duration.ofSeconds(45), false));
    }

    @Test
    void formatZeroOrNegativeDurationIsUnaffectedByAlwaysDisplayIn() {
        assertEquals("0ms", DurationFormatter.format(Duration.ZERO, true, 8, DurationUnit.DAYS));
        assertEquals("0 milliseconds", DurationFormatter.format(Duration.ofSeconds(-5), false, 8, DurationUnit.HOURS));
    }

    @Test
    void parseUsesConfigurableHoursPerDay() {
        assertEquals(Duration.ofHours(15), DurationFormatter.parse("2d", 7.5));
        assertEquals(Duration.ofHours(8), DurationFormatter.parse("1d", 8));
    }

    @Test
    void parseDefaultOverloadPreservesEightHourWorkingDay() {
        assertEquals(Duration.ofHours(25), DurationFormatter.parse("3d 1h"));
    }

    @Test
    void parseCascadesConfigurableHoursPerDayThroughWeeksMonthsYears() {
        // 1 week = 5 days, 1 day = 7h here -> 35h
        assertEquals(Duration.ofHours(35), DurationFormatter.parse("1w", 7));
    }
}
