#!/bin/bash

MAX_WAIT_SECONDS=600
elapsed_seconds=0
HOST="localhost"
PORT=8443

# Espera a que la aplicación responda a un GET en el puerto 8443
while ! curl --silent --fail https://$HOST:$PORT; do
  printf "\n  => Waiting for application to start - IP: https://$HOST and PORT: '$PORT'\n"
  sleep 1s
  ((elapsed_seconds++))

  if [ "$elapsed_seconds" -ge "$MAX_WAIT_SECONDS" ]; then
    printf "\n  => Application took too long to start. Exiting...\n"
    exit 1
  fi
done

echo "\n  => Application started successfully at https://$HOST:$PORT"