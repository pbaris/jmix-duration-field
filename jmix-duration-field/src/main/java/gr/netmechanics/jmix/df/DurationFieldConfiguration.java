package gr.netmechanics.jmix.df;

import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author Panos Bariamis (pbaris)
 */
@Configuration
@ComponentScan
@ConfigurationPropertiesScan
@PropertySource(name = "gr.netmechanics.jmix.df", value = "classpath:/gr/netmechanics/jmix/df/module.properties")
public class DurationFieldConfiguration {

}
