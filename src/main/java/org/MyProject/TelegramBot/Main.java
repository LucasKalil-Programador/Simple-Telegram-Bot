package org.MyProject.TelegramBot;

import java.util.List;

import org.MyProject.ThreadPoolSys.BlockingThreadPool;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;

public class Main {
	private static final String TOKEN = "5325829367:AAFmkCc7ydsMRXIJZ1dEGHWHzRAh5DDtiHI";
	private static BlockingThreadPool threadPool = new BlockingThreadPool(12);
	private static TelegramBot bot;

	public static void main(String[] args) {
		bot = new TelegramBot(TOKEN);
		bot.setUpdatesListener(Main::On_ReceiveMenssageUpdates, new
		GetUpdates().allowedUpdates("message"));
		
	}

	private static int On_ReceiveMenssageUpdates(List<Update> updates) {
		for (Update update : updates) {
			threadPool.execute(() -> On_Menssage(update));
		} 

		return UpdatesListener.CONFIRMED_UPDATES_ALL;
	}
	
	private static void On_Menssage(Update update) {
		InlineKeyboardMarkup markup = new InlineKeyboardMarkup(
				new InlineKeyboardButton("opção A").callbackData("A"), 
				new InlineKeyboardButton("opção B").callbackData("B"));
		
		SendMessage sendMessage = new SendMessage(update.message().chat().id(), "ola");
		sendMessage.replyMarkup(markup);
		SendResponse response = bot.execute(sendMessage);
		System.out.println(response.isOk());
	}
}
