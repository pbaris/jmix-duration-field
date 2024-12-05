package gr.netmechanics.jmix.df;

import gr.netmechanics.jmix.df.component.DurationField;
import io.jmix.flowui.xml.layout.loader.AbstractComponentLoader;
import io.jmix.flowui.xml.layout.support.DataLoaderSupport;
import io.jmix.flowui.xml.layout.support.PrefixSuffixLoaderSupport;
import org.springframework.lang.NonNull;

/**
 * @author Panos Bariamis (pbaris)
 */
public class DurationFieldLoader extends AbstractComponentLoader<DurationField> {

    private DataLoaderSupport dataLoaderSupport;
    private PrefixSuffixLoaderSupport prefixSuffixLoaderSupport;

    @Override
    @NonNull
    protected DurationField createComponent() {
        return factory.create(DurationField.class);
    }

    @Override
    public void initComponent() {
        super.initComponent();
        getPrefixSuffixLoaderSupport().createPrefixSuffixComponents(resultComponent, element);
    }

    @Override
    public void loadComponent() {
        getDataLoaderSupport().loadData(resultComponent, element);
        getPrefixSuffixLoaderSupport().loadPrefixSuffixComponents();

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

    private DataLoaderSupport getDataLoaderSupport() {
        if (dataLoaderSupport == null) {
            dataLoaderSupport = applicationContext.getBean(DataLoaderSupport.class, context);
        }
        return dataLoaderSupport;
    }

    private PrefixSuffixLoaderSupport getPrefixSuffixLoaderSupport() {
        if (prefixSuffixLoaderSupport == null) {
            prefixSuffixLoaderSupport = applicationContext.getBean(PrefixSuffixLoaderSupport.class, context);
        }
        return prefixSuffixLoaderSupport;
    }
}
