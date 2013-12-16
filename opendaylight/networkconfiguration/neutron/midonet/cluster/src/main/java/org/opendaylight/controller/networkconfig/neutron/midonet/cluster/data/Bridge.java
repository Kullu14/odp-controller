/*
* Copyright 2012 Midokura Europe SARL
*/
package org.opendaylight.controller.networkconfig.neutron.midonet.cluster.data;

import java.util.UUID;

public interface Bridge {
    public UUID getNetworkUUID();
    public void setNetworkUUID(UUID uuid);
}
