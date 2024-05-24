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

    public OtStatusController(OtStatusService service, OtStatusLinker linker, FintFilterService oDataFilterService, ServerCodecConfigurer serverCodecConfigurer) {
        super(service, linker, oDataFilterService);
    }

}
