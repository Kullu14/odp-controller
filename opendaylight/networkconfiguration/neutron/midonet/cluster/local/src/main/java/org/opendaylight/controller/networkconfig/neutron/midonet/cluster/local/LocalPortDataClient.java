/*
 * Copyright (c) 2013 Midokura Europe SARL, All Rights Reserved.
 */

package org.opendaylight.controller.networkconfig.neutron.midonet.cluster.local;

import java.util.UUID;

import org.midonet.cluster.data.Port;
import org.opendaylight.controller.networkconfig.neutron.midonet.cluster.PortDataClient;


public class LocalPortDataClient implements PortDataClient {
    public boolean portsExists(UUID id) {
        return false;
    }

    public UUID portsCreate(Port<?,?> port) {
        return null;
    }

    public void portsDelete(UUID id) {
        return;
    }

    public Port<?,?> portsGet(UUID id) {
        return null;
    }

    @Override
    public void portsUpdate(Port<?,?> port) {
        return;
    }
}
