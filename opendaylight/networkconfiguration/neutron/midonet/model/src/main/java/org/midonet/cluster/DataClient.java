/*
 * Copyright 2012 Midokura Europe SARL
 * Copyright 2013 Midokura Pte Ltd
 */
package org.midonet.cluster;

import java.util.UUID;

import org.midonet.midolman.serialization.SerializationException;
import org.midonet.midolman.state.StateAccessException;
import org.midonet.cluster.data.*;

public interface DataClient {

    /* Bridges related methods */
    boolean bridgeExists(UUID id)
            throws StateAccessException;

    Bridge bridgesGet(UUID id)
            throws StateAccessException, SerializationException;

    void bridgesDelete(UUID id)
            throws StateAccessException, SerializationException;

    UUID bridgesCreate(Bridge bridge)
            throws StateAccessException, SerializationException;

    Bridge bridgesGetByName(String tenantId, String name)
            throws StateAccessException, SerializationException;

    void bridgesUpdate(Bridge bridge)
            throws StateAccessException, SerializationException;

    /* Ports related methods */
    boolean portsExists(UUID id) throws StateAccessException;

    UUID portsCreate(Port<?, ?> port)
            throws StateAccessException, SerializationException;

    void portsDelete(UUID id)
            throws StateAccessException, SerializationException;

    Port<?, ?> portsGet(UUID id)
            throws StateAccessException, SerializationException;

    void portsUpdate(Port<?,?> port)
            throws StateAccessException, SerializationException;

}
