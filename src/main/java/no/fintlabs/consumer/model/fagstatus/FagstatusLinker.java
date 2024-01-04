package no.fintlabs.consumer.model.otenhet;


import no.fint.model.resource.utdanning.kodeverk.OtStatusResource;
import no.fint.model.resource.utdanning.kodeverk.OtStatusResources;
import no.fint.relations.FintLinker;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.Objects.isNull;

@Component
public class OtEnhetLinker extends FintLinker<OtStatusResource> {

    public OtEnhetLinker() {
        super(OtStatusResource.class);
    }

    public void mapLinks(OtStatusResource resource) {
        super.mapLinks(resource);
    }

    @Override
    public OtStatusResources toResources(Collection<OtStatusResource> collection) {
        return toResources(collection.stream(), 0, 0, collection.size());
    }

    @Override
    public OtStatusResources toResources(Stream<OtStatusResource> stream, int offset, int size, int totalItems) {
        OtStatusResources resources = new OtStatusResources();
        stream.map(this::toResource).forEach(resources::addResource);
        addPagination(resources, offset, size, totalItems);
        return resources;
    }

    @Override
    public String getSelfHref(OtStatusResource resource) {
        return getAllSelfHrefs(resource).findFirst().orElse(null);
    }

    @Override
    public Stream<String> getAllSelfHrefs(OtStatusResource resource) {
        Stream.Builder<String> builder = Stream.builder();
        if (!isNull(resource.getSystemId()) && !StringUtils.isEmpty(resource.getSystemId().getIdentifikatorverdi())) {
            builder.add(createHrefWithId(resource.getSystemId().getIdentifikatorverdi(), "systemid"));
        }

        return builder.build();
    }

    int[] hashCodes(OtStatusResource resource) {
        IntStream.Builder builder = IntStream.builder();
        if (!isNull(resource.getSystemId()) && !StringUtils.isEmpty(resource.getSystemId().getIdentifikatorverdi())) {
            builder.add(resource.getSystemId().getIdentifikatorverdi().hashCode());
        }

        return builder.build().toArray();
    }
}