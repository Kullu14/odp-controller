/*
 * Copyright (c) 2013 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.controller.netconf.confignetconfconnector.operations.editconfig;

import org.opendaylight.controller.config.util.ConfigTransactionClient;
import org.opendaylight.controller.netconf.confignetconfconnector.mapping.attributes.fromxml.AttributeConfigElement;
import org.opendaylight.controller.netconf.confignetconfconnector.mapping.config.Services;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.InstanceNotFoundException;
import javax.management.ObjectName;
import java.util.Map;

public abstract class AbstractEditConfigStrategy implements EditConfigStrategy {

    private static final Logger logger = LoggerFactory.getLogger(AbstractEditConfigStrategy.class);

    @Override
    public void executeConfiguration(String module, String instance, Map<String, AttributeConfigElement> configuration,
                                     ConfigTransactionClient ta, Services services) {

        try {
            ObjectName on = ta.lookupConfigBean(module, instance);
            logger.debug("ServiceInstance for {} {} located successfully under {}", module, instance, on);
            executeStrategy(configuration, ta, on, services);
        } catch (InstanceNotFoundException e) {
            handleMissingInstance(configuration, ta, module, instance, services);
        }

    }

    abstract void handleMissingInstance(Map<String, AttributeConfigElement> configuration, ConfigTransactionClient ta,
                                        String module, String instance, Services services);

    abstract void executeStrategy(Map<String, AttributeConfigElement> configuration, ConfigTransactionClient ta,
                                  ObjectName objectName, Services services);

}
