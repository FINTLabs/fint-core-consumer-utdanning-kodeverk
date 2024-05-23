package no.fintlabs.consumer.model.otstatus;

import lombok.extern.slf4j.Slf4j;
import no.fint.antlr.FintFilterService;
import no.fint.model.resource.utdanning.kodeverk.OtStatusResource;
import no.fint.relations.FintRelationsMediaType;
import no.fintlabs.consumer.config.RestEndpoints;
import no.fintlabs.core.consumer.shared.resource.ConsumerRestController;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping(name = "OTStatus", value = RestEndpoints.OTSTATUS, produces = {FintRelationsMediaType.APPLICATION_HAL_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE})
public class OtStatusController extends ConsumerRestController<OtStatusResource> {

    private final ServerCodecConfigurer serverCodecConfigurer;

    // temp code:
    private final OtStatusService service;

    public OtStatusController(OtStatusService service, OtStatusLinker linker, FintFilterService oDataFilterService, ServerCodecConfigurer serverCodecConfigurer) {
        super(service, linker, oDataFilterService);
        this.serverCodecConfigurer = serverCodecConfigurer;
        this.service = service;
    }

    @GetMapping("/ping")
    public String ping() {

        log.info("Resource name: {}", service.getResourceName());
        log.info("Cache size: {}", service.getCacheSize());
        log.info("Cache urn: {}", service.getCacheUrn());

        Optional<OtStatusResource> result = service.getBySystemId("OTFU");
        if (result.isEmpty()) {
            log.info("OTFU: No result");
        } else {
            log.info("OTFU result: {}", result.get());
        }

        service.streamAll().forEach(r -> log.info("Resource: {}", r));

        return "pong";
    }
}
