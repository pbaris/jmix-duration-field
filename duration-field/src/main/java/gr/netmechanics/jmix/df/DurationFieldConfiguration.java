package gr.netmechanics.jmix.df;

import gr.netmechanics.jmix.df.ui.component.DurationField;
import gr.netmechanics.jmix.df.ui.component.impl.DurationFieldImpl;
import gr.netmechanics.jmix.df.ui.xml.layout.loader.DurationFieldLoader;
import io.jmix.core.annotation.JmixModule;
import io.jmix.ui.UiConfiguration;
import io.jmix.ui.sys.registration.ComponentRegistration;
import io.jmix.ui.sys.registration.ComponentRegistrationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author Panos Bariamis (pbaris)
 */
@Configuration
@ComponentScan
@JmixModule(dependsOn = UiConfiguration.class)
public class DurationFieldConfiguration {

    @Bean
    public ComponentRegistration durationField() {
        return ComponentRegistrationBuilder.create(DurationField.NAME)
            .withComponentClass(DurationFieldImpl.class)
            .withComponentLoaderClass(DurationFieldLoader.class)
            .build();
    }
}
