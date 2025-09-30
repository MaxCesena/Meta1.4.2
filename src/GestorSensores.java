// GestorSensores.java
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class GestorSensores {
    private static volatile GestorSensores instancia;
    private Map<String, Sensor> sensores;

    private GestorSensores() {
        this.sensores = new ConcurrentHashMap<>();
        System.out.println("🔧 Gestor de Sensores inicializado");
    }

    public static GestorSensores obtenerInstancia() {
        if (instancia == null) {
            synchronized (GestorSensores.class) {
                if (instancia == null) {
                    instancia = new GestorSensores();
                }
            }
        }
        return instancia;
    }

    public void registrarSensor(Sensor sensor) {

        Sensor prev = sensores.putIfAbsent(sensor.getId(), sensor);
        if (prev == null) {
            System.out.println("✅ Sensor registrado: " + sensor.getId() + " en " + sensor.getUbicacion());
        } else {
            System.out.println("⚠️ Sensor ya existente (omitido): " + sensor.getId());
        }
    }

    public void actualizarValorSensor(String idSensor, double nuevoValor) {
        Sensor sensor = sensores.get(idSensor);
        if (sensor != null) {
            sensor.setValor(nuevoValor);
            sensor.setUltimaActualizacion(new Date());
            System.out.println("📊 Sensor " + idSensor + " actualizado: " + nuevoValor);


            NotificadorAlertas.obtenerInstancia().verificarYNotificar(sensor);
        } else {
            System.out.println("❌ Sensor no encontrado: " + idSensor);
        }
    }

    public Sensor obtenerSensor(String idSensor) {
        return sensores.get(idSensor);
    }

    public void eliminarSensor(String idSensor) {
        sensores.remove(idSensor);
        System.out.println("🗑️ Sensor eliminado: " + idSensor);
    }

    public List<Sensor> obtenerTodosSensores() {
        return new ArrayList<>(sensores.values());
    }

    public int obtenerCantidadSensores() {
        return sensores.size();
    }

    public List<Sensor> obtenerSensoresPorTipo(String tipo) {
        return sensores.values().stream()
                .filter(s -> tipo.equals(s.getTipo()))
                .collect(Collectors.toList());
    }


    public Map<String, Map<String, Double>> obtenerEstadisticas() {
        Map<String, List<Sensor>> porTipo = sensores.values().stream()
                .collect(Collectors.groupingBy(Sensor::getTipo));

        Map<String, Map<String, Double>> resultado = new HashMap<>();
        for (Map.Entry<String, List<Sensor>> entry : porTipo.entrySet()) {
            String tipo = entry.getKey();
            List<Sensor> lista = entry.getValue();

            double count = lista.size();
            double sum = lista.stream().mapToDouble(Sensor::getValor).sum();
            double avg = count > 0 ? sum / count : 0.0;
            double min = lista.stream().mapToDouble(Sensor::getValor).min().orElse(0.0);
            double max = lista.stream().mapToDouble(Sensor::getValor).max().orElse(0.0);

            Map<String, Double> stats = new HashMap<>();
            stats.put("count", count);
            stats.put("avg", avg);
            stats.put("min", min);
            stats.put("max", max);
            resultado.put(tipo, stats);
        }

        return resultado;
    }
}
