#!/bin/bash

# Iniciar MySQL
mysqld &

# Esperar a que MySQL esté listo
sleep 20

# Iniciar la aplicación Java
java -jar /app/app.jar --spring.profiles.active=dev
