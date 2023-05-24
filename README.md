# Test de Coopeuch - Back-end en Spring
Este es el back-end del test de Coopeuch, desarrollado en Spring. Incluye el archivo test_coopeuch.sql para cargar la base de datos.

## Configuración
Para cargar la base de datos se debe crear la base de datos test_coopeuch dentro de MariaDB. Luego se utiliza el comando:
mysql -u root -p test_coopeuch < test_coopeuch.sql
Debe configurarse un usuario válido de la base de datos en src/main/resources/application.properties, colocando el nombre, la contraseña y el puerto. El usuario de MariaDB debe tener acceso a la base de datos test_coopeuch.

## Iniciar
Para ejecutar la aplicación se debe utilizar el comando ./gradlew bootRun.

## Tests
Los test están desarrollados utilizando JUnit. Se encuentran dentro de src/test, y se pueden ejecutar desde IntelliJ.

## Swagger
Para iniciar Swagger se debe visitar la siguiente URL:
http://localhost:8082/swagger-ui/index.html
