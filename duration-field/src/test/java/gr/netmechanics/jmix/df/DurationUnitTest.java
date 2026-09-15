package gr.netmechanics.jmix.df;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DurationUnitTest {

    @Test
    void fromAliasResolvesShortForms() {
        assertEquals(DurationUnit.DAYS, DurationUnit.fromAlias("d"));
        assertEquals(DurationUnit.HOURS, DurationUnit.fromAlias("h"));
        assertEquals(DurationUnit.MONTHS, DurationUnit.fromAlias("mo"));
    }

    @Test
    void fromAliasResolvesLongFormsCaseInsensitiveSingularOrPlural() {
        assertEquals(DurationUnit.DAYS, DurationUnit.fromAlias("Days"));
        assertEquals(DurationUnit.DAYS, DurationUnit.fromAlias("day"));
        assertEquals(DurationUnit.WEEKS, DurationUnit.fromAlias("WEEKS"));
    }

    @Test
    void fromAliasRejectsUnknownUnit() {
        assertThrows(IllegalArgumentException.class, () -> DurationUnit.fromAlias("fortnight"));
    }
}
