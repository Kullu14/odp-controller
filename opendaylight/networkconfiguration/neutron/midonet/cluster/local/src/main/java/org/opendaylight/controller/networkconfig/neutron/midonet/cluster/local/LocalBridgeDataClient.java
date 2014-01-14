/*
 * Copyright (c) 2013 Midokura Europe SARL, All Rights Reserved.
 */

package org.opendaylight.controller.networkconfig.neutron.midonet.cluster.local;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.midonet.cluster.DataClient;
import org.midonet.cluster.data.Bridge;
import org.midonet.midolman.ZkCluster;
import org.opendaylight.controller.networkconfig.neutron.midonet.cluster.BridgeDataClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class LocalBridgeDataClient implements BridgeDataClient {
    private static final Logger logger = LoggerFactory.getLogger(LocalBridgeDataClient.class);
    private volatile DataClient dataClient = null;

    private void ensureDataClient() {
        if (this.dataClient == null) {
            synchronized (this) {
                if (this.dataClient == null) {
                    this.dataClient = ZkCluster.getClusterClient();
                }
            }
        }
    }

    public boolean bridgeExists(UUID bridgeId) {
        this.ensureDataClient();
        try {
            return this.dataClient.bridgeExists(bridgeId);
        } catch (Exception e) {
            logger.warn("Failed to tests existance of a bridge by ID. {}", e);
        }
        return false;
    }

    public Bridge bridgesGet(UUID bridgeId) {
        this.ensureDataClient();
        Bridge bridge = null;
        try {
            bridge = this.dataClient.bridgesGet(bridgeId);
        } catch (Exception e) {
            logger.warn("Failed to retrieve a bridge with a given ID. {}", e);
        }
        return bridge;
    }

    public void bridgesDelete(UUID bridgeId) {
        this.ensureDataClient();
        try {
            this.dataClient.bridgesDelete(bridgeId);
        } catch (Exception e) {
            logger.warn("Failed to delete a bridge with a given ID. {}", e);
        }
    }

    public UUID bridgesCreate(Bridge bridge) {
        UUID bridgeUuid = null;
        if (bridge != null) {
            this.ensureDataClient();
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

    public Bridge bridgesGetByName(String tenantId, String name) {
        Bridge bridge = null;
        this.ensureDataClient();
        try {
            bridge = this.dataClient.bridgesGetByName(tenantId, name);
        } catch (Exception e) {
            logger.warn("Failed to get a bridge by tenant_id / name, {}", e);
        }
        return bridge;
    }

    public boolean bridgesUpdate(Bridge bridge) {
        this.ensureDataClient();
        try {
            this.dataClient.bridgesUpdate(bridge);
        } catch (Exception e) {
            logger.warn("Failed to update a bridge, {}", e);
            return false;
        }
        return true;
    }

    public List<Bridge> bridgesGetAll() {
        this.ensureDataClient();
        List<Bridge> bridges = null;
        try {
            bridges = this.dataClient.bridgesGetAll();
            logger.debug("Successfully retrieved all bridges with size {}.",
                         bridges.size());
        } catch (Exception e) {
            logger.warn("Failed to list all bridges. {}", e);
            bridges = new ArrayList<Bridge>();
        }
        return bridges;
    }
}
