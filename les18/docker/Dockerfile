FROM debian:bookworm

RUN apt-get update && apt-get install -y cowsay

COPY entrtypoint.sh /

RUN chmod +x  /entrtypoint.sh

ENTRYPOINT ["/entrtypoint.sh"]