// NotificadorEmail.java
public class NotificadorEmail implements ObservadorAlerta {
    @Override
    public void actualizar(Alerta alerta) {
        String prefijo = "";
        switch (alerta.getNivel()) {
            case CRITICO: prefijo = "🚨 CRÍTICO - "; break;
            case ADVERTENCIA: prefijo = "⚠️ ADVERTENCIA - "; break;
            case INFORMATIVO: prefijo = "ℹ️ INFORMATIVO - "; break;
        }
        System.out.println("📧 ENVIANDO EMAIL - " + prefijo + alerta.getMensaje());
    }

    @Override
    public String obtenerTipoObservador() {
        return "NotificadorEmail";
    }
}
