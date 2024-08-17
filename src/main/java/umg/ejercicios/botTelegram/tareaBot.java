package umg.ejercicios.botTelegram;

import org.json.JSONArray;
import org.json.JSONObject;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

public class tareaBot extends TelegramLongPollingBot {

    private static final String BING_API_KEY = "TU_CLAVE_DE_API";
    private static final String BING_API_URL = "https://api.bing.microsoft.com/v7.0/images/search?q=";
    private static final String API_KEY = "e9e49aa40e6c887b83e91d06";
    private static final String API_URL = "https://v6.exchangerate-api.com/v6/" + API_KEY + "/latest/EUR";

    @Override
    public String getBotUsername() {
        return "NymeriaVariant_bot";
    }

    @Override
    public String getBotToken() {
        return "6587765280:AAFpuoyJLEugt-GCGrxxR_ybLQm8fZzrnlg";
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            System.out.println("User id: " + chatId + " Message: " + messageText);

            if (messageText.equals("/start")) {
                sendMainMenu(chatId);
            } else if (messageText.equals("/juego_de_tronos")) {
                sendGameOfThronesMenu(chatId);
            } else if (messageText.equals("/info")) {
                sendText(chatId, "Carnet: 0905-23-6329\nNombre: Melki Ortiz\nSemestre: 4to Semestre");
            } else if (messageText.equals("/progra")) {
                sendText(chatId, "En el arte de la programación, como en la espada, cada línea de código es un paso hacia la perfección." +
                        " No importa si enfrentamos errores como si fueran lobos salvajes o si el camino parece tan intrincado como los laberintos de Invernalia," +
                        " la programación es nuestra forma de dar vida a nuestras ideas. Cada bug que resolvemos es como una batalla ganada, cada nuevo programa, " +
                        "una victoria en nuestra propia saga. No es solo un trabajo; es el arte de construir nuestro propio destino, pieza a pieza, con la misma tenacidad que cualquier Stark enfrentaría los desafíos de Poniente." +
                        "|Valar morghulis|");
            } else if (messageText.equals("/hola")) {
                sendGreeting(chatId, update);
            } else if (messageText.startsWith("/cambio")) {
                handleCurrencyConversion(chatId, messageText);
            } else if (messageText.equalsIgnoreCase("Valar morghulis")) {
                sendText(chatId, "Valar Dohaeris");
            } else if (messageText.startsWith("/raven")) {
                handleRavenMessage(chatId, messageText);
            } else if (messageText.equalsIgnoreCase("/faces")) {
                sendText(chatId, "Una chica no es nadie. Un chico no es nadie. Todos los hombres deben morir.");
            } else if (messageText.startsWith("/grupal")) {
                handleGroupMessage(messageText);
            } else if (messageText.startsWith("/personaje")) {
                handleCharacterInfo(chatId, messageText);
            } else if (messageText.equalsIgnoreCase("/casa")) {
                assignHouse(chatId);
            } else if (messageText.startsWith("/help")) {
                handleHelp(chatId, messageText);
            }
        }
    }

    private void sendMainMenu(long chatId) {
        sendText(chatId, "¡El Invierno Se Acerca!\nBienvenido a NymeriaBot Aquí están los comandos disponibles:\n"
                + "/info\n"
                + "/progra\n"
                + "/hola\n"
                + "/cambio\n"
                + "/grupal\n"
                + "/juego_de_tronos\n"
                + "Usar el siguiete comando para obtener informacion de los demas:\n"
                + "/help");
    }

    private void sendGameOfThronesMenu(long chatId) {
        sendText(chatId, "Menú de Juego de Tronos:\n"
                + "/faces\n"
                + "/raven\n"
                + "/personaje\n"
                + "/casa");
    }

    private void sendGreeting(long chatId, Update update) {
        String nombreUsuario = update.getMessage().getFrom().getFirstName();
        LocalDateTime ahora = LocalDateTime.now();
        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("EEEE dd MMMM yyyy, HH:mm:ss");
        String fechaFormateada = ahora.format(formatoFecha);
        sendText(chatId, "Hola " + nombreUsuario + ", hoy es " + fechaFormateada);
    }

    private void handleCurrencyConversion(long chatId, String messageText) {
        try {
            String[] partes = messageText.split(" ");
            double euros = Double.parseDouble(partes[1]);
            double tipoCambio = obtenerTipoCambio("GTQ");
            if (tipoCambio == -1) {
                sendText(chatId, "Error al obtener el tipo de cambio.");
            } else {
                double conversion = euros * tipoCambio;
                String quetzales = String.format("%.2f", conversion);
                sendText(chatId, euros + " Euros son " + quetzales + " Quetzales.");
            }
        } catch (Exception e) {
            sendText(chatId, "Por favor ingresa un valor válido para el cambio.");
        }
    }

    private void handleRavenMessage(long chatId, String messageText) {
        String[] partes = messageText.split(" ", 3);
        if (partes.length == 3) {
            long destinatarioId = Long.parseLong(partes[1]);
            String mensaje = partes[2];
            sendText(destinatarioId, "Mensaje de cuervo: " + mensaje);
            sendText(chatId, "Tu cuervo ha sido enviado.");
        } else {
            sendText(chatId, "Formato incorrecto. Usa: /raven [user_id] [mensaje]");
        }
    }

    private void handleGroupMessage(String messageText) {
        String mensaje = messageText.substring(8).trim();
        List<Long> listaChats = List.of(6688363556L, 1533824724L, 6597569075L, 123456789L); // Reemplaza con los IDs de tus compañeros
        for (Long id : listaChats) {
            sendText(id, mensaje);
        }
    }

    private void handleCharacterInfo(long chatId, String messageText) {
        String personaje = messageText.substring(11).trim();
        String apiUrl = "https://anapioficeandfire.com/api/characters?name=" + personaje;
        String infoPersonaje = obtenerInfoPersonaje(apiUrl, personaje);

        sendText(chatId, "Información sobre " + personaje + ": " + infoPersonaje);
    }

    public String obtenerInfoPersonaje(String apiUrl, String personaje) {
        StringBuilder info = new StringBuilder();
        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();

            JSONArray jsonArray = new JSONArray(content.toString());
            if (jsonArray.length() > 0) {
                JSONObject jsonResponse = jsonArray.getJSONObject(0);
                String name = jsonResponse.getString("name");
                String culture = jsonResponse.optString("culture", "Desconocido");
                String titles = jsonResponse.getJSONArray("titles").join(", ");
                String aliases = jsonResponse.getJSONArray("aliases").join(", ");
                String born = jsonResponse.optString("born", "Desconocido");
                String died = jsonResponse.optString("died", "Desconocido");
                String gender = jsonResponse.optString("gender", "Desconocido");
                String playedBy = jsonResponse.getJSONArray("playedBy").join(", ");

                info.append("Nombre: ").append(name)
                        .append("\nCultura: ").append(culture)
                        .append("\nTítulos: ").append(titles)
                        .append("\nAlias: ").append(aliases)
                        .append("\nNacido: ").append(born)
                        .append("\nFallecido: ").append(died)
                        .append("\nGénero: ").append(gender)
                        .append("\nInterpretado por: ").append(playedBy);
            } else {
                info.append("No se encontró información sobre el personaje: ").append(personaje);
            }
        } catch (Exception e) {
            e.printStackTrace();
            info.append("Error al obtener la información.");
        }
        return info.toString();
    }

    private void assignHouse(long chatId) {
        String[] casas = {"Stark", "Lannister", "Targaryen", "Baratheon", "Greyjoy", "Martell", "Tyrell"};
        String[] lemas = {"Winter is Coming", "Hear Me Roar!", "Fire and Blood", "Ours is the Fury", "We Do Not Sow", "Unbowed, Unbent, Unbroken", "Growing Strong"};
        Random random = new Random();
        int indice = random.nextInt(casas.length);
        String casa = casas[indice];
        String lema = lemas[indice];
        sendText(chatId, "Has sido asignado a la Casa " + casa + ". Su lema es: \"" + lema + "\"");
    }

    private void handleHelp(long chatId, String messageText) {
        String[] partes = messageText.split(" ");
        if (partes.length == 1) {
            sendText(chatId, "Comandos disponibles:\n"
                    + "/start - Mostrar el menú principal\n"
                    + "/info - Mostrar información personal\n"
                    + "/progra - Mostrar información sobre la clase de programación\n"
                    + "/hola - Mostrar un saludo con la fecha actual\n"
                    + "/cambio [euros] - Convertir euros a quetzales\n"
                    + "/grupal [mensaje] - Enviar un mensaje grupal\n"
                    + "/juego_de_tronos - Mostrar el menú de Juego de Tronos\n"
                    + "/help [comando] - Obtener información sobre un comando específico");
        } else {
            String comando = partes[1];
            switch (comando) {
                case "/info":
                    sendText(chatId, "/info - Muestra información personal del usuario.");
                    break;
                case "/progra":
                    sendText(chatId, "/progra - Muestra una frase inspiradora sobre la programación.");
                    break;
                case "/hola":
                    sendText(chatId, "/hola - Envía un saludo al usuario con la fecha y hora actuales.");
                    break;
                case "/cambio":
                    sendText(chatId, "/cambio [euros] - Convierte la cantidad especificada de euros a quetzales.");
                    break;
                case "/grupal":
                    sendText(chatId, "/grupal [mensaje] - Envía el mensaje especificado a un grupo de usuarios.");
                    break;
                case "/juego_de_tronos":
                    sendText(chatId, "/juego_de_tronos - Muestra un menú con comandos relacionados con Juego de Tronos.");
                    break;
                case "/faces":
                    sendText(chatId, "/faces - Muestra una frase famosa de Juego de Tronos.");
                    break;
                case "/raven":
                    sendText(chatId, "/raven [user_id] [mensaje] - Envía un mensaje de cuervo a otro usuario.");
                    break;
                case "/personaje":
                    sendText(chatId, "/personaje [nombre] - Muestra información sobre un personaje de Juego de Tronos.");
                    break;
                case "/casa":
                    sendText(chatId, "/casa - Asigna aleatoriamente una casa de Juego de Tronos y muestra su lema.");
                    break;
                default:
                    sendText(chatId, "No se encontró información para el comando: " + comando);
                    break;
            }
        }
    }

    private double obtenerTipoCambio(String currency) {
        double conversionRate = -1;
        try {
            URL url = new URL(API_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();

            JSONObject jsonResponse = new JSONObject(content.toString());
            conversionRate = jsonResponse.getJSONObject("conversion_rates").getDouble(currency);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conversionRate;
    }

    private void sendText(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
