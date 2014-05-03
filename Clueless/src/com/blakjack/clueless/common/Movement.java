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
   
}
