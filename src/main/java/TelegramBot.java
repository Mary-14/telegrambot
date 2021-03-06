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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TelegramBot extends TelegramLongPollingBot {

    ArrayList<String> users = new ArrayList<>();
    HashMap<String, String> cities = new HashMap<>();
    boolean addMode = false;

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            String message = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            int messageId = update.getMessage().getMessageId();

            if (update.getMessage().hasPhoto()) {
                List<PhotoSize> photos = update.getMessage().getPhoto();
                sendPhoto(chatId, photos.get(0).getFileId());
            } else if (addMode) {
                addCity(message, chatId);
                addMode = false;
            }else {
                switch (message) {
                    case "/addCity":
                        sendMessage("Введите город: ", chatId);
                        addMode = true;

                        break;
                    case "/getCity":
                        getCities(chatId);
                        break;

                    case "/showKeyboard":
                        showKeyboard("Клавиатура активирована ", chatId, messageId);
                        break;
                    case "/hideKeyboard":
                        hideKeyboard("Клавиатура скрыта", chatId, messageId);
                    default:
                        sendMessage(message, chatId, messageId);
                }

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

    private void sendMessage(String text, long chatId) {
        SendMessage sendMessage = new SendMessage()
                .setText(text)
                .setChatId(chatId);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(String text, long chatId, int messageId) {
        SendMessage sendMessage = new SendMessage()
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
        ReplyKeyboardMarkup rkm = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add("/showKeyboard");
        row.add("/hideKeyboard");
        row.add("/getMeme");
        row.add("/register");
        row.add("/unregister");
        row.add("/getRandomUser");
        keyboard.add(row);
        rkm.setKeyboard(keyboard);

        SendMessage sendMessage = new SendMessage()
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

    private void hideKeyboard(String text, long chatId, int messageId) {
        ReplyKeyboardRemove rkm = new ReplyKeyboardRemove();

        SendMessage sendMessage = new SendMessage()
                .setText(text)
                .setChatId(chatId)
                .setReplyToMessageId(messageId);

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }
    private void addCity(String text, long chatId) {
        String [] кусочки = text.split(" ");
        cities.put (кусочки[0], кусочки [1]);

       sendMessage("Город добавлен:", chatId);

    }

    private void getCities(long chatId) {
        String result = "Города: \n";
        for (Map.Entry<String, String> строчка : cities.entrySet()) {
            result += строчка.getKey() + " ~ " + строчка.getValue();
            result += "\n";

        }
        sendMessage(result, chatId);

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
