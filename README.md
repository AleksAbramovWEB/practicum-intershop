## Сдача проектной работы седьмого спринта (На основе существующего проекта «Витрина интернет-магазина» нужно разработать RESTful-сервис платежей с помощью OpenAPI и использовать Redis в качестве кеша товаров.) 

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