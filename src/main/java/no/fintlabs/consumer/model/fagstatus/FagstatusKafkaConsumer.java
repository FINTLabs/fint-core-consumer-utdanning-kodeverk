package no.fintlabs.consumer.model.fagstatus;

import no.fint.model.resource.utdanning.kodeverk.FagstatusResource;
import no.fintlabs.core.consumer.shared.resource.kafka.EntityKafkaConsumer;
import no.fintlabs.kafka.common.ListenerBeanRegistrationService;
import no.fintlabs.kafka.entity.EntityConsumerFactoryService;
import no.fintlabs.kafka.entity.topic.EntityTopicService;
import org.springframework.stereotype.Service;

@Service
public class FagstatusKafkaConsumer extends EntityKafkaConsumer<FagstatusResource> {
    public FagstatusKafkaConsumer(
            EntityConsumerFactoryService entityConsumerFactoryService,
            ListenerBeanRegistrationService listenerBeanRegistrationService,
            EntityTopicService entityTopicService,
            FagstatusConfig config) {
        super(entityConsumerFactoryService,
                listenerBeanRegistrationService,
                entityTopicService,
                config);
    }
}
