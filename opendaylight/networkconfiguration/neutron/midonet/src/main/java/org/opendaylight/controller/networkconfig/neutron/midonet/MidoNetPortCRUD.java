/*
 * Copyright (c) 2014 Midokura Europe SARL, All Rights Reserved.
 */
package org.opendaylight.controller.networkconfig.neutron.midonet;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public boolean portExists(String uuid) {
        logger.debug("MidoNetPortCRUD.portExists was called. uuid={}", uuid);
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public NeutronPort getPort(String uuid) {
        logger.debug("MidoNetPortCRUD.getPort was called. uuid={}", uuid);
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<NeutronPort> getAllPorts() {
        logger.debug("MidoNetPortCRUD.getAllPorts was called.");
        // TODO Auto-generated method stub
        return new ArrayList<NeutronPort>();
    }

    @Override
    public boolean addPort(NeutronPort input) {
        logger.debug("MidoNetPortCRUD.addPort was called. tenant_id={}, " +
                     "network_id={}",
                     input.getTenantID(),
                     input.getNetworkUUID());
        PortDataClient dataClient = this.lookUpDataClient();
        // TODO Implement port data construction.
        dataClient.portsCreate(null);
        return false;
    }

    @Override
    public boolean removePort(String uuid) {
        logger.debug("MidoNetPortCRUD.removePort was called: uuid={}",
                     uuid);
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean updatePort(String uuid, NeutronPort delta) {
        logger.debug("MidoNetPortCRUD.updatePort was called: " +
                     "uuid={}", uuid);
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean macInUse(String macAddress) {
        logger.debug("MidoNetPortCRUD.macInUse was called: mac={}", macAddress);
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public NeutronPort getGatewayPort(String subnetUUID) {
        logger.debug("MidoNetPortCRUD.getGatewayPort was called: subnet " +
                     "uuid={}", subnetUUID);
        // TODO Auto-generated method stub
        return null;
    }

}
