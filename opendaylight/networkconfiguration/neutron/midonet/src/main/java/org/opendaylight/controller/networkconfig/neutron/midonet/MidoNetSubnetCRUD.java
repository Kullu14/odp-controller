/*
 * Copyright (c) 2014 Midokura Europe SARL, All Rights Reserved.
 */
package org.opendaylight.controller.networkconfig.neutron.midonet;

import java.util.List;

import org.opendaylight.controller.networkconfig.neutron.INeutronSubnetCRUD;
import org.opendaylight.controller.networkconfig.neutron.NeutronSubnet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MidoNetSubnetCRUD implements INeutronSubnetCRUD {
    private static final Logger logger = LoggerFactory.getLogger(MidoNetSubnetCRUD.class);

    @Override
    public boolean subnetExists(String uuid) {
        logger.debug("MidoNetSubnetCRUD.subnetExists was called. uuid={}",
                     uuid);
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public NeutronSubnet getSubnet(String uuid) {
        logger.debug("MidoNetSubnetCRUD.getSubnet was called. uuid={}",
                     uuid);
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<NeutronSubnet> getAllSubnets() {
        logger.debug("MidoNetSubnetCRUD.getAllSubnets was called.");
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean addSubnet(NeutronSubnet input) {
        logger.debug("MidoNetSubnetCRUD.addSubnet was called. tenant_id={}, " +
                     "network_id={}",
                     input.getTenantID(),
                     input.getNetworkUUID());
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean removeSubnet(String uuid) {
        logger.debug("MidoNetSubnetCRUD.removeSubnet was called. uuid={}",
                     uuid);
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean updateSubnet(String uuid, NeutronSubnet delta) {
        logger.debug("MidoNetSubnetCRUD.updateSubnet was called. uuid={}",
                     uuid);
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean subnetInUse(String subnetUUID) {
        logger.debug("MidoNetSubnetCRUD.subnetInUse was called. uuid={}",
                     subnetUUID);
        // TODO Auto-generated method stub
        return false;
    }

}
