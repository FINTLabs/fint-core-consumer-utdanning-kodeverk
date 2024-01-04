package no.fintlabs.consumer.model.otenhet;

import jakarta.annotation.PostConstruct;
import no.fint.model.felles.kompleksedatatyper.Identifikator;
import no.fint.model.resource.utdanning.kodeverk.OtStatusResource;
import no.fintlabs.cache.Cache;
import no.fintlabs.cache.CacheManager;
import no.fintlabs.cache.packing.PackingTypes;
import no.fintlabs.core.consumer.shared.resource.CacheService;
import no.fintlabs.core.consumer.shared.resource.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OtEnhetService extends CacheService<OtStatusResource> {

    private final OtEnhetKafkaConsumer elevfravarKafkaConsumer;
    private final OtEnhetLinker linker;

    public OtEnhetService(
            OtEnhetConfig config,
            CacheManager cacheManager,
            OtEnhetKafkaConsumer kafkaConsumer,
            OtEnhetLinker linker) {
        super(config, cacheManager, kafkaConsumer);
        this.elevfravarKafkaConsumer = kafkaConsumer;
        this.linker = linker;
    }

    @Override
    protected Cache<OtStatusResource> initializeCache(CacheManager cacheManager, ConsumerConfig<OtStatusResource> consumerConfig, String s) {
        return cacheManager.create(PackingTypes.POJO, consumerConfig.getOrgId(), consumerConfig.getResourceName());
    }

    @PostConstruct
    private void registerKafkaListener() {
        long retension = elevfravarKafkaConsumer.registerListener(OtStatusResource.class, this::addResourceToCache);
        getCache().setRetentionPeriodInMs(retension);
    }

    private void addResourceToCache(ConsumerRecord<String, OtStatusResource> consumerRecord) {
        this.eventLogger.logDataRecieved();
        if (consumerRecord.value() == null) {
            getCache().remove(consumerRecord.key());
        } else {
            OtStatusResource OtStatusResource = consumerRecord.value();
            linker.mapLinks(OtStatusResource);
            getCache().put(consumerRecord.key(), OtStatusResource, linker.hashCodes(OtStatusResource));
        }
    }

    @Override
    public Optional<OtStatusResource> getBySystemId(String systemId) {
        return getCache().getLastUpdatedByFilter(systemId.hashCode(),
                resource -> Optional
                        .ofNullable(resource)
                        .map(OtStatusResource::getSystemId)
                        .map(Identifikator::getIdentifikatorverdi)
                        .map(systemId::equals)
                        .orElse(false));
    }
}