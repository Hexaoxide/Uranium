package net.draycia.chatgames.games;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import net.draycia.chatgames.ChatGames;
import net.kyori.text.TextComponent;
import net.kyori.text.adapter.bukkit.TextAdapter;
import net.kyori.text.event.HoverEvent;
import net.kyori.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public abstract class ChatGame {
   private ChatGames main;
   private List rewardCommands;

   public abstract String getWord();

   public abstract String getDisplayWord();

   abstract long getStartTime();

   abstract long getReward();

   public List getRewardCommands() {
      return this.rewardCommands;
   }

   ChatGame(ChatGames main) {
      this.main = main;
      this.rewardCommands = main.getConfig().getStringList("CommandsOnWin");
   }

   String getRandomWord() {
      File words = new File(this.main.getDataFolder(), "words.txt");

      try {
         RandomAccessFile file = new RandomAccessFile(words, "r");

         String line;
         for(line = null; line == null; line = file.readLine()) {
            long result = ThreadLocalRandom.current().nextLong(words.length());
            file.seek(result);
            file.readLine();
         }

         return line;
      } catch (IOException var6) {
         var6.printStackTrace();
         return "pizza";
      }
   }

   public void onStart(String message) {
      message = ChatColor.translateAlternateColorCodes('&', message);
      TextComponent component = (TextComponent)LegacyComponentSerializer.INSTANCE.deserialize(message).hoverEvent(HoverEvent.of(HoverEvent.Action.SHOW_TEXT, TextComponent.of(this.getDisplayWord())));
      TextAdapter.sendComponent((Iterable)Bukkit.getOnlinePlayers(), component);
   }

   public void onSuccess(Player player) {
      String message;
      if (this instanceof HoverGame) {
         message = this.main.getConfig().getString("Language.TypeSuccess");
      } else {
         message = this.main.getConfig().getString("Language.UnscrambleSuccess");
      }

      if (message == null) {
         message = "&eGames &8» &a%name% &6has typed %word% in %seconds% seconds!";
      }

      double duration = (double)(System.currentTimeMillis() - this.getStartTime()) / 1000.0D;
      message = message.replace("%name%", player.getName()).replace("%seconds%", this.main.getDecimalFormat().format(duration)).replace("%word%", this.getWord());
      message = ChatColor.translateAlternateColorCodes('&', message);
      Iterator var5 = Bukkit.getOnlinePlayers().iterator();

      while(var5.hasNext()) {
         Player onlinePlayer = (Player)var5.next();
         onlinePlayer.sendMessage(message);
      }

      var5 = this.getRewardCommands().iterator();

      while(var5.hasNext()) {
         String reward = (String)var5.next();
         reward = reward.replace("%player%", player.getName()).replace("%reward%", Long.toString(this.getReward()));
         Bukkit.dispatchCommand(Bukkit.getConsoleSender(), reward);
      }

      this.onFinish();
   }

   public void onFailure() {
      String message = this.main.getConfig().getString("Language.Failure");
      if (message == null) {
         message = "&eGames &8» &7Nobody typed the word in time!";
      }

      message = message.replace("%word%", this.getWord());
      message = ChatColor.translateAlternateColorCodes('&', message);
      Iterator var2 = Bukkit.getOnlinePlayers().iterator();

      while(var2.hasNext()) {
         Player onlinePlayer = (Player)var2.next();
         onlinePlayer.sendMessage(message);
      }

      this.onFinish();
   }

   private void onFinish() {
      this.main.startNewGame();
   }
}
