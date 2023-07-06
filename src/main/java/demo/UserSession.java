package demo;

import com.mongodb.quickstart.Connection;

import java.time.LocalDateTime;
import java.time.Duration;

public class UserSession {
    private static LocalDateTime loginTime;

    public static void loginUser() {
        loginTime = LocalDateTime.now();
        System.out.println("Usuario conectado. Hora de inicio de sesión: " + loginTime);
    }

    public static int logoutUser() {
        if (loginTime != null) {
            LocalDateTime logoutTime = LocalDateTime.now();
            Duration sessionDuration = Duration.between(loginTime, logoutTime);
            System.out.println("Usuario desconectado. Duración de la sesión: " + sessionDuration.toMinutes() + " minutos");
            loginTime = null;
            int duracion = sessionDuration.toMinutesPart();
            return duracion ;
        } else {
            System.out.println("El usuario no ha iniciado sesión");
        }
        return 0;
    }
}