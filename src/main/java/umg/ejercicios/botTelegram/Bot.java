//package umg.ejercicios.botTelegram;
//
//import org.telegram.telegrambots.bots.TelegramLongPollingBot;
//import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
//import org.telegram.telegrambots.meta.api.objects.Update;
//import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
//
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.List;
//
//public class Bot extends TelegramLongPollingBot {
//
//    @Override
//    public String getBotUsername() {
//        return "NymeriaVariant_bot";
//    }
//
//    @Override
//    public String getBotToken() {
//        return "6587765280:AAFpuoyJLEugt-GCGrxxR_ybLQm8fZzrnlg";
//    }
//
//    @Override
//    public void onUpdateReceived(Update update) {
//        if (update.hasMessage() && update.getMessage().hasText()) {
//            String message_text = update.getMessage().getText();
//            long chat_id = update.getMessage().getChatId();
//            System.out.println("User id: " + chat_id + " Message: " + message_text);
//
//            if (message_text.equals("/start")) {
//                sendText(chat_id, "¡El Invierno Se Acerca!\nBienvenido a NymeriaBot Aquí están los comandos disponibles:\n"
//                        + "/info - Información personal\n"
//                        + "/progra - Información sobre la clase de programación\n"
//                        + "/hola - Saludo con la fecha actual\n"
//                        + "/cambio [euros] - Conversión de Euros a Quetzales\n"
//                        + "/grupal [mensaje] - Enviar mensaje grupal\n"
//                        + "/clima [ciudad] - Obtener el clima actual de una ciudad\n"
//                        + "/chiste - Escuchar un chiste\n"
//                        + "/moneda - Lanzar una moneda");
//            }else if(message_text.equals("/info")) {
//                sendText(chat_id, "Carnet: 123456\nNombre: Juan Perez\nSemestre: 4to Semestre");
//            } else if (message_text.equals("/progra")) {
//                sendText(chat_id, "La clase de programación es interesante y desafiante.");
//            } else if (message_text.equals("/hola")) {
//                String nombreUsuario = update.getMessage().getFrom().getFirstName();
//                LocalDateTime ahora = LocalDateTime.now();
//                DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("EEEE dd MMMM yyyy, HH:mm:ss");
//                String fechaFormateada = ahora.format(formatoFecha);
//                sendText(chat_id, "Hola " + nombreUsuario + ", hoy es " + fechaFormateada);
//            } else if (message_text.startsWith("/cambio")) {
//                try {
//                    String[] partes = message_text.split(" ");
//                    double euros = Double.parseDouble(partes[1]);
//                    double tipoCambio = 8.91; // Este es un valor de ejemplo, investiga el valor actual.
//                    double quetzales = euros * tipoCambio;
//                    sendText(chat_id, euros + " Euros son " + quetzales + " Quetzales.");
//                } catch (Exception e) {
//                    sendText(chat_id, "Por favor ingresa un valor válido para el cambio.");
//                }
//            } else if (message_text.equalsIgnoreCase("Valar morghulis")) {
//            sendText(chat_id, "Valar Dohaeris");
//            } else if (message_text.startsWith("/raven")) {
//            String[] partes = message_text.split(" ", 3);
//            if (partes.length == 3) {
//                long destinatarioId = Long.parseLong(partes[1]);
//                String mensaje = partes[2];
//                sendText(destinatarioId, "Mensaje de cuervo: " + mensaje);
//                sendText(chat_id, "Tu cuervo ha sido enviado.");
//            } else {
//                sendText(chat_id, "Formato incorrecto. Usa: /raven [user_id] [mensaje]");
//            }
//        } else if (message_text.equalsIgnoreCase("/faces")) {
//            sendText(chat_id, "Una chica no es nadie. Un chico no es nadie. Todos los hombres deben morir.");
//        }else if (message_text.startsWith("/grupal")) {
//                String mensaje = message_text.substring(8).trim();
//                List<Long> listaChats = List.of(654654654L, 48791321L, 46573123L, 123456789L); // Reemplaza con los IDs de tus compañeros
//                for (Long id : listaChats) {
//                    sendText(id, mensaje);
//                }
//            }
//        }
//    }
//
//    public void sendText(Long who, String what) {
//        SendMessage sm = SendMessage.builder()
//                .chatId(who.toString())
//                .text(what).build();
//        try {
//            execute(sm);
//        } catch (TelegramApiException e) {
//            throw new RuntimeException(e);
//        }
//    }
//}
