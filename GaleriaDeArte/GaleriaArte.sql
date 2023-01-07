
CREATE SCHEMA ARTISTAS
CREATE SCHEMA VENTAS
CREATE SCHEMA EXPOSICIONES

CREATE TABLE ARTISTAS.Artistas
(
	Id_Artista bigserial NOT NULL,
	Nombre varchar(100) NOT NULL,
	Apellidos varchar(100) NOT NULL,
	Fecha_Nacimiento date NOT NULL,
	Direccion varchar(100) NOT NULL,
	Telefono varchar(20) NULL,
	Pais varchar(100) NOT NULL,
	Email varchar(100) NOT NULL,
	Resena varchar (500) NOT NULL,
	
	CONSTRAINT PK_ID_ARTISTA PRIMARY KEY (Id_Artista)
)

select * from ARTISTAS.Artistas

CREATE TABLE ARTISTAS.Obras
(
	Id_Obra bigserial NOT NULL,
	Id_Artista bigint NOT NULL,
	Titulo varchar(100) NOT NULL,
	Tipo varchar(50) NOT NULL,
	Estilo varchar(50) NOT NULL,
	Medio varchar(50) NOT NULL,
	Precio float(2) NOT NULL,
	Fecha_Creacion date NOT NULL,
	Fecha_Ingreso date NOT NULL,
	Estado int NOT NULL,
	
	CONSTRAINT PK_OBRA PRIMARY KEY (Id_Obra),
	CONSTRAINT KF_ARTISTA FOREIGN KEY (Id_Artista) REFERENCES ARTISTAS.Artistas(Id_Artista) ON DELETE CASCADE
)

select * from ARTISTAS.Obras

CREATE TABLE VENTAS.Clientes
(
	Id_Cliente bigserial NOT NULL,
	Nombre varchar(30) NOT NULL,
	Apellido_Paterno varchar(30) NOT NULL,
	Apellido_Materno varchar(30) NOT NULL,
	Direccion varchar(100) NOT NULL,
	Telefono varchar(8) NULL,
	Ciudad varchar(30) NOT NULL,
	CP varchar(5) NOT NULL,
	Email varchar(50) NULL,
	
	CONSTRAINT PK_CLIENTE PRIMARY KEY (Id_Cliente)
);


CREATE TABLE VENTAS.Vendedores
(
	Id_Vendedor bigserial NOT NULL,
	Nombre varchar(30) NOT NULL,
	Apellido_Paterno varchar(30) NOT NULL,
	Apellido_Materno varchar(30) NOT NULL,
	Fecha_Nacimiento date NOT NULL,
	Direccion varchar(50) NOT NULL,
	Telefono varchar(8) NULL,
	Ciudad varchar(30) NOT NULL,
	CP varchar(5) NOT NULL,
	Email varchar(50) NULL,
	Fecha_Incorporacion date NOT NULL,
	Porcentaje_Comision int NOT NULL,
	
	CONSTRAINT PK_VENDEDOR PRIMARY KEY (Id_Vendedor)
)

CREATE TABLE EXPOSICIONES.Exposicion
(
	Id_Exposicion bigserial NOT NULL,
	Titulo varchar(100) NOT NULL,
	Descripcion varchar(100) NOT NULL,
	Fecha_Apertura DATE NOT NULL,
	Fecha_Cierre DATE NOT NULL,
	
	CONSTRAINT PK_EXPOSICION PRIMARY KEY (Id_Exposicion)
)


CREATE TABLE EXPOSICIONES.Det_Artistas_Exposicion
(
	Id_Exposicion BIGINT NOT NULL,	
	Id_Artista BIGINT NOT NULL,

	CONSTRAINT PK_DET_ART_EXP PRIMARY KEY(Id_Exposicion,Id_Artista),
	CONSTRAINT FK_EXPOSICION FOREIGN KEY (Id_Exposicion) REFERENCES EXPOSICIONES.Exposicion(Id_Exposicion) ON DELETE CASCADE,
	CONSTRAINT FK_ARTISTA FOREIGN KEY (Id_Artista) REFERENCES ARTISTAS.Artistas(Id_Artista) ON DELETE CASCADE
)

