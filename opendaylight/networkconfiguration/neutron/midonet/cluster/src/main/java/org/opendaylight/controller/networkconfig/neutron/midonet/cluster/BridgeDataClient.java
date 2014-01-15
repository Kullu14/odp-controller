/*
 * Copyright (c) 2013 Midokura Europe SARL, All Rights Reserved.
 */

package org.opendaylight.controller.networkconfig.neutron.midonet.cluster;

import java.util.List;
import java.util.UUID;

import org.midonet.cluster.data.Bridge;


public interface BridgeDataClient {
    /**
     * Tests if a bridge with a specified ID exists.
     * @param bridgeId A UUID of a bridge.
     * @return True if one exists and false otherwise.
     */
    boolean bridgeExists(UUID bridgeId);

    /**
     * Returns data for a bridge with specified ID.
     * @param id A UUID of the bridge.
     * @return bridge data if a bridge exists, or null otherwise.
     */
    Bridge bridgesGet(UUID bridgeId);

    /**
     * Deletes a MidoNet bridge with a specified ID.
     * @param bridgeId An id of the bridge to be deleted.
     */
    void bridgesDelete(UUID bridgeId);

    /**
     * Creates a new MidoNet bridge with a specified tenant ID and a name. A new
     * bridge UUID is assigned and populates to the given Bridge instance.
     * @param bridge Bridge data to be created.
     * @return A UUID of the bridge created, or null it fails to create.
     */
    UUID bridgesCreate(Bridge bridge);

    /**
     * Returns data for a bridge with specified tenant ID and bridge name.
     * @param tenantId A tenant ID.
     * @param name A bridge name.
     * @return bridge data if a bridge exists, or null otherwise.
     */
    Bridge bridgesGetByName(String tenantId, String name);

    /**
     * Updates the data for a given bridge.
     * @param bridge The update info.
     * @return True if it successfully updated the bridge data, and false
     * otherwise.
     */
    boolean bridgesUpdate(Bridge bridge);

    /**
     * Returns a list of all bridges.
     * @return a list of all bridges. Null if it failed to retrieve bridges.
     */
    List<Bridge> bridgesGetAll();
}
