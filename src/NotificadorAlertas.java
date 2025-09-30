// NotificadorAlertas.java
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class NotificadorAlertas implements SujetoAlerta {
    private static volatile NotificadorAlertas instancia;
    private List<ObservadorAlerta> observadores;
    private EstrategiaAnalisis estrategiaPorDefecto;
    private Map<String, EstrategiaAnalisis> estrategiasPorTipo;

    private NotificadorAlertas() {
        this.observadores = Collections.synchronizedList(new ArrayList<>());
        this.estrategiaPorDefecto = new EstrategiaAnalisisBasica();
        this.estrategiasPorTipo = new ConcurrentHashMap<>();
        System.out.println("🔔 Notificador de Alertas inicializado");
    }

    public static NotificadorAlertas obtenerInstancia() {
        if (instancia == null) {
            synchronized (NotificadorAlertas.class) {
                if (instancia == null) {
                    instancia = new NotificadorAlertas();
                }
            }
        }
        return instancia;
    }

    public void verificarYNotificar(Sensor sensor) {
        EstrategiaAnalisis estrategia = estrategiasPorTipo.get(sensor.getTipo());
        if (estrategia == null) {
            estrategia = this.estrategiaPorDefecto;
        }

        Alerta alerta = estrategia.analizar(sensor);
        if (alerta != null) {
            notificarObservadores(alerta);
        }
    }

    @Override
    public void registrarObservador(ObservadorAlerta observador) {

        synchronized (observadores) {
            boolean existe = observadores.stream()
                    .anyMatch(o -> o.obtenerTipoObservador().equals(observador.obtenerTipoObservador()));
            if (!existe) {
                observadores.add(observador);
                System.out.println("👀 Observador registrado: " + observador.obtenerTipoObservador());
            } else {
                System.out.println("⚠️ Observador ya registrado (omitido): " + observador.obtenerTipoObservador());
            }
        }
    }

    @Override
    public void eliminarObservador(ObservadorAlerta observador) {
        synchronized (observadores) {
            observadores.removeIf(o -> o.obtenerTipoObservador().equals(observador.obtenerTipoObservador()));
            System.out.println("👋 Observador eliminado: " + observador.obtenerTipoObservador());
        }
    }

    @Override
    public void notificarObservadores(Alerta alerta) {
        System.out.println("\n🚨 Notificando " + observadores.size() + " observadores...");

        synchronized (observadores) {
            for (ObservadorAlerta observador : new ArrayList<>(observadores)) {
                try {
                    observador.actualizar(alerta);
                } catch (Exception e) {
                    System.out.println("❌ Error al notificar a " + observador.obtenerTipoObservador() + ": " + e.getMessage());
                }
            }
        }
    }

    public void establecerEstrategiaAnalisis(EstrategiaAnalisis estrategia) {
        this.estrategiaPorDefecto = estrategia;
        System.out.println("🔄 Estrategia de análisis por defecto cambiada: " + estrategia.getClass().getSimpleName());
    }

    public void registrarEstrategiaPorTipo(String tipoSensor, EstrategiaAnalisis estrategia) {
        estrategiasPorTipo.put(tipoSensor, estrategia);
        System.out.println("🔧 Estrategia registrada para tipo '" + tipoSensor + "': " + estrategia.getClass().getSimpleName());
    }

    public void eliminarEstrategiaPorTipo(String tipoSensor) {
        estrategiasPorTipo.remove(tipoSensor);
        System.out.println("🗑️ Estrategia removida para tipo '" + tipoSensor + "'");
    }

    public List<ObservadorAlerta> obtenerObservadores() {
        synchronized (observadores) {
            return new ArrayList<>(observadores);
        }
    }

    public int obtenerCantidadObservadores() {
        return observadores.size();
    }
}
