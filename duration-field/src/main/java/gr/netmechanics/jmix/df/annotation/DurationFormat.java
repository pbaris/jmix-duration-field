package gr.netmechanics.jmix.df.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Overrides the {@code jmix.durationField.*} display options for a single {@link java.time.Duration}
 * entity attribute. Picked up automatically by {@code <nm:durationField>} fields bound to the
 * annotated attribute (unless the field itself sets an XML attribute, which takes precedence).
 *
 * @author Panos Bariamis (pbaris)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface DurationFormat {

    /**
     * {@code true} for short labels (e.g. "1d"), {@code false} for full words (e.g. "1 day").
     */
    boolean shortLabels() default true;

    /**
     * Working hours in a day, driving the day/week/month/year breakdown.
     */
    double hoursADay() default 8;

    /**
     * When set (e.g. "days", "hours"), always renders the duration as a single decimal value in
     * this unit instead of the multi-part breakdown.
     */
    String alwaysDisplayIn() default "";
}
