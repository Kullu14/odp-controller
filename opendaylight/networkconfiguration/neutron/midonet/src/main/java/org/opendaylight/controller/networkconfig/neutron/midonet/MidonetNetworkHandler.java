/*
 * Copyright IBM Corporation, 2013.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.controller.networkconfig.neutron.midonet;

import java.util.UUID;

import org.opendaylight.controller.networkconfig.neutron.INeutronNetworkAware;
import org.opendaylight.controller.networkconfig.neutron.NeutronNetwork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MidonetNetworkHandler implements INeutronNetworkAware {
    private static final Logger logger = LoggerFactory.getLogger(MidonetNetworkHandler.class);

    @Override
    public int canCreateNetwork(NeutronNetwork network) {
        logger.debug("MidonetNetworkHandler.canCreateNetwork: " +
                     network.getID());
        network.setNetworkUUID(UUID.randomUUID().toString());
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
