package com.blakjack.clueless.common;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * This is an example abstract message class. If you don't like it, change it!
 * Implementing Serializable is not mandatory for our connections, but it's
 * probably a good idea.
 * 
 * @author nauglrj1
 */
public class CluelessMessage implements Serializable {
    
    public enum Type {
        ERROR,      //for sending an error
        LOGIN,      //for logging in (duh)
        LOGOFF,     //when a player goes away - generally originates at server
        MESSAGE,    //used for sending a message to the user
        MOVE,       //moves a player
        END_TURN,    //ends player turn
        NEXT_TURN,  // starts the next player's turn
        START,      //start the game!
        SUGGEST,    //make a suggestion
        RESP_SUGGEST, // responce to suggestion
        UPDATE      //contains the entire game state
        , ACCUSE
    }
    
    private final Map<String, Serializable> fields = new HashMap<String, Serializable>();
    
    public CluelessMessage(Type type) {
        setField("type", type);
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
