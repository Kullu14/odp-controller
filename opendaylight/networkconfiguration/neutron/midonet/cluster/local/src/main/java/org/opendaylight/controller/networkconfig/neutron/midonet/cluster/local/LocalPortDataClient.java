/*
 * Copyright (c) 2013 Midokura Europe SARL, All Rights Reserved.
 */

package org.opendaylight.controller.networkconfig.neutron.midonet.cluster.local;

import java.util.UUID;

import org.midonet.cluster.DataClient;
import org.midonet.cluster.data.Port;
import org.midonet.midolman.ZkCluster;
import org.opendaylight.controller.networkconfig.neutron.midonet.cluster.PortDataClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class LocalPortDataClient implements PortDataClient {
    private static final Logger logger =
            LoggerFactory.getLogger(LocalPortDataClient.class);
    private DataClient dataClient = null;

    public LocalPortDataClient() {
        logger.debug("LocalPortDataClient constructor was called.");
    }

    private void ensureDataClient() {
        if (this.dataClient == null) {
            synchronized (this) {
                if (this.dataClient == null) {
                    this.dataClient = ZkCluster.getClusterClient();
                }
            }
        }
    }
    public boolean portsExists(UUID id) {
        return false;
    }

    public UUID portsCreate(final Port<?,?> port) {
        logger.debug("Creating a port for device id = {}", port.getDeviceId());
        this.ensureDataClient();
        UUID portId = null;
        try {
            portId = this.dataClient.portsCreate(port);
        } catch (Exception e) {
            logger.warn("Failed to create a port: {}", e);
        }
        logger.debug("Created a port: {}", portId);
        return portId;
    }

    public void portsDelete(UUID id) {
        return;
    }

    public Port<?,?> portsGet(UUID id) {
        return null;
    }

    @Override
    public void portsUpdate(Port<?,?> port) {
        return;
    }
}
