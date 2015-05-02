package org.foomla.api.client.providers.base;

import java.util.List;

import org.foomla.api.annotations.AnnotationsUtil;
import org.foomla.api.client.AuthorizationException;
import org.foomla.api.client.FoomlaClient.RestConnectionSettings;
import org.foomla.api.client.RemoteInvocationException;
import org.foomla.api.client.service.ServiceProvider;
import org.foomla.api.entities.base.Entity;
import org.foomla.api.services.FoomlaService;
import org.restlet.data.Method;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

@Deprecated
public abstract class EntityProvider<T extends Entity> extends ServiceProvider {

    public static class EntityProviderClasses {

        public static EntityProviderClasses create(final Class<? extends FoomlaService> entityListResource,
                final Class<? extends FoomlaService> entityResource, final Class<?> entityListType,
                final Class<?> entityType) {
            EntityProviderClasses entityProviderClasses = new EntityProviderClasses();

            entityProviderClasses.entityListResource = entityListResource;
            entityProviderClasses.entityResource = entityResource;
            entityProviderClasses.entityListType = entityListType;
            entityProviderClasses.entityType = entityType;

            return entityProviderClasses;
        }

        public Class<?> entityListResource;
        public Class<?> entityListType;
        public Class<?> entityResource;

        public Class<?> entityType;

        private EntityProviderClasses() {
        }
    }

    public EntityProvider(final RestConnectionSettings restConnectionSettings) {
        super(restConnectionSettings);
    }

    public void create(final T entity) throws RemoteInvocationException, AuthorizationException {
        ClientResource clientResource = createClientResource(getEntityProviderClasses().entityListResource);
        callResource(clientResource, Method.POST, entity);
    }

    public void delete(final int id) throws RemoteInvocationException, AuthorizationException {
        final ClientResource clientResource = createClientResource(getEntityProviderClasses().entityResource);
        clientResource.addSegment(String.valueOf(id));
        callResource(clientResource, Method.DELETE);
    }

    public T get() throws RemoteInvocationException, AuthorizationException {
        ClientResource clientResource = createClientResource(getEntityProviderClasses().entityResource);
        Representation representation = callResource(clientResource, Method.GET);
        @SuppressWarnings("unchecked")
        T entity = (T) clientResource.toObject(representation, getEntityProviderClasses().entityType);
        return entity;
    }

    @SuppressWarnings("unchecked")
    public T get(final int id) throws RemoteInvocationException, AuthorizationException {
        ClientResource clientResource = createClientResource(getEntityProviderClasses().entityResource);
        clientResource.addSegment(String.valueOf(id));
        Representation representation = callResource(clientResource, Method.GET);
        T entity = (T) clientResource.toObject(representation, getEntityProviderClasses().entityType);
        return entity;
    }

    @SuppressWarnings("unchecked")
    public List<T> getAll() throws RemoteInvocationException, AuthorizationException {
        ClientResource clientResource = createClientResource(getEntityProviderClasses().entityListResource);
        Representation representation = callResource(clientResource, Method.GET);
        List<T> entityList = (List<T>) clientResource.toObject(representation,
                getEntityProviderClasses().entityListType);
        return entityList;
    }

    public void update(final T entity) throws RemoteInvocationException, AuthorizationException {
        ClientResource clientResource = createClientResource(getEntityProviderClasses().entityResource);
        clientResource.getRequestAttributes().put(FoomlaService.ID, entity.getId());
        callResource(clientResource, Method.PUT, entity);
    }

    protected ClientResource createClientResource(final Class<?> resourceInterface) {
        ClientResource clientResource = new ClientResource(getSettings().restUrl
                + AnnotationsUtil.getRestPath(resourceInterface));
        setCustomHeaders(clientResource);
        setAccessTokenHeader(clientResource);

        return clientResource;
    }

    protected abstract EntityProviderClasses getEntityProviderClasses();
}
