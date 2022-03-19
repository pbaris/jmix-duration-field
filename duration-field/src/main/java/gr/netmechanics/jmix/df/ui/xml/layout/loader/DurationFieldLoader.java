package gr.netmechanics.jmix.df.ui.xml.layout.loader;

import gr.netmechanics.jmix.df.ui.component.DurationField;
import io.jmix.core.Messages;
import io.jmix.ui.xml.layout.loader.AbstractTextFieldLoader;

/**
 * @author Panos Bariamis (pbaris)
 */
public class DurationFieldLoader extends AbstractTextFieldLoader<DurationField> {

    @Override
    public void createComponent() {
        resultComponent = factory.create(DurationField.NAME);
        loadId(resultComponent, element);

        final Messages messages = applicationContext.getBean(Messages.class);

        resultComponent.setInputPrompt(messages.getMessage("gr.netmechanics.jmix.durationField/prompt"));
        resultComponent.setDescription(messages.getMessage("gr.netmechanics.jmix.durationField/help"));
        resultComponent.setDescriptionAsHtml(true);
    }

    @Override
    public void loadComponent() {
        super.loadComponent();
        loadInputPrompt(resultComponent, element);
    }
}
