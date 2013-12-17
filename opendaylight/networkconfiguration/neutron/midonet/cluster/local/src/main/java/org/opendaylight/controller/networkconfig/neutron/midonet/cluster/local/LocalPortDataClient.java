/*
 * Copyright (c) 2013 Midokura Europe SARL, All Rights Reserved.
 */

package org.opendaylight.controller.networkconfig.neutron.midonet.cluster.local;

import java.util.UUID;

import org.opendaylight.controller.networkconfig.neutron.midonet.cluster.PortDataClient;
import org.opendaylight.controller.networkconfig.neutron.midonet.cluster.StateAccessException;
import org.opendaylight.controller.networkconfig.neutron.midonet.cluster.data.Port;


public class LocalPortDataClient implements PortDataClient {
    public boolean portsExists(UUID id) throws StateAccessException {
        return false;
    }

    public UUID portsCreate(Port port) throws StateAccessException {
        return null;
    }

    public void portsDelete(UUID id) throws StateAccessException {
        return;
    }

    public Port portsGet(UUID id) throws StateAccessException {
        return null;
    }

    public void portsUpdate(Port port) throws StateAccessException {
        return;
    }
}
