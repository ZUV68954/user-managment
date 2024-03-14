package main;

import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) throws SQLException {
        int select;
        GestionUsuarios g = new GestionUsuarios();
        System.out.println("""
                Elige una opción:\s
                1. Añadir
                2. Borrar
                3. Cambiar contenido
                4. Acceder
                0. Salir""");
        do {
            select = sc.nextInt();
            switch (select) {
                case 1:
                    System.out.println("Introduzca el nombre de usuario:");
                    String contenido = sc.next();
                    System.out.println("Introduzca la contraseña:");
                    String passAdd = sc.next();
                    if ((g.add(contenido, passAdd))) {
                        System.out.println("Inserción correcta");
                    } else {
                        System.out.println("Algo salió mal X_X");
                    }
                    break;
                case 2:
                    System.out.println("Introduzca el nombre de usuario:");
                    String id = sc.next();
                    if (g.delete(id)) {
                        System.out.println("Borrado correcto");
                    } else {
                        System.out.println("Algo salió mal X_X");
                    }
                    break;
                case 3:
                    System.out.println("ID a modificar:");
                    String cambiar = sc.next();
                    System.out.println("Contraseña vieja:");
                    String passOld = sc.next();
                    if (!g.login(cambiar, passOld)) {
                        System.out.println("Usuario o contraseña incorrectos.");
                        break;
                    }
                    System.out.println("Contraseña nueva:");
                    String passUpdate = sc.next();
                    if (g.update(cambiar, passUpdate)) {
                        System.out.println("Contraseña cambiada");
                    } else {
                        System.out.println("Algo salió mal X_X");
                    }
                    break;
                case 4:
                    System.out.println("Introduzca el usuario:");
                    String user = sc.next();
                    System.out.println("Introduzca la contraseña:");
                    String passLogin = sc.next();
                    if (g.login(user, passLogin)) {
                        System.out.println("Login correcto");
                    } else {
                        System.out.println("Algo salió mal X_X");
                    }
                    break;
            }
        } while (select < 0 || select > 4);
    }
}
