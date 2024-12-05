package gr.netmechanics.jmix.autoconfigure.df;

import gr.netmechanics.jmix.df.DurationFieldConfiguration;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;

/**
 * @author Panos Bariamis (pbaris)
 */
@AutoConfiguration
@Import({DurationFieldConfiguration.class})
public class DurationFieldAutoConfiguration {
}

