package com.blakjack.clueless;

import java.io.Serializable;
import java.util.Map;

public abstract class CluelessMessage implements Serializable {
    
    private final Map<String, Serializable> fields;
    
    protected CluelessMessage(Map<String, Serializable> fields) {
        this.fields = fields;
    }
    
    public Map<String, Serializable> getFields() {
        return fields;
    }
    
    public Object getField(String fieldName) {
        return fields.get(fieldName);
    }
    
    public void setField(String fieldName, Serializable value) {
        fields.put(fieldName, value);
    }
}
