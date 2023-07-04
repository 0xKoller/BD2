package cassandraConnector;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;

import java.util.Optional;

//import static com.ibm.dtfj.javacore.parser.j9.section.classloader.ClassLoaderPatternMatchers.system;
import static java.lang.System.out;

public class PercistenciaLog {

    private  Cluster cluster;
    private Session session;

    public PercistenciaLog(String node, int port) {
        cluster = Cluster.builder().addContactPoint(node).withPort(port).build();
        session = cluster.connect();
    }

    public void insertLog(int idProd, String campo, String antes, String despues, int idAdmin) {
        String query = "INSERT INTO tpo.log_catalogo (id_producto,campo_modificado,antes,despues,id_admin) " +
                "VALUES (?, ?, ?, ?, ?)";

        session.execute(query, idProd, campo, antes, despues, idAdmin);

        System.out.println("log successfully updated!");
    }

    public void selectAllLog(){
        String keyspace = "tpo;";
        session.execute("USE " + keyspace);
        String query = "SELECT * FROM log_catalogo;";
        ResultSet resultSet = session.execute(query);


        // Recorrer los resultados y mostrarlos en la consola
        for (Row row : resultSet) {
            // Obtener los valores de cada columna
            int columna1 = row.getInt("id_producto");
            String columna2 = row.getString("campo_modificado");
            String columna3 = row.getString("antes");
            String columna4 = row.getString("despues");
            int columna5 = row.getInt("id_admin");


            // Mostrar los valores en la consola
            System.out.println("idProducto: " + columna1 + " -- campo modificado: " + columna2 + " -- antes: " + columna3 +
                    " -- despues: " + columna4 + " -- idAdmin: " + columna5);
        }
    }

    public void selectProdEspecifico(int prod){
        String keyspace = "tpo;";
        session.execute("USE " + keyspace);
        String query = "SELECT * FROM log_catalogo WHERE id_producto = " + prod + ";";
        ResultSet resultSet = session.execute(query);


        // Recorrer los resultados y mostrarlos en la consola
        for (Row row : resultSet) {
            // Obtener los valores de cada columna
            int columna1 = row.getInt("id_producto");
            String columna2 = row.getString("campo_modificado");
            String columna3 = row.getString("antes");
            String columna4 = row.getString("despues");
            int columna5 = row.getInt("id_admin");


            // Mostrar los valores en la consola
            System.out.println("idProducto: " + columna1 + " -- campo modificado: " + columna2 + " -- antes: " + columna3 +
                    " -- despues: " + columna4 + " -- idAdmin: " + columna5);
        }
    }
    public void close() {
        session.close();
        cluster.close();
    }

    public static void main(String[] args) {
        String ipAddress = "127.0.0.1";
        int port = 9042;
        int prod = 10;

        /*PercistenciaLog insert = new PercistenciaLog(ipAddress, port);
        insert.insertLog(44, "Precio", "100", "90", 1146056);
        insert.close();*/

        PercistenciaLog selectAll = new PercistenciaLog(ipAddress,port);
        selectAll.selectAllLog();
        selectAll.close();

        System.out.println("--------------------------------------------------------------------------------------------------------------");

        PercistenciaLog selectProd = new PercistenciaLog(ipAddress,port);
        selectProd.selectProdEspecifico(prod);
        selectProd.close();
    }
}
