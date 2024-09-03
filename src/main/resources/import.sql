-- parametros
INSERT INTO comercio_muebles.parameters (name, default_value, data_type, actual_value) VALUES ('productButton', 'Ver Catálogo', 1, 'Ver Catálogo');
INSERT INTO comercio_muebles.parameters (name, default_value, data_type, actual_value) VALUES ('heroTitle', 'Los mejores precios en muebles', 1, 'Los mejores precios en muebles');
INSERT INTO comercio_muebles.parameters (name, default_value, data_type, actual_value) VALUES ('heroText', 'Sorprendete en la tienta', 1, 'Sorprendete en la tienta');
INSERT INTO comercio_muebles.parameters (name, default_value, data_type, actual_value) VALUES ('ofertSection', 'Ofertas!!', 1, 'Ofertas!!');
INSERT INTO comercio_muebles.parameters (name, default_value, data_type, actual_value) VALUES ('productSection', 'Muebles para el Hogar', 1, 'Muebles para el Hogar');
INSERT INTO comercio_muebles.parameters (name, default_value, data_type, actual_value) VALUES ('phone', '123-456-7890', 1, '123-456-7890');
INSERT INTO comercio_muebles.parameters (name, default_value, data_type, actual_value) VALUES ('email', 'muebles-ortiz@gmail.com', 1, 'muebles-ortiz@gmail.com');
INSERT INTO comercio_muebles.parameters (name, default_value, data_type, actual_value) VALUES ('youtube', 'https://www.youtube.com', 1, 'https://www.youtube.com');
INSERT INTO comercio_muebles.parameters (name, default_value, data_type, actual_value) VALUES ('twitter', 'https://www.twitter.com', 1, 'https://www.twitter.com');
INSERT INTO comercio_muebles.parameters (name, default_value, data_type, actual_value) VALUES ('instagram', 'https://www.instagram.com', 1, 'https://www.instagram.com');
INSERT INTO comercio_muebles.parameters (name, default_value, data_type, actual_value) VALUES ('facebook', 'https://www.facebok.com', 1, 'https://www.facebok.com');
INSERT INTO comercio_muebles.parameters (name, default_value, data_type, actual_value) VALUES ('siteName', 'Muebles Ortiz', 1, 'Muebles Ortiz');
INSERT INTO comercio_muebles.parameters (name, default_value, data_type, actual_value) VALUES ('calle', 'Av. Alberdi', 1, 'Av. Alberdi');
INSERT INTO comercio_muebles.parameters (name, default_value, data_type, actual_value) VALUES ('altura', '2545', 1, '2545');
INSERT INTO comercio_muebles.parameters (name, default_value, data_type, actual_value) VALUES ('provincia', 'Chaco', 1, 'Chaco');
INSERT INTO comercio_muebles.parameters (name, default_value, data_type, actual_value) VALUES ('ciudad', 'Resistencia', 1, 'Resistencia');
INSERT INTO comercio_muebles.parameters (name, default_value, data_type, actual_value) VALUES ('pais', 'Argentina', 1, 'Argentina');

-- usuarios
INSERT INTO comercio_muebles.users (username, password, enabled) VALUES ('user','$2a$10$p7LHk/KItqUEAyK2VksvzeOMeZWi7TXbA7uta3bMOz89uzlNeSe3q',1);
INSERT INTO comercio_muebles.users (username, password, enabled) VALUES ('admin','$2a$10$aT7985FBPnPc2WGcMYwiSeoHHjJKvid214iYR.NQRGaLcKWfa9YK.',1);

-- roles
--INSERT INTO roles
-- usuarios y roles
INSERT INTO comercio_muebles.authorities (user_id, authority) VALUES (1,'ROLE_USER');
INSERT INTO comercio_muebles.authorities (user_id, authority) VALUES (2,'ROLE_ADMIN');
INSERT INTO comercio_muebles.authorities (user_id, authority) VALUES (2,'ROLE_USER');


-- Insertar una silla de madera
INSERT INTO comercio_muebles.product (name, img, stock, price, enabled, tag, material, sale, version, real_stock, best_seller) VALUES ('Silla de Jardin', 'silla_jardin.webp', 15, 1200.00, true, 'silla madera', 'madera', 10, 1, 15, 0);

-- Insertar una mesa de comedor
INSERT INTO comercio_muebles.product (name, img, stock, price, enabled, tag, material, sale, version, real_stock, best_seller) VALUES ('Mesa de Comedor', 'mesa_comedor.webp', 10, 3500.00, true, 'mesa madera', 'madera', 20, 1, 10, 0);

-- Insertar un sofá de cuero
INSERT INTO comercio_muebles.product (name, img, stock, price, enabled, tag, material, sale, version, real_stock, best_seller) VALUES ('Sofá de Exterior', 'sofa_exterior.webp', 7, 8000.00, true, 'sofá madera jardin exterior', 'cuero', 15, 1, 7, 0);

-- Insertar una estantería de metal
INSERT INTO comercio_muebles.product (name, img, stock, price, enabled, tag, material, sale, version, real_stock, best_seller) VALUES ('Silla Tapizada', 'silla_tapizada.webp', 20, 2200.00, true, 'tapizado silla metal', 'metal', 5, 1, 20, 0);

-- Insertar una lámpara de escritorio
INSERT INTO comercio_muebles.product (name, img, stock, price, enabled, tag, material, sale, version, real_stock, best_seller) VALUES ('Lámpara de Escritorio', 'lampara_escritorio.webp', 30, 650.00, true, 'lámpara madera escritorio', 'metal', 0, 1, 30, 0);

-- Insertar un escritorio de vidrio
INSERT INTO comercio_muebles.product (name, img, stock, price, enabled, tag, material, sale, version, real_stock, best_seller) VALUES ('Libreria algarrobo', 'libreria3.webp', 5, 4500.00, true, 'escritorio madera', 'vidrio', 10, 1, 5, 0);

-- Insertar una cama de madera
INSERT INTO comercio_muebles.product (name, img, stock, price, enabled, tag, material, sale, version, real_stock, best_seller) VALUES ('Ventana de Madera', 'ventana4.jpg', 8, 7000.00, true, 'ventana madera abertura', 'madera', 25, 1, 8, 0);

-- Insertar una silla de oficina
INSERT INTO comercio_muebles.product (name, img, stock, price, enabled, tag, material, sale, version, real_stock) VALUES ('Mesas de Luz', 'mueble2.webp', 25, 1500.00, true, 'Mesas madera luz', 'plástico', 5, 1, 25);

-- Insertar una mesa de centro
INSERT INTO comercio_muebles.product (name, img, stock, price, enabled, tag, material, sale, version, real_stock, best_seller) VALUES ('Alacena de Algarrobo', 'mueble6.jpg', 12, 2000.00, true, 'mueble madera alacena', 'madera', 10, 1, 12, 0);

-- Insertar un armario de madera
INSERT INTO comercio_muebles.product (name, img, stock, price, enabled, tag, material, sale, version, real_stock, best_seller) VALUES ('Puerta de Madera', 'puerta1.webp', 3, 6000.00, true, 'abertura Puerta madera entrada', 'madera', 30, 1, 3, 0);