package no.fintlabs.consumer.model.fagstatus;

import jakarta.annotation.PostConstruct;
import no.fint.model.felles.kompleksedatatyper.Identifikator;
import no.fint.model.resource.utdanning.kodeverk.FagstatusResource;
import no.fintlabs.cache.Cache;
import no.fintlabs.cache.CacheManager;
import no.fintlabs.cache.packing.PackingTypes;
import no.fintlabs.core.consumer.shared.resource.CacheService;
import no.fintlabs.core.consumer.shared.resource.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FagstatusService extends CacheService<FagstatusResource> {

    private final FagstatusKafkaConsumer kafkaConsumer;
    private final FagstatusLinker linker;

    public FagstatusService(
            FagstatusConfig config,
            CacheManager cacheManager,
            FagstatusKafkaConsumer kafkaConsumer,
            FagstatusLinker linker) {
        super(config, cacheManager, kafkaConsumer);
        this.kafkaConsumer = kafkaConsumer;
        this.linker = linker;
    }

    @Override
    protected Cache<FagstatusResource> initializeCache(CacheManager cacheManager, ConsumerConfig<FagstatusResource> consumerConfig, String s) {
        return cacheManager.create(PackingTypes.POJO, consumerConfig.getOrgId(), consumerConfig.getResourceName());
    }

    @PostConstruct
    private void registerKafkaListener() {
        long retension = kafkaConsumer.registerListener(FagstatusResource.class, this::addResourceToCache);
        getCache().setRetentionPeriodInMs(retension);
    }

    private void addResourceToCache(ConsumerRecord<String, FagstatusResource> consumerRecord) {
        this.eventLogger.logDataRecieved();
        if (consumerRecord.value() == null) {
            getCache().remove(consumerRecord.key());
        } else {
            FagstatusResource FagstatusResource = consumerRecord.value();
            linker.mapLinks(FagstatusResource);
            getCache().put(consumerRecord.key(), FagstatusResource, linker.hashCodes(FagstatusResource));
        }
    }

    @Override
    public Optional<FagstatusResource> getBySystemId(String systemId) {
        return getCache().getLastUpdatedByFilter(systemId.hashCode(),
                resource -> Optional
                        .ofNullable(resource)
                        .map(FagstatusResource::getSystemId)
                        .map(Identifikator::getIdentifikatorverdi)
                        .map(systemId::equals)
                        .orElse(false));
    }
}