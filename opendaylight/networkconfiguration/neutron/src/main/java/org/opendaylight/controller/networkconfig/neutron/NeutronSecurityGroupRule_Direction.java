package org.opendaylight.controller.networkconfig.neutron;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

@XmlType
@XmlEnum
public enum NeutronSecurityGroupRule_Direction {
    // See OpenStack Network API v2.0 Reference for description of
    // annotated attributes

    @XmlEnumValue("egress") EGRESS,
    @XmlEnumValue("ingress") INGRESS
}