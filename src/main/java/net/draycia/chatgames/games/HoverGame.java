package net.draycia.chatgames.games;

import net.draycia.chatgames.ChatGames;

public class HoverGame extends ChatGame {
   private String word;
   private long startTime;
   private long reward;
   private ChatGames main;

   public String getWord() {
      return this.word;
   }

   public String getDisplayWord() {
      return this.getWord();
   }

   public long getStartTime() {
      return this.startTime;
   }

   long getReward() {
      return this.reward;
   }

   public HoverGame(ChatGames main) {
      super(main);
      this.main = main;
      this.startTime = System.currentTimeMillis();
      this.word = this.getRandomWord();
      if (main.getConfig().getBoolean("StaticRewards")) {
         this.reward = main.getConfig().getLong("RewardPerWin");
      } else {
         this.reward = main.getConfig().getLong("RewardPerCharacter") * (long)this.getWord().length();
      }

      String message = main.getConfig().getString("Language.TypeStart");
      if (message == null) {
         message = "&eGames &8» &aHover and type the word shown!";
      }

      super.onStart(message);
   }
}
