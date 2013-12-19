/*
* Copyright 2012 Midokura Europe SARL
*/
package org.midonet.cluster.data;

public interface Entity<Id, Data, Self extends Entity<Id, Data, Self>> {

    public Id getId();

    public Self setId(Id id);

    public Data getData();

    public Self setData(Data data);

    public static abstract class Base<Id, Data, Self extends Base<Id, Data, Self>> implements Entity<Id, Data, Self> {
        Id id;
        Data data;

        protected Base(Id id, Data data) {
            this.id = id;
            this.data = data;
        }

        @Override
        public Id getId() {
            return id;
        }

        @Override
        public Self setId(Id id) {
            this.id = id;
            return self();
        }

        @Override
        public Data getData() {
            return data;
        }

        @Override
        public Self setData(Data data) {
            this.data = data;
            return self();
        }

        protected abstract Self self();

        @Override
        public String toString() {
            return self().getClass().getName() + "{" +
                "id=" + id +
                ", data=" + data +
                '}';
        }
    }

    public static interface TaggableEntity {
    }
}
