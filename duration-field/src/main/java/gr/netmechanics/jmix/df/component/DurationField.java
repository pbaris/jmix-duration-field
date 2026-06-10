package gr.netmechanics.jmix.df.component;

import java.time.Duration;

import gr.netmechanics.jmix.df.datatype.DurationDatatype;
import io.jmix.flowui.component.textfield.TypedTextField;

/**
 * @author Panos Bariamis (pbaris)
 */
public class DurationField extends TypedTextField<Duration> {

    private boolean shortLabels = true;

    public boolean isShortLabels() {
        return shortLabels;
    }

    public void setShortLabels(final boolean shortLabels) {
        this.shortLabels = shortLabels;
        setDatatype(new DurationDatatype(shortLabels));
    }
}
