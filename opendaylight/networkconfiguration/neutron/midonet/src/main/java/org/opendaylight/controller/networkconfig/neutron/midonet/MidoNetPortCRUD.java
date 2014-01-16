/*
 * Copyright (c) 2014 Midokura Europe SARL, All Rights Reserved.
 */
package org.opendaylight.controller.networkconfig.neutron.midonet;

import java.util.ArrayList;
import java.util.List;

import org.opendaylight.controller.networkconfig.neutron.INeutronPortCRUD;
import org.opendaylight.controller.networkconfig.neutron.NeutronPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MidoNetPortCRUD implements INeutronPortCRUD {
    private static final Logger logger = LoggerFactory.getLogger(MidoNetPortCRUD.class);

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
        logger.debug("MidoNetPortCRUD.addPort was called.");
        // TODO Auto-generated method stub
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
