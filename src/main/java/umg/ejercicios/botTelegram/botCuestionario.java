package umg.ejercicios.botTelegram;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import umg.ejercicios.model.User;
import umg.ejercicios.service.UserService;
import umg.ejercicios.service.RespuestaService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class botCuestionario extends TelegramLongPollingBot {
    private final Map<Long, Integer> indicePregunta = new HashMap<>();
    private final Map<Long, String> seccionActiva = new HashMap<>();
    private final Map<String, String[]> preguntas = new HashMap<>();
    private Map<Long, String> estadoConversacion = new HashMap<>();
    User usuarioConectado = null;
    UserService userService = new UserService();
    RespuestaService respuestaService = new RespuestaService();

    public botCuestionario() {
        // Inicializa los cuestionarios con las preguntas.
        preguntas.put("SECTION_1", new String[]{"🤦‍♂️1.1- Estas aburrido?", "😂😂 1.2- Te bañaste hoy?", "🤡🤡 Pregunta 1.3-Te cepillaste los dientes?"});
        preguntas.put("SECTION_2", new String[]{"Pregunta 2.1-¿De donde eres?", "Pregunta 2.2-¿Cuantos años tienes?", "Pregunta 2.3-¿Cual es tu genero?"});
        preguntas.put("SECTION_3", new String[]{"Pregunta 3.1-¿Artick Monkeys si o no? ", "Pregunta 3.2-¿Linkin Park si o no?", "Pregunta 3.3-¿Gojira si o no?"});
        preguntas.put("SECTION_4", new String[]{"Pregunta 4.1-Escuchas musica?", "Pregunta 4.2- que genero?", "Pregunta 4.3- Artic Monkeys?", "Pregunta 4.4- Hola tilin?", "Pregunta 4.5- ya no se que poner jaja", "Pregunta 4.6- por si acaso"});
    }

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
            String userFirstName = update.getMessage().getFrom().getFirstName();
            String userLastName = update.getMessage().getFrom().getLastName();
            String nickName = update.getMessage().getFrom().getUserName();

            try {
                String state = estadoConversacion.getOrDefault(chatId, "");
                usuarioConectado = userService.getUserByTelegramId(chatId);

                if (usuarioConectado == null && state.isEmpty()) {
                    sendText(chatId, "Hola " + formatUserInfo(userFirstName, userLastName, nickName) + " no tienes un usuario registrado en el sistema. Por favor ingresa tu correo electrónico:");
                    estadoConversacion.put(chatId, "ESPERANDO_CORREO");
                    return;
                }

                if (state.equals("ESPERANDO_CORREO")) {
                    processEmailInput(chatId, messageText);
                    return;
                }

                if (messageText.equals("/menu")) {
                    sendMenu(chatId);
                } else if (seccionActiva.containsKey(chatId)) {
                    manejaCuestionario(chatId, messageText);
                } else {
                    sendText(chatId, "envía /menu para iniciar el cuestionario");
                }

            } catch (Exception e) {
                sendText(chatId, "Ocurrió un error al procesar tu mensaje. Por favor intenta de nuevo.");
            }

        } else if (update.hasCallbackQuery()) {
            String callbackData = update.getCallbackQuery().getData();
            long chatId = update.getCallbackQuery().getMessage().getChatId();
            inicioCuestionario(chatId, callbackData);
        }
    }

    private void sendMenu(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Selecciona una sección:");
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        rows.add(crearFilaBoton("Sección 1", "SECTION_1"));
        rows.add(crearFilaBoton("Sección 2", "SECTION_2"));
        rows.add(crearFilaBoton("Sección 3", "SECTION_3"));
        rows.add(crearFilaBoton("Sección 4", "SECTION_4"));

        markup.setKeyboard(rows);
        message.setReplyMarkup(markup);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private List<InlineKeyboardButton> crearFilaBoton(String text, String callbackData) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(text);
        button.setCallbackData(callbackData);
        List<InlineKeyboardButton> row = new ArrayList<>();
        row.add(button);
        return row;
    }

    private void inicioCuestionario(long chatId, String section) {
        seccionActiva.put(chatId, section);
        indicePregunta.put(chatId, 0);
        enviarPregunta(chatId);
    }

    private void enviarPregunta(long chatId) {
        String seccion = seccionActiva.get(chatId);
        int index = indicePregunta.get(chatId);
        String[] questions = preguntas.get(seccion);

        if (index < questions.length) {
            sendText(chatId, questions[index]);
        } else {
            sendText(chatId, "¡Has completado el cuestionario!");
            seccionActiva.remove(chatId);
            indicePregunta.remove(chatId);
        }
    }

    private void manejaCuestionario(long chatId, String response) {
        String section = seccionActiva.get(chatId);
        int index = indicePregunta.get(chatId);

        // Añadir validación de edad para la segunda pregunta de la segunda sección
        if (section.equals("SECTION_2") && index == 1) {
            try {
                int edad = Integer.parseInt(response);
                if (edad < 0 || edad > 120) {
                    sendText(chatId, "Por favor, introduce una edad válida entre 0 y 120 años.");
                    return; // No continuar si la edad es inválida
                }
            } catch (NumberFormatException e) {
                sendText(chatId, "Por favor, introduce un número válido para la edad.");
                return; // No continuar si la entrada no es un número
            }
        }

        respuestaService.guardarRespuesta(chatId, section, index, response);
        indicePregunta.put(chatId, index + 1);
        enviarPregunta(chatId);
    }

    private void processEmailInput(long chatId, String email) {
        sendText(chatId, "Recibo su Correo: " + email);
        estadoConversacion.remove(chatId);

        try {
            usuarioConectado = userService.getUserByEmail(email);
        } catch (Exception e) {
            System.err.println("Error al obtener el usuario por correo: " + e.getMessage());
            e.printStackTrace();
        }

        if (usuarioConectado == null) {
            sendText(chatId, "El correo no se encuentra registrado en el sistema por favor contacte al administrador.");
        } else {
            usuarioConectado.setTelegramid(chatId);
            try {
                userService.updateUser(usuarioConectado);
            } catch (Exception e) {
                System.err.println("Error al actualizar el usuario: " + e.getMessage());
                e.printStackTrace();
            }
            sendText(chatId, "Usuario actualizado con éxito!");
        }
    }

    private String formatUserInfo(String firstName, String lastName, String userName) {
        return firstName + " " + lastName + " (" + userName + ")";
    }

    private void sendText(Long chatId, String text) {
        SendMessage message = SendMessage.builder()
                .chatId(chatId.toString())
                .text(text)
                .build();
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
