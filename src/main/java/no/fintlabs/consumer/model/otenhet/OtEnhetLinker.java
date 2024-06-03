package no.fintlabs.consumer.model.otenhet;


import no.fint.model.resource.utdanning.kodeverk.OtEnhetResource;
import no.fint.model.resource.utdanning.kodeverk.OtEnhetResources;
import no.fint.relations.FintLinker;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.Objects.isNull;

@Component
public class OtEnhetLinker extends FintLinker<OtEnhetResource> {

    public OtEnhetLinker() {
        super(OtEnhetResource.class);
    }

    public void mapLinks(OtEnhetResource resource) {
        super.mapLinks(resource);
    }

    @Override
    public OtEnhetResources toResources(Collection<OtEnhetResource> collection) {
        return toResources(collection.stream(), 0, 0, collection.size());
    }

    @Override
    public OtEnhetResources toResources(Stream<OtEnhetResource> stream, int offset, int size, int totalItems) {
        OtEnhetResources resources = new OtEnhetResources();
        stream.map(this::toResource).forEach(resources::addResource);
        addPagination(resources, offset, size, totalItems);
        return resources;
    }

    @Override
    public String getSelfHref(OtEnhetResource resource) {
        return getAllSelfHrefs(resource).findFirst().orElse(null);
    }

    @Override
    public Stream<String> getAllSelfHrefs(OtEnhetResource resource) {
        Stream.Builder<String> builder = Stream.builder();

        if (!isNull(resource.getSystemId()) && !StringUtils.isEmpty(resource.getSystemId().getIdentifikatorverdi())) {
            builder.add(createHrefWithId(resource.getSystemId().getIdentifikatorverdi(), "systemid"));
        }

        return builder.build();
    }

    int[] hashCodes(OtEnhetResource resource) {
        IntStream.Builder builder = IntStream.builder();

        if (!isNull(resource.getSystemId()) && !StringUtils.isEmpty(resource.getSystemId().getIdentifikatorverdi())) {
            builder.add(resource.getSystemId().getIdentifikatorverdi().hashCode());
        }

        return builder.build().toArray();
    }
}