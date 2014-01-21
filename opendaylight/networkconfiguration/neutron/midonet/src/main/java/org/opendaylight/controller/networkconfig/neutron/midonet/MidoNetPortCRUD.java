/*
 * Copyright (c) 2014 Midokura Europe SARL, All Rights Reserved.
 */
package org.opendaylight.controller.networkconfig.neutron.midonet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.midonet.cluster.data.Port;
import org.midonet.cluster.data.ports.BridgePort;
import org.opendaylight.controller.networkconfig.neutron.INeutronPortCRUD;
import org.opendaylight.controller.networkconfig.neutron.NeutronPort;
import org.opendaylight.controller.networkconfig.neutron.midonet.cluster.PortDataClient;
import org.opendaylight.controller.sal.utils.ServiceHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MidoNetPortCRUD implements INeutronPortCRUD {
    private static final Logger logger = LoggerFactory.getLogger(MidoNetPortCRUD.class);

    /**
     * Looks up a MidoNet DataClient service and returns it if one is available.
     * @return A PortDataClient service if one is available, and null if not.
     */
    private PortDataClient lookUpDataClient() {
        Object service = ServiceHelper.getGlobalInstance(
                PortDataClient.class, this);

        if (service == null) {
            logger.warn("Failed to look up PortDataClient impl.");
            return null;
        }

        logger.debug("Found a PortDataClient impl.");
        return (PortDataClient) service;
    }

    private void populateNeutronPort(Port<?, ?> midoNetPort,
                                     UUID tenantId,
                                     NeutronPort neutronPort) {
        neutronPort.setNetworkUUID(midoNetPort.getDeviceId().toString());
        neutronPort.setPortUUID(midoNetPort.getId().toString());
        if (tenantId != null) {
            neutronPort.setTenantID(tenantId.toString());
        }
        neutronPort.setAdminStateUp(midoNetPort.isAdminStateUp());
        // TODO(tomohiko) Copy more fields.
    }

    private void populateMidoNetPort(NeutronPort neutronPort,
                                     Port<?, ?> midoNetPort) {
        midoNetPort.setDeviceId(UUID.fromString(neutronPort.getNetworkUUID()));
        midoNetPort.setAdminStateUp(neutronPort.isAdminStateUp());
        // TODO(tomohiko) Copy more fields.
    }

    private void populateNeutronPortDelta(NeutronPort delta,
                                          Port<?, ?> midoNetPort) {
        midoNetPort.setAdminStateUp(delta.isAdminStateUp());
        // TODO(tomohiko) Copy more fields.
    }

    @Override
    public boolean portExists(String uuid) {
        logger.debug("MidoNetPortCRUD.portExists was called. uuid={}", uuid);
        PortDataClient dataClient = this.lookUpDataClient();
        if (dataClient != null) {
            return dataClient.portsExists(UUID.fromString(uuid));
        }
        return false;
    }

    @Override
    public NeutronPort getPort(String uuid) {
        logger.debug("MidoNetPortCRUD.getPort was called. uuid={}", uuid);
        NeutronPort neutronPort = null;
        PortDataClient dataClient = this.lookUpDataClient();
        if (dataClient != null) {
            Port<?, ?> midoNetPort = dataClient.portsGet(UUID.fromString(uuid));
            if (midoNetPort != null) {
                neutronPort = new NeutronPort();
                // TODO(tomohiko) Retrieve and pass tenant ID.
                populateNeutronPort(midoNetPort, null, neutronPort);
            }
        }
        return neutronPort;
    }

    @Override
    public List<NeutronPort> getAllPorts() {
        logger.debug("MidoNetPortCRUD.getAllPorts was called.");
        PortDataClient dataClient = this.lookUpDataClient();
        List<NeutronPort> neutronPorts = new ArrayList<NeutronPort>();
        if (dataClient != null) {
            Map<UUID, List<Port<?, ?>>> tenantPortsList =
                    dataClient.portsGetAll();
            for (Map.Entry<UUID, List<Port<?, ?>>> tenantEntry :
                 tenantPortsList.entrySet()) {
                UUID tenantId = tenantEntry.getKey();
                for (Port<?, ?> port : tenantEntry.getValue()) {
                    NeutronPort neutronPort = new NeutronPort();
                    this.populateNeutronPort(port, tenantId, neutronPort);
                    neutronPorts.add(neutronPort);
                }
            }
        }
        return neutronPorts;
    }

    @Override
    public boolean addPort(NeutronPort input) {
        logger.debug("MidoNetPortCRUD.addPort was called. tenant_id={}, " +
                     "network_id={}",
                     input.getTenantID(),
                     input.getNetworkUUID());
        PortDataClient dataClient = this.lookUpDataClient();
        UUID portId = null;
        if (dataClient != null) {
            BridgePort bridgePort = new BridgePort();
            this.populateMidoNetPort(input, bridgePort);
            portId = dataClient.portsCreate(bridgePort);
        }
        return portId != null;
    }

    @Override
    public boolean removePort(String uuid) {
        logger.debug("MidoNetPortCRUD.removePort was called: uuid={}",
                     uuid);
        PortDataClient dataClient = this.lookUpDataClient();
        if (dataClient == null) {
            return false;
        }
        dataClient.portsDelete(UUID.fromString(uuid));
        return true;
    }

    @Override
    public boolean updatePort(String uuid, NeutronPort delta) {
        logger.debug("MidoNetPortCRUD.updatePort was called: " +
                     "uuid={}", uuid);
        PortDataClient dataClient = this.lookUpDataClient();
        boolean updated = false;
        if (dataClient != null) {
            // TODO(tomohiko) Need to look up the device to see if it's really
            // a bridge or router.
            Port<?, ?> port = dataClient.portsGet(UUID.fromString(uuid));
            if (port == null) {
                logger.debug("The port to be updated did not exist.");
            } else {
                this.populateNeutronPortDelta(delta, port);
                updated = dataClient.portsUpdate(port);
                logger.debug("Updated the port: {}", updated);
            }
        }
        return updated;
    }

    @Override
    public boolean macInUse(String macAddress) {
        logger.debug("MidoNetPortCRUD.macInUse was called: mac={}", macAddress);
        // Not implemented.
        return false;
    }

    @Override
    public NeutronPort getGatewayPort(String subnetUUID) {
        logger.debug("MidoNetPortCRUD.getGatewayPort was called: subnet " +
                     "uuid={}", subnetUUID);
        // Not implemented.
        return null;
    }

}
