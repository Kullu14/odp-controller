/*
 * Copyright (c) 2013 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.controller.netconf.confignetconfconnector.mapping.attributes.mapping;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import org.opendaylight.controller.netconf.confignetconfconnector.mapping.attributes.AttributesConstants;
import org.opendaylight.controller.netconf.confignetconfconnector.mapping.config.Services;
import org.opendaylight.controller.netconf.confignetconfconnector.util.Util;

import javax.management.ObjectName;
import javax.management.openmbean.SimpleType;

public class ObjectNameAttributeMappingStrategy extends
        AbstractAttributeMappingStrategy<ObjectNameAttributeMappingStrategy.MappedDependency, SimpleType<?>> {

    private final Services tracker;
    private final String serviceName;
    private final String namespace;

    public ObjectNameAttributeMappingStrategy(SimpleType<?> openType, Services dependencyTracker, String serviceName, String namespace) {
        super(openType);
        this.tracker = dependencyTracker;
        this.serviceName = serviceName;
        this.namespace = namespace;
    }

    @Override
    public Optional<MappedDependency> mapAttribute(Object value) {
        if (value == null)
            return Optional.absent();

        String expectedClass = getOpenType().getClassName();
        String realClass = value.getClass().getName();
        Preconditions.checkArgument(realClass.equals(expectedClass), "Type mismatch, expected " + expectedClass
                + " but was " + realClass);
        Util.checkType(value, ObjectName.class);

        ObjectName on = (ObjectName) value;

        String expectedRefName = on.getKeyProperty(AttributesConstants.REF_NAME_ON_PROPERTY_KEY);

        String refName = expectedRefName == null ? tracker.getRefName(namespace, serviceName, on, Optional.<String> absent())
                : tracker.getRefName(namespace, serviceName, on, Optional.of(expectedRefName));

        return Optional.of(new MappedDependency(namespace, serviceName, refName));
    }

    public static class MappedDependency {
        private final String namespace, serviceName, refName;

        public MappedDependency(String namespace, String serviceName, String refName) {
            this.serviceName = serviceName;
            this.refName = refName;
            this.namespace = namespace;
        }

        public String getServiceName() {
            return serviceName;
        }

        public String getRefName() {
            return refName;
        }

        public String getNamespace() {
            return namespace;
        }

        @Override
        public String toString() {
            final StringBuffer sb = new StringBuffer("MappedDependency{");
            sb.append("namespace='").append(namespace).append('\'');
            sb.append(", serviceName='").append(serviceName).append('\'');
            sb.append(", refName='").append(refName).append('\'');
            sb.append('}');
            return sb.toString();
        }
    }

}
