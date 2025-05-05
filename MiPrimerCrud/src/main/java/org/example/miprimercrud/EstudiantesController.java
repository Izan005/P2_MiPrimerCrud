package org.example.miprimercrud;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.*;
import java.time.LocalDate;

/**
 * @author Izan López Mora 1DAM
 */
public class EstudiantesController {

    /**
     * Atributo global establecido en null que recibe posteriormente el estudiante a modificar del método editarEstudiante
     * para después ser usado en la query del método guardarCambios para saber cual estudiante quiero editar.
     */
    public static Estudiante estudiante_editar = null;

    /**
     * Método que devuelve la conexión a la base de datos realizada en la aplicación, además de deshabilitar en
     * botón de guardar.
     * @return la conexión establecida.
     */
    private Connection conexion(){
        botonGuardar.setDisable(true);
        return EstudiantesApplication.conexion;
    }

    /**
     * Estos son todos los elementos del Scene Builder
     */
    @FXML
    private Button botonInsertar;

    @FXML
    private Button botonGuardar;

    @FXML
    private TextField niaTextField;

    @FXML
    private TextField nombreTextField;

    @FXML
    private DatePicker fechaDatePicker;

    @FXML
    private TableColumn <Estudiante,Integer> niaTableColumn;

    @FXML
    private TableColumn <Estudiante,String> nombreTableColumn;

    @FXML
    private TableColumn <Estudiante, LocalDate> fechaTableColumn;

    @FXML
    private TableView<Estudiante> tablaEstudiantes;

    @FXML
    public ObservableList<Estudiante> listaEstudiantes = FXCollections.observableArrayList();

    /**
     * Establecemos la tabla de estudiantes en vacío para que se refresque la información al alterar su contenido.
     * Después hacemos la consulta inicial desde el método consulta() para rellenar la tabla, se inicializan las columnas
     * y establecemos el contenido de la lista de estudiantes como contenido de la tabla.
     */
    @FXML
    public void initialize(){
        tablaEstudiantes.getItems().clear();
        consulta(conexion());
        niaTableColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getNia()).asObject());
        nombreTableColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNombre()));
        fechaTableColumn.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getFecha_nacimiento()));
        tablaEstudiantes.setItems(listaEstudiantes);

    }

    /**
     * Método que realiza un SELECT a la tabla estudiantes para extraer su información, añadirla a la lista de estudiantes y
     * enseñarla en la tabla.
     * @param conexion
     */
    public void consulta(Connection conexion){
        String query = "SELECT * FROM estudiantes";
        Statement stmt;
        ResultSet respuesta;

        try {
            stmt = conexion.createStatement();
            respuesta = stmt.executeQuery(query);

            while (respuesta.next()){
                int nia = respuesta.getInt("nia");
                String nombre = respuesta.getString("nombre");
                Date fecha = respuesta.getDate("fecha_nacimiento");
                listaEstudiantes.add(new Estudiante(fecha.toLocalDate(),nombre,nia));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Método que realiza un INSERT a la base de datos con la información de los cuadros de texto tras pulsar el botón
     * Insertar.
     */
    @FXML
    public void insertarEstudiante() {
        String nombre = nombreTextField.getText();
        int nia;
        LocalDate fechaNacimiento = fechaDatePicker.getValue();

        try {
            nia = Integer.parseInt(niaTextField.getText());

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }

        //Comprobamos si el NIA está en el formato correcto de 8 dígitos.
        if(niaTextField.getText().length() != 8){
            System.out.println("Un NIA solo está compuesto por 8 caracteres");
        } else {
            Estudiante estudiante = new Estudiante(fechaNacimiento, nombre, nia);

            String insert = "INSERT INTO estudiantes (nia, nombre, fecha_nacimiento) VALUES (" + nia + ", '" + nombre + "', '"
                    + java.sql.Date.valueOf(fechaNacimiento) + "')";

            Statement stmt;

            try {
                Connection conexion = conexion();
                stmt = conexion.createStatement();
                stmt.executeUpdate(insert);
                listaEstudiantes.add(estudiante);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }



    }

    /**
     * Método que realiza el UPDATE del estudiante seleccionado y recogido del método editarEstudiante() tras pulsar
     * el botón Guardar
     */
    @FXML
    public void guardarCambios(){
        int nia = Integer.parseInt(niaTextField.getText());
        String nombre = nombreTextField.getText();
        LocalDate fecha = fechaDatePicker.getValue();

        String query = "UPDATE estudiantes SET nia = " + nia + ", nombre = '" + nombre +
                "', fecha_nacimiento = '" + fecha + "' WHERE nia = " + estudiante_editar.getNia() + " AND " +
               "nombre = '" + estudiante_editar.getNombre() + "' AND fecha_nacimiento = '" + estudiante_editar.getFecha_nacimiento() + "'";

        Statement stmt;

        try {
            Connection conexion = conexion();
            stmt = conexion.createStatement();
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }

        botonGuardar.setDisable(true);
        botonInsertar.setDisable(false);

        //Regreso al método initialize para que la TableView se limpie y cargue nuevos contenidos
        initialize();
    }

    /**
     * Método que habilita el botón Guardar y deshabilita el botón Insertar. Se encarga de recoger la información
     * del estudiante a editar y la guarda en el objeto global estudiante_editar.
     */
    @FXML
    public void editarEstudiante() {
        botonGuardar.setDisable(false);
        botonInsertar.setDisable(true);
        estudiante_editar = tablaEstudiantes.getSelectionModel().getSelectedItem();



        if (estudiante_editar != null) {
            niaTextField.setText(String.valueOf(estudiante_editar.getNia()));
            nombreTextField.setText(estudiante_editar.getNombre());
            fechaDatePicker.setValue(estudiante_editar.getFecha_nacimiento());

        } else {
            System.out.println("Ninguna fila ha sido seleccionada");
        }
    }

    /**
     * Método que se encarga de recibir el estudiante seleccionado y de llamar al método borrarEstudiante(), el cual elimina
     * al estudiante seleccionado.
     */
    @FXML
    public void eliminarEstudiante(){
        Connection conexion = conexion();
        Estudiante seleccionado = tablaEstudiantes.getSelectionModel().getSelectedItem();

        if (seleccionado != null){
            borrarEstudiante(conexion, seleccionado);
        }else {
            System.out.println("No hay filas seleccionadas");
        }
    }

    /**
     * Método que recibe la conexión a la base de datos y el estudinate a eliminar del método eliminarEstudiante() para
     * realizar un DELETE de ese mismo.
     * @param conexion
     * @param estudiante
     */
    public void borrarEstudiante(Connection conexion, Estudiante estudiante){

        int nia = Integer.parseInt(String.valueOf(estudiante.getNia()));
        String nombre = String.valueOf(estudiante.getNombre());
        LocalDate fecha = Date.valueOf(estudiante.getFecha_nacimiento()).toLocalDate();

        String query = "DELETE FROM estudiantes WHERE nia = " + nia + " AND nombre = '" + nombre + "' AND fecha_nacimiento = '" + fecha + "'";
        Statement stmt;

        try {
            stmt = conexion.createStatement();
            stmt.executeUpdate(query);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }

        //Regreso al método initialize para que la TableView se limpie y cargue nuevos contenidos
        initialize();
    }

}