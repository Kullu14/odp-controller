/*
 * Copyright (c) 2013 Midokura Europe SARL, All Rights Reserved.
 */

package org.opendaylight.controller.networkconfig.neutron.midonet.cluster;

import java.util.UUID;

import org.opendaylight.controller.networkconfig.neutron.midonet.cluster.data.Bridge;


public interface BridgeDataClient {
    boolean bridgeExists(UUID id) throws StateAccessException;

    Bridge bridgesGet(UUID id) throws StateAccessException;

    void bridgesDelete(UUID id) throws StateAccessException;

    UUID bridgesCreate(Bridge bridge) throws StateAccessException;

    Bridge bridgesGetByName(String tenantId, String name)
            throws StateAccessException;

    void bridgesUpdate(Bridge bridge) throws StateAccessException;
}
