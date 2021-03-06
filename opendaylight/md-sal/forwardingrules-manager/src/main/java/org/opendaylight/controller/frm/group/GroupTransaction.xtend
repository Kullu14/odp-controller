package org.opendaylight.controller.frm.group

import org.opendaylight.controller.frm.AbstractTransaction
import org.opendaylight.controller.md.sal.common.api.data.DataModification
import org.opendaylight.yang.gen.v1.urn.opendaylight.group.service.rev130918.AddGroupInputBuilder
import org.opendaylight.yang.gen.v1.urn.opendaylight.group.service.rev130918.RemoveGroupInputBuilder
import org.opendaylight.yang.gen.v1.urn.opendaylight.group.service.rev130918.SalGroupService
import org.opendaylight.yang.gen.v1.urn.opendaylight.group.service.rev130918.UpdateGroupInputBuilder
import org.opendaylight.yang.gen.v1.urn.opendaylight.group.service.rev130918.group.update.OriginalGroupBuilder
import org.opendaylight.yang.gen.v1.urn.opendaylight.group.service.rev130918.group.update.UpdatedGroupBuilder
import org.opendaylight.yang.gen.v1.urn.opendaylight.group.types.rev131018.GroupRef
import org.opendaylight.yang.gen.v1.urn.opendaylight.group.types.rev131018.groups.Group
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeRef
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.nodes.Node
import org.opendaylight.yangtools.yang.binding.DataObject
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier

class GroupTransaction extends AbstractTransaction {
    
    @Property
    val SalGroupService groupService;
    
    new(DataModification<InstanceIdentifier<? extends DataObject>, DataObject> modification,SalGroupService groupService) {
        super(modification)
        _groupService = groupService;
    }
    
    override remove(InstanceIdentifier<?> instanceId, DataObject obj) {
        if(obj instanceof Group) {
            val group = (obj as Group)
            val nodeInstanceId = instanceId.firstIdentifierOf(Node);
            val builder = new RemoveGroupInputBuilder(group);
            builder.setNode(new NodeRef(nodeInstanceId));
            builder.setGroupRef(new GroupRef(instanceId));
            _groupService.removeGroup(builder.build());            
        }
    }
    
    override update(InstanceIdentifier<?> instanceId, DataObject originalObj, DataObject updatedObj) {
        if(originalObj instanceof Group && updatedObj instanceof Group) {
            val originalGroup = (originalObj as Group)
            val updatedGroup = (updatedObj as Group)
            val nodeInstanceId = instanceId.firstIdentifierOf(Node);
            val builder = new UpdateGroupInputBuilder();
            builder.setNode(new NodeRef(nodeInstanceId));
            builder.setGroupRef(new GroupRef(instanceId));
            val ufb = new UpdatedGroupBuilder(updatedGroup);
            builder.setUpdatedGroup((ufb.build()));
            val ofb = new OriginalGroupBuilder(originalGroup);
            builder.setOriginalGroup(ofb.build());      
            _groupService.updateGroup(builder.build());
           
        }
    }
    
    override add(InstanceIdentifier<?> instanceId, DataObject obj) {
        if(obj instanceof Group) {
            val group = (obj as Group)
            val nodeInstanceId = instanceId.firstIdentifierOf(Node);
            val builder = new AddGroupInputBuilder(group);
            builder.setNode(new NodeRef(nodeInstanceId));
            builder.setGroupRef(new GroupRef(instanceId));
            _groupService.addGroup(builder.build());            
        }
    }
    
    override validate() throws IllegalStateException {
        GroupTransactionValidator.validate(this)
    }  
}