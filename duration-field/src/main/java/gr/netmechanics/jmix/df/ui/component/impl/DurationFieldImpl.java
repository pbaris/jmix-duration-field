package gr.netmechanics.jmix.df.ui.component.impl;

import java.text.ParseException;
import java.time.Duration;
import javax.annotation.Nullable;

import com.google.common.base.Strings;
import com.vaadin.shared.ui.ValueChangeMode;
import gr.netmechanics.jmix.df.ui.component.DurationField;
import gr.netmechanics.jmix.df.ui.widget.JmixDurationField;
import io.jmix.core.Messages;
import io.jmix.core.metamodel.datatype.Datatype;
import io.jmix.ui.component.data.ConversionException;
import io.jmix.ui.component.data.DataAwareComponentsTools;
import io.jmix.ui.component.impl.AbstractField;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Panos Bariamis (pbaris)
 */
public class DurationFieldImpl extends AbstractField<JmixDurationField, String, Duration>
    implements DurationField, InitializingBean {

    protected Datatype<Duration> datatype;

    protected DataAwareComponentsTools dataAwareComponentsTools;

    public DurationFieldImpl() {
        this.component = createComponent();

        attachValueChangeListener(component);
    }

    protected JmixDurationField createComponent() {
        return new JmixDurationField();
    }

    @Autowired
    public void setDataAwareComponentsTools(DataAwareComponentsTools dataAwareComponentsTools) {
        this.dataAwareComponentsTools = dataAwareComponentsTools;
    }

    @Override
    public void afterPropertiesSet() {
        initComponent(component);
    }

    protected void initComponent(JmixDurationField component) {
        component.setValueChangeMode(ValueChangeMode.BLUR);
    }

    @Nullable
    @Override
    protected String convertToPresentation(@Nullable final Duration modelValue) throws ConversionException {
        return Strings.nullToEmpty(datatype.format(modelValue));
    }

    @Nullable
    @Override
    protected Duration convertToModel(@Nullable final String componentRawValue) throws ConversionException {
        String value = StringUtils.trimToNull(Strings.emptyToNull(componentRawValue));

        try {
            return datatype.parse(value);
        } catch (ParseException e) {
            throw new ConversionException(getConversionErrorMessage(), e);
        }
    }

    protected String getConversionErrorMessage() {
        Messages messages = applicationContext.getBean(Messages.class);
        return messages.getMessage("databinding.conversion.error");
    }

    @Override
    public String getRawValue() {
        return component.getValue();
    }

    @Override
    public void focus() {
        component.focus();
    }

    @Override
    public int getTabIndex() {
        return component.getTabIndex();
    }

    @Override
    public void setTabIndex(final int tabIndex) {
        component.setTabIndex(tabIndex);
    }

    @Nullable
    @Override
    public String getInputPrompt() {
        return component.getPlaceholder();
    }

    @Override
    public void setInputPrompt(@Nullable final String inputPrompt) {
        component.setPlaceholder(inputPrompt);
    }

    @Override
    public void selectAll() {
        component.selectAll();
    }

    @Override
    public void setSelectionRange(final int pos, final int length) {
        component.setSelection(pos, length);
    }

    @Override
    public void commit() {
        super.commit();
    }

    @Override
    public void discard() {
        super.discard();
    }

    @Override
    public boolean isBuffered() {
        return super.isBuffered();
    }

    @Override
    public void setBuffered(final boolean buffered) {
        super.setBuffered(buffered);
    }

    @Override
    public boolean isModified() {
        return super.isModified();
    }

    @Nullable
    @Override
    public Datatype<Duration> getDatatype() {
        return datatype;
    }

    @Override
    public void setDatatype(@Nullable final Datatype<Duration> datatype) {
        this.datatype = datatype;
    }
}
