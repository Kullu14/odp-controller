/*
 * Copyright (c) 2013 Midokura Europe SARL, All Rights Reserved.
 */
package org.opendaylight.controller.networkconfig.neutron.midonet.cluster;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.midonet.cluster.data.Port;


public interface PortDataClient {
    boolean portsExists(UUID id);

    /**
     * Returns a list of ports for each tenant.
     * @return A map from a tenant ID to the tenant's ports.
     */
    public Map<UUID, List<Port<?, ?>>> portsGetAll();

    public UUID portsCreate(Port<?,?> port);

    public void portsDelete(UUID id);

    public Port<?,?> portsGet(UUID id);

    public boolean portsUpdate(Port<?,?> port);
}
