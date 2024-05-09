package gr.netmechanics.jmix.df.component;

import java.time.Duration;
import java.util.function.Consumer;

import com.vaadin.flow.component.HasAriaLabel;
import com.vaadin.flow.component.HasPlaceholder;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.shared.HasClearButton;
import com.vaadin.flow.component.shared.HasThemeVariant;
import com.vaadin.flow.component.textfield.HasAutocomplete;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import gr.netmechanics.jmix.df.DurationTransformations;
import io.jmix.flowui.component.HasRequired;
import io.jmix.flowui.component.SupportsStatusChangeHandler;
import io.jmix.flowui.component.UiComponentUtils;
import io.jmix.flowui.component.delegate.FieldDelegate;
import io.jmix.flowui.data.SupportsValueSource;
import io.jmix.flowui.data.ValueSource;
import io.jmix.flowui.kit.component.HasTitle;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;

/**
 * @author Panos Bariamis (pbaris)
 */
public class DurationField extends CustomField<Duration>
    implements SupportsValueSource<Duration>, SupportsStatusChangeHandler<DurationField>, HasThemeVariant<TextFieldVariant>,
    HasRequired, HasAutocomplete, HasTitle, HasPlaceholder, HasAriaLabel, HasClearButton, ApplicationContextAware, InitializingBean {

    private final TextField field = new TextField();

    private ApplicationContext applicationContext;
    private DurationTransformations transformations;
    private FieldDelegate<DurationField, Duration, Duration> fieldDelegate;

    public DurationField() {
        add(field);
    }

    @Override
    protected Duration generateModelValue() {
        return transformations.transformToDuration(UiComponentUtils.getValue(field));
    }

    @Override
    protected void setPresentationValue(final Duration duration) {
        UiComponentUtils.setValue(field, transformations.transformToString(duration));
    }

    @Override
    public ValueSource<Duration> getValueSource() {
        return fieldDelegate.getValueSource();
    }

    @Override
    public void setValueSource(final ValueSource<Duration> valueSource) {
        fieldDelegate.setValueSource(valueSource);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void afterPropertiesSet() {
        fieldDelegate = applicationContext.getBean(FieldDelegate.class, this);
        transformations = applicationContext.getBean(DurationTransformations.class);
    }

    @Override
    public void setApplicationContext(@NonNull final ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public String getRequiredMessage() {
        return fieldDelegate.getRequiredMessage();
    }

    @Override
    public void setRequiredMessage(final String requiredMessage) {
        fieldDelegate.setRequiredMessage(requiredMessage);
    }

    @Override
    public void setStatusChangeHandler(final Consumer<StatusContext<DurationField>> handler) {
        fieldDelegate.setStatusChangeHandler(handler);
    }

    @Override
    public void setRequired(boolean required) {
        getElement().setProperty(PROPERTY_REQUIRED, required);
        fieldDelegate.updateInvalidState();
    }

    @Override
    public void setRequiredIndicatorVisible(boolean required) {
        super.setRequiredIndicatorVisible(required);
        fieldDelegate.updateInvalidState();
    }
}
