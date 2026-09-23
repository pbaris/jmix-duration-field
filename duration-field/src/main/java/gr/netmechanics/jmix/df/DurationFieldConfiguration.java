package gr.netmechanics.jmix.df;

import gr.netmechanics.jmix.df.component.DurationField;
import gr.netmechanics.jmix.df.metamodel.DurationAwareMetaModelLoader;
import io.jmix.core.Messages;
import io.jmix.core.Stores;
import io.jmix.core.annotation.JmixModule;
import io.jmix.core.impl.MetaModelLoader;
import io.jmix.core.metamodel.datatype.DatatypeRegistry;
import io.jmix.core.metamodel.datatype.FormatStringsRegistry;
import io.jmix.flowui.FlowuiConfiguration;
import io.jmix.flowui.sys.registration.ComponentRegistration;
import io.jmix.flowui.sys.registration.ComponentRegistrationBuilder;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;

/**
 * @author Panos Bariamis (pbaris)
 */
@Configuration
@ComponentScan
@ConfigurationPropertiesScan
@JmixModule(dependsOn = {FlowuiConfiguration.class})
@PropertySource(name = "gr.netmechanics.jmix.df", value = "classpath:/gr/netmechanics/jmix/df/module.properties")
public class DurationFieldConfiguration {

    @Bean
    public ComponentRegistration durationField() {
        return ComponentRegistrationBuilder.create(DurationField.class)
            .withComponentLoader("durationField", DurationFieldLoader.class)
            .build();
    }

    /**
     * Registered {@code @Primary} (rather than replacing the framework's {@code "core_MetaModelLoader"}
     * bean by name) so entity attributes annotated with {@code @DurationFormat} get a per-property
     * datatype wired into their metamodel range, the same way {@code @NumberFormat} does — making
     * form fields and grid/list columns render identically with no extra wiring. See
     * {@link DurationAwareMetaModelLoader} for details.
     */
    @Bean
    @Primary
    public MetaModelLoader durationAwareMetaModelLoader(final DatatypeRegistry datatypes, final Stores stores,
                                                          final FormatStringsRegistry formatStringsRegistry,
                                                          final Messages messages) {
        return new DurationAwareMetaModelLoader(datatypes, stores, formatStringsRegistry, messages);
    }
}
