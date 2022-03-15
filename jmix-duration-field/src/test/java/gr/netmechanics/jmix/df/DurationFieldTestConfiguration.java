package gr.netmechanics.jmix.df;

import io.jmix.core.annotation.JmixModule;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Import;

@SpringBootConfiguration
@EnableAutoConfiguration
@Import(DurationFieldConfiguration.class)
@JmixModule(id = "gr.netmechanics.jmix.df.test", dependsOn = DurationFieldConfiguration.class)
public class DurationFieldTestConfiguration {

}
