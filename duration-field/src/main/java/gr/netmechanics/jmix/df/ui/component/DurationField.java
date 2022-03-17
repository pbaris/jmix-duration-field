package gr.netmechanics.jmix.df.ui.component;

import java.time.Duration;

import io.jmix.ui.component.HasDatatype;
import io.jmix.ui.component.HasInputPrompt;
import io.jmix.ui.component.TextInputField;
import io.jmix.ui.meta.CanvasBehaviour;
import io.jmix.ui.meta.PropertiesConstraint;
import io.jmix.ui.meta.PropertiesGroup;
import io.jmix.ui.meta.PropertyType;
import io.jmix.ui.meta.StudioComponent;
import io.jmix.ui.meta.StudioProperties;
import io.jmix.ui.meta.StudioProperty;

/**
 * @author Panos Bariamis (pbaris)
 */
@StudioComponent(
    caption = "DurationField",
    category = "Components",
    xmlElement = "durationField",
    icon = "gr/netmechanics/jmix/df/ui/icon/component/durationField.svg",
    canvasBehaviour = CanvasBehaviour.INPUT_FIELD
)
@StudioProperties(
    properties = {
        @StudioProperty(name = "property", type = PropertyType.PROPERTY_PATH_REF, typeParameter = "V"),
        @StudioProperty(name = "dataContainer", type = PropertyType.DATACONTAINER_REF)
    },
    groups = {
        @PropertiesGroup(constraint = PropertiesConstraint.ALL_OR_NOTHING,
            properties = {"dataContainer", "property"})
    }
)
public interface DurationField extends
    TextInputField<Duration>,
    HasDatatype<Duration>,
    TextInputField.TextSelectionSupported,
    HasInputPrompt {

    String NAME = "durationField";

    String getRawValue();
}
