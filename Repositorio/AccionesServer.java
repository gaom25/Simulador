
import java.util.ArrayList;
import java.util.Date;
import java.net.*;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.text.DateFormat;


public class AccionesServer {

    public static String crearRepo(String cliente, String repo) {

        File file = new File("./" + cliente);
        if (!file.exists()) {

            if (file.mkdir()) {
            } else {
                return "Problemas creando el repositorio";
            }
        }

        File file2 = new File("./" + cliente + "/" + repo);
        if (!file2.exists()) {

            if (file2.mkdir()) {
                return "Repositorio creado exitosamente";
            } else {
                return "Problemas creando el repositorio";
            }
        } else {
            return "Repositorio ya existente";
        }
    }

     public static String actualizarRepo(String cliente, String repo, ArrayList<File> archivos, Date hora) {

        File file = new File("./" + cliente);
        File file2 = new File("./" + cliente + "/" + repo);
        if (!file.exists() && !file2.exists()) {

            return "Repositorio Inexistente";
        }

        String tiempo;
        DateFormat df = DateFormat.getDateTimeInstance(
                           DateFormat.DEFAULT, 
                           DateFormat.DEFAULT,new Locale("es","ES"));
        tiempo = df.format(hora);
        File nuevaVer = new File("./" + cliente + "/" + repo + "/" + tiempo);
        
        if (!nuevaVer.exists()) {

            if (nuevaVer.mkdir()) {

                String destino = "./" + cliente + "/" + repo + "/" + tiempo + "/";
                
                for (int i = 0; i < archivos.size(); i++) {

                    File tmp = archivos.get(i);
                    
                    String salidaDescifrada = destino + tmp.getName();
                    File dest = new File(salidaDescifrada);

                    if (tmp.renameTo(dest)) {
                   
                    } else {
                        System.out.println("Error Subiendo Archivos.");
                        break;
                    }
                }

            } else {
                return "Problemas creando el repositorio";
            }
        } else {
            return "ERROR:: Repositorio ya existente!! =$ timeseption....";
        }
        return "";
    }

    public static void commitRepo(String cliente,String repo,ArrayList<File> archivos, Date hora){
        File file = new File("./" + cliente);
        File file2 = new File("./" + cliente + "/" + repo);
        if (!file.exists() && !file2.exists()) {

            System.out.println("Repositorio Inexistente");
        }

        String tiempo;
        DateFormat df = DateFormat.getDateTimeInstance(
                           DateFormat.DEFAULT, 
                           DateFormat.DEFAULT,new Locale("es","ES"));
        tiempo = df.format(hora);
        File nuevaVer = new File("./" + cliente + "/" + repo + "/" + tiempo);

         if (!nuevaVer.exists()) {

            if (nuevaVer.mkdir()) {

                String destino = "./" + cliente + "/" + repo + "/" + tiempo + "/";
                File f1;
                for (int i = 0; i < archivos.size(); i++) {
                    f1 = archivos.get(i);

                    try {
                        File f2 = new File(destino + f1.getName());
                        InputStream in = new FileInputStream(f1);
                        OutputStream out = new FileOutputStream(f2);

                        byte[] buf = new byte[1024];
                        int len;
                        while ((len = in.read(buf)) > 0) {
                            out.write(buf, 0, len);
                        }
                        
                        in.close();
                        out.close();
                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    
                   
                }

            } 
        } 

    }


        // Es invocada por el coordinador una vez que el lciente le solicita un commit.
    // Le avisa a todos los esclavos que quiere hacer un commit, si todos le responden entonces
    // les confirma el commit y les da los archivos. En caso contrario elimina a los servidores
    // que esten caidos de su lista de servidores, y reintenta el proceso.
    public static void votacionTPC(Servidor servidor,String cliente, String repo, ArrayList<File> archivos, Date hora) {
      
    
    String msg;
    DatagramPacket votacion;
    DatagramPacket rvc;
    
        
        
    // Los pone a todos a escuchar con la funcion tpc
    // Como le digo a cada esclavo que arranque tpc.... ?
    Actualizacion dato = new Actualizacion("tpc",cliente,repo,archivos,hora);

    try {
       
        InetAddress group = InetAddress.getByName("230.0.0.5");
        MulticastSocket s = new MulticastSocket(55555);
        s.joinGroup(group);
    
        MulticastSocket enviador = new MulticastSocket(55557);
        dato.setTiempAct(new Date());

        
        //Serealizamos el objeto para poder enviarlo por la red
        
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(bs);
        os.writeObject(dato);  // this es de tipo DatoUdp
        os.close();
        byte[] bytes = bs.toByteArray(); // devuelve byte[]

        
        // Usamos la direccion Multicast 230.0.0.5, por poner alguna dentro
        // del rango y el puerto 77775, uno cualquiera que esté libre.
        DatagramPacket dgp;
        dgp = new DatagramPacket(bytes, bytes.length, InetAddress.getByName("230.0.0.5"), 55557);

        enviador.send(dgp);
    } catch (Exception e) {
        System.out.println("Error creando el socket multicast" + e);
    }
    
       
   }
   
   public static void ejecucionTPC(ArrayList<Servidor> originales,ArrayList<Servidor> respuesta){
    

    Actualizacion dato=null;
    String msg ;
    
    // Eliminamos el coordinador
    originales.remove(0);
    int tamServ = originales.size();
    int tamAct = respuesta.size();
    
    
    if ( tamServ != tamAct){
        
        msg = "coordinador";
        for (int i =0; i < tamServ; i++){
        
        Servidor tmp = originales.get(i);
        if (respuesta.contains(tmp)){
          originales.remove(tmp);
        }
        }
        
        //ACTUALIZAR LA LISTA DE SERVIDORES ACTIVOS EN TODOS LOS SERVER
        dato = new Actualizacion(msg,null,originales);
        
    }else{
    
        msg = "tpc-commit";
        dato = new Actualizacion(msg);
        
        /**
        * El maestro hace commit
        */
    
    }

    try {
        MulticastSocket enviador = new MulticastSocket(55557);
        dato.setTiempAct(new Date());

        
        //Serealizamos el objeto para poder enviarlo por la red
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(bs);
        os.writeObject(dato);  // this es de tipo DatoUdp
        os.close();
        byte[] bytes = bs.toByteArray(); // devuelve byte[]

        
        // Usamos la direccion Multicast 230.0.0.5, por poner alguna dentro
        // del rango y el puerto 77775, uno cualquiera que esté libre.
        DatagramPacket dgp;
        dgp = new DatagramPacket(bytes, bytes.length, InetAddress.getByName("230.0.0.5"), 55557);
        enviador.send(dgp);
        
    } catch (Exception e) {
        System.out.println("Error creando el socket Multicast" + e);
    }   
   }

    
}
