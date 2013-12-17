/*
 * Copyright (c) 2013 Midokura Europe SARL, All Rights Reserved.
 */

package org.opendaylight.controller.networkconfig.neutron.midonet.cluster.data;

import java.util.UUID;

public interface Bridge {
    public UUID getNetworkUUID();
    public void setNetworkUUID(UUID uuid);
}
