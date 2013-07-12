
import java.io.File;
import java.util.ArrayList;
import java.util.Date;

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


        File nuevaVer = new File("./" + cliente + "/" + repo + "/" + hora.toString());
        if (!nuevaVer.exists()) {

            if (nuevaVer.mkdir()) {

                String destino = "./" + cliente + "/" + repo + "/" + hora.toString() + "/";
                for (int i = 0; i < archivos.size(); i++) {

                    File tmp = archivos.get(i);
                    File dest = new File(destino + tmp.getName());
                    if (tmp.renameTo(dest)) {
                    } else {
                        System.out.println("Error Subiando Archivos.");
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
}
