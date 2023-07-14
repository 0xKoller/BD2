package cassandraConnector;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import org.yaml.snakeyaml.emitter.ScalarAnalysis;

import java.util.Optional;
import java.util.Scanner;

//import static com.ibm.dtfj.javacore.parser.j9.section.classloader.ClassLoaderPatternMatchers.system;
import static java.lang.System.out;

public class PercistenciaLog {

    private  Cluster cluster;
    private static Session session;

    public PercistenciaLog(String node, int port) {
        cluster = Cluster.builder().addContactPoint(node).withPort(port).build();
        session = cluster.connect();
    }

   public static void insertLog(int idProd, String antesDesc, String antesNom, double antesPrecio, String antesStock,
                             String despuesDesc, String despuesNom, double despuesPrecio, String despuesStock) {
    String query = "INSERT INTO tpo.log_catalogo (id, id_producto, antes_desc, antes_nomb, antes_precio, antes_stock, " +
            "despues_desc, despues_nomb, despues_precio, despues_stock) " +
            "VALUES (uuid(), " + idProd + ", '" + antesDesc + "', '" + antesNom + "', " + antesPrecio + ", '" + antesStock + "', " +
            "'" + despuesDesc + "', '" + despuesNom + "', " + despuesPrecio + ", '" + despuesStock + "');";

    session.execute(query);
    System.out.println("-------------------------");
    System.out.println("log successfully updated!");
    System.out.println("-------------------------");
}


    public static void selectAllLog(){
        String keyspace = "tpo;";
        session.execute("USE " + keyspace);
        String query = "SELECT * FROM log_catalogo;";
        ResultSet resultSet = session.execute(query);


        // Recorrer los resultados y mostrarlos en la consola
        for (Row row : resultSet) {
            // Obtener los valores de cada columna
            int columna1 = row.getInt("id_producto");
            String columna2 = row.getString("antes_desc");
            String columna3 = row.getString("antes_nomb");
            double columna4 = row.getDouble("antes_precio");
            String columna5 = row.getString("antes_stock");
            String columna6 = row.getString("despues_desc");
            String columna7 = row.getString("despues_nomb");
            double columna8 = row.getDouble("despues_precio");
            String columna9 = row.getString("despues_stock");


            // Mostrar los valores en la consola
            System.out.println("-------------------------------------------------------------------------------------");
            System.out.println("idProducto: " + columna1 + " -- antesDesc: " + columna2 + " -- antesNomb: " + columna3 +
                    " -- antesPrecio: " + columna4 + " -- antesStock: " + columna5 + " -- despuesDesc: "
            + columna6 + " -- despuesNomb: " + columna7 + " -- despuesPrecio: " + columna8 + " -- despuesStock: "
            + columna9);
            System.out.println("-------------------------------------------------------------------------------------");
        }
    }

    public void close() {
        session.close();
        cluster.close();
    }

}