CREATE TABLE VENTAS.ComisionesPorExposicion
(
  Codigo_Exposicion bigint not null,
  Codigo_Vendedor bigint not null,
  Comision_Vendedor float(2) not null,
  Comision_Galeria float(2) not null,
  
  CONSTRAINT PK_COMISIONES PRIMARY KEY (Codigo_Exposicion,Codigo_Vendedor),
  CONSTRAINT FK_EXPOSICION2 FOREIGN KEY (Codigo_Exposicion) REFERENCES EXPOSICIONES.Exposicion(Id_Exposicion) ON DELETE CASCADE,
  CONSTRAINT FK_VENDEDORES2 FOREIGN KEY (Codigo_Vendedor) REFERENCES VENTAS.Vendedores(Id_Vendedor)ON DELETE CASCADE
)

CREATE TABLE VENTAS.Ventas
(
	Codigo BIGSERIAL NOT NULL,
	Exposicion BIGINT NULL,
	Cliente BIGINT NULL,
	Vendedor BIGINT NULL,
	Fecha_Venta DATE NOT NULL,
	Total float(2) NOT NULL,
	
	CONSTRAINT PK_ID_VENTA PRIMARY KEY (Codigo),
	CONSTRAINT FK_EXPOSICION FOREIGN KEY (Exposicion) REFERENCES EXPOSICIONES.Exposicion(Id_Exposicion) ON DELETE SET NULL,
	CONSTRAINT FK_CLIENTE FOREIGN KEY (Cliente) REFERENCES VENTAS.Clientes(Id_Cliente) ON DELETE SET NULL,
	CONSTRAINT FK_VENDEDOR FOREIGN KEY (Vendedor) REFERENCES VENTAS.Vendedores(Id_Vendedor) ON DELETE SET NULL
)


CREATE TABLE VENTAS.DetalleVenta
(
   Codigo_Venta BIGINT NOT NULL,
   Codigo_Obra BIGINT NOT NULL,
   Sub_Total float(2) NOT NULL,
 
   CONSTRAINT PK_DET_VENTA PRIMARY KEY (Codigo_Venta,Codigo_Obra),
   CONSTRAINT FK_VENTAS FOREIGN KEY (Codigo_Venta) REFERENCES VENTAS.Ventas(Codigo) ON DELETE CASCADE,
   CONSTRAINT FK_OBRAS FOREIGN KEY (Codigo_Obra) REFERENCES ARTISTAS.Obras(Id_Obra)ON DELETE CASCADE
)

--DISPARADORES--

--1.- Disparador para cambiar el estado de una obra--
CREATE OR REPLACE FUNCTION VENTAS.cambiaEstadoObra() RETURNS TRIGGER AS $cambiaEstadoObra$
	BEGIN
		update ARTISTAS.Obras SET Estado = 1
		where Id_Obra = NEW.Codigo_Obra;
	RETURN NEW;
	END;
$cambiaEstadoObra$ LANGUAGE plpgsql;

CREATE TRIGGER cambiaEstadoObra AFTER INSERT 
	ON VENTAS.DetalleVenta FOR EACH ROW
	EXECUTE PROCEDURE VENTAS.cambiaEstadoObra();

--2.- Disparador para Calcular la comision de los vendedores y de la galeria--
CREATE OR REPLACE FUNCTION VENTAS.actComision() RETURNS TRIGGER AS $actComision$
	DECLARE
		comT  float;
		porcV float;

	BEGIN
		comT := NEW.Total * 0.4;
		porcV := (select Porcentaje_Comision from VENTAS.Vendedores V where V.Id_Vendedor = NEW.Vendedor) * 0.01;
		
		update VENTAS.ComisionesPorExposicion C SET Comision_Vendedor = Comision_Vendedor + (comT * porcV)
		where C.Codigo_Vendedor = NEW.Vendedor AND C.Codigo_Exposicion = NEW.Exposicion;

		update VENTAS.ComisionesPorExposicion C SET Comision_Galeria = Comision_Galeria + (comT - (comT * porcV))
		where C.Codigo_Vendedor = NEW.Vendedor AND C.Codigo_Exposicion = NEW.Exposicion;

	RETURN NEW;
	END;
