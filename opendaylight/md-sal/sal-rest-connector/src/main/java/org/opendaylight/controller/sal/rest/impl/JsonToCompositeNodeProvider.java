package org.opendaylight.controller.sal.rest.impl;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;

import org.opendaylight.controller.sal.rest.api.Draft01;
import org.opendaylight.controller.sal.rest.api.Draft02;
import org.opendaylight.controller.sal.rest.api.RestconfService;
import org.opendaylight.yangtools.yang.data.api.CompositeNode;

@Provider
@Consumes({ Draft01.MediaTypes.DATA + RestconfService.JSON, Draft02.MediaTypes.DATA + RestconfService.JSON,
        MediaType.APPLICATION_JSON })
public enum JsonToCompositeNodeProvider implements MessageBodyReader<CompositeNode> {
    INSTANCE;

    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return true;
    }

    @Override
    public CompositeNode readFrom(Class<CompositeNode> type, Type genericType, Annotation[] annotations,
            MediaType mediaType, MultivaluedMap<String, String> httpHeaders, InputStream entityStream)
            throws IOException, WebApplicationException {
        JsonReader jsonReader = new JsonReader();
        try {
            return jsonReader.read(entityStream);
        } catch (UnsupportedFormatException e) {
            throw new WebApplicationException(e, Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage())
                    .build());
        }
    }

}
