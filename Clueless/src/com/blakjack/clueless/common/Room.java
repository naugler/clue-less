package com.blakjack.clueless.common;

public class Room
{
   //declare attributes
   private boolean empty;
   private boolean hallway;
   private Room left;
   private Room right;
   private Room up;
   private Room down;
   private Room shortcut;
   private String name;
   
   //for instantiating hallways   
   public Room()
   {
      setHallway(true);
      setAllNull();
      setName("hallway");
   }
   
   //for instantiating rooms
   public Room(String roomname)
   {
      setName(roomname);
      setHallway(false);
      setAllNull();
   }
   
   public void setAllNull()
   {
      setEmpty(true);
      setLeft(null);
      setRight(null);
      setUp(null);
      setDown(null);
      setShortcut(null);
   }
   
   public void setEmpty(boolean empt)
   {
      empty = empt;
   }
   
   public boolean getEmpty()
   {
      return empty;
   }
   
   public void setHallway(boolean hall)
   {
      hallway = hall;
   }
   
   public boolean getHallway()
   {
      return hallway;
   }
   
   public void setLeft(Room leftroom)
   {
      left = leftroom;
   }
   
   public Room getLeft()
   {
      return left;
   }
   
   public void setRight(Room rightroom)
   {
      right = rightroom;
   }
   
   public Room getRight()
   {
      return right;
   }
   
   public void setUp(Room uproom)
   {
      up = uproom;
   }
   
   public Room getUp()
   {
      return up;
   }
   
   public void setDown(Room downroom)
   {
      down = downroom;
   }
   
   public Room getDown()
   {
      return down; 
   }

   public void setName(String roomname)
   {
      name = roomname;
   }
   
   public String getName()
   {
      return name;
   }

   public void setShortcut(Room sc)
   {
      shortcut = sc;
   } 
   
   public Room getShortcut()
   {
      return shortcut;
   }
}