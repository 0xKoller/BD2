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


    public void selectAllLog(){
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
            System.out.println("idProducto: " + columna1 + " -- antesDesc: " + columna2 + " -- antesNomb: " + columna3 +
                    " -- antesPrecio: " + columna4 + " -- antesStock: " + columna5 + " -- despuesDesc: "
            + columna6 + " -- despuesNomb: " + columna7 + " -- despuesPrecio: " + columna8 + " -- despuesStock: "
            + columna9);
        }
    }

    public void selectProdEspecifico(int prod){
        String keyspace = "tpo;";
        session.execute("USE " + keyspace);
        String query = "SELECT * FROM log_catalogo WHERE id_producto = " + prod + " allow filtering;";
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
                "campo_modificado = '"+ campo +"' allow filtering ;";

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

        // descomentar cuando se quiere hacer un insert. Ver como implementar
        PercistenciaLog insert = new PercistenciaLog(ipAddress, port);
        insert.insertLog(15, "juan", "cafe", 10.0,"10"
        , "algo","cafe", 11.2, null);
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