package no.fintlabs.consumer.model.fagstatus;


import no.fint.model.resource.utdanning.kodeverk.FagstatusResource;
import no.fint.model.resource.utdanning.kodeverk.FagstatusResources;
import no.fint.relations.FintLinker;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.Objects.isNull;

@Component
public class FagstatusLinker extends FintLinker<FagstatusResource> {

    public FagstatusLinker() {
        super(FagstatusResource.class);
    }

    public void mapLinks(FagstatusResource resource) {
        super.mapLinks(resource);
    }

    @Override
    public FagstatusResources toResources(Collection<FagstatusResource> collection) {
        return toResources(collection.stream(), 0, 0, collection.size());
    }

    @Override
    public FagstatusResources toResources(Stream<FagstatusResource> stream, int offset, int size, int totalItems) {
        FagstatusResources resources = new FagstatusResources();
        stream.map(this::toResource).forEach(resources::addResource);
        addPagination(resources, offset, size, totalItems);
        return resources;
    }

    @Override
    public String getSelfHref(FagstatusResource resource) {
        return getAllSelfHrefs(resource).findFirst().orElse(null);
    }

    @Override
    public Stream<String> getAllSelfHrefs(FagstatusResource resource) {
        Stream.Builder<String> builder = Stream.builder();
        if (!isNull(resource.getSystemId()) && !StringUtils.isEmpty(resource.getSystemId().getIdentifikatorverdi())) {
            builder.add(createHrefWithId(resource.getSystemId().getIdentifikatorverdi(), "systemid"));
        }

        return builder.build();
    }

    int[] hashCodes(FagstatusResource resource) {
        IntStream.Builder builder = IntStream.builder();
        if (!isNull(resource.getSystemId()) && !StringUtils.isEmpty(resource.getSystemId().getIdentifikatorverdi())) {
            builder.add(resource.getSystemId().getIdentifikatorverdi().hashCode());
        }

        return builder.build().toArray();
    }
}