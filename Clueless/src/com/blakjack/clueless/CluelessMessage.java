package com.blakjack.clueless;

import java.io.Serializable;
import java.util.Map;
import java.util.Map.Entry;

/**
 * This is an example abstract message class. If you don't like it, change it!
 * Implementing Serializable is not mandatory for our connections, but it's
 * probably a good idea.
 * 
 * @author nauglrj1
 */
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
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(this.getClass().getName());
        builder.append(":\n");
        for (Entry<String, Serializable> e : fields.entrySet()) {
            builder.append(e.getKey()).append(":").append(e.getValue()).append("\n");
        }
        return builder.toString();
    }
}
