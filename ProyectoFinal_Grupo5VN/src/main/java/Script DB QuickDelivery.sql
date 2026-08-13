CREATE DATABASE QuickDelivery;

USE QuickDelivery;




CREATE TABLE Roles (
    idRol INT AUTO_INCREMENT,
    nombreRol VARCHAR(50) NOT NULL,
    PRIMARY KEY (idRol)
);




CREATE TABLE Usuarios (
    idUsuario INT AUTO_INCREMENT,
    nombreUsuario VARCHAR(50) NOT NULL,
    contrasena VARCHAR(255) NOT NULL,
    nombreCompleto VARCHAR(150) NOT NULL,
    email VARCHAR(150) NOT NULL,
    idRol INT NOT NULL,
    PRIMARY KEY (idUsuario),
    FOREIGN KEY (idRol) REFERENCES Roles(idRol)
);




CREATE TABLE TiposVehiculo (
    idTipoVehiculo INT AUTO_INCREMENT,
    nombreTipo VARCHAR(50) NOT NULL,
    capacidadCarga VARCHAR(50),
    PRIMARY KEY (idTipoVehiculo)
);




CREATE TABLE Vehiculos (
    idVehiculo INT AUTO_INCREMENT,
    placa VARCHAR(20) NOT NULL,
    marca VARCHAR(50),
    modelo VARCHAR(50),
    idTipoVehiculo INT NOT NULL,
    disponible VARCHAR(2) NOT NULL,
    PRIMARY KEY (idVehiculo),
    FOREIGN KEY (idTipoVehiculo) REFERENCES TiposVehiculo(idTipoVehiculo)
);




CREATE TABLE Conductores (
    idConductor INT AUTO_INCREMENT,
    idUsuario INT NOT NULL,
    licencia VARCHAR(50) NOT NULL,
    idVehiculo INT,
    PRIMARY KEY (idConductor),
    FOREIGN KEY (idUsuario) REFERENCES Usuarios(idUsuario),
    FOREIGN KEY (idVehiculo) REFERENCES Vehiculos(idVehiculo)
);




CREATE TABLE EstadosPaquete (
    idEstado INT AUTO_INCREMENT,
    nombreEstado VARCHAR(50) NOT NULL,
    PRIMARY KEY (idEstado)
);




CREATE TABLE Paquetes (
    idPaquete INT AUTO_INCREMENT,
    descripcion VARCHAR(255) NOT NULL,
    direccionOrigen VARCHAR(255) NOT NULL,
    direccionDestino VARCHAR(255) NOT NULL,
    peso VARCHAR(20),
    idEstado INT NOT NULL,
    fechaRegistro VARCHAR(20),
    PRIMARY KEY (idPaquete),
    FOREIGN KEY (idEstado) REFERENCES EstadosPaquete(idEstado)
);




CREATE TABLE AsignacionesPaquetes (
    idAsignacion INT AUTO_INCREMENT,
    idPaquete INT NOT NULL,
    idVehiculo INT NOT NULL,
    fechaAsignacion VARCHAR(20),
    PRIMARY KEY (idAsignacion),
    FOREIGN KEY (idPaquete) REFERENCES Paquetes(idPaquete),
    FOREIGN KEY (idVehiculo) REFERENCES Vehiculos(idVehiculo)
);




CREATE TABLE UbicacionesVehiculos (
    idUbicacion INT AUTO_INCREMENT,
    idVehiculo INT NOT NULL,
    latitud VARCHAR(20) NOT NULL,
    longitud VARCHAR(20) NOT NULL,
    fechaHora VARCHAR(20),
    PRIMARY KEY (idUbicacion),
    FOREIGN KEY (idVehiculo) REFERENCES Vehiculos(idVehiculo)
);




