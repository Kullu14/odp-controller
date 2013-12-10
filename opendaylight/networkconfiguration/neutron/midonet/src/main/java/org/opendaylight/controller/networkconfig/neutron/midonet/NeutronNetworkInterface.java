/*
 * Copyright IBM Corporation, 2013.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.controller.networkconfig.neutron.midonet;

import java.util.ArrayList;
import java.util.List;

import org.opendaylight.controller.networkconfig.neutron.INeutronNetworkAware;
import org.opendaylight.controller.networkconfig.neutron.INeutronNetworkCRUD;
import org.opendaylight.controller.networkconfig.neutron.NeutronNetwork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NeutronNetworkInterface implements INeutronNetworkCRUD,
        INeutronNetworkAware {
    private static final Logger logger = LoggerFactory.getLogger(NeutronNetworkInterface.class);

    @Override
    public boolean networkExists(String uuid) {
        logger.debug("Entering midonet.NeutronNetworkInterface.networkExists.");
        return true;
    }

    @Override
    public NeutronNetwork getNetwork(String uuid) {
        logger.debug("Entering midonet.NeutronNetworkInterface.getNetwork.");
        return null;
    }

    @Override
    public List<NeutronNetwork> getAllNetworks() {
        logger.debug("Entering midonet.NeutronNetworkInterface.getAllNetworks");
        List<NeutronNetwork> ans = new ArrayList<NeutronNetwork>();
        NeutronNetwork network0 = new NeutronNetwork();
        network0.setNetworkName("foo");
        NeutronNetwork network1 = new NeutronNetwork();
        network1.setNetworkName("bar");
        ans.add(network0);
        ans.add(network1);
        return ans;
    }

    @Override
    public boolean addNetwork(NeutronNetwork input) {
        logger.debug("Entering midonet.NeutronNetworkInterface.addNetwork");
        return true;
    }

    @Override
    public boolean removeNetwork(String uuid) {
        logger.debug("Entering midonet.NeutronNetworkInterface.removeNetwork");
        return true;
    }

    @Override
    public boolean updateNetwork(String uuid, NeutronNetwork delta) {
        logger.debug("Entering midonet.NeutronNetworkInterface.updateNetwork");
        return true;
    }

    @Override
    public boolean networkInUse(String netUUID) {
        logger.debug("Entering midonet.NeutronNetworkInterface.networkInUse");
        return true;
    }

    @Override
    public int canCreateNetwork(NeutronNetwork network) {
        logger.debug(
                "Entering midonet.NeutronNetworkInterface.canCreateNetwork");
        return 200;
    }

    @Override
    public void neutronNetworkCreated(NeutronNetwork network) {
        logger.debug("Entering midonet.NeutronNetworkInterface" +
                     ".neutronNetworkCreated");
    }

    @Override
    public int canUpdateNetwork(NeutronNetwork delta, NeutronNetwork original) {
        logger.debug(
                "Entering midonet.NeutronNetworkInterface.canUpdateNetwork");
        return 200;
    }

    @Override
    public void neutronNetworkUpdated(NeutronNetwork network) {
        logger.debug("Entering midonet.NeutronNetworkInterface" +
                     ".neutronNetworkUpdated");
    }

    @Override
    public int canDeleteNetwork(NeutronNetwork network) {
        logger.debug(
                "Entering midonet.NeutronNetworkInterface.canDeleteNetwork");
        return 200;
    }

    @Override
    public void neutronNetworkDeleted(NeutronNetwork network) {
        logger.debug("Entering midonet.NeutronNetworkInterface" +
                     ".neutronNetworkDeleted");
    }
}
