package cassandraConnector;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;

import java.util.Optional;

//import static com.ibm.dtfj.javacore.parser.j9.section.classloader.ClassLoaderPatternMatchers.system;
import static java.lang.System.out;

public class PercistenciaLog {
    private final CassandraConnector client = new CassandraConnector();

    public PercistenciaLog(final String newHost, final int newPort)
    {
        out.println("Connecting to IP Address " + newHost + ":" + newPort + "...");
        client.connect(newHost, newPort);
    }

    public  void insertLogCarrito(final int idProd, final String campoMod,final String antes,
                                 final String despues,final int idAdmin ) {
        client.getSession().execute("INSERT INTO log_catalogo(id_producto,campo_modificado,antes,despues,id_admin" +
                ")VALUES(?,?,?,?,?)", idProd, campoMod, antes, despues, idAdmin);

        System.out.println("Fila insertada");
    }
    /**
     * Close my underlying Cassandra connection.
     */
    private void close(){
        client.close();
    }



    public static void main(String[] args) {
        int id = 10;
        String campo = "precio";
        String antes = "100";
        String despues = "90";
        int admin = 1909756;

       
    }
}
