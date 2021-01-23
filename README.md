# Maskchecker
> Im Rahmen des Studiums für den Fachbereich Informatik an der HFTM gilt es im Kurs Softwarearchitektur ein Projekt umzusetzen welches die folgende Punkte abdeckt.
> - Technologie: Machine Learnig
> - Architektur: Event Driven 

## Projektidee
> Es soll mittels ML Technologie herausgefunden ob Personen inkorrekt maskiert sind, sollte eine inkorrekt maskierte Person identifiziert werden, wird eine Email gesendet.  
> Der ganze Ablauf, vom Upload bis zur Sendung der Email soll anhand von Ereignissen getriggert werden.


## Befehle

### Build all services and run docker

    ./build

### Build docker image for one service

	./mvnw spring-boot:build-image

### Run docker-compose
    
    docker-compose -f docker/docker-compose.yml up -d

## Übersicht
```bash
├── build.sh
├── docker
│   └── docker-compose.yml
├── README.md
├── services
│   ├── face-finder
│   ├── mask-detection-notifier
│   ├── mouth-finder
│   └── nose-finder
└── tools
    ├── cascade-creator # POC for Training with OpenCV
    ├── load-test # Load Test (Gatling)
    └── client # Java App for FileUpload
```
