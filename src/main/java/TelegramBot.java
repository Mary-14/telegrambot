import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.api.objects.PhotoSize;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

public class TelegramBot extends TelegramLongPollingBot {

    ArrayList<String> users = new ArrayList<>();

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()){
            String message = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            int messageId = update.getMessage().getMessageId();

            if (update.getMessage().hasPhoto()) {
                List <PhotoSize> photos = update.getMessage().getPhoto();
                sendPhoto(chatId, photos.get(0).getFileId());
            }

            if (message.equals("/showKeyboard")){
                showKeyboard("Клавиатура активирована ", chatId, messageId);
            } else if (message.equals("/hideKeyboard")) {
                hideKeyboard("Клавиатура скрыта", chatId, messageId);
            } else if (message.equals("/sendPhoto")) {
                sendPhoto (chatId, "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcStAW2R0fWAMQcqiN0YnF5Jt93bAocLBnX-jJk7W9uwHUBtfL0B");
            } else if ( message.equals("/register")) {
                User user = update.getMessage().getFrom();
                users.add(user.getUserName());
            } else if ( message.equals("/unregister")) {
                User user = update.getMessage().getFrom();
                users.remove(user.getUserName());
            } else if ( message.equals("/getRandomUser")) {
                if (users.size() > 0 ) {
                    String randomUserName = users.get((int) (Math.random() * users.size()));
                    sendMessage("Поздравляю, @" + randomUserName + ", вы выиграли!", chatId, messageId);
                } else {
                    sendMessage("Никто не играет." , chatId, messageId);
                }
            } else  {
                sendMessage(message, chatId, messageId );
            }

        }


    }

    @Override
    public String getBotUsername() {
        return "yyy9687905bot";
    }

    @Override
    public String getBotToken() {
        return "508522025:AAEuvt2dGo71d1ggEwshAhMZ2M0s4fHFxFw";
    }

    private void sendMessage(String text, long chatId, int messageId) {
        SendMessage sendMessage = new SendMessage ()
                .setText(text)
                .setChatId(chatId)
                .setReplyToMessageId(messageId);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void showKeyboard(String text, long chatId, int messageId) {
        ReplyKeyboardMarkup rkm = new  ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add("/showKeyboard");
        row.add("/hideKeyboard");
        row.add("/getMeme");
        row.add("/register");
        row.add ("/unregister");
        row.add("/getRandomUser");
        keyboard.add(row);
        rkm.setKeyboard(keyboard);

        SendMessage sendMessage = new SendMessage ()
                .setText(text)
                .setChatId(chatId)
                .setReplyToMessageId(messageId)
                .setReplyMarkup(rkm);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }
    private void hideKeyboard(String text, long chatId, int messageId){
        ReplyKeyboardRemove rkm = new ReplyKeyboardRemove();

        SendMessage sendMessage = new SendMessage ()
                .setText(text)
                .setChatId(chatId)
                .setReplyToMessageId(messageId);

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }
    private void sendPhoto(long chatId, String photo)  {
        SendPhoto request = new SendPhoto();
        request.setChatId(chatId);
        request.setPhoto(photo);
        try {
            sendPhoto(request);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
