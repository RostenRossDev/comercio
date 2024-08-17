-- Crear una base de datos
CREATE DATABASE IF NOT EXISTS tienda;

-- Usar la base de datos recién creada
USE tienda;

-- Crear un usuario y otorgar privilegios
CREATE USER 'adminSoleado17C'@'%' IDENTIFIED BY 'adminSoleado17C';
GRANT ALL PRIVILEGES ON my_database.* TO 'adminSoleado17C'@'%';
FLUSH PRIVILEGES;