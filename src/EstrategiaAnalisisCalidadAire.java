// EstrategiaAnalisisCalidadAire.java
import java.util.Date;

public class EstrategiaAnalisisCalidadAire implements EstrategiaAnalisis {

    @Override
    public Alerta analizar(Sensor sensor) {
        if (!"calidad_aire".equals(sensor.getTipo())) return null;

        double valor = sensor.getValor(); // supongamos: índice AQI (0-500)
        if (valor > 200) {
            return new Alerta(sensor.getId(),
                    "🚨 CALIDAD DE AIRE MUY MALA (AQI=" + valor + ") en " + sensor.getUbicacion(),
                    NivelAlerta.CRITICO, new Date());
        } else if (valor > 100) {
            return new Alerta(sensor.getId(),
                    "⚠️ Calidad de aire moderadamente mala (AQI=" + valor + ") en " + sensor.getUbicacion(),
                    NivelAlerta.ADVERTENCIA, new Date());
        }
        return null;
    }
}
