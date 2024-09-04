FROM nats:latest

COPY nats-server.conf /etc/nats/nats-server.conf

EXPOSE 4222 8222 6222

ENTRYPOINT ["nats-server"]
CMD ["-c", "/etc/nats/nats-server.conf"]

