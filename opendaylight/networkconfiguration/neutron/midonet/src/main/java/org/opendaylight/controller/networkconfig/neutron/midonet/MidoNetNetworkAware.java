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

    /**
     * Looks up a MidoNet DataClient service sets it to dataClient.
     * @return True if it finds the service, and false otherwise.
     */
    private boolean lookUpDataClient() {
        this.dataClient = null;
        Object service = ServiceHelper.getGlobalInstance(
                BridgeDataClient.class, this);

        if (service == null) {
            logger.warn("Failed to look up BridgeDataClient impl.");
            return false;
        }

        logger.debug("Found a BridgeDataClient impl.");
        this.dataClient = (BridgeDataClient) service;
        return true;
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

        this.lookUpDataClient();
        if (this.dataClient == null) {
            logger.warn("Returning 503/SERVICE UNAVAILABLE. Cannot look up " +
                        "BridgeDataClient impl.");
            return 503;  // SERVICE UNAVAILABLE
        }

        try {
            if (this.dataClient.bridgesGetByName(
                    network.getTenantID(), network.getNetworkName()) != null) {
                logger.warn("Returning 400/BAD REQUEST. Tenant ID / network " +
                            "must be unique.");
                return 400;  // BAD REQUEST
            }
            logger.debug("No bridge with tenant ID / name: {} / {}",
                         network.getTenantID(), network.getNetworkName());
        } catch (Exception e) {
            logger.warn("Failed to get a bridge by tenant ID / name: {}, {}",
                        network.getTenantID(), network.getNetworkName());
            return 500;  // SERVER INTERNAL ERROR
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
        this.lookUpDataClient();
        if (this.dataClient == null) {
            logger.warn("Returning 503/SERVICE UNAVAILABLE. Cannot look up " +
                        "BridgeDataClient impl.");
            return 503;  // SERVICE UNAVAILABLE
        }
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
        this.lookUpDataClient();
        if (this.dataClient == null) {
            logger.warn("Returning 503/SERVICE UNAVAILABLE. Cannot look up " +
                        "BridgeDataClient impl.");
            return 503;  // SERVICE UNAVAILABLE
        }
        return 200;
    }

    @Override
    public void neutronNetworkDeleted(NeutronNetwork network) {
        logger.debug("MidonetNetworkHandler.neutronNetworkDeleted: " +
                     network.getID());
    }
}
