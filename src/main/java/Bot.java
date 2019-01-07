

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.api.methods.updatingmessages.EditMessageCaption;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;
import com.vdurmont.emoji.EmojiParser;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static java.lang.StrictMath.toIntExact;


public class Bot extends TelegramLongPollingBot {

    public static String url = "jdbc:postgresql://ec2-54-75-230-41.eu-west-1.compute.amazonaws.com:5432/d97176rrg5ibqd";
    public static String user = "ncveawzpvjrxzv";
    public static String password = "90830a4a2aacfe1a436156df98cd435dfafe571b03e15ef93b4eae98604c2b8a";

    public static String hello = EmojiParser.parseToUnicode(":dizzy: Eventor готовий допомогти!");
    String sorry = EmojiParser.parseToUnicode(":cloud: Вибачте, але на даний момент цієї події немає");



    public static void main(String[] args)
    {
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try
        {
            telegramBotsApi.registerBot(new Bot());
        }
        catch (TelegramApiRequestException e)
        {
            e.printStackTrace();
        }

    }

    public void sendImageFromUrl(Message message,String url,String text)
    {
        SendPhoto sendPhotoRequest = new SendPhoto();
        sendPhotoRequest.setChatId(message.getChatId().toString());
        sendPhotoRequest.setPhoto(url);
        sendPhotoRequest.setCaption(text);
        try
        {
            sendPhoto(sendPhotoRequest);
        }
        catch (TelegramApiException e)
        {
            e.printStackTrace();
        }
    }

