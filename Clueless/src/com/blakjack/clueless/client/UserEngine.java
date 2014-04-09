package com.blakjack.clueless.client;

import java.io.IOException;
import java.io.Serializable;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.blakjack.clueless.common.Card;
import com.blakjack.clueless.common.CluelessMessage;
import com.blakjack.clueless.common.CluelessMessage.Type;
import com.blakjack.clueless.common.Connection;
import com.blakjack.clueless.common.Player;
import com.blakjack.clueless.common.Connection.ConnectionEvent;
import com.blakjack.clueless.common.Connection.ConnectionEventListener;
import com.blakjack.clueless.common.Connection.MessageHandler;
import com.blakjack.clueless.gui.GameFrame;
import com.blakjack.clueless.server.CluelessServer;

/**
 * The User Engine is used to control the flow of the game from the user's perspective
 * This class will be handling messages from both the gui and the server.  This way the 
 * Gui will be a separate entity so that if we ever wanted to change the layout, the logic
 * will not need to change.
 * The GameFrame will take all messages and call these functions as necessary
 */
public class UserEngine {
	private Player player;
	private Connection connection;
	private int position;
	private List<Card> cards = new ArrayList<Card>();
	private CluelessServer server;
	private CluelessClient client;
	
    public UserEngine(){
    }
//	TODO Not sure if I want this here or in player...
//	private String username;
	
	// TODO Here is where the logic will be called for determining what a user can do.
	
	
	public Player getPlayer()
	{
		return player;
	}
	
	public void setPlayer(Player p)
	{
		this.player = p;
	}
	
//	public Connection getConnection()
//	{
//		return connection;
//	}

//	public String getUsername()
//	{
//		return username;
//	}
//	
//	public void setUsername(String name)
//	{
//		username = name;
//	}
        public CluelessClient getClient() {
                return client;
        }

        public void setClient(CluelessClient client) {
                this.client = client;
        }
        
//	public void setConnection(Connection connection)
//	{
//		this.connection = connection;
//	}
//	
//	public void dealCard(Card c)
//	{
//		cards.add(c);
//	}
//	
//	public List<Card> getCards()
//	{
//		return cards;
//	}
//	
//	public int getPosition()
//	{
//		return position;
//	}
//	
//	public void setPosition(int pos)
//	{
//		position = pos;
//	}
	
//  @Override
//  //TODO This should be the logic side of things.  The GameFrame has the gui side of things
//  public void handle(Connection connection, CluelessMessage msg) {
//      CluelessMessage.Type type = (CluelessMessage.Type)msg.getField("type");
//      switch(type) {
//          case ERROR:
//              break;
//          case LOGIN:
//              break;
//          case LOGOFF:
//              break;
//          case MESSAGE:
//              break;
//          case UPDATE:
//              break;
//          // Note: Do opposite since message is coming from the server 
//          case SUGGEST:    	  
//        	  break;
//          case RESP_SUGGEST:
//          default:
//      }
//  }
  
  public void makeSuggestion(String person, String weapon )
  {
	  CluelessMessage message = new CluelessMessage(Type.SUGGEST);
      message.setField("person", person);
      message.setField("weapon", weapon);
      sendToServer(message);
  }
  
  public void accuse(String person, String weapon, String room)
  {
	  CluelessMessage message = new CluelessMessage(Type.SUGGEST);
      message.setField("person", person);
      message.setField("weapon", weapon);
      message.setField("room", room);
      sendToServer(message);
  }
  
  public void respondToSuggestion(Card card, CluelessMessage msg)
  {
	  CluelessMessage message = new CluelessMessage(Type.RESP_SUGGEST);
	  for (String key : msg.getFields().keySet()){
		  message.setField(key, (Serializable) msg.getField(key));
	  }
	  message.setField("card", card);
	  sendToServer(message);
  }
  
  /*
   * includes UP, DOWN, LEFT, RIGHT, and SECRET
   */
  public void move(String direction)
  {
	  // If direction is allowed
	  CluelessMessage msg = new CluelessMessage(Type.MOVE);
	  msg.setField("direction", direction);
	  sendToServer(msg);
	  
  }
  
  public void endTurn()
  {
	  CluelessMessage msg = new CluelessMessage(Type.END_TURN);
	  sendToServer(msg);
  }
  
  public void sendToServer(CluelessMessage msg) {
      client.send(msg);
  }
  
  public void sendToClient(CluelessMessage msg) {
      connection.send(msg);
  }
  
  public void connect(boolean startServer, boolean okOption, int port, String address, String username, GameFrame gameFrame) throws IOException,UnknownHostException {	  
	  //TODO: Maybe here????
//	  this.username = username;
      if (okOption) {
          //TODO(naugler) validate login parameters
          if (startServer) {
              server = new CluelessServer(port);
              try {
                  server.start();
              } catch (IOException ex) {
            	  server.stop();
            	  throw ex;
              }
          }
          client = new CluelessClient(username,address, port);
          client.addMessageHandler(gameFrame);
          client.addConnectionEventListener(gameFrame);
          try {
              client.start();
          } catch (UnknownHostException ex) {
        	  client.stop();
        	  throw ex;
          } catch (IOException ex) {
        	  client.stop();
        	  throw ex;
          }
      }
  }
  
  public void shutdown() {
      if (client != null) {
          client.stop();
      }
      if (server != null) {
          server.stop();
      }
  }
	
}
