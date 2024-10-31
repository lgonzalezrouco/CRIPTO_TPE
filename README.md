[![Java CI with Maven](https://github.com/lgonzalezrouco/CRIPTO_TPE/actions/workflows/maven.yml/badge.svg)](https://github.com/lgonzalezrouco/CRIPTO_TPE/actions/workflows/maven.yml)

# Proyecto: stegobmp

## Indice

1. [Descripción](#descripción)
2. [Objetivos del Proyecto](#objetivos-del-proyecto)
3. [Requisitos](#requisitos)
4. [Instrucciones de Instalación](#instrucciones-de-instalación)
5. [Funcionalidades Principales](#funcionalidades-principales)
6. [Algoritmos Esteganográficos](#algoritmos-esteganográficos)
7. [Parámetros de Encriptación](#parámetros-de-encriptación)
8. [Referencias](#referencias)
9. [Autores](#autores)

## Descripción

Este proyecto implementa métodos de esteganografía en archivos BMP, permitiendo ocultar y extraer archivos dentro de
imágenes usando técnicas basadas en LSB (Least Significant Bit).

Los algoritmos utilizados son LSB1, LSB4 y LSBI (LSB Improved), brindando opciones con y sin encriptación.

## Objetivos del Proyecto

Realizar un programa stegobmp que permita al usuario efectuar las siguientes operaciones:

1. Oculte un archivo cualquiera en un archivo de extensión “.bmp”, mediante un método de
   esteganografiado elegido, con o sin password.
2. Descubra un archivo oculto en un archivo de extensión “.bmp” que haya sido previamente
   esteganografiado con uno de los métodos provistos.
3. Estegoanalice un archivo de extensión “.bmp” para determinar si tiene un archivo
   incrustado, con qué algoritmo, y lo extraiga correctamente.

## Requisitos

- **Lenguaje**: Java 21.03 y Javac 21.03
- **Herramientas**: Maven 3.8.1

## Instrucciones de Instalación

1. **Clonar el repositorio**:

    ```bash
    git clone https://github.com/lgonzalezrouco/CRIPTO_TPE.git
    cd CRIPTO_TPE
    ```

2. **Compilar el proyecto**:

   Asegúrese de tener configurado el entorno de Java. Compila el proyecto con el siguiente comando:

    ```bash
    mvn clean install
    ```

3. **Ejecutar**:

   Para ejecutar el programa, use el comando `./stegobmp`, seguido de los parámetros especificados para las
   diferentes funcionalidades.

## Funcionalidades Principales

### 1. Ocultar un Archivo

Para ocultar un archivo en una imagen BMP, el comando es:

```bash
./stegobmp -embed -in <archivo_a_ocultar> -p <imagen_portador> -out <archivo_salida> -steg <LSB1 | LSB4 | LSBI> [-a <aes128|aes192|aes256|3des>] [-m <ecb|cfb|ofb|cbc>] [-pass <password>]
```

Por ejemplo:

```bash
./stegobmp -embed -in mensaje.txt -p imagen.bmp -out imagen_con_mensaje -steg LSBI -a 3des -m cbc -pass "password"
```

### 2. Extraer un Archivo

Para extraer un archivo oculto en una imagen BMP, el comando es:

```bash
./stegobmp -extract -p <imagen_portador> -out <archivo_extraido> -steg <LSB1 | LSB4 | LSBI> [-a <aes128|aes192|aes256|3des>] [-m <ecb|cfb|ofb|cbc>] [-pass <password>]
```

Por ejemplo:

```bash
./stegobmp -extract -p imagen_con_mensaje.bmp -out mensaje_extraido -steg LSBI -a 3des -m cbc -pass "password"
```

### 3. Estegoanálisis

El programa también permite realizar análisis de archivos BMP para detectar y extraer datos ocultos. Se requiere el uso
de un editor de archivos binarios para comparar archivos BMP y evaluar los patrones de inserción de datos en diferentes
técnicas esteganográficas.

## Algoritmos Esteganográficos

1. LSB1: Inserta el archivo oculto en el bit menos significativo de cada componente de color.
2. LSB4: Inserta los 4 bits menos significativos del archivo oculto en cada componente de color.
3. LSBI (LSB Improved): Algoritmo basado en bit-inverse, desarrollado por Majeed y Sulaiman, el cual optimiza la
   inserción de datos en imágenes BMP de 24 bits.

## Parámetros de Encriptación

- Algoritmos: aes128, aes192, aes256, 3des
- Modos: ecb, cfb, ofb, cbc
- Contraseña: Si se especifica -pass, se encripta la información con la contraseña provista.

> **Nota**: Si se omiten algunos parámetros, se asumen los valores predeterminados aes128 y cbc.
> Si se usa modo y algoritmo y no contraseña, genera un error.

## Referencias

- Documento de Majeed y Sulaiman sobre el método LSBI: [Enlace](https://www.jatit.org/volumes/Vol80No2/16Vol80No2.pdf)
- Documentación sobre el formato
  BMP: [Enlace](https://learn.microsoft.com/en-us/windows/win32/api/wingdi/ns-wingdi-bitmapfileheader?redirectedfrom=MSDN)

## Autores

Este proyecto fue desarrollado como parte del curso de Criptografía y Seguridad (72.04) por:

- Bendayan, Alberto (62786)
- Boullosa Gutierrez, Juan Cruz (63414)
- Gonzalez Rouco, Lucas (63366)
- Perez de Gracia, Mateo (63401)