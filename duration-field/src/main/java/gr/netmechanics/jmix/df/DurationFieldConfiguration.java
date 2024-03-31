package gr.netmechanics.jmix.df;

import gr.netmechanics.jmix.df.component.DurationField;
import gr.netmechanics.jmix.df.loader.DurationFieldLoader;
import io.jmix.core.annotation.JmixModule;
import io.jmix.flowui.FlowuiConfiguration;
import io.jmix.flowui.sys.registration.ComponentRegistration;
import io.jmix.flowui.sys.registration.ComponentRegistrationBuilder;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
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
}
