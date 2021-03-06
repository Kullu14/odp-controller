/*
 * Copyright (c) 2013 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.controller.netconf.confignetconfconnector.osgi;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import org.opendaylight.controller.config.api.LookupRegistry;
import org.opendaylight.controller.config.util.ConfigRegistryJMXClient;
import org.opendaylight.controller.config.yang.store.api.YangStoreException;
import org.opendaylight.controller.config.yang.store.api.YangStoreService;
import org.opendaylight.controller.config.yang.store.api.YangStoreSnapshot;
import org.opendaylight.controller.config.yangjmxgenerator.ModuleMXBeanEntry;
import org.opendaylight.controller.netconf.confignetconfconnector.transactions.TransactionProvider;
import org.opendaylight.controller.netconf.confignetconfconnector.util.Util;
import org.opendaylight.controller.netconf.mapping.api.Capability;
import org.opendaylight.controller.netconf.mapping.api.NetconfOperation;
import org.opendaylight.controller.netconf.mapping.api.NetconfOperationFilter;
import org.opendaylight.controller.netconf.mapping.api.NetconfOperationService;
import org.opendaylight.yangtools.yang.model.api.Module;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Manages life cycle of {@link YangStoreSnapshot}.
 */
public class NetconfOperationServiceImpl implements NetconfOperationService {

    private final YangStoreSnapshot yangStoreSnapshot;
    private final NetconfOperationProvider operationProvider;
    private final Set<Capability> capabilities;
    private final TransactionProvider transactionProvider;

    public NetconfOperationServiceImpl(YangStoreService yangStoreService, ConfigRegistryJMXClient jmxClient,
            String netconfSessionIdForReporting) throws YangStoreException {

        yangStoreSnapshot = yangStoreService.getYangStoreSnapshot();
        checkConsistencyBetweenYangStoreAndConfig(jmxClient, yangStoreSnapshot);

        transactionProvider = new TransactionProvider(jmxClient, netconfSessionIdForReporting);
        operationProvider = new NetconfOperationProvider(yangStoreSnapshot, jmxClient, transactionProvider,
                netconfSessionIdForReporting);
        capabilities = setupCapabilities(yangStoreSnapshot);
    }


    @VisibleForTesting
    static void checkConsistencyBetweenYangStoreAndConfig(LookupRegistry jmxClient, YangStoreSnapshot yangStoreSnapshot) {
        Set<String> missingModulesFromConfig = Sets.newHashSet();

        Set<String> modulesSeenByConfig = jmxClient.getAvailableModuleFactoryQNames();
        Map<String, Map<String, ModuleMXBeanEntry>> moduleMXBeanEntryMap = yangStoreSnapshot.getModuleMXBeanEntryMap();

        for (Map<String, ModuleMXBeanEntry> moduleNameToMBE : moduleMXBeanEntryMap.values()) {
            for (ModuleMXBeanEntry moduleMXBeanEntry : moduleNameToMBE.values()) {
                String moduleSeenByYangStore = moduleMXBeanEntry.getYangModuleQName().toString();
                if(modulesSeenByConfig.contains(moduleSeenByYangStore) == false)
                    missingModulesFromConfig.add(moduleSeenByYangStore);
            }
        }

        Preconditions
                .checkState(
                        missingModulesFromConfig.isEmpty(),
                        "There are inconsistencies between configuration subsystem and yangstore in terms of discovered yang modules, yang modules missing from config subsystem but present in yangstore: %s, %sAll modules present in config: %s",
                        missingModulesFromConfig, System.lineSeparator(), modulesSeenByConfig);

    }

    @Override
    public void close() {
        yangStoreSnapshot.close();
        transactionProvider.close();
    }

    @Override
    public Set<Capability> getCapabilities() {
        return capabilities;
    }

    @Override
    public Set<NetconfOperation> getNetconfOperations() {
        return operationProvider.getOperations();
    }

    @Override
    public Set<NetconfOperationFilter> getFilters() {
        return Collections.emptySet();
    }

    private static Set<Capability> setupCapabilities(YangStoreSnapshot yangStoreSnapshot) {
        Set<Capability> capabilities = Sets.newHashSet();

        // [RFC6241] 8.3.  Candidate Configuration Capability
        capabilities.add(new BasicCapability("urn:ietf:params:netconf:capability:candidate:1.0"));
        // [RFC6241] 8.5.  Rollback-on-Error Capability
        capabilities.add(new BasicCapability("urn:ietf:params:netconf:capability:rollback-on-error:1.0"));

        final Collection<Map.Entry<Module, String>> modulesAndContents = yangStoreSnapshot.getModuleMap().values();
        for (Map.Entry<Module, String> moduleAndContent : modulesAndContents) {
            capabilities.add(new YangStoreCapability(moduleAndContent));
        }

        return capabilities;
    }

    private static class BasicCapability implements Capability {

        private final String capability;

        private BasicCapability(String capability) {
            this.capability = capability;
        }

        @Override
        public String getCapabilityUri() {
            return capability;
        }

        @Override
        public Optional<String> getModuleNamespace() {
            return Optional.absent();
        }

        @Override
        public Optional<String> getModuleName() {
            return Optional.absent();
        }

        @Override
        public Optional<String> getRevision() {
            return Optional.absent();
        }

        @Override
        public Optional<String> getCapabilitySchema() {
            return Optional.absent();
        }

        @Override
        public Optional<List<String>> getLocation() {
            return Optional.absent();
        }
    }

    private static class YangStoreCapability extends BasicCapability {

        private final String content;
        private final String revision;
        private final String moduleName;
        private final String moduleNamespace;

        public YangStoreCapability(Map.Entry<Module, String> moduleAndContent) {
            super(getAsString(moduleAndContent.getKey()));
            this.content = moduleAndContent.getValue();
            Module module = moduleAndContent.getKey();
            this.moduleName = module.getName();
            this.moduleNamespace = module.getNamespace().toString();
            this.revision = Util.writeDate(module.getRevision());
        }

        @Override
        public Optional<String> getCapabilitySchema() {
            return Optional.of(content);
        }

        private static String getAsString(Module module) {
            final StringBuffer capabilityContent = new StringBuffer();
            capabilityContent.append(module.getNamespace());
            capabilityContent.append("?module=");
            capabilityContent.append(module.getName());
            capabilityContent.append("&revision=");
            capabilityContent.append(Util.writeDate(module.getRevision()));
            return capabilityContent.toString();
        }

        @Override
        public Optional<String> getModuleName() {
            return Optional.of(moduleName);
        }

        @Override
        public Optional<String> getModuleNamespace() {
            return Optional.of(moduleNamespace);
        }

        @Override
        public Optional<String> getRevision() {
            return Optional.of(revision);
        }
    }
}
