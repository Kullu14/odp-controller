package org.opendaylight.controller.networkconfig.neutron.implementation;

import org.opendaylight.controller.networkconfig.neutron.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NeutronSecurityGroupInterface extends AbstractNeutronInterface<NeutronSecurityGroup>
                                           implements INeutronSecurityGroupCRUD {

    protected static final Logger logger = LoggerFactory.getLogger(NeutronSecurityGroupInterface.class);

    @Override
    protected String getCacheName() {
        return "neutronSecurityGroups";
    }

    @Override
    protected Logger getLogger() {
        return logger;
    }

    @Override
    public boolean securityGroupInUse(String uuid) {
        NeutronSecurityGroup target = db.get(uuid);
        return target != null && target.getPorts().size() > 0;
    }

    @Override
    public boolean add(NeutronSecurityGroup input) {
        if (!super.add(input)) {
            return false;
        }

        INeutronSecurityGroupRuleCRUD sgRuleCRUD = NeutronCRUDInterfaces.getNeutronSecurityGroupRuleCRUD(this);
        for (NeutronSecurityGroupRule rule : input.getRules()) {
            if (!sgRuleCRUD.add(rule)) {
                remove(input.getSecGroupUUID());
                return false;
            }
        }

        return true;
    }
}
