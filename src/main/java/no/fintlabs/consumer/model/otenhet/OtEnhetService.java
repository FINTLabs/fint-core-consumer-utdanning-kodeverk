package no.fintlabs.consumer.model.otenhet;

import jakarta.annotation.PostConstruct;
import no.fint.model.felles.kompleksedatatyper.Identifikator;
import no.fint.model.resource.utdanning.kodeverk.OtEnhetResource;
import no.fintlabs.cache.Cache;
import no.fintlabs.cache.CacheManager;
import no.fintlabs.cache.packing.PackingTypes;
import no.fintlabs.core.consumer.shared.resource.CacheService;
import no.fintlabs.core.consumer.shared.resource.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OtEnhetService extends CacheService<OtEnhetResource> {

    private final OtEnhetKafkaConsumer kafkaConsumer;
    private final OtEnhetLinker linker;

    public OtEnhetService(
            OtEnhetConfig config,
            CacheManager cacheManager,
            OtEnhetKafkaConsumer kafkaConsumer,
            OtEnhetLinker linker) {
        super(config, cacheManager, kafkaConsumer);
        this.kafkaConsumer = kafkaConsumer;
        this.linker = linker;
    }

    @Override
    protected Cache<OtEnhetResource> initializeCache(CacheManager cacheManager, ConsumerConfig<OtEnhetResource> consumerConfig, String s) {
        return cacheManager.create(PackingTypes.POJO, consumerConfig.getOrgId(), consumerConfig.getResourceName());
    }

    @PostConstruct
    private void registerKafkaListener() {
        kafkaConsumer.registerListener(OtEnhetResource.class, this::addResourceToCache);
    }

    private void addResourceToCache(ConsumerRecord<String, OtEnhetResource> consumerRecord) {
        updateRetensionTime(consumerRecord.headers().lastHeader("topic-retension-time"));
        this.eventLogger.logDataRecieved();
        if (consumerRecord.value() == null) {
            getCache().remove(consumerRecord.key());
        } else {
            OtEnhetResource OtEnhetResource = consumerRecord.value();
            linker.mapLinks(OtEnhetResource);
            getCache().put(consumerRecord.key(), OtEnhetResource, linker.hashCodes(OtEnhetResource));
        }
    }

    @Override
    public Optional<OtEnhetResource> getBySystemId(String systemId) {
        return getCache().getLastUpdatedByFilter(systemId.hashCode(),
                resource -> Optional
                        .ofNullable(resource)
                        .map(OtEnhetResource::getSystemId)
                        .map(Identifikator::getIdentifikatorverdi)
                        .map(systemId::equals)
                        .orElse(false));
    }
}