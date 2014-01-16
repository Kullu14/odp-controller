/*
 * Copyright (c) 2013 Midokura Europe SARL, All Rights Reserved.
 */

package org.opendaylight.controller.networkconfig.neutron.midonet;

import org.apache.felix.dm.Component;
import org.opendaylight.controller.networkconfig.neutron.INeutronNetworkAware;
import org.opendaylight.controller.networkconfig.neutron.INeutronNetworkCRUD;
import org.opendaylight.controller.networkconfig.neutron.INeutronPortCRUD;
import org.opendaylight.controller.networkconfig.neutron.INeutronSubnetCRUD;
import org.opendaylight.controller.sal.core.ComponentActivatorAbstractBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Activator extends ComponentActivatorAbstractBase {
    protected static final Logger logger = LoggerFactory
    .getLogger(Activator.class);

    /**
     * Function called when the activator starts just after some
     * initializations are done by the
     * ComponentActivatorAbstractBase.
     *
     */
    public void init() {

    }

    /**
     * Function called when the activator stops just before the
     * cleanup done by ComponentActivatorAbstractBase
     *
     */
    public void destroy() {

    }

    /**
     * Function that is used to communicate to dependency manager the
     * list of known implementations for services inside a container
     *
     *
     * @return An array containing all the CLASS objects that will be
     * instantiated in order to get an fully working implementation
     * Object
     */
    public Object[] getImplementations() {
        Object[] res = {
                MidoNetNetworkAware.class,
                MidoNetNetworkCRUD.class,
                MidoNetPortCRUD.class,
                MidoNetSubnetCRUD.class
                };
        return res;
    }

    /**
     * Function that is called when configuration of the dependencies
     * is required.
     *
     * @param c dependency manager Component object, used for
     * configuring the dependencies exported and imported
     * @param imp Implementation class that is being configured,
     * needed as long as the same routine can configure multiple
     * implementations
     * @param containerName The containerName being configured, this allow
     * also optional per-container different behavior if needed, usually
     * should not be the case though.
     */
    public void configureInstance(Component c,
                                  Object imp,
                                  String containerName) {
        if (imp.equals(MidoNetNetworkAware.class)) {
            c.setInterface(
                    new String[] { INeutronNetworkAware.class.getName()}, null);
        }
        if (imp.equals(MidoNetNetworkCRUD.class)) {
            c.setInterface(
                    new String[] { INeutronNetworkCRUD.class.getName()}, null);
        }
        if (imp.equals(MidoNetPortCRUD.class)) {
            c.setInterface(
                    new String[] { INeutronPortCRUD.class.getName()}, null);
        }
        if (imp.equals(MidoNetSubnetCRUD.class)) {
            c.setInterface(
                    new String[] { INeutronSubnetCRUD.class.getName()}, null);
        }
    }
}
