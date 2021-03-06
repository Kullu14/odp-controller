package org.opendaylight.controller.sal.compatibility

import org.opendaylight.controller.sal.core.Node
import org.opendaylight.controller.sal.core.NodeConnector
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.node.NodeConnectorKey
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier.IdentifiableItem

import static com.google.common.base.Preconditions.*;
import static extension org.opendaylight.controller.sal.common.util.Arguments.*;

import org.opendaylight.yangtools.yang.binding.InstanceIdentifier
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeConnectorRef
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeConnectorId
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.nodes.NodeKey
import org.opendaylight.controller.sal.core.ConstructionException
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeRef
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.Nodes
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeId
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeConnectorUpdated
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.inventory.rev130819.FlowCapableNodeConnectorUpdated
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.types.port.rev130925.PortFeatures
import org.opendaylight.controller.sal.core.Bandwidth
import org.opendaylight.controller.sal.core.AdvertisedBandwidth
import org.opendaylight.controller.sal.core.SupportedBandwidth
import org.opendaylight.controller.sal.core.PeerBandwidth
import org.opendaylight.controller.sal.core.Name
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.types.port.rev130925.PortConfig
import org.opendaylight.controller.sal.core.Config
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.types.port.rev130925.flow.capable.port.State

public class NodeMapping {

    public static val MD_SAL_TYPE = "MD_SAL";
    private static val NODE_CLASS = org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.nodes.Node;
    private static val NODECONNECTOR_CLASS = org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.node.NodeConnector;

    private new() {
        throw new UnsupportedOperationException("Utility class. Instantiation is not allowed.");
    }

    public static def toADNode(InstanceIdentifier<?> node) throws ConstructionException {
        checkNotNull(node);
        checkNotNull(node.getPath());
        checkArgument(node.getPath().size() >= 2);
        val arg = node.getPath().get(1);
        val item = arg.checkInstanceOf(IdentifiableItem);
        val nodeKey = item.getKey().checkInstanceOf(NodeKey);
        return new Node(MD_SAL_TYPE, nodeKey.id.toADNodeId);
    }
    
    public static def toADNodeId(NodeId nodeId) {
        checkNotNull(nodeId);
        return nodeId.value
    }


    public static def toADNodeConnector(NodeConnectorRef source) throws ConstructionException {
        checkNotNull(source);
        val InstanceIdentifier<?> path = checkNotNull(source.getValue());
        val node = path.toADNode();
        checkArgument(path.path.size() >= 3);
        val arg = path.getPath().get(2);
        val item = arg.checkInstanceOf(IdentifiableItem);
        val connectorKey = item.getKey().checkInstanceOf(NodeConnectorKey);
        return new NodeConnector(MD_SAL_TYPE, connectorKey.id.toADNodeConnectorId, node);
    }
    
    public static def toADNodeConnectorId(NodeConnectorId nodeConnectorId) {
        return nodeConnectorId.value
    }
    
    public static def toNodeRef(Node node) {
        checkArgument(MD_SAL_TYPE.equals(node.getType()));
        var nodeId = node.ID.checkInstanceOf(String)
        val nodeKey = new NodeKey(new NodeId(nodeId));
        val nodePath = InstanceIdentifier.builder().node(Nodes).child(NODE_CLASS, nodeKey).toInstance();
        return new NodeRef(nodePath);
    }
    
    public static def toNodeConnectorRef(NodeConnector nodeConnector) {
        val node = nodeConnector.node.toNodeRef();
        val nodePath = node.getValue() as InstanceIdentifier<org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.nodes.Node>
        var nodeConnectorId = nodeConnector.ID.checkInstanceOf(String)
        val connectorKey = new NodeConnectorKey(new NodeConnectorId(nodeConnectorId));
        val path = InstanceIdentifier.builder(nodePath).child(NODECONNECTOR_CLASS, connectorKey).toInstance();
        return new NodeConnectorRef(path);
    }

    public static def toADNode(NodeRef node) throws ConstructionException {
        return toADNode(node.getValue());
    }
    
