package gr.netmechanics.jmix.df.component;

import java.time.Duration;
import javax.annotation.Nullable;

import gr.netmechanics.jmix.df.DurationFormatter;
import gr.netmechanics.jmix.df.DurationUnit;
import gr.netmechanics.jmix.df.datatype.DurationDatatype;
import io.jmix.core.Messages;
import io.jmix.flowui.component.textfield.TypedTextField;
import io.jmix.flowui.data.ConversionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

/**
 * @author Panos Bariamis (pbaris)
 */
public class DurationField extends TypedTextField<Duration> {

    @Autowired
    private DurationDatatype durationDatatype;

    @Autowired(required = false)
    private Messages messages;

    @Nullable
    private Boolean shortLabels;

    @Nullable
    private Double hoursADay;

    @Nullable
    private String alwaysDisplayIn;

    /**
     * @return whether this field renders/expects short labels (e.g. "1d"); falls back to the
     *         app-wide {@code jmix.durationField.shortLabels} value when not overridden
     */
    public boolean isShortLabels() {
        return shortLabels != null ? shortLabels : durationDatatype.isShortLabels();
    }

    /**
     * Overrides the app-wide {@code jmix.durationField.shortLabels} value for this field only.
     *
     * @param shortLabels {@code true}/{@code false} to override, or {@code null} to inherit the
     *                     app-wide value again
     */
    public void setShortLabels(@Nullable final Boolean shortLabels) {
        this.shortLabels = shortLabels;
    }

    /**
     * @return the working hours in a day used by this field; falls back to the app-wide
     *         {@code jmix.durationField.hoursADay} value when not overridden
     */
    public double getHoursADay() {
        return hoursADay != null ? hoursADay : durationDatatype.getHoursADay();
    }

    /**
     * Overrides the app-wide {@code jmix.durationField.hoursADay} value for this field only.
     *
     * @param hoursADay the working hours in a day, or {@code null} to inherit the app-wide value again
     */
    public void setHoursADay(@Nullable final Double hoursADay) {
        this.hoursADay = hoursADay;
    }

    /**
     * @return the unit alias this field always renders durations in (e.g. "days"), or {@code null}
     *         when the default multi-part breakdown is used
     */
    @Nullable
    public String getAlwaysDisplayIn() {
        return alwaysDisplayIn != null ? alwaysDisplayIn : durationDatatype.getAlwaysDisplayIn();
    }

    /**
     * Overrides the app-wide {@code jmix.durationField.alwaysDisplayIn} value for this field only.
     *
     * @param alwaysDisplayIn the unit alias to always render in, or {@code null} to inherit the
     *                        app-wide value again
     */
    public void setAlwaysDisplayIn(@Nullable final String alwaysDisplayIn) {
        this.alwaysDisplayIn = alwaysDisplayIn;
    }

    @Override
    protected String convertToPresentation(@Nullable final Object modelValue) {
        if (modelValue instanceof Duration duration) {
            return DurationFormatter.format(duration, isShortLabels(), getHoursADay(), resolveAlwaysDisplayIn(),
                DurationDatatype.resolver(messages, null));
        }

        return "";
    }

    @Override
    protected Duration convertToModel(final String presentationValue) throws ConversionException {
        if (!StringUtils.hasText(presentationValue)) {
            return null;
        }

        try {
            return DurationFormatter.parse(presentationValue, getHoursADay());
        } catch (IllegalArgumentException e) {
            throw new ConversionException(e.getMessage(), e);
        }
    }

    @Nullable
    private DurationUnit resolveAlwaysDisplayIn() {
        String alias = getAlwaysDisplayIn();
        return StringUtils.hasText(alias) ? DurationUnit.fromAlias(alias) : null;
    }
}
