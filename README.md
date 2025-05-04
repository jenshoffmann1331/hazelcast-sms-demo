# Hazelcast SMS Demo

Dieses Projekt demonstriert einen verteilten SMS-Versand mit Hazelcast als Shared Memory Queue. Nachrichten werden über einen REST-Endpunkt erzeugt (Producer) und von mehreren Worker-Services (Consumer) verarbeitet, die SMS über SMPP versenden.

## Architektur

- **Producer-Service** (Quarkus/Kotlin): Empfängt REST-POSTs und legt `SmsMessage`-Objekte in eine Hazelcast-Queue.
- **Consumer-Service** (Quarkus/Kotlin): Liest regelmäßig aus der Queue und versendet die SMS über SMPP (mit der Bibliothek [jSMPP](https://jsmpp.org/)).
- **Hazelcast**: In-Memory-Datenstruktur, teilt die Queue zwischen Producer und allen Consumer-Instanzen.
- **SMPP-Simulator**: Lokaler SMSC-Simulator zur Entwicklung und zum Testen des SMPP-Protokolls.
- **nginx**: Einfacher Load-Balancer, der eingehende Requests auf mehrere Producer-Instanzen verteilt.
## Starten mit Docker Compose

### Voraussetzungen

- Docker
- Docker Compose
- Java 21 + Gradle (nur zum lokalen Bauen der Images notwendig)

### Schnellstart

```bash
./build-and-run.sh
```

Dieses Skript:
1. Baut Producer- und Consumer-Services (als `fast-jar`)
2. Startet Container inkl. Hazelcast und SMPP-Simulator

### Testen des Endpunkts

Der Versand einer SMS-Nachricht kann dann über den REST-Endpunkt des Producer-Services getestet werden:

```bash
curl -X POST http://localhost:8088/producer
```

Oder, um etwas Last zu erzeugen und den Load-Balancer zu testen:

```bash
while true; do curl -X POST http://localhost:8088/producer; echo; done
```