    public void sendMsg(Message message, String text)
    {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());
        //sendMessage.setReplyToMessageId(message.getMessageId()); - return message history
        sendMessage.setText(text);
        try
        {
            setButton(sendMessage);
            sendMessage(sendMessage);
        }
        catch (TelegramApiException e)
        {
            e.printStackTrace();
        }
    }

    public void setButton(SendMessage sendMessage)
    {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false); //true if close after enter; false if on always

        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow keyboardNullRow = new KeyboardRow();
        KeyboardRow keyboardZeroRow = new KeyboardRow();
        KeyboardRow keyboardOneRow = new KeyboardRow();
        KeyboardRow keyboardTwoRow = new KeyboardRow();
        KeyboardRow keyboardThreeRow = new KeyboardRow();
        KeyboardRow keyboardFourRow = new KeyboardRow();
        KeyboardRow keyboardFiveRow = new KeyboardRow();
        KeyboardRow keyboardSixRow = new KeyboardRow();
        KeyboardRow keyboardLastRow = new KeyboardRow();

        keyboardZeroRow.add(new KeyboardButton("Last <"));
        keyboardNullRow.add(new KeyboardButton("- at this week -"));
        keyboardZeroRow.add(new KeyboardButton("> Next"));
        keyboardOneRow.add(new KeyboardButton("#Java"));
        keyboardOneRow.add(new KeyboardButton("#Design"));
        keyboardTwoRow.add(new KeyboardButton("#C++"));
        keyboardTwoRow.add(new KeyboardButton("#Marketing"));
        keyboardThreeRow.add(new KeyboardButton("#C"));
        keyboardThreeRow.add(new KeyboardButton("#Developing"));
        keyboardFourRow.add(new KeyboardButton("#Python"));
        keyboardFourRow.add(new KeyboardButton("#JavaScript"));
        keyboardFiveRow.add(new KeyboardButton("#.NET"));
        keyboardFiveRow.add(new KeyboardButton("#iOS/macOS"));
        keyboardSixRow.add(new KeyboardButton("#Android"));
        keyboardSixRow.add(new KeyboardButton("#Meetups"));
        keyboardLastRow.add(new KeyboardButton("#help"));
        keyboardRowList.add(keyboardNullRow);
        keyboardRowList.add(keyboardZeroRow);
        keyboardRowList.add(keyboardOneRow);
        keyboardRowList.add(keyboardTwoRow);
        keyboardRowList.add(keyboardThreeRow);
        keyboardRowList.add(keyboardFourRow);
        keyboardRowList.add(keyboardLastRow);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
    }

    public void onUpdateReceived(Update update)
    {

        String type = "";
        String week = "";
        Message message = update.getMessage();

        if(update.hasCallbackQuery())
        {
            String chat_id = update.getCallbackQuery().getMessage().getChatId().toString();
            long message_id = update.getCallbackQuery().getMessage().getMessageId();
            String call_data = update.getCallbackQuery().getData();

                    Connection connection;
                    try
                    {
                        Class.forName("org.postgresql.Driver");
                        connection = DriverManager.getConnection(url,user,password);
                        if (!connection.isClosed()) {
                            Statement statement = connection.createStatement();
                            ResultSet weekSet;
                            String query = "SELECT * FROM events WHERE id  = '" + call_data + "'";
                            weekSet = statement.executeQuery(query);
                            while (weekSet.next()) {
                                String title = weekSet.getString("title");
                                String speakerAbout = weekSet.getString("speakerabout");
                                String organizerAbout = weekSet.getString("organizerabout");
                                String link = weekSet.getString("link");

                                String postExtended = EmojiParser.parseToUnicode(":star: " + title + "\n===================\n" +
                                        ":hash: ПРО СПІКЕРІВ: \n" + speakerAbout + "\n===================\n" +
                                        ":hash: ПРО ОРГАНІЗАТОРІВ: \n" + organizerAbout + "\n===================\n" +
                                        ":hash: Link: " + link + "\n===================\n" );

                                EditMessageCaption new_message = new EditMessageCaption()
                                        .setChatId(chat_id)
                                        .setMessageId(toIntExact(message_id))
                                        .setCaption(postExtended);
                                try {
                                    execute(new_message);
                                } catch (TelegramApiException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }

        }


        if (update.hasMessage() && update.getMessage().hasText())
        {

            switch (message.getText())
            {
                case "/start":
                    sendMsg(message, hello);
                    break;
                case "Last <":
                    week = "last";
                    break;
                case "- at this week -":
                    week = "this";
                    break;
                case "> Next":
                    week = "next";
                    break;
                case "#Java":
                    type = "java";
                    break;
                case "#Design":
                    type = "design";
                    break;
                case "#C++":
                    type = "c_plus_plus";
                    break;
                case "#Developing":
                    type = "developing";
                    break;
                case "#Python":
                    type = "python";
                    break;
                case "#JavaScript":
                    type = "javaScript";
                    break;
                case "#.NET":
                    type = "dot_net";
                    break;
                case "#Marketing":
                    type = "marketing";
                    break;
                case "#iOS/macOS":
                    type = "ios_mac";
                    break;
                case "#Meetups":
                    type = "meetups";
                    break;
                case "#Android":
                    type = "android";
                    break;
                case "#help":
                    String help = EmojiParser.parseToUnicode(" :chains: Ваші іструкції:\n===================\n Щоб знайти івент:\n===================\n- Виберіть категорію або тиждень;\n- Оберіть ваш івент із списку наданих;\n- Насолоджуйтесь!\n===================\nЗвяжіться з нами:\n eventor.rebels@yahoo.com\n===================\n");
                    sendMsg(message, help);
                    break;
                default:
                    sendMsg(message, sorry);
            }
        }
            Connection connection;
            try
            {
                Class.forName("org.postgresql.Driver");
                connection = DriverManager.getConnection(url,user,password);
                if (!connection.isClosed())
                {
                    Statement statement = connection.createStatement();
                    ResultSet weekSet;
                    String query = "SELECT * FROM events WHERE week = '" + week + "' OR category ='" + type + "'";
                    weekSet = statement.executeQuery(query);

                        while (weekSet.next())
                        {
                            String id = weekSet.getString("id");
                            String title = weekSet.getString("title");
                            String whenStart = weekSet.getString("whenstart");
                            String whereStart = weekSet.getString("wherestart");
                            String description = weekSet.getString("description");
                            String speaker = weekSet.getString("speaker");
                            String organizer = weekSet.getString("organizer");
                            String image = weekSet.getString("image");

                            String post = EmojiParser.parseToUnicode(
                                      ":star: " + title +
                                            "\n===================\n" +
                                            ":hash: КОЛИ:  " + whenStart
                                            + "\n===================\n" +
                                              ":hash: ДЕ:  " + whereStart
                                             + "\n===================\n" +
                                            ":hash: ОПИС:  " + description +
                                            "\n===================\n" +
                                            ":loudspeaker: " + speaker +
                                            "\n===================\n" +
                                             "ОРГАНІЗОВУЄ: " + organizer +
                                            "\n===================\n");

                            String eventId = id; //id - is always unique

                            InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
                            InlineKeyboardButton button = new InlineKeyboardButton();

                            List<List<InlineKeyboardButton>> buttons = new ArrayList();
                            List buttonList = new ArrayList();
                            button.setText("Детальніше");
                            button.setCallbackData(eventId);
                            buttonList.add(button);
                            buttons.add(buttonList);
                            keyboardMarkup.setKeyboard(buttons);

                            SendPhoto sending = new SendPhoto();
                            sending.setChatId(message.getChatId().toString());
                            sending.setPhoto(image);
                            sending.setCaption(post);
                            sending.setReplyMarkup(keyboardMarkup);
                            try {
                                sendPhoto(sending);
                            } catch (TelegramApiException e) {
                                e.printStackTrace();
                            }

                        }

                }


            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        public String getBotUsername ()
        {
            return "Eventor";
        }

        public String getBotToken ()
        {
            return "635921682:AAFUk8LS51wda3JQ02LSN3N3kOVHscGK8-c";
        }


    }
