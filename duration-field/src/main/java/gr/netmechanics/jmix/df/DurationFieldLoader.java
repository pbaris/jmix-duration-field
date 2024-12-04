package gr.netmechanics.jmix.df;

import gr.netmechanics.jmix.df.component.DurationField;
import io.jmix.flowui.xml.layout.loader.AbstractComponentLoader;
import io.jmix.flowui.xml.layout.support.DataLoaderSupport;
import org.springframework.lang.NonNull;

/**
 * @author Panos Bariamis (pbaris)
 */
public class DurationFieldLoader extends AbstractComponentLoader<DurationField> {

    protected DataLoaderSupport dataLoaderSupport;

    @Override
    @NonNull
    protected DurationField createComponent() {
        return factory.create(DurationField.class);
    }

    @Override
    public void loadComponent() {
        getDataLoaderSupport().loadData(resultComponent, element);

        loadBoolean(element, "clearButtonVisible", resultComponent::setClearButtonVisible);
        loadResourceString(element, "title", context.getMessageGroup(), resultComponent::setTitle);

        componentLoader().loadPlaceholder(resultComponent, element);
        componentLoader().loadLabel(resultComponent, element);
        componentLoader().loadEnabled(resultComponent, element);
        componentLoader().loadTooltip(resultComponent, element);
        componentLoader().loadFocusableAttributes(resultComponent, element);
        componentLoader().loadThemeNames(resultComponent, element);
        componentLoader().loadClassNames(resultComponent, element);
        componentLoader().loadHelperText(resultComponent, element);
        componentLoader().loadAutocomplete(resultComponent, element);
        componentLoader().loadAriaLabel(resultComponent, element);
        componentLoader().loadSizeAttributes(resultComponent, element);
        componentLoader().loadRequired(resultComponent, element, context);
        componentLoader().loadValueAndElementAttributes(resultComponent, element);
    }

    protected DataLoaderSupport getDataLoaderSupport() {
        if (dataLoaderSupport == null) {
            dataLoaderSupport = applicationContext.getBean(DataLoaderSupport.class, context);
        }
        return dataLoaderSupport;
    }
}
