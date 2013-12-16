/*
* Copyright 2012 Midokura Europe SARL
*/
package org.opendaylight.controller.networkconfig.neutron.midonet.cluster.local.data;

import java.util.UUID;

import org.opendaylight.controller.networkconfig.neutron.midonet.cluster.data.Bridge;

public class BridgeImpl implements Bridge {
    private UUID networkUUID;

    public UUID getNetworkUUID() {
        return this.networkUUID;
    }

    public void setNetworkUUID(UUID uuid) {
        this.networkUUID = uuid;
    }
}
