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
    private Session session;

    public PercistenciaLog(String node, int port) {
        cluster = Cluster.builder().addContactPoint(node).withPort(port).build();
        session = cluster.connect();
    }

    public void insertLog(int idProd, String campo, String antes, String despues, int idAdmin) {
        String query = "INSERT INTO tpo.log_catalogo (id_producto,campo_modificado,antes,despues,id_admin) " +
                "VALUES ("+ idProd +", '" + campo +"','" + antes + "','" + despues + "'," + idAdmin + ");";

        session.execute(query);
        System.out.println("-------------------------");
        System.out.println("log successfully updated!");
        System.out.println("-------------------------");
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

    public void selectProdEspecificoYCampo(int prod,String campo){
        String keyspace = "tpo;";
        session.execute("USE " + keyspace);
        String query = "SELECT * FROM log_catalogo WHERE id_producto = " + prod + " AND " +
                "campo_modificado = '"+ campo +"';";

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
        Scanner sc = new Scanner(System.in);
        String ipAddress = "127.0.0.1";
        int port = 9042;

        System.out.println("--> Si quiere ver el log completo del catalogo de productos ingrese 1");
        System.out.println("--> Si quiere ver el log de un producto especifico del catalgo ingrese 2." +
                " A continuacion se le solicitara el id del producto.");
        System.out.println("--> Si desea conocer los el log de cambio de un campo especifico de un " +
                "producto del catalogo especifico ingrese 3");
        int decision = sc.nextInt();

        while (decision != 1 && decision != 2 && decision != 3){
            System.out.println("Numero invalido. Ingrese 1 si desea ver el log completo del catalo" +
                    "go. Si desea conocer el log de un producto en especifico ingrese 2");
            decision = sc.nextInt();
        }

        PercistenciaLog insert = new PercistenciaLog(ipAddress, port);
        insert.insertLog(10, "Precio", "3", "4", 1147157);
        insert.close();

        if(decision == 1){
            PercistenciaLog selectAll = new PercistenciaLog(ipAddress,port);
            selectAll.selectAllLog();
            selectAll.close();
        }else{
            if(decision == 2){
                System.out.println("Ingrese el id del producto que desee conocer su log");
                int prod = sc.nextInt();
                PercistenciaLog selectProd = new PercistenciaLog(ipAddress,port);
                selectProd.selectProdEspecifico(prod);
                selectProd.close();
            }else {
                System.out.println("Ingrese el id del producto que desee conocer su log");
                int prod = sc.nextInt();
                System.out.println("Ingrese el campo que desea ver");
                String campo = sc.next();

                PercistenciaLog selectProdYCampo = new PercistenciaLog(ipAddress,port);
                selectProdYCampo.selectProdEspecificoYCampo(prod,campo);
                selectProdYCampo.close();
            }
        }


    }
}
