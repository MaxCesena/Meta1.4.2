// SistemaMonitoreoIoT.java
public class SistemaMonitoreoIoT {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== INICIO SISTEMA MONITOREO IoT ===");

        // 1. Obtener instancia del GestorSensores
        GestorSensores gestor = GestorSensores.obtenerInstancia();
        NotificadorAlertas notificador = NotificadorAlertas.obtenerInstancia();

        // 2. Registrar estrategias por tipo (Strategy por tipo)
        notificador.registrarEstrategiaPorTipo("temperatura", new EstrategiaAnalisisTemperatura());
        notificador.registrarEstrategiaPorTipo("vibracion", new EstrategiaAnalisisVibracion());
        notificador.registrarEstrategiaPorTipo("energia", new EstrategiaAnalisisEnergia());
        notificador.registrarEstrategiaPorTipo("calidad_aire", new EstrategiaAnalisisCalidadAire());
        notificador.registrarEstrategiaPorTipo("humedad", new EstrategiaAnalisisHumedad());

        // 3. Configurar observadores
        notificador.registrarObservador(new NotificadorEmail());
        notificador.registrarObservador(new NotificadorDashboard());
        notificador.registrarObservador(new NotificadorSMS());
        notificador.registrarObservador(new RegistradorLogs());

        // 4. Registrar sensores
        Sensor sensorTemperatura = new Sensor("TEMP-001", "temperatura", 25.0, "Sala de Máquinas A");
        Sensor sensorVibracion = new Sensor("VIB-001", "vibracion", 2.0, "Motor Principal");
        Sensor sensorEnergia = new Sensor("ENER-001", "energia", 800.0, "Subestación Eléctrica");
        Sensor sensorAire = new Sensor("AIR-001", "calidad_aire", 50.0, "Taller Pintura");
        Sensor sensorHumedad = new Sensor("HUM-001", "humedad", 45.0, "Sala de Máquinas A");

        gestor.registrarSensor(sensorTemperatura);
        gestor.registrarSensor(sensorVibracion);
        gestor.registrarSensor(sensorEnergia);
        gestor.registrarSensor(sensorAire);
        gestor.registrarSensor(sensorHumedad);

        System.out.println("\n=== 🔬 SIMULANDO LECTURAS DE SENSORES ===\n");

        // Lecturas normales
        System.out.println("--- Lecturas Normales ---");
        gestor.actualizarValorSensor("TEMP-001", 45.0);
        Thread.sleep(500);

        // Temperatura crítica
        System.out.println("\n--- Temperatura Crítica ---");
        gestor.actualizarValorSensor("TEMP-001", 85.0);
        Thread.sleep(500);

        // Vibración peligrosa
        System.out.println("\n--- Vibración Peligrosa ---");
        gestor.actualizarValorSensor("VIB-001", 6.5);
        Thread.sleep(500);

        // Cambiar estrategia global a básica temporalmente
        System.out.println("\n--- Cambio temporal de estrategia por defecto ---");
        notificador.establecerEstrategiaAnalisis(new EstrategiaAnalisisBasica());
        gestor.actualizarValorSensor("VIB-001", 3.5); // no generará alerta por estrategia global básica
        Thread.sleep(500);

        // Restaurar estrategia por tipo para vibración
        notificador.registrarEstrategiaPorTipo("vibracion", new EstrategiaAnalisisVibracion());
        gestor.actualizarValorSensor("VIB-001", 3.5); // ahora con estrategia por tipo sí puede alertar si pasa umbral
        Thread.sleep(500);

        // Alto consumo energético
        System.out.println("\n--- Alto Consumo Energético ---");
        gestor.actualizarValorSensor("ENER-001", 1200.0);
        Thread.sleep(500);

        // Aire malo y humedad crítica
        System.out.println("\n--- Calidad de aire ---");
        gestor.actualizarValorSensor("AIR-001", 180.0);
        Thread.sleep(500);

        System.out.println("\n--- Humedad ---");
        gestor.actualizarValorSensor("HUM-001", 85.0);
        Thread.sleep(500);

        System.out.println("\n=== 📊 ESTADÍSTICAS FINALES ===");
        System.out.println("Sensores registrados: " + gestor.obtenerCantidadSensores());
        System.out.println("Observadores activos: " + notificador.obtenerCantidadObservadores());

        System.out.println("\nEstadísticas por tipo:");
        gestor.obtenerEstadisticas().forEach((tipo, stats) -> {
            System.out.println("- " + tipo + " -> count=" + stats.get("count") +
                    ", avg=" + stats.get("avg") + ", min=" + stats.get("min") + ", max=" + stats.get("max"));
        });

        System.out.println("\n=== 🏁 FIN SISTEMA MONITOREO IoT ===");
    }
}
