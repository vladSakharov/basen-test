package org.example;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class BaseEntity implements Entity {

    String id = "";
    Set<Entity> subEntities = new HashSet<Entity>();
    Map<String, String> data = new LinkedHashMap<String, String>();

    public BaseEntity() {

    }

    public BaseEntity(Builder builder) {
        this.id = builder.id;
        this.subEntities = builder.subEntities;
        this.data = builder.data;
    }

    @Override
    public String getID() {
        return id;
    }

    @Override
    public Set<Entity> getSubEntities() {
        return subEntities;
    }

    @Override
    public Map<String, String> getData() {
        return data;
    }

    public void setSubEntities(Set<Entity> subEntities) {
        this.subEntities = subEntities;
    }

    public void addSubEntity(Entity entity) {
        subEntities.add(entity);
    }

    public void removeSubEntity(Entity entity) {
        if (subEntities.contains(entity)) {
            subEntities.remove(entity);
        } else {
            App.Log("[INFO] Tried removing a non-existent entity");
        }
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }

    public void addData(String key, String value) {
        this.data.put(key, value);
    }

    public void removeData(String key) {
        if (data.containsKey(key)) {
            data.remove(key);
        } else {
            App.Log("[INFO] Tried removing a non-existent data entry");
        }
    }

    public static class Builder {
        String id = "";
        Set<Entity> subEntities = new HashSet<Entity>();
        Map<String, String> data = new LinkedHashMap<String, String>();

        private Builder() {
        }

        public static Builder newInstance() {
            return new Builder();
        }

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public Builder setSubEntities(Set<Entity> subEntities) {
            this.subEntities = subEntities;
            return this;
        }

        public Builder addSubEntity(Entity entity) {
            this.subEntities.add(entity);
            return this;
        }

        public Builder setData(Map<String, String> data) {
            this.data = data;
            return this;
        }

        public Builder addData(String key, String value) {
            this.data.put(key, value);
            return this;
        }


        public BaseEntity build() {
            return new BaseEntity(this);
        }

    }
}


