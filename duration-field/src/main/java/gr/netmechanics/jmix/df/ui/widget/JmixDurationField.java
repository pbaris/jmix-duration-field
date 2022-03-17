package gr.netmechanics.jmix.df.ui.widget;

import gr.netmechanics.jmix.df.ui.widget.client.textfield.JmixDurationFieldState;
import io.jmix.ui.widget.JmixTextField;
import org.springframework.lang.NonNull;

/**
 * @author Panos Bariamis (pbaris)
 */
public class JmixDurationField extends JmixTextField {

    @NonNull
    @Override
    protected JmixDurationFieldState getState() {
        return (JmixDurationFieldState) super.getState();
    }

    @NonNull
    @Override
    protected JmixDurationFieldState getState(boolean markAsDirty) {
        return (JmixDurationFieldState) super.getState(markAsDirty);
    }
}
