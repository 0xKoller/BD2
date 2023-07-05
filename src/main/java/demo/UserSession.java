package demo;

import java.time.LocalDateTime;
import java.time.Duration;

public class UserSession {
    private static LocalDateTime loginTime;

    public static void loginUser() {
        loginTime = LocalDateTime.now();
        System.out.println("Usuario conectado. Hora de inicio de sesión: " + loginTime);
    }

    public static void logoutUser() {
        if (loginTime != null) {
            LocalDateTime logoutTime = LocalDateTime.now();
            Duration sessionDuration = Duration.between(loginTime, logoutTime);
            System.out.println("Usuario desconectado. Duración de la sesión: " + sessionDuration.toMinutes() + " minutos");
            loginTime = null;
        } else {
            System.out.println("El usuario no ha iniciado sesión");
        }
    }
}