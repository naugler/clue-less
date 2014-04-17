package com.blakjack.clueless.common;

import com.blakjack.clueless.server.GameEngine;

public class Movement 
   {
      //shortcut move is always valid for corners
      public boolean isShortcutValid(SquareTile[] board, Player player);
      {
         int position = player.getPosition();
         if ((position == 0) || (position == 4) || (position == 20) || (position == 24))
            return true;
         else
            return false;
      }
      
     //left move is valid for horizontal hallways and non-leftmost rooms
      public boolean isLeftValid(SquareTile[] board, List<UserEngine> playerList, Player player);
      {
         int position = player.getPosition();
         
         //rule out left column
         if (position % 5 == 0)
            return false;
            
         //rule out vertical hallways
         if ((position % 10 == 7) || (position % 10 == 9))
            return false;
         
         //allow all horizontal hallways
         if ((position % 10 == 1) || (position % 10 == 3))
            return true;
          
         //check for blocked hallway
         else
         {
            boolean emptyhall = true;
            
            int num = playerList.size();
            
            for (i = 0; i < size; i++)
            {
               int j = playerList<i>.getPosition();
               if (j == position - 1)
                  emptyhall = false;
            }
               return emptyhall;
         }          
         
      }
      
      public boolean isRightValid(SquareTile[] board, List<UserEngine> playerList, Player player)
      {
         int position = player.getPosition();
         
         //disallow right column
         if ((position % 10 == 4) || (position % 10 == 9))
            return false;
         
         //disallow other vertical hallways
         if ((position % 10 == 5) || (position % 10 == 7))
            return false;
         
         //allow in horizontal hallways
         if ((position % 10 == 1) || (position % 10 == 3))
            return true;
         
         //check for adjacent player
         else
         {
            boolean emptyhall = true;
            
            int num = playerList.size();
            
            for (i = 0; i < size; i++)
            {
               int j = playerList<i>.getPosition();
               if (j == position + 1)
                  emptyhall = false;
            }
               return emptyhall;
         }
         
      }
      
      public boolean isUpValid(SquareTile[] board, List<UserEngine> playerList, Player player)
      {
         int position = player.getPosition();
         
         //rule out top row
         if (position < 5)
            return false;
         
         //rule out horizontal hallways
         if ((position % 10 == 1) || (position % 10 == 3))
            return false;
         
         //allow all vertical hallways
         if ((position % 10 == 5) || (position % 10 == 7) || (position % 10 == 9))
            return true;
      
         //check for blocked hallways
         else
         {
            boolean emptyhall = true;
            
            int num = playerList.size();
            
            for (i = 0; i < size; i++)
            {
               int j = playerList<i>.getPosition();
               if (j == position - 5)
                  emptyhall = false;
            }
            
            return emptyhall;
         }
      }
      
      public boolean isDownValid(SquareTile[] board, List<UserEngine> playerList, Player player)
      {
         int position = player.getPosition();
         
         //rule out bottom row
         if (position >= 20)
            return false;
            
         //rule out horizontal hallways
         if ((position % 10 == 1) || (position % 10 == 3))
            return false;
         
         //allow all vertical hallways
         if ((position % 10 == 5) || (position % 10 == 7) || (position % 10 == 9))
            return true;      
            
         //check for blocked hallways
         else
         {
            boolean emptyhall = true;
            
            int num = playerList.size();
            
            for (i = 0; i < size; i++)
            {
               int j = playerList<i>.getPosition();
               if (j == position + 5)
                  emptyhall = false;
            }
               return emptyhall;
         }  
      }
	}
	
}