    public static def toADNodeConnectorProperties(NodeConnectorUpdated nc) {
        val props = new java.util.HashSet<org.opendaylight.controller.sal.core.Property>();
        val fcncu = nc.getAugmentation(FlowCapableNodeConnectorUpdated)
        if(fcncu != null) {
            if(fcncu.currentFeature != null && fcncu.currentFeature.toAdBandwidth != null) {
                props.add(fcncu.currentFeature.toAdBandwidth)
            }
            if(fcncu.advertisedFeatures != null && fcncu.advertisedFeatures.toAdAdvertizedBandwidth != null) {
                props.add(fcncu.advertisedFeatures.toAdAdvertizedBandwidth)
            }
            if(fcncu.supported != null && fcncu.supported.toAdSupportedBandwidth != null) {
                props.add(fcncu.supported.toAdSupportedBandwidth)
            }
            if(fcncu.peerFeatures != null && fcncu.peerFeatures.toAdPeerBandwidth != null) {
                props.add(fcncu.peerFeatures.toAdPeerBandwidth)
            }
            if(fcncu.name != null && fcncu.name.toAdName != null) {
                props.add(fcncu.name.toAdName)
            }
            if(fcncu.configuration != null && fcncu.configuration.toAdConfig != null) {
                props.add(fcncu.configuration.toAdConfig)
            }
            if(fcncu.state != null && fcncu.state.toAdState != null) {
                props.add(fcncu.state.toAdState)
            }
        }
        return props
    }
    
    public static def toAdName(String name) {
        return new Name(name)
    } 
    
    public static def toAdConfig(PortConfig pc) {
        var Config config;
        if(pc.PORTDOWN){
            config = new Config(Config.ADMIN_DOWN)
        } else {
            config = new Config(Config.ADMIN_UP)
        }
        return config
    }
    
    public static def toAdState(State s) {
        var org.opendaylight.controller.sal.core.State state
        if(s.linkDown) {
            state = new org.opendaylight.controller.sal.core.State(org.opendaylight.controller.sal.core.State.EDGE_DOWN)
        } else {
            state = new org.opendaylight.controller.sal.core.State(org.opendaylight.controller.sal.core.State.EDGE_UP)
        }
        return state
    }
    
    public static def toAdBandwidth(PortFeatures pf) {
        var Bandwidth bw = null
        if (pf.is_10mbHd || pf.is_10mbFd ) {
            bw= new Bandwidth(Bandwidth.BW10Mbps)
        } else if (pf.is_100mbHd || pf.is_100mbFd ) {
            bw= new Bandwidth(Bandwidth.BW100Mbps)
        } else if (pf.is_1gbHd || pf.is_1gbFd ) {
            bw= new Bandwidth(Bandwidth.BW1Gbps)
        } else if (pf.is_1gbFd ) {
            bw= new Bandwidth(Bandwidth.BW10Gbps)
        } else if ( pf.is_10gbFd ) {
            bw= new Bandwidth(Bandwidth.BW10Gbps)
        } else if ( pf.is_40gbFd ) {
            bw= new Bandwidth(Bandwidth.BW40Gbps)
        } else if ( pf.is_100gbFd ) {
            bw= new Bandwidth(Bandwidth.BW100Gbps)
        } else if ( pf.is_1tbFd ) {
            bw= new Bandwidth(Bandwidth.BW1Tbps)
        } 
        return bw;
    }
    
    public static def toAdAdvertizedBandwidth(PortFeatures pf) {
        var AdvertisedBandwidth abw
        val bw = pf.toAdBandwidth
        if(bw != null) {
            abw = new AdvertisedBandwidth(bw.value)
        }
        return abw
    }
    
    public static def toAdSupportedBandwidth(PortFeatures pf) {
        var SupportedBandwidth sbw
        val bw = pf.toAdBandwidth
        if(bw != null ) {
            sbw = new SupportedBandwidth(bw.value)
        }
        return sbw
    }
    
    public static def toAdPeerBandwidth(PortFeatures pf) {
        var PeerBandwidth pbw
        val bw = pf.toAdBandwidth
        if(bw != null) {
            pbw = new PeerBandwidth(bw.value)
        }
        return pbw
    }
    
    
}
