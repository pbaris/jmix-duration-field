package gr.netmechanics.jmix.df.datatype;

import java.time.Duration;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DurationDatatypeTest {

    @Test
    void formatUsesConfiguredHoursADayAndAlwaysDisplayIn() {
        DurationDatatype datatype = new DurationDatatype();
        ReflectionTestUtils.setField(datatype, "shortLabels", true);
        ReflectionTestUtils.setField(datatype, "hoursADay", 7.5);
        ReflectionTestUtils.setField(datatype, "alwaysDisplayIn", "days");

        assertEquals("4d", datatype.format(Duration.ofHours(30)));
    }

    @Test
    void formatFallsBackToBreakdownWhenAlwaysDisplayInIsBlank() {
        DurationDatatype datatype = new DurationDatatype();
        ReflectionTestUtils.setField(datatype, "shortLabels", true);
        ReflectionTestUtils.setField(datatype, "hoursADay", 8.0);
        ReflectionTestUtils.setField(datatype, "alwaysDisplayIn", "");

        assertEquals("3d 1h", datatype.format(Duration.ofHours(25)));
    }

    @Test
    void parseUsesConfiguredHoursADay() {
        DurationDatatype datatype = new DurationDatatype();
        ReflectionTestUtils.setField(datatype, "hoursADay", 7.5);

        assertEquals(Duration.ofHours(15), datatype.parse("2d"));
    }
}
