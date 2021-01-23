# HAAR Cascade Classifier erstellen
> Es soll ein classifier.xml erstellt werden, welches schüsse von meiner Nerv Gun erkennenn soll

## Steps
- Umgebung aufsetzen
- Sammle oder erstelle **negative** und **hintergrund** Bilder
- Sammle oder erstelle **positive** Bilder
- Erstelle eine **positive Vektordatei** mit allen positiven Bilder
- Trainiere die Cascade

---

### Umgebung aufsetzen

- `docker build -t experiment/open-cv docker/`
- `docker run -it experiment/open-cv zsh`
```Dockerfile
FROM debian
RUN apt update && apt get install \
    zsh \
    build-essential \
    cmake \
    libgtk2.0-dev \
    pkg-config \
    libavcodec-dev \
    libavformat-dev \
    libswscale-dev \
    python-dev \
    python-numpy \
    libtbb2 \
    libtbb-dev \
    libjpeg-dev \
    libpng-dev \
    libtiff-dev \
    libjasper-dev \
    libdc1394-22-dev \
    libopencv-dev \
    wget \
    git -y
RUN sh -c "$(wget -O- https://github.com/deluan/zsh-in-docker/releases/download/v1.1.1/zsh-in-docker.sh)" -- \
    -t bira
```

### Negative Bilder sammeln  
> Ich werde lediglich Hintergrundbilder von Zimmer verwenden.  
>
>Quelle: 
> - http://www.image-net.org/
>- http://image-net.org/api/text/imagenet.synset.geturls?wnid=n04105893

1. Bilder herunterladen
2. Falsche bilder aussortieren (tote links)
3. Bilder auf einheitliche Grösse zuschneiden
4. Description file erstellen `bg.txt`


### Positive Bilder sammeln  
> Ich werde ein Foto von einem NerfGun Schuss verwenden.  

**Doppelt so viele positive wie negative Bilder !!!**  
1. Bild finden
2. Bild auf einheitliche Grösse zuschneiden
3. Description file erstellen `info.dat`
4. Samples von positiven bilder generieren  
    ```bash
    opencv_createsamples -img pos/nerf.jpg \
        -bg bg.txt \
        -info info/info.lst \
        -pngoutput info \
        -maxxangle 0.5 \
        -maxyangle 0.5 \
        -maxzangle 0.5 \
        -num 2000
    ```
4. Vektor von positiven images erstellen
    ```
    opencv_createsamples -info info/info.lst \
        -num 2000 \
        -w 20 \
        -h 20 \
        -vec positives.vec 
    ```

### Training
```
opencv_traincascade \
    -data data \
    -vec positives.vec \
    -bg bg.txt \
    -numPos 1800 \
    -numNeg 900 \
    -numStages 10 \
    -w 20 \
    -h 20
```