$actComision$ LANGUAGE plpgsql;

CREATE TRIGGER actComision AFTER INSERT 
	ON VENTAS.Ventas FOR EACH ROW
	EXECUTE PROCEDURE VENTAS.actComision();

--REGLAS DE DOMINIO--
ALTER TABLE ARTISTAS.Obras
	ADD CONSTRAINT TipoObra CHECK (Tipo in ('Pintura','Escultura','Collage'));

ALTER TABLE ARTISTAS.Obras
	ADD CONSTRAINT TipoEstilo CHECK (Estilo in ('Contemporaneo','Impresionista','Folk'));

ALTER TABLE ARTISTAS.Obras
	ADD CONSTRAINT TipoMedio CHECK (Medio in ('Óleo','Acuarela','Acrílico','Mármol','Mixto'));

ALTER TABLE ARTISTAS.Obras
	ADD CONSTRAINT PrecioObra CHECK (Precio > 0 );

ALTER TABLE ARTISTAS.Obras
	ADD CONSTRAINT FechaCreacion CHECK( Fecha_Creacion < Fecha_Ingreso);

select * from ARTISTAS.Artistas
select * from ARTISTAS.Obras
select * from VENTAS.Clientes
select * from VENTAS.Vendedores
select * from EXPOSICIONES.Exposicion
select * from VENTAS.Ventas
select * from VENTAS.DetalleVenta
select * from VENTAS.ComisionesPorExposicion
select * from EXPOSICIONES.Exposicion
select * from Exposiciones.det_artistas_exposicion

delete from VENTAS.DetalleVenta
delete from Ventas.Ventas where codigo = 1001;

truncate table Ventas.Ventas
SELECT setval('ventas.ventas_codigo_seq',1000,'t');

drop table VENTAS.DetalleVenta
drop table Ventas.Ventas
drop database GaleriaDeArte

--CONSULTA PARA OBTENER LAS OBRAS DISPONIBLES--
select *
from ARTISTAS.Obras O inner join Exposiciones.det_artistas_exposicion E on O.Id_Artista = E.Id_Artista 
where E.Id_Exposicion = 3
and O.Estado = 0;

select (apellido_paterno || ' ' || apellido_materno || ' ' ||Nombre) as NombreCompleto
from VENTAS.Vendedores

delete from EXPOSICIONES.Exposicion where id_exposicion = 11
delete from 


insert into ARTISTAS.Artistas values (default,'Daniel','abc','10/10/2013','gfdg','13548','fdhgd','ghgkdf','hfghgfj')
insert into ARTISTAS.Obras values (default,27,'Libertad','Pintura','Contemporaneo','Mixto',500,'1/10/2013','04-03-2013',0)
insert into VENTAS.Clientes values(default, 'Marco Antonio','Galicia','Torrez','Tercera','8309064','SLP','12345','abc@yahoo');
insert into VENTAS.Vendedores values(default, 'Miguel Angel','ABC','CBA','10/10/2013','Durango','21548','Mexico','21547','abc@yahoo','09/10/2013',20);
insert into EXPOSICIONES.Exposicion values(default, 'Sociologia','fsndfg','04/10/2013','24/10/2013')
insert into VENTAS.Ventas values(default,3,1,4,'19/03/2013',1000);
insert into VENTAS.DetalleVenta values(23,51,400);
insert into VENTAS.ComisionesPorExposicion values(3,6,0,0)
INSERT INTO VENTAS.Ventas values(default,1,1,1,'18-3-2013',4908.0)
insert into Exposiciones.det_artistas_exposicion values(3,65);
	

update VENTAS.ComisionesPorExposicion set Comision_Vendedor = 0 where Codigo_Vendedor = 1

--	USUARIOS Y PERMISOS --

