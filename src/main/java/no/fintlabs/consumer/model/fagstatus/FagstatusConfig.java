package no.fintlabs.consumer.model.fagstatus;

import no.fint.model.resource.utdanning.kodeverk.FagstatusResource;
import no.fintlabs.core.consumer.shared.config.ConsumerProps;
import no.fintlabs.core.consumer.shared.resource.ConsumerConfig;
import org.springframework.stereotype.Component;

@Component
public class FagstatusConfig extends ConsumerConfig<FagstatusResource> {

    public FagstatusConfig(ConsumerProps consumerProps) {
        super(consumerProps);
    }

    @Override
    protected String resourceName() {
        return "fagstatus";
    }
}
