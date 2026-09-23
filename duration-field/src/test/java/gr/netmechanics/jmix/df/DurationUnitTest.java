package gr.netmechanics.jmix.df;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.function.Function;

import org.junit.jupiter.api.Test;

class DurationUnitTest {

    @Test
    void shortKeyBuildsMessageKeyFromEnumName() {
        assertEquals("durationUnit.DAYS.short", DurationUnit.DAYS.shortKey());
        assertEquals("durationUnit.MILLISECONDS.short", DurationUnit.MILLISECONDS.shortKey());
    }

    @Test
    void longKeyBuildsSingularOrPluralMessageKey() {
        assertEquals("durationUnit.DAYS.long", DurationUnit.DAYS.longKey(false));
        assertEquals("durationUnit.DAYS.long.plural", DurationUnit.DAYS.longKey(true));
    }

    @Test
    void labelFallsBackToDefaultEnglishTextWhenResolverIsNull() {
        assertEquals("2d", DurationUnit.DAYS.label(2L, true, null));
        assertEquals("2 days", DurationUnit.DAYS.label(2L, false, null));
        assertEquals("1 day", DurationUnit.DAYS.label(1L, false, null));
    }

    @Test
    void labelUsesResolverTextWhenProvided() {
        Function<String, String> resolver = key -> switch (key) {
            case "durationUnit.DAYS.short" -> "j";
            case "durationUnit.DAYS.long" -> "jour";
            case "durationUnit.DAYS.long.plural" -> "jours";
            default -> key;
        };

        assertEquals("2j", DurationUnit.DAYS.label(2L, true, resolver));
        assertEquals("2 jours", DurationUnit.DAYS.label(2L, false, resolver));
        assertEquals("1 jour", DurationUnit.DAYS.label(1L, false, resolver));
    }

    @Test
    void labelWithNumericStringUsesResolverAndPluralizesWhenNotExactlyOne() {
        Function<String, String> resolver = key -> switch (key) {
            case "durationUnit.DAYS.long" -> "jour";
            case "durationUnit.DAYS.long.plural" -> "jours";
            default -> key;
        };

        assertEquals("3.75 jours", DurationUnit.DAYS.label("3.75", false, resolver));
        assertEquals("1 jour", DurationUnit.DAYS.label("1", false, resolver));
    }

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