CREATE TABLE Incidencias (
    idIncidencia INT AUTO_INCREMENT,
    idPaquete INT NOT NULL,
    idConductor INT NOT NULL,
    descripcion VARCHAR(255) NOT NULL,
    fechaHora VARCHAR(20),
    PRIMARY KEY (idIncidencia),
    FOREIGN KEY (idPaquete) REFERENCES Paquetes(idPaquete),
    FOREIGN KEY (idConductor) REFERENCES Conductores(idConductor)
);




CREATE TABLE LogsSistema (
    idLog INT AUTO_INCREMENT,
    idUsuario INT,
    accion VARCHAR(100) NOT NULL,
    detalle VARCHAR(255),
    fechaHora VARCHAR(20),
    PRIMARY KEY (idLog),
    FOREIGN KEY (idUsuario) REFERENCES Usuarios(idUsuario)
);






INSERT INTO Roles (nombreRol) VALUES ("Administrador");
INSERT INTO Roles (nombreRol) VALUES ("Despachador");
INSERT INTO Roles (nombreRol) VALUES ("Conductor");

INSERT INTO TiposVehiculo (nombreTipo, capacidadCarga) VALUES ("Moto", "20kg");
INSERT INTO TiposVehiculo (nombreTipo, capacidadCarga) VALUES ("Furgoneta", "500kg");
INSERT INTO TiposVehiculo (nombreTipo, capacidadCarga) VALUES ("Camion", "2000kg");
INSERT INTO TiposVehiculo (nombreTipo, capacidadCarga) VALUES ("Carro", "400kg");

INSERT INTO EstadosPaquete (nombreEstado) VALUES ("Pendiente");
INSERT INTO EstadosPaquete (nombreEstado) VALUES ("En transito");
INSERT INTO EstadosPaquete (nombreEstado) VALUES ("Entregado");
INSERT INTO EstadosPaquete (nombreEstado) VALUES ("Incidencia");

INSERT INTO Usuarios (nombreUsuario, contrasena, nombreCompleto, email, idRol)
VALUES ("admin1", "admin123", "Sebastian Rojas", "admin@quickdelivery.com", 1);

INSERT INTO Usuarios (nombreUsuario, contrasena, nombreCompleto, email, idRol)
VALUES ("desp1", "desp123", "Karla Vindas", "despachador@quickdelivery.com", 2);

INSERT INTO Usuarios (nombreUsuario, contrasena, nombreCompleto, email, idRol)
VALUES ("cond1", "cond123", "Luis Mora", "luis@quickdelivery.com", 3);

INSERT INTO Vehiculos (placa, marca, modelo, idTipoVehiculo, disponible)
VALUES ("SJO-1234", "Yamaha", "FZ25", 1, "SI");

INSERT INTO Conductores (idUsuario, licencia, idVehiculo)
VALUES (3, "B2-123456", 1);

INSERT INTO Paquetes (descripcion, direccionOrigen, direccionDestino, peso, idEstado, fechaRegistro)
VALUES ("Caja de documentos", "San Jose Centro", "Escazu", "2kg", 1, "01/08/2026");

INSERT INTO Paquetes (descripcion, direccionOrigen, direccionDestino, peso, idEstado, fechaRegistro)
VALUES ("Repuestos electronicos", "Heredia", "Alajuela", "5kg", 1, "01/08/2026");





SELECT * FROM Paquetes;
SELECT idPaquete, descripcion, idEstado FROM Paquetes;
SELECT * FROM Paquetes WHERE idEstado = 1;
SELECT * FROM Paquetes WHERE direccionDestino LIKE "%Escazu%";

UPDATE Paquetes SET idEstado = 2 WHERE idPaquete = 1;

DELETE FROM Paquetes WHERE idPaquete = 2;

SELECT * FROM Paquetes;



CREATE USER IF NOT EXISTS 'quickdelivery_user'@'localhost'
IDENTIFIED BY 'QuickDelivery123';

GRANT SELECT, INSERT, UPDATE, DELETE
      ON quickdelivery.*
          TO 'quickdelivery_user'@'localhost';

FLUSH PRIVILEGES;