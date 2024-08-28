
//Clase que me va ayudar para la clase de "botCuestionario"
//basicamente toma los datos del bot y los inserta el la tabla "tb_respuestas"

package umg.ejercicios.service;

import umg.ejercicios.db.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RespuestaService {

    public void guardarRespuesta(long telegramId, String seccion, int preguntaId, String respuestaTexto) {
        String query = "INSERT INTO tb_respuestas (telegram_id, seccion, pregunta_id, respuesta_texto) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setLong(1, telegramId);
            stmt.setString(2, seccion);
            stmt.setInt(3, preguntaId);
            stmt.setString(4, respuestaTexto);

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}