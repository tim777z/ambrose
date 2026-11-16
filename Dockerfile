FROM python:3.11-slim

WORKDIR /ambrose

# Serve the bundled Ambrose web demo (static assets) directly.
COPY common/src/main/resources/com/twitter/ambrose/server/web /ambrose/web

EXPOSE 8080

CMD ["python", "-m", "http.server", "8080", "--directory", "/ambrose/web"]