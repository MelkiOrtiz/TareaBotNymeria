package umg.ejercicios;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import umg.ejercicios.botTelegram.PokemonBot;
import umg.ejercicios.botTelegram.tareaBot;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        try {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        //Bot bot = new Bot();
            tareaBot Nymerya = new tareaBot();
        botsApi.registerBot(Nymerya);
        System.out.println("Esto es un bot funcionando . . . . ");
        }
        catch (Exception ex) {
            System.out.println("Error"+ex.getMessage());
        }

    }
}