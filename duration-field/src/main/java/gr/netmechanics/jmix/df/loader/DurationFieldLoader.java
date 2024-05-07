package gr.netmechanics.jmix.df.loader;

import gr.netmechanics.jmix.df.component.DurationField;
import io.jmix.core.Messages;
import io.jmix.flowui.xml.layout.loader.AbstractComponentLoader;
import io.jmix.flowui.xml.layout.support.DataLoaderSupport;

/**
 * @author Panos Bariamis (pbaris)
 */
public class DurationFieldLoader extends AbstractComponentLoader<DurationField> {

    protected DataLoaderSupport dataLoaderSupport;

    @Override
    protected DurationField createComponent() {
        return factory.create(DurationField.class);
    }

    @Override
    public void loadComponent() {
        getDataLoaderSupport().loadData(resultComponent, element);

        componentLoader().loadPlaceholder(resultComponent, element);
        componentLoader().loadLabel(resultComponent, element);
        componentLoader().loadEnabled(resultComponent, element);
        componentLoader().loadFocusableAttributes(resultComponent, element);
        componentLoader().loadThemeNames(resultComponent, element);
        componentLoader().loadClassNames(resultComponent, element);
        componentLoader().loadHelperText(resultComponent, element);
        componentLoader().loadAutocomplete(resultComponent, element);
        componentLoader().loadSizeAttributes(resultComponent, element);
        componentLoader().loadValueChangeMode(resultComponent, element);
        componentLoader().loadRequired(resultComponent, element, context);
        componentLoader().loadValueAndElementAttributes(resultComponent, element);
        componentLoader().loadValidationAttributes(resultComponent, element, context);
        componentLoader().loadAriaLabel(resultComponent, element);

        resultComponent.setTooltipText(applicationContext.getBean(Messages.class)
            .getMessage("gr.netmechanics.jmix.durationField/help"));
    }

    protected DataLoaderSupport getDataLoaderSupport() {
        if (dataLoaderSupport == null) {
            dataLoaderSupport = applicationContext.getBean(DataLoaderSupport.class, context);
        }
        return dataLoaderSupport;
    }
}
