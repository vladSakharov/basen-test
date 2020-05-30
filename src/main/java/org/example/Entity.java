package org.example;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.Map;
import java.util.Set;


@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = User.class, name = "User"),
        @JsonSubTypes.Type(value = Account.class, name = "Account") })

public interface Entity {
    // Returns a unique identifier
    String getID();

    // Returns the sub-entities of this entity
    Set<Entity> getSubEntities();

    // Returns a set of key-value data belonging to this entity
    Map<String, String> getData();

    void setSubEntities(Set<Entity> subEntities);

    void setData(Map<String, String> data);

}
