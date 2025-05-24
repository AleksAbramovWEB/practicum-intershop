## Сдача проектной работы восьмого спринта 

### ✅  Устанавливаем переменное окружение в .env

```
UPLOAD_IMAGE_DIR=

POSTGRES_DB=
POSTGRES_USER=
POSTGRES_PASSWORD=
POSTGRES_PORT=
POSTGRES_HOST=

SERVICE_PAY_HOST=
SERVICE_PAY_PORT=

TEST_UPLOAD_IMAGE_DIR=

TEST_POSTGRES_DB=
TEST_POSTGRES_USER=
TEST_POSTGRES_PASSWORD=
TEST_POSTGRES_PORT=
TEST_POSTGRES_HOST=

REDIS_HOST=
REDIS_PORT=

KC_HOSTNAME=
KC_PORT=
KC_HTTP_ENABLED=true
KC_PROXY=edge
KEYCLOAK_ADMIN=
KEYCLOAK_ADMIN_PASSWORD=
KC_DB=postgres
KC_DB_USERNAME=
KC_DB_PASSWORD=
KC_DB_URL=jdbc:postgresql:

OAUTH2_INTERSHOP_CLIENT_ID=
OAUTH2_INTERSHOP_CLIENT_SECRET=
OAUTH2_REALM=

OAUTH2_PAY_CLIENT_ID=
OAUTH2_PAY_CLIENT_SECRET=

```
### ✅ Накатить миграции
```bash
chmod +x run.sh
./migrate.sh
```

### ✅ Генерация сервиса платежей
```bash
./gradlew project:pay:openApiGenerate
```

### ✅ Генерация клиента платежей
```bash
./gradlew project:intershop:openApiGenerate
```

### ✅ Сборка/старт приложения
```bash
chmod +x run.sh
./run.sh
```

### ✅ Запуск тестов
```bash
chmod +x run-test.sh
./run-test.sh
```

### ✅ Создание образов и запуск контейнеров
```bash
chmod +x run-docker.sh
./run-docker.sh
```