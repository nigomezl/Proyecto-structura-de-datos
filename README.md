# Simulador de aeropuerto
Nicolás Gómez León
Java 23 / NetBeans 23 / JDK 23
Proyecto de estructura de datos

INDICE
-Descripcion
-Requerimientos
-Instalacion
-Interfaz de usuario
-Desarrollo con IA

# Descripcion

Es una aplicacion que trata de simular el funcionamiento de una torre de control a nivel muy simple "Pistas en interseccion, gasolina..." teniendo que ordenar a los aviones y asignandoles puestos para que cumplan sus tareas llevado a cabo a partir de estructuras lineales

# Requerimientos

Requerimientos Necesita JDK 23.0.1 o superior

# Instalacion

Para ejecutar el programa es necesario descargar el AeropuestroProyectoEstructuras.jar en 
https://github.com/nigomezl/Proyecto-structura-de-datos/blob/master/AeropuestroProyectoEstructuras.jar

# Interfaz de usuario

<img width="289" height="124" alt="image" src="https://github.com/user-attachments/assets/fe16db9a-3955-4e2b-9958-3f06408b73ae" />

Estas seran para definir los atributos del aeropuerto sobre el que se va a llevar a cabo la simulacion reciben unicamente numeros naturales.

<img width="1079" height="686" alt="image" src="https://github.com/user-attachments/assets/802b0e19-4789-46c3-97f7-d064169244e4" />

Aqui se pueden ver los distintos espacios como las pistas "Runways" y "Gates" y dos botones el de iniciar simulacion y agregar aviones.

<img width="1082" height="689" alt="image" src="https://github.com/user-attachments/assets/07e36fcd-9e78-4269-a4d8-53cfe2dc0839" />

En agregar aviones se puede añadir uno manual poniendo cada parametro o agregar una cantidad definida por el usuario al azar estos se podran ver en la derecha inferior como una fila donde se organizar segun prioridad

<img width="2559" height="1400" alt="image" src="https://github.com/user-attachments/assets/092b435e-3f2d-420e-ab72-e0e034477250" />

Color verde significa que el espacio no esta reservado y no hay un avion en este
Color naranja significa que el espacio ha sido reservado
Color rojo significa que el espacio contiene un avion

Se nos mostrara que tarea estan haciendo y cuanto tiempo les hace falta para completarla

<img width="2556" height="1396" alt="image" src="https://github.com/user-attachments/assets/170f179c-a219-463b-bc48-817311cec723" />

Dar click sobre un gate o una pista nos dara a lugar a poder ver el avion que esta ubicada en este

# Desarrollo con IA

Alrededor de 350 líneas de código fueron generadas con apoyo de inteligencia artificial, lo que representa aproximadamente el 50 % del programa. El uso de esta herramienta se concentró principalmente en la parte de la interfaz gráfica de usuario, donde contribuyó cerca del 95 % del código, con excepción de pequeños ajustes y adiciones manuales.
Fuera de esa sección, la IA también participó en la creación de los métodos de impresión en consola —que correspondían igualmente a la interfaz de usuario, aunque actualmente no están en uso— y en la generación aleatoria de aviones, funcionalidad que posteriormente fue modificada y complementada manualmente.
