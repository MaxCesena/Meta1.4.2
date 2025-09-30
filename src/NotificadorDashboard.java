// NotificadorDashboard.java
public class NotificadorDashboard implements ObservadorAlerta {
    @Override
    public void actualizar(Alerta alerta) {
        System.out.println("📊 ACTUALIZANDO DASHBOARD - " + alerta.getMensaje());
    }

    @Override
    public String obtenerTipoObservador() {
        return "NotificadorDashboard";
    }
}
