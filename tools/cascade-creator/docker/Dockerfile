FROM debian
RUN apt-get update && apt-get install \
    zsh \
    build-essential \
    groovy \
    wget \
    curl \
    libopencv-dev \
    vim \
    git -y

RUN sh -c "$(wget -O- https://github.com/deluan/zsh-in-docker/releases/download/v1.1.1/zsh-in-docker.sh)" -- \
    -t bira

RUN mkdir -p /opencv_workspace/info
RUN mkdir /opencv_workspace/data

COPY neg /opencv_workspace/neg
COPY pos /opencv_workspace/pos
COPY scripts /opencv_workspace/scripts

RUN chmod +x /opencv_workspace/scripts/*.sh
WORKDIR /opencv_workspace