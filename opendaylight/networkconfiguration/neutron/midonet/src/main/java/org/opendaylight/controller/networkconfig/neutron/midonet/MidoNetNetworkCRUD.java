/*
 * Copyright (c) 2014 Midokura Europe SARL, All Rights Reserved.
 */

package org.opendaylight.controller.networkconfig.neutron.midonet;

import java.util.List;

import org.midonet.cluster.data.Bridge;
import org.opendaylight.controller.networkconfig.neutron.INeutronNetworkCRUD;
import org.opendaylight.controller.networkconfig.neutron.NeutronNetwork;
import org.opendaylight.controller.networkconfig.neutron.midonet.cluster.BridgeDataClient;
import org.opendaylight.controller.networkconfig.neutron.midonet.cluster.StateAccessException;
import org.opendaylight.controller.sal.utils.ServiceHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MidoNetNetworkCRUD implements INeutronNetworkCRUD {
    private static final Logger logger = LoggerFactory.getLogger(MidoNetNetworkCRUD.class);

    BridgeDataClient dataClient;

    @Override
    public boolean networkExists(String uuid) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public NeutronNetwork getNetwork(String uuid) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<NeutronNetwork> getAllNetworks() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean addNetwork(NeutronNetwork input) {
        Object service = ServiceHelper.getGlobalInstance(
                BridgeDataClient.class, this);
        if (service != null) {
            logger.debug("Found a BridgeDataClient impl.");
            this.dataClient = (BridgeDataClient) service;
        } else {
            logger.warn("Cannot look up BridgeDataClient impl.");
            return false;
        }

        Bridge bridge = new Bridge();
        bridge.setName(input.getNetworkName());
        try {
            dataClient.bridgesCreate(bridge);
        } catch (StateAccessException e) {
            logger.error("Failed to create a network.");
            return false;
        }
        input.setNetworkUUID(bridge.getId().toString());
        logger.info("Created a network: ID={}", input.getNetworkUUID());

        return true;
    }

    @Override
    public boolean removeNetwork(String uuid) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean updateNetwork(String uuid, NeutronNetwork delta) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean networkInUse(String netUUID) {
        // TODO Auto-generated method stub
        return false;
    }
}
