package org.example.miprimercrud;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author Izan López Mora 1DAM
 */
public class EstudiantesApplication extends Application {

    //Conexión a la base de datos
    public static Connection conexion;

    /**
     * Este método inicia la escena y la conexión a base de datos.
     * @param stage
     * @throws IOException
     */
    @Override
    public void start(Stage stage) throws IOException {

        String host = "jdbc:mariadb://localhost:3306/";
        String user = "root";
        String psw = "";
        String base = "miprimercrudbd";

        try {
            conexion = DriverManager.getConnection(host+base,user,psw);
            System.out.println("Conexión realizada con éxito.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }

        FXMLLoader fxmlLoader = new FXMLLoader(EstudiantesApplication.class.getResource("mantenimiento.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 480, 480);
        stage.setTitle("Mantenimiento de Estudiantes");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Este método cierra la conexión a la base de datos.
     */
    public void stop(){
        try {
            if(conexion != null && !conexion.isClosed()){
                conexion.close();
                System.out.println("Conexión cerrada correctamente.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        launch();
    }
}