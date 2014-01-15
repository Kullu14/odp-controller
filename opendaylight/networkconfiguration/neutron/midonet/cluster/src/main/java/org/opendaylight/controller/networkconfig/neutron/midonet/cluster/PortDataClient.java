/*
 * Copyright (c) 2013 Midokura Europe SARL, All Rights Reserved.
 */
package org.opendaylight.controller.networkconfig.neutron.midonet.cluster;

import java.util.UUID;

import org.midonet.cluster.data.Port;


public interface PortDataClient {
    boolean portsExists(UUID id);

    UUID portsCreate(Port<?,?> port);

    void portsDelete(UUID id);

    Port<?,?> portsGet(UUID id);

    void portsUpdate(Port<?,?> port);
}
