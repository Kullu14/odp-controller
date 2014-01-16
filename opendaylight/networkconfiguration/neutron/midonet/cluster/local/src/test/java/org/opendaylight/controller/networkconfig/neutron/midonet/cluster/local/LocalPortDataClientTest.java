/*
 * Copyright (c) 2013 Midokura Europe SARL, All Rights Reserved.
 */
package org.opendaylight.controller.networkconfig.neutron.midonet.cluster.local;

import static org.junit.Assert.assertNotNull;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.midonet.cluster.data.Port;
import org.midonet.cluster.data.ports.BridgePort;
import org.midonet.midolman.ZkCluster;
import org.opendaylight.controller.networkconfig.neutron.midonet.cluster.PortDataClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Tests ZooKeeper port data CRUD operations.
 */
public class LocalPortDataClientTest {
    private static final Logger logger =
            LoggerFactory.getLogger(LocalPortDataClientTest.class);
    private PortDataClient portDataClient = null;

    @Before
    public void setUp() throws Exception {
        ZkCluster.startCluster("/home/dev/mido/midonet/midolman/conf/midolman.conf");
        this.portDataClient = new LocalPortDataClient();
    }

    @Test
    public void testPortsCreate() {
        logger.info("Testing createPort().");
        assertNotNull(this.portDataClient);
        Port<?, ?> port = new BridgePort();
        port.setDeviceId(UUID.fromString("92e506cc-9089-4bb4-a100-51ee891c7941"));
        UUID portId = this.portDataClient.portsCreate(port);
        assertNotNull("The created port ID is not null.", portId);
    }

}
