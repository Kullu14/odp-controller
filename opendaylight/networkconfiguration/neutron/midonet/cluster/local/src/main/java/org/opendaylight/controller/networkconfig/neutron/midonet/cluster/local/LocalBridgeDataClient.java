/*
 * Copyright (c) 2013 Midokura Europe SARL, All Rights Reserved.
 */

package org.opendaylight.controller.networkconfig.neutron.midonet.cluster.local;

import java.util.UUID;

import org.midonet.cluster.DataClient;
import org.midonet.cluster.data.Bridge;
import org.midonet.midolman.ZkCluster;
import org.opendaylight.controller.networkconfig.neutron.midonet.cluster.BridgeDataClient;
import org.opendaylight.controller.networkconfig.neutron.midonet.cluster.StateAccessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class LocalBridgeDataClient implements BridgeDataClient {
    private static final Logger logger = LoggerFactory.getLogger(LocalBridgeDataClient.class);

    public boolean bridgeExists(UUID id) throws StateAccessException {
        return false;
    }

    public Bridge bridgesGet(UUID id) throws StateAccessException {
        return null;
    }

    public void bridgesDelete(UUID id) throws StateAccessException {
    }

    public UUID bridgesCreate(Bridge bridge) throws StateAccessException {
        UUID bridgeUuid = null;
        if (bridge != null) {
            DataClient dataClient = ZkCluster.getClusterClient();
            try {
                dataClient.bridgesCreate(bridge);
                bridgeUuid = bridge.getId();
                logger.debug("Successfully created a bridge.");
            } catch (Exception e) {
                logger.warn("Failed to create a bridge. {}", e);
            }
        }
        return bridgeUuid;
    }

    public Bridge bridgesGetByName(String tenantId, String name)
            throws StateAccessException {
        return null;
    }

    public void bridgesUpdate(Bridge bridge) throws StateAccessException {
        return;
    }
}
