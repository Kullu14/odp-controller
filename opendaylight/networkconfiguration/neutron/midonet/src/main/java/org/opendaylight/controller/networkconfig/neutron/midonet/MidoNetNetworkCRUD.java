/*
 * Copyright (c) 2014 Midokura Europe SARL, All Rights Reserved.
 */

package org.opendaylight.controller.networkconfig.neutron.midonet;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.midonet.cluster.data.Bridge;
import org.opendaylight.controller.networkconfig.neutron.INeutronNetworkCRUD;
import org.opendaylight.controller.networkconfig.neutron.NeutronNetwork;
import org.opendaylight.controller.networkconfig.neutron.midonet.cluster.BridgeDataClient;
import org.opendaylight.controller.sal.utils.ServiceHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MidoNetNetworkCRUD implements INeutronNetworkCRUD {
    private static final Logger logger = LoggerFactory.getLogger(MidoNetNetworkCRUD.class);

    /**
     * Looks up a MidoNet DataClient service and returns it if one is available.
     * @return A BridgeDataClient service if one is available, and null if not.
     */
    private BridgeDataClient lookUpDataClient() {
        Object service = ServiceHelper.getGlobalInstance(
                BridgeDataClient.class, this);

        if (service == null) {
            logger.warn("Failed to look up BridgeDataClient impl.");
            return null;
        }

        logger.debug("Found a BridgeDataClient impl.");
        return (BridgeDataClient) service;
    }

    private void populateNeutronNetwork(Bridge bridge, NeutronNetwork network) {
        network.setNetworkName(bridge.getName());
        network.setNetworkUUID(bridge.getId().toString());
        network.setTenantID(bridge.getProperty(Bridge.Property.tenant_id));
        // TODO(tomohiko) Copy more fields.
    }

    private void populateBridge(NeutronNetwork network, Bridge bridge) {
        String networkId = network.getID();
        if (networkId != null && !networkId.isEmpty()) {
            bridge.setId(UUID.fromString(networkId));
        }
        bridge.setName(network.getNetworkName());
        bridge.setProperty(Bridge.Property.tenant_id, network.getTenantID());
        // TODO(tomohiko) Copy more fields.
    }

    private void populateDelta(NeutronNetwork delta, Bridge bridge) {
        bridge.setName(delta.getNetworkName());
        // TODO(tomohiko) Copy more fields.
    }

    @Override
    public boolean networkExists(String uuid) {
        if (uuid == null || uuid.isEmpty()) {
            return false;
        }
        BridgeDataClient dataClient = this.lookUpDataClient();
        if (dataClient == null) {
            return false;
        }

        return dataClient.bridgeExists(UUID.fromString(uuid));
    }

    @Override
    public NeutronNetwork getNetwork(String uuid) {
        NeutronNetwork network = new NeutronNetwork();
        BridgeDataClient dataClient = this.lookUpDataClient();
        if (dataClient == null) {
            return network;
        }

        Bridge bridge = dataClient.bridgesGet(UUID.fromString(uuid));
        if (bridge != null) {
            populateNeutronNetwork(bridge, network);
        }
        return network;
    }

    @Override
    public List<NeutronNetwork> getAllNetworks() {
        BridgeDataClient dataClient = this.lookUpDataClient();
        if (dataClient == null) {
            return new ArrayList<NeutronNetwork>();
        }
        List<Bridge> bridges = dataClient.bridgesGetAll();

        List<NeutronNetwork> neutronNetworks = new ArrayList<NeutronNetwork>();
        if (bridges != null) {
            for (Bridge bridge : bridges) {
                NeutronNetwork network = new NeutronNetwork();
                populateNeutronNetwork(bridge, network);
                neutronNetworks.add(network);
            }
        }
        return neutronNetworks;
    }

    @Override
    public boolean addNetwork(NeutronNetwork input) {
        BridgeDataClient dataClient = this.lookUpDataClient();
        if (dataClient == null) {
            return false;
        }

        Bridge bridge = new Bridge();
        populateBridge(input, bridge);
        UUID bridgeId = dataClient.bridgesCreate(bridge);
        if (bridgeId == null) {
            logger.warn("Failed to create a network.");
            return false;
        }

        input.setNetworkUUID(bridgeId.toString());
        logger.info("Created a network: ID={}", input.getNetworkUUID());
        return true;
    }

    @Override
    public boolean removeNetwork(String uuid) {
        BridgeDataClient dataClient = this.lookUpDataClient();
        if (dataClient == null) {
            return false;
        }

        dataClient.bridgesDelete(UUID.fromString(uuid));
        return true;
    }

    @Override
    public boolean updateNetwork(String uuid, NeutronNetwork delta) {
        BridgeDataClient dataClient = this.lookUpDataClient();
        if (dataClient == null) {
            return false;
        }

        Bridge bridge = dataClient.bridgesGet(UUID.fromString(uuid));
        if (bridge == null) {
            return false;
        }

        populateDelta(delta, bridge);
        return dataClient.bridgesUpdate(bridge);
    }

    @Override
    public boolean networkInUse(String netUUID) {
        // Not implemented.
        return false;
    }
}
