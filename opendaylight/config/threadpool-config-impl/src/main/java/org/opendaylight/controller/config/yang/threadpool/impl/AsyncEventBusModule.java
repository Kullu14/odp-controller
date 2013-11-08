/**
 * Generated file

 * Generated from: yang module name: threadpool-impl  yang module local name: async-eventbus
 * Generated by: org.opendaylight.controller.config.yangjmxgenerator.plugin.JMXGenerator
 * Generated at: Tue Nov 05 15:40:46 CET 2013
 *
 * Do not modify this file unless it is present under src/main directory
 */
package org.opendaylight.controller.config.yang.threadpool.impl;

import org.opendaylight.controller.config.threadpool.util.CloseableAsyncEventBus;

/**
*
*/
public final class AsyncEventBusModule extends
        org.opendaylight.controller.config.yang.threadpool.impl.AbstractAsyncEventBusModule {

    public AsyncEventBusModule(org.opendaylight.controller.config.api.ModuleIdentifier identifier,
            org.opendaylight.controller.config.api.DependencyResolver dependencyResolver) {
        super(identifier, dependencyResolver);
    }

    public AsyncEventBusModule(org.opendaylight.controller.config.api.ModuleIdentifier identifier,
            org.opendaylight.controller.config.api.DependencyResolver dependencyResolver,
            AsyncEventBusModule oldModule, java.lang.AutoCloseable oldInstance) {
        super(identifier, dependencyResolver, oldModule, oldInstance);
    }

    @Override
    public void validate() {
        super.validate();
        // Add custom validation for module attributes here.
    }

    @Override
    public java.lang.AutoCloseable createInstance() {
        return new CloseableAsyncEventBus(getIdentifier().toString(), getThreadpoolDependency(),
                getRootRuntimeBeanRegistratorWrapper());
    }
}