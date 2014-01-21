/*
 * Copyright (c) 2013 Midokura Europe SARL, All Rights Reserved.
 */

package org.opendaylight.controller.networkconfig.neutron.midonet.cluster.local;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import org.midonet.cluster.DataClient;
import org.midonet.cluster.data.Bridge;
import org.midonet.cluster.data.Port;
import org.midonet.cluster.data.ports.BridgePort;
import org.midonet.midolman.ZkCluster;
import org.mortbay.log.Log;
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
        this.ensureDataClient();
        try {
            return this.dataClient.portsExists(id);
        } catch (Exception e) {
            logger.warn("Failed to test port existence: {}", e);
            return false;
        }
    }

    public Map<UUID, List<Port<?, ?>>> portsGetAll() {
        logger.info("LocalPortDataClient.portsGetAll() was called.");
        this.ensureDataClient();
        List<Bridge> bridges = null;
        try {
            bridges = this.dataClient.bridgesGetAll();
            Log.debug("Retrieved {} bridges.", bridges.size());
        } catch (Exception e) {
            logger.warn("Failed to get all bridges: {}", e);
        }

        Map<UUID, List<Port<?,?>>> tenantPortsList =
                new TreeMap<UUID, List<Port<?, ?>>>();
        if (bridges != null) {
            for (Bridge bridge : bridges) {
                try {
                    List<BridgePort> bridgePorts =
                           this.dataClient.portsFindByBridge(bridge.getId());
                    logger.debug("{} bridge ports for bridge {}.",
                                 bridgePorts.size(),
                                 bridge.getId());

                    String tenantId = bridge.getProperty(
                            Bridge.Property.tenant_id);
                    UUID tenantUuid = UUID.fromString(tenantId);
                    List<Port<?, ?>> tenantPorts =
                            tenantPortsList.get(tenantUuid);
                    if (tenantPorts == null) {
                        tenantPorts = new ArrayList<Port<?, ?>>(bridgePorts);
                        tenantPortsList.put(tenantUuid, tenantPorts);
                    } else {
                        tenantPorts.addAll(bridgePorts);
                    }
                } catch (Exception e) {
                    logger.warn("Failed to get ports by bridge: {}", e);
                }
            }
        }
        return tenantPortsList;
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
        logger.debug("Deleting a port; id = {}", id);
        this.ensureDataClient();
        try {
            this.dataClient.portsDelete(id);
            logger.debug("Deleted a port.");
        } catch (Exception e) {
            logger.warn("Failure in deleting a port: {}", e);
        }
    }

    public Port<?,?> portsGet(UUID id) {
        logger.debug("Getting port info; id = {}", id);
        this.ensureDataClient();
        Port<?, ?> port = null;
        try {
            port = this.dataClient.portsGet(id);
        } catch (Exception e) {
            logger.warn("Failed to get the port info for id = {}", id);
        }
        return port;
    }

    @Override
    public boolean portsUpdate(Port<?,?> port) {
        logger.debug("Updating a port for device id = {}, port id = {}",
                     port.getDeviceId(), port.getId());
        this.ensureDataClient();
        boolean updated = false;
        try {
            this.dataClient.portsUpdate(port);
            logger.warn("Updated a port, id={}", port.getId());
            updated = true;
        } catch (Exception e) {
            logger.warn("Failed to update a port, id={}", port.getId());
        }
        return updated;
    }
}
