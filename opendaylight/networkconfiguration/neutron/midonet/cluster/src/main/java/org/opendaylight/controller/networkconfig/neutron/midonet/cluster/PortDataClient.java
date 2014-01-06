/*
 * Copyright (c) 2013 Midokura Europe SARL, All Rights Reserved.
 */
package org.opendaylight.controller.networkconfig.neutron.midonet.cluster;

import java.util.UUID;

import org.midonet.cluster.data.Port;


public interface PortDataClient {
    boolean portsExists(UUID id) throws StateAccessException;

    UUID portsCreate(Port<?,?> port) throws StateAccessException;

    void portsDelete(UUID id) throws StateAccessException;

    Port<?,?> portsGet(UUID id) throws StateAccessException;

    void portsUpdate(Port<?,?> port) throws StateAccessException;
}
