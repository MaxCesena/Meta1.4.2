// NotificadorDashboard.java
public class NotificadorDashboard implements ObservadorAlerta {
    @Override
    public void actualizar(Alerta alerta) {
        System.out.println("📊 ACTUALIZANDO DASHBOARD - " + alerta.getMensaje());
        // Actualizar interfaz de usuario en tiempo real (simulado)
    }

    @Override
    public String obtenerTipoObservador() {
        return "NotificadorDashboard";
    }
}
