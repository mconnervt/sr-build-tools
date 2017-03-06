FROM docker/whalesay:latest

RUN apt-get -y update && apt-get install -y fortunes && \
    mkdir test_dir && \
    cd test_dir && \
    ls

CMD /usr/games/fortune -a | cowsay
