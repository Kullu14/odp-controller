/*
 * Copyright (c) 2013 Midokura Europe SARL, All Rights Reserved.
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
