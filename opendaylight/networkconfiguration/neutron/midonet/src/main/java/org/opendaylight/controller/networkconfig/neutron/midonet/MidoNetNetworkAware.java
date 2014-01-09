/*
 * Copyright (c) 2013 Midokura Europe SARL, All Rights Reserved.
 */

package org.opendaylight.controller.networkconfig.neutron.midonet;

import org.opendaylight.controller.networkconfig.neutron.INeutronNetworkAware;
import org.opendaylight.controller.networkconfig.neutron.NeutronNetwork;
import org.opendaylight.controller.networkconfig.neutron.midonet.cluster.BridgeDataClient;
import org.opendaylight.controller.sal.utils.ServiceHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MidoNetNetworkAware implements INeutronNetworkAware {
    private static final Logger logger = LoggerFactory.getLogger(MidoNetNetworkAware.class);

    BridgeDataClient dataClient;

    public MidoNetNetworkAware() {
    }

    @Override
    public int canCreateNetwork(NeutronNetwork network) {
        logger.debug("MidonetNetworkHandler.canCreateNetwork: ID={}, name={}",
                     network.getID(), network.getNetworkName());
        if (network.getNetworkName() == null ||
            network.getNetworkName().isEmpty()) {
            logger.warn("Returning 400/BAD REQUEST. Network name cannot be " +
                        "empty.");
            return 400;  // BAD REQUEST
        }

        Object service = ServiceHelper.getGlobalInstance(
                BridgeDataClient.class, this);
        if (service == null) {
            logger.warn("Returning 503/SERVICE UNAVAILABLE. Cannot look up " +
                        "BridgeDataClient impl.");
            return 503;  // SERVICE UNAVAILABLE
        }

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
