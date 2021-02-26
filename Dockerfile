FROM ubuntu
RUN mkdir -p /data
COPY ./target/debug/CatExplorer /app/CatExplorer
ENTRYPOINT ["/app/CatExplorer"]