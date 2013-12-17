/*
 * Copyright (c) 2013 Midokura Europe SARL, All Rights Reserved.
 */

package org.opendaylight.controller.networkconfig.neutron.midonet;

import org.opendaylight.controller.networkconfig.neutron.INeutronNetworkAware;
import org.opendaylight.controller.networkconfig.neutron.NeutronNetwork;
import org.opendaylight.controller.networkconfig.neutron.midonet.cluster.BridgeDataClient;
import org.opendaylight.controller.networkconfig.neutron.midonet.cluster.StateAccessException;
import org.opendaylight.controller.networkconfig.neutron.midonet.cluster.data.Bridge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MidonetNetworkHandler implements INeutronNetworkAware {
    private static final Logger logger = LoggerFactory.getLogger(MidonetNetworkHandler.class);

    BridgeDataClient dataClient;

    @Override
    public int canCreateNetwork(NeutronNetwork network) {
        logger.debug("MidonetNetworkHandler.canCreateNetwork: " +
                     network.getID());
        Bridge bridge = null;
        try {
            dataClient.bridgesCreate(bridge);
        } catch (StateAccessException e) {
            logger.error("Failed to create a network.");
            return 500;
        }

        network.setNetworkUUID(bridge.getNetworkUUID().toString());
        return 200;
    }

    @Override
    public void neutronNetworkCreated(NeutronNetwork network) {
        logger.debug("MidonetNetworkHandler.neutronNetworkCreated: " +
                     network.getID());
    }

    @Override
    public int canUpdateNetwork(NeutronNetwork delta, NeutronNetwork original) {
        logger.debug("MidonetNetworkHandler.canUpdateNetwork: " +
                     original.getID());
        return 200;
    }

    @Override
    public void neutronNetworkUpdated(NeutronNetwork network) {
        logger.debug("MidonetNetworkHandler.neutronNetworkUpdated: " +
                     network.getID());
    }

    @Override
    public int canDeleteNetwork(NeutronNetwork network) {
        logger.debug("MidonetNetworkHandler.canDeleteNetwork: " +
                     network.getID());
        return 200;
    }

    @Override
    public void neutronNetworkDeleted(NeutronNetwork network) {
        logger.debug("MidonetNetworkHandler.neutronNetworkDeleted: " +
                     network.getID());
    }
}