--Creación del usuario-
create user Administrador with password '123456'
grant all privileges on database "GaleriaDeArte" to Administrador

grant all privileges on schema ARTISTAS to Administrador
grant all privileges on schema ARTISTAS,VENTAS to Administrador
grant all privileges on schema EXPOSICIONES to Administrador

grant all privileges on ARTISTAS.Artistas,ARTISTAS.Obras,VENTAS.Clientes,VENTAS.Vendedores,EXPOSICIONES.Exposicion,VENTAS.Ventas,VENTAS.DetalleVenta,
VENTAS.ComisionesPorExposicion to Administrador


create user Claudia with password 'Claudia'
grant all privileges on database "GaleriaDeArte" to Claudia

grant all privileges on schema EXPOSICIONES, VENTAS,ARTISTAS to Claudia

grant all privileges on EXPOSICIONES.Exposicion,exposiciones.det_artistas_exposicion,VENTAS.Vendedores,VENTAS.ComisionesPorExposicion, 
ARTISTAS.Artistas to Claudia


revoke all privileges on VENTAS.Clientes,VENTAS.Ventas from Claudia
revoke all privileges on schema VENTAS from Claudia

create user Miguel with password '123456'
grant all on schema VENTAS to Miguel;   
grant select, insert on VENTAS.Ventas to Mike


set session authorization postgres
select * from VENTAS.Ventas
select * from 

update ARTISTAS.Artistas set Nombre = 'Flor' where Id_Artista = 52


SELECT V.Id_Vendedor,V.Nombre
FROM VENTAS.Vendedores V
WHERE V.Id_Vendedor in (
                        SELECT C.Codigo_Vendedor
                        FROM VENTAS.ComisionesPorExposicion C
                        WHERE C.Codigo_Exposicion = 1);

select *

select upper(E.Titulo), E.Fecha_Apertura, E.Fecha_Cierre, sum(C.Comision_Galeria) Comision
from EXPOSICIONES.Exposicion E left join VENTAS.ComisionesPorExposicion C on E.Id_Exposicion = C.Codigo_Exposicion
group by E.Titulo, E.Fecha_Apertura, E.Fecha_Cierre

select * from VENTAS.ComisionesPorExposicion

select * from ARTISTAS.Obras

update ARTISTAS.Obras set estado = 0 where Id_Obra = 17

select * from VENTAS.Ventas
select * from VENTAS.Vendedores

select V.Codigo, V.Fecha_Venta, C.Nombre Cliente, VEN.Nombre Vendedor
from VENTAS.Ventas V join VENTAS.Clientes C on V.Cliente = C.Id_Cliente
		     join VENTAS.Vendedores VEN on V.Vendedor = VEN.Id_Vendedor

select V.Codigo, V.Fecha_Venta, C.Nombre Cliente, VEN.Nombre Vendedor
from VENTAS.Ventas V, VENTAS.Clientes C, VENTAS.Vendedores VEN
where V.Vendedor = VEN.Id_Vendedor and V.Cliente = C.Id_Cliente

select * from VENTAS.Ventas
select * from VENTAS.DetalleVenta
select * from ARTISTAS.Obras

select V2.Nombre Vendedor ,V.Codigo NumVenta,E.Titulo Exposicion, C.Nombre Cliente, V.Fecha_Venta, V.Total
from VENTAS.Vendedores V2, VENTAS.Ventas V, EXPOSICIONES.Exposicion E, VENTAS.Clientes C
where E.Id_Exposicion = V.Exposicion and C.Id_Cliente = V.Cliente and V2.Id_Vendedor = V.Vendedor and V2.Id_Vendedor = 2 and extract(month from V.Fecha_Venta) =3
order by V2.Id_Vendedor asc

select O.Titulo, O.Precio Importe
from VENTAS.DetalleVenta D, ARTISTAS.Obras O, VENTAS.Ventas V
where  D.Codigo_Obra = O.Id_Obra and D.Codigo_Venta = V.Codigo 
and    V.Codigo = 23

select V.* 
from VENTAS.Ventas V
where V.Codigo = @Codigo

select * from 









