package no.fintlabs.consumer.model.otenhet;


import no.fint.model.resource.utdanning.kodeverk.OTEnhetResource;
import no.fint.model.resource.utdanning.kodeverk.OTEnhetResources;
import no.fint.relations.FintLinker;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.Objects.isNull;

@Component
public class OTEnhetLinker extends FintLinker<OTEnhetResource> {

    public OTEnhetLinker() {
        super(OTEnhetResource.class);
    }

    public void mapLinks(OTEnhetResource resource) {
        super.mapLinks(resource);
    }

    @Override
    public OTEnhetResources toResources(Collection<OTEnhetResource> collection) {
        return toResources(collection.stream(), 0, 0, collection.size());
    }

    @Override
    public OTEnhetResources toResources(Stream<OTEnhetResource> stream, int offset, int size, int totalItems) {
        OTEnhetResources resources = new OTEnhetResources();
        stream.map(this::toResource).forEach(resources::addResource);
        addPagination(resources, offset, size, totalItems);
        return resources;
    }

    @Override
    public String getSelfHref(OTEnhetResource resource) {
        return getAllSelfHrefs(resource).findFirst().orElse(null);
    }

    @Override
    public Stream<String> getAllSelfHrefs(OTEnhetResource resource) {
        Stream.Builder<String> builder = Stream.builder();
        if (!isNull(resource.getSystemId()) && !StringUtils.isEmpty(resource.getSystemId().getIdentifikatorverdi())) {
            builder.add(createHrefWithId(resource.getSystemId().getIdentifikatorverdi(), "systemid"));
        }

        return builder.build();
    }

    int[] hashCodes(OTEnhetResource resource) {
        IntStream.Builder builder = IntStream.builder();
        if (!isNull(resource.getSystemId()) && !StringUtils.isEmpty(resource.getSystemId().getIdentifikatorverdi())) {
            builder.add(resource.getSystemId().getIdentifikatorverdi().hashCode());
        }

        return builder.build().toArray();
    }
}