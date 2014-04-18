package com.blakjack.clueless.common;

import com.blakjack.clueless.client.UserEngine;
import java.util.List;

public class GameBoard
{
   private Room study = new Room("Study");
   private Room hall = new Room("Hall");
   private Room lounge = new Room("Lounge");
   private Room library = new Room("Library");
   private Room billiard = new Room("Billiard Room");
   private Room dining = new Room("Dining Room");
   private Room conservatory = new Room("Conservatory");
   private Room ballroom = new Room("Ballroom");
   private Room kitchen = new Room("Kitchen");
      
   //create hallways
   private Room hallSH = new Room();
   private Room hallHL = new Room();
   private Room hallLD = new Room();
   private Room hallDK = new Room();
   private Room hallBK = new Room();
   private Room hallCB = new Room();
   private Room hallLC = new Room();
   private Room hallSL = new Room();
   private Room hallLB = new Room();
   private Room hallBD = new Room();
   private Room hallHB = new Room();
   private Room hallBB = new Room();

   public GameBoard()
   {
      connectRooms();
   }
   
   private void connectRooms()
   {
      //connect rooms
      connectSC(study, kitchen);
      connectSC(lounge, conservatory);
      connectLR(study, hallSH);
      connectLR(hallSH, hall);
      connectLR(hall, hallHL);
      connectLR(hallHL, lounge);
      connectLR(hallBK, kitchen);
      connectLR(ballroom, hallBK);
      connectLR(hallCB, ballroom);
      connectLR(conservatory, hallCB);
      connectLR(library, hallLB);
      connectLR(hallLB, billiard);
      connectLR(billiard, hallBD);
      connectLR(hallBD, dining);
      connectUD(hallLC, conservatory);
      connectUD(library, hallLC);
      connectUD(hallSL, library);
      connectUD(study, hallSL);
      connectUD(hall, hallHB);
      connectUD(hallHB, billiard);
      connectUD(billiard, hallBB);
      connectUD(hallBB, ballroom);
      connectUD(lounge, hallLD);
      connectUD(hallLD, dining);
      connectUD(dining, hallDK);
      connectUD(hallDK, kitchen);
   }

   private void connectLR(Room left, Room right)
   {
      left.setRight(right);
      right.setLeft(left);
   }
   
   private void connectUD(Room up, Room down)
   {
      up.setDown(down);
      down.setUp(up);
   }
   
   private void connectSC(Room sc1, Room sc2)
   {
      sc1.setShortcut(sc2);
      sc2.setShortcut(sc1);
   }
   
   public Room getStudy()
   {  return study; }
   
   public Room getHall()
   {  return hall;   }
   
   public Room getLounge()
   {  return lounge; }
   
   public Room getLibrary()
   {  return library;   }
   
   public Room getBilliard()
   {  return billiard;  }
   
   public Room getDining()
   {  return dining;    }
   
   public Room getConservatory()
   {  return conservatory; }
   
   public Room getBallroom()
   {  return ballroom;  }
   
   public Room getKitchen()
   {  return kitchen;   }
   
   
}