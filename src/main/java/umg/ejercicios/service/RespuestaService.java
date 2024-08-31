
//Clase que me va ayudar para la clase de "botCuestionario"
//basicamente toma los datos del bot y los inserta el la tabla "tb_respuestas"

package umg.ejercicios.service;

import umg.ejercicios.dao.RespuestaDao;
import umg.ejercicios.db.DatabaseConnection;
import umg.ejercicios.model.Respuesta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

//public class RespuestaService {
//
//    public void guardarRespuesta(long telegramId, String seccion, int preguntaId, String respuestaTexto) {
//        String query = "INSERT INTO tb_respuestas (telegram_id, seccion, pregunta_id, respuesta_texto) VALUES (?, ?, ?, ?)";
//
//        try (Connection conn = DatabaseConnection.getConnection();
//             PreparedStatement stmt = conn.prepareStatement(query)) {
//
//            stmt.setLong(1, telegramId);
//            stmt.setString(2, seccion);
//            stmt.setInt(3, preguntaId);
//            stmt.setString(4, respuestaTexto);
//
//            stmt.executeUpdate();
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//}

public class RespuestaService {
    private RespuestaDao respuestaDao;

    // Constructor que inicializa el DAO
    public RespuestaService() {
        this.respuestaDao = new RespuestaDao();
    }

    // Método para guardar una respuesta
    public void saveRespuesta(Respuesta respuesta) {
        try {
            respuestaDao.save(respuesta);
        } catch (SQLException e) {
            e.printStackTrace();
            // Manejar el error
        }
    }






    // Método para obtener una respuesta por su ID
    public Respuesta getRespuestaById(int id) {
        try {
            return respuestaDao.getById(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Método para obtener todas las respuestas
    public List<Respuesta> getAllRespuestas() {
        try {
            return respuestaDao.getAll();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Método para actualizar una respuesta
    public void updateRespuesta(Respuesta respuesta) {
        try {
            respuestaDao.update(respuesta);
        } catch (SQLException e) {
            e.printStackTrace();
            // Manejar el error
        }
    }

    // Método para eliminar una respuesta por su ID
    public void deleteRespuesta(int id) {
        try {
            respuestaDao.delete(id);
        } catch (SQLException e) {
            e.printStackTrace();
            // Manejar el error
        }
    }
}