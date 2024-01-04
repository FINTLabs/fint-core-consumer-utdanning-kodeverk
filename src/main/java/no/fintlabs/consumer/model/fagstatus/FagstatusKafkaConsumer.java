package no.fintlabs.consumer.model.otenhet;

import no.fint.model.resource.utdanning.kodeverk.OtStatusResource;
import no.fintlabs.core.consumer.shared.resource.kafka.EntityKafkaConsumer;
import no.fintlabs.kafka.common.ListenerBeanRegistrationService;
import no.fintlabs.kafka.entity.EntityConsumerFactoryService;
import no.fintlabs.kafka.entity.topic.EntityTopicService;
import org.springframework.stereotype.Service;

@Service
public class OtEnhetKafkaConsumer extends EntityKafkaConsumer<OtStatusResource> {
    public OtEnhetKafkaConsumer(
            EntityConsumerFactoryService entityConsumerFactoryService,
            ListenerBeanRegistrationService listenerBeanRegistrationService,
            EntityTopicService entityTopicService,
            OtEnhetConfig config) {
        super(entityConsumerFactoryService,
                listenerBeanRegistrationService,
                entityTopicService,
                config);
    }
}
