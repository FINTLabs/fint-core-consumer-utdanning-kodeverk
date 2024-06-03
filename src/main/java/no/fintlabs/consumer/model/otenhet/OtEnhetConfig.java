package no.fintlabs.consumer.model.otenhet;

import no.fint.model.resource.utdanning.kodeverk.OtEnhetResource;
import no.fint.model.resource.utdanning.kodeverk.OtStatusResource;
import no.fintlabs.core.consumer.shared.config.ConsumerProps;
import no.fintlabs.core.consumer.shared.resource.ConsumerConfig;
import org.springframework.stereotype.Component;

@Component
public class OtEnhetConfig extends ConsumerConfig<OtEnhetResource> {

    public OtEnhetConfig(ConsumerProps consumerProps) {
        super(consumerProps);
    }

    @Override
    protected String resourceName() {
        return "otenhet";
    }
}
