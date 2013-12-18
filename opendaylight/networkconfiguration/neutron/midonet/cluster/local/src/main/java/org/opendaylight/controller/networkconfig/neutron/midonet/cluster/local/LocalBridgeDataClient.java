/*
 * Copyright (c) 2013 Midokura Europe SARL, All Rights Reserved.
 */

package org.opendaylight.controller.networkconfig.neutron.midonet.cluster.local;

import java.util.UUID;

import org.midonet.cluster.data.Bridge;
import org.opendaylight.controller.networkconfig.neutron.midonet.cluster.BridgeDataClient;
import org.opendaylight.controller.networkconfig.neutron.midonet.cluster.StateAccessException;


public class LocalBridgeDataClient implements BridgeDataClient {

    public boolean bridgeExists(UUID id) throws StateAccessException {
        return false;
    }

    public Bridge bridgesGet(UUID id) throws StateAccessException {
        return null;
    }

    public void bridgesDelete(UUID id) throws StateAccessException {
    }

    public UUID bridgesCreate(Bridge bridge) throws StateAccessException {
        if (bridge != null) {
            bridge.setId(UUID.randomUUID());
            return bridge.getId();
        }
        return null;
    }

    public Bridge bridgesGetByName(String tenantId, String name)
            throws StateAccessException {
        return null;
    }

    public void bridgesUpdate(Bridge bridge) throws StateAccessException {
        return;
    }
}
