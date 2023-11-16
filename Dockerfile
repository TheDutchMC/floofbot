FROM rust:1-slim AS BUILDER
RUN apt update -qq && apt install -y -qq --no-install-recommends \
    gcc \
    musl-tools

RUN rustup set profile minimal
RUN rustup target add x86_64-unknown-linux-musl

COPY src/ /opt/project/src/
COPY ./Cargo.toml /opt/project/

WORKDIR /opt/project
ENV RUSTFLAGS='-C link-args=-s'

RUN cargo build --release --target x86_64-unknown-linux-musl

FROM alpine
RUN apk add --no-cache ca-certificates
COPY --from=BUILDER /opt/project/target/x86_64-unknown-linux-musl/release/catexplorer /usr/local/bin/catexplorer

RUN chmod a+rx /usr/local/bin/*
RUN adduser rest -s /bin/false -D -H
USER rest

EXPOSE 8080
WORKDIR /usr/local/bin
ENTRYPOINT [ "/usr/local/bin/catexplorer" ]