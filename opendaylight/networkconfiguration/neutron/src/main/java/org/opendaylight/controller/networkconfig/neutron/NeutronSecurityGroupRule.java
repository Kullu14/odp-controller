package org.opendaylight.controller.networkconfig.neutron;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class NeutronSecurityGroupRule {
    // See OpenStack Network API v2.0 Reference for description of
    // annotated attributes

    @XmlElement (name="id")
    String secGroupRuleUUID;

    @XmlElement (name="security_group_id")
    String secGroupUUID;

    @XmlElement (name="tenant_id")
    String tenantUUID;

    @XmlElement (name="direction")
    NeutronSecurityGroupRule_Direction direction;

    @XmlElement (name="ethertype")
    NeutronSecurityGroupRule_Ethertype ethertype;

    @XmlElement (name="port_range_max")
    Integer portRangeMax;

    @XmlElement (name="port_range_min")
    Integer portRangeMin;

    @XmlElement (name="protocol")
    String protocol;

    @XmlElement (name="remote_group_id")
    String remoteGroupUUID;

    @XmlElement (name="remote_ip_prefix")
    String remoteIpPrefix;
}
