// EstrategiaAnalisisHumedad.java
import java.util.Date;

public class EstrategiaAnalisisHumedad implements EstrategiaAnalisis {
    @Override
    public Alerta analizar(Sensor sensor) {
        if (!"humedad".equals(sensor.getTipo())) return null;

        double valor = sensor.getValor(); // porcentaje
        if (valor < 20.0) {
            return new Alerta(sensor.getId(),
                    "⚠️ Humedad muy baja: " + valor + "% en " + sensor.getUbicacion(),
                    NivelAlerta.ADVERTENCIA, new Date());
        } else if (valor > 80.0) {
            return new Alerta(sensor.getId(),
                    "⚠️ Humedad muy alta: " + valor + "% en " + sensor.getUbicacion(),
                    NivelAlerta.ADVERTENCIA, new Date());
        }
        return null;
    }
}
