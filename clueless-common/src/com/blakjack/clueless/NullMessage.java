/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.blakjack.clueless;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

/**
 * This message is a proof of concept. It should be removed when real messages
 * are authored.
 * 
 * @author nauglrj1
 */
public class NullMessage extends CluelessMessage {
    
    //since we're passing these between applications, this version number
    //is important and must match between server and client.
    private static final long serialVersionUID = 1L;

    public NullMessage() {
        super(new HashMap<String, Serializable>() {{
            //NullMessage only has two fields: source and time
            put("src", "clueless-client");
            put("time", new Date(System.currentTimeMillis()));
        }});
    }
    
}
