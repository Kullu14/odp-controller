/*
 * Copyright 2012 Midokura Europe SARL
 * Copyright 2013 Midokura Pte Ltd
 */
package org.midonet.cluster;

import java.util.UUID;

import org.midonet.cluster.data.*;
import org.midonet.midolman.serialization.SerializationException;
import org.midonet.midolman.state.StateAccessException;

public class LocalDataClientImpl implements DataClient {
    public boolean bridgeExists(UUID id)
            throws StateAccessException {
        return true;
    }

    public Bridge bridgesGet(UUID id)
            throws StateAccessException, SerializationException {
        return null;
    }

    public void bridgesDelete(UUID id)
            throws StateAccessException, SerializationException {
        return;
    }

    public UUID bridgesCreate(Bridge bridge)
            throws StateAccessException, SerializationException {
        return null;
    }

    public Bridge bridgesGetByName(String tenantId, String name)
            throws StateAccessException, SerializationException {
        return null;
    }

    public void bridgesUpdate(Bridge bridge)
            throws StateAccessException, SerializationException {
    }

    public boolean portsExists(UUID id) throws StateAccessException {
        return true;
    }

    public UUID portsCreate(Port<?, ?> port)
            throws StateAccessException, SerializationException {
        return null;
    }

    public void portsDelete(UUID id)
            throws StateAccessException, SerializationException {
    }

    public Port<?, ?> portsGet(UUID id)
            throws StateAccessException, SerializationException {
        return null;
    }

    public void portsUpdate(Port<?,?> port)
            throws StateAccessException, SerializationException {
    }

}
