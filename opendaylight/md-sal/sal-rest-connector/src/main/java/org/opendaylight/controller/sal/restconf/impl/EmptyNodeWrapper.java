package org.opendaylight.controller.sal.restconf.impl;

import java.net.URI;
import java.util.Collections;

import org.opendaylight.yangtools.yang.common.QName;
import org.opendaylight.yangtools.yang.data.api.CompositeNode;
import org.opendaylight.yangtools.yang.data.api.ModifyAction;
import org.opendaylight.yangtools.yang.data.api.MutableSimpleNode;
import org.opendaylight.yangtools.yang.data.api.Node;
import org.opendaylight.yangtools.yang.data.api.SimpleNode;
import org.opendaylight.yangtools.yang.data.impl.NodeFactory;

import com.google.common.base.Preconditions;

public final class EmptyNodeWrapper implements NodeWrapper<Node<?>>, Node<Void> {
    
    private Node<?> unwrapped;
    
    private String localName;
    private URI namespace;
    private QName name;

    private boolean composite;

    public boolean isComposite() {
        return composite;
    }
    
    public void setComposite(boolean composite) {
        this.composite = composite;
    }
    
    public EmptyNodeWrapper(URI namespace, String localName) {
        this.localName = Preconditions.checkNotNull(localName);
        this.namespace = namespace;
    }
    
    @Override
    public void setQname(QName name) {
        Preconditions.checkState(unwrapped == null, "Cannot change the object, due to data inconsistencies.");
        this.name = name;
    }
    
    @Override
    public String getLocalName() {
        if (unwrapped != null) {
            return unwrapped.getNodeType().getLocalName();
        }
        return localName;
    }
    
    @Override
    public URI getNamespace() {
        if (unwrapped != null) {
            return unwrapped.getNodeType().getNamespace();
        }
        return namespace;
    }

    @Override
    public void setNamespace(URI namespace) {
        Preconditions.checkState(unwrapped == null, "Cannot change the object, due to data inconsistencies.");
        this.namespace = namespace;
    }

    @Override
    public Node<?> unwrap() {
        if (unwrapped == null) {
            if (name == null) {
                Preconditions.checkNotNull(namespace);
                name = new QName(namespace, localName);
            }
            if(composite) {
                unwrapped = NodeFactory.createImmutableCompositeNode(name, null, Collections.<Node<?>>emptyList(),null);
            } else {
                unwrapped = NodeFactory.createImmutableSimpleNode(name, null, null);
            }
            namespace = null;
            localName = null;
            name = null;
        }
        return unwrapped;
    }

    @Override
    public QName getNodeType() {
        return unwrap().getNodeType();
    }

    @Override
    public CompositeNode getParent() {
        return unwrap().getParent();
    }

    @Override
    public Void getValue() {
        return null;
    }

    @Override
    public QName getKey() {
        return unwrap().getKey();
    }

    @Override
    public Void setValue(Void value) {
        return null;
    }

}
