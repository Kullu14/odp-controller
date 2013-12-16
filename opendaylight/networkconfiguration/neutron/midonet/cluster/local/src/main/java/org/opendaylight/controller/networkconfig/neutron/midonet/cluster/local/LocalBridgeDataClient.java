/*
 * Copyright 2012 Midokura Europe SARL
 * Copyright 2013 Midokura Pte Ltd
 */
package org.opendaylight.controller.networkconfig.neutron.midonet.cluster.local;

import java.util.UUID;

import org.opendaylight.controller.networkconfig.neutron.midonet.cluster.BridgeDataClient;
import org.opendaylight.controller.networkconfig.neutron.midonet.cluster.StateAccessException;
import org.opendaylight.controller.networkconfig.neutron.midonet.cluster.data.Bridge;


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
            bridge.setNetworkUUID(UUID.randomUUID());
            return bridge.getNetworkUUID();
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
