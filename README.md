# Sistema de Monitoreo con Gray Box Profiling

Este proyecto es una demostración de implementación de Gray Box Profiling utilizando Spring Boot 3 y Micrometer, que nos permite obtener una visión detallada del comportamiento y rendimiento de nuestra aplicación en tiempo real.

## Componentes Principales

### Tecnologías Utilizadas
- Spring Boot 3.2.0
- Micrometer (para métricas)
- Spring Actuator
- Prometheus Registry
- AOP (Aspect-Oriented Programming)

### Estructura del Proyecto
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

### 1. Métricas Automáticas
La aplicación captura automáticamente varias métricas importantes:

```bash
# Mostrar métricas del sistema
curl http://localhost:8080/actuator/metrics
```

Métricas destacadas:
- JVM metrics (memoria, threads)
- Tomcat metrics (conexiones, pool)
- HTTP request metrics

### 2. Métricas Personalizadas
Hemos implementado métricas específicas para el negocio:

#### 2.1 Contador de Usuarios Activos
```bash
# Verificar usuarios activos
curl http://localhost:8080/actuator/metrics/demo.users.active
```

#### 2.2 Monitoreo de Operaciones
```bash
# Ver tiempos de operaciones complejas
curl http://localhost:8080/actuator/metrics/demo.operation.complex
```

#### 2.3 Control de Errores
```bash
# Verificar fallos de login
curl http://localhost:8080/actuator/metrics/demo.login.failures
```

### 3. Ejemplos de Uso

#### 3.1 Simular Login de Usuarios
```bash
# Login de usuarios
curl -X POST http://localhost:8080/api/login/usuario1
curl -X POST http://localhost:8080/api/login/usuario2
```

#### 3.2 Ejecutar Operación Compleja
```bash
# Operación con tiempo medido
curl http://localhost:8080/api/complex-operation
```

#### 3.3 Verificar Métricas
```bash
# Ver todas las métricas personalizadas
curl http://localhost:8080/actuator/prometheus | grep demo
```

## Características Destacadas

### 1. Gray Box Profiling
El sistema implementa Gray Box Profiling, proporcionando:
- Métricas automáticas del sistema
- Métricas personalizadas de negocio
- Tiempos de respuesta detallados
- Histogramas y percentiles

### 2. Monitoreo en Tiempo Real
- Todas las métricas están disponibles en tiempo real
- Endpoints REST para consulta de métricas
- Formato compatible con Prometheus

### 3. Integración con Sistemas de Monitoreo
Las métricas están listas para ser integradas con:
- Grafana
- Prometheus
- Sistemas de alertas

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
