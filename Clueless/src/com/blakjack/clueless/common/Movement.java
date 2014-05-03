package com.blakjack.clueless.common;

import java.util.List;

public class Movement 
   {
      public static boolean isShortcutValid(GameBoard board, Player player)
      {
         Room room = player.getRoom();
         Room sc = room.getShortcut();
         if (sc == null)
            return false;
         else return true;
      }
     
      public static boolean isSuggestValid(GameBoard board, Player player)
      {
         Room room = player.getRoom();
         if (room.getHallway() == false)
            return true;
         else 
         return false;
      }
     
     //private method used in all movement checks to return false for full hallways
      private static boolean validMove(Room room, List<Player> playerList)
      {
         boolean valid = false;
         
         if (room.getHallway() == false)
            valid = true;
         else
         {
            int num = playerList.size();
            for (int i = 0; i < num; i++)
            {
               Room j = playerList.get(i).getRoom();
               if (j == room)
                  valid = false;
            }
         }
        return valid; 
      }
     
     
      public static boolean isLeftValid(GameBoard board, List<Player> playerList, Player player)
      {
         Room room = player.getRoom();
         Room left = room.getLeft();
         if (left != null)
         {
          boolean valid = validMove(left, playerList);
          return valid;  
         }
         else return false;
      }      
      
      public static boolean isRightValid(GameBoard board, List<Player> playerList, Player player)
      {
         Room room = player.getRoom();
         Room right = room.getRight();
         if (right != null)
         {
            boolean valid = validMove(right, playerList);
            return valid;
         }
         else return false; 
      }
      
      public static boolean isUpValid(GameBoard board, List<Player> playerList, Player player)
      {
         Room room = player.getRoom();
         Room up = room.getUp();
         if (up != null)
         {
            boolean valid = validMove(up, playerList);
            return valid;
         }
         else 
            return false;
      }
      
      public static boolean isDownValid(GameBoard board, List<Player> playerList, Player player)
      {
         Room room = player.getRoom();
         Room down = room.getDown();
         if (down != null)
         {
            boolean valid = validMove(down, playerList);
            return valid;
         }
         else
            return false;
      }
      
      //shortcut move is always valid for corners
      public static boolean isShortcutValid(SquareTile[] board, Player player)
      {
         int position = player.getPosition();
         if ((position == 0) || (position == 4) || (position == 20) || (position == 24))
            return true;
         else
            return false;
      }
      
      public static boolean isSuggestValid(SquareTile[] board, Player player)
     {
         Room position = player.getRoom();
         if (position.getHallway() == false)
            return true;
         else return false;
     }
      
     //left move is valid for horizontal hallways and non-leftmost rooms
      public static boolean isLeftValid(SquareTile[] board, List<Player> playerList, Player player)
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
            
            for (int i = 0; i < num; i++)
            {
               int j = playerList.get(i).getPosition();
               if (j == position - 1)
                  emptyhall = false;
            }
               return emptyhall;
         }          
         
      }
      
      public static boolean isRightValid(SquareTile[] board, List<Player> playerList, Player player)
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
            
            for (int i = 0; i < num; i++)
            {
               int j = playerList.get(i).getPosition();
               if (j == position + 1)
                  emptyhall = false;
            }
               return emptyhall;
         }
         
      }
      
      public static boolean isUpValid(SquareTile[] board, List<Player> playerList, Player player)
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
            
            for (int i = 0; i < num; i++)
            {
               int j = playerList.get(i).getPosition();
               if (j == position - 5)
                  emptyhall = false;
            }
            
            return emptyhall;
         }
      }
      
      public static boolean isDownValid(SquareTile[] board, List<Player> playerList, Player player)
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
            
            for (int i = 0; i < num; i++)
            {
               int j = playerList.get(i).getPosition();
               if (j == position + 5)
                  emptyhall = false;
            }
               return emptyhall;
         }  
      }
   
}
