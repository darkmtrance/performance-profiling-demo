# Demo de Monitoreo y Trazabilidad Distribuida

Este proyecto demuestra patrones y anti-patrones comunes en arquitecturas de microservicios, implementando monitoreo y trazabilidad distribuida usando Spring Boot 3.

## 🚀 Guía Rápida

### 1. Iniciar los Servicios
```bash
# Iniciar Prometheus, Grafana y Zipkin
docker compose up -d
```

### 2. Ejecutar la Aplicación
```bash
./mvnw spring-boot:run
```

### 3. Probar los Ejemplos
```bash
# Ejecutar el script de prueba
./test-distributed-tracing.sh
```

## 📚 Ejemplos Incluidos

### 1. Patrones de Comunicación
Ubicación: `src/main/java/com/matomaylla/demo/service/DistributedDemoService.java`

#### Anti-patrón: Llamadas Secuenciales
```java
// Mal: Llamadas secuenciales innecesarias
String result1 = service1Client.callService1(param);
String result2 = service2Client.callService2(result1);
```

#### Buena Práctica: Llamadas Paralelas
```java
// Bien: Ejecución paralela cuando es posible
CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> 
    service1Client.callService1(param)
);
CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> 
    service2Client.callService2(param)
);
```

### 2. Monitoreo de Rendimiento
Ubicación: `src/main/java/com/matomaylla/demo/controller/DemoController.java`
```
com.matomaylla.demo/
├── config/
│   └── MetricsConfig.java         # Configuración de métricas
├── controller/
│   └── DemoController.java        # Endpoints REST
├── service/
│   └── ComplexOperationService.java # Lógica de negocio
└── PerformanceProfilingDemoApplication.java
```

## Guía de Demostración

## 🔍 Comparación de Patrones

| Patrón | Endpoint | Descripción | Tiempo Promedio |
|--------|----------|-------------|-----------------|
| Secuencial | `/demo.api/distributed/sequential` | Anti-patrón: Llamadas en serie | ~350ms |
| Paralelo | `/demo.api/distributed/parallel` | Buena práctica: Ejecución paralela | ~220ms |
| Encadenado | `/demo.api/distributed/chained` | Anti-patrón: Cadena larga | ~580ms |
| Reactivo | `/demo.api/distributed/reactive` | Buena práctica: WebClient reactivo | ~340ms |

## 📊 Métricas Clave

### Visualizar en Prometheus
```bash
# Tiempos de respuesta
demo_api_login_seconds_count
demo_api_complex_seconds_max

# Errores
demo_login_failures_total
```

### Visualizar en Zipkin
- Trazas completas de llamadas entre servicios
- Latencias por componente
- Dependencias entre servicios

## 🔧 Configuración Principal

1. **Zipkin** (`application.properties`):
```properties
management.tracing.sampling.probability=1.0
management.zipkin.tracing.endpoint=http://localhost:9411/api/v2/spans
```

2. **Prometheus** (`prometheus/prometheus.yml`):
```yaml
scrape_configs:
  - job_name: 'spring-boot-app'
    metrics_path: '/actuator/prometheus'
```

## 📦 Estructura del Proyecto

```
src/main/java/com/matomaylla/demo/
├── controller/
│   ├── DemoController.java           # Endpoints básicos
│   ├── Service1Controller.java       # Primer microservicio
│   ├── Service2Controller.java       # Segundo microservicio
│   └── DistributedDemoController.java # Ejemplos de patrones
├── service/
│   ├── ComplexOperationService.java   # Lógica de negocio
│   └── DistributedDemoService.java    # Implementación de patrones
└── config/
    └── TracingConfig.java            # Configuración de trazabilidad
```
# Ver todas las métricas personalizadas
curl http://localhost:8080/actuator/prometheus | grep demo
```

## 🎯 Objetivos del Proyecto

1. **Demostrar Patrones y Anti-patrones**
   - Cómo NO hacer llamadas entre servicios
   - Mejores prácticas de implementación

2. **Monitoreo Efectivo**
   - Métricas relevantes
   - Visualización en tiempo real
   - Detección de problemas

3. **Trazabilidad Completa**
   - Seguimiento de solicitudes
   - Análisis de latencias
   - Diagnóstico de problemas

## 📝 Uso Básico

1. Clonar el repositorio
2. Iniciar servicios:
   ```bash
   docker compose up -d
   ```
3. Ejecutar la aplicación:
   ```bash
   ./mvnw spring-boot:run
   ```
4. Probar ejemplos:
   ```bash
   ./test-distributed-tracing.sh
   ```

## Casos de Uso

### 1. Monitoreo de Performance
Detecta:
- Tiempos de respuesta anormales
- Saturación de recursos
- Patrones de uso

### 2. Detección de Problemas
Identifica:
- Errores frecuentes
- Cuellos de botella
- Problemas de recursos

## Script de Prueba de Carga

```bash
#!/bin/bash
# Script para generar carga de prueba
for i in {1..10}
do
    curl -X POST http://localhost:8080/api/login/user$i
    curl http://localhost:8080/api/complex-operation
    sleep 1
done
```

## Observación de Resultados

Después de la carga, se pueden observar:
1. Número de usuarios activos
2. Tiempos de respuesta promedio
3. Distribución de errores
4. Uso de recursos del sistema

## Conclusión

Este proyecto demuestra la implementación de un sistema de monitoreo efectivo que:
- Proporciona visibilidad detallada del sistema
- Es fácil de integrar con herramientas de monitoreo
- Ayuda en la detección temprana de problemas
- Facilita el análisis de performance

## Cómo Empezar

1. Clonar el repositorio
2. Ejecutar `./mvnw clean install`
3. Iniciar la aplicación con `./mvnw spring-boot:run`
4. Acceder a las métricas en `http://localhost:8080/actuator/metrics`
