package gr.netmechanics.jmix.df.ui.xml.layout.loader;

import gr.netmechanics.jmix.df.ui.component.DurationField;
import io.jmix.ui.xml.layout.loader.AbstractTextFieldLoader;

/**
 * @author Panos Bariamis (pbaris)
 */
public class DurationFieldLoader extends AbstractTextFieldLoader<DurationField> {

    @Override
    public void createComponent() {
        resultComponent = factory.create(DurationField.NAME);
        loadId(resultComponent, element);
    }

    @Override
    public void loadComponent() {
        super.loadComponent();
        loadDatatype(resultComponent, element);
    }
}
