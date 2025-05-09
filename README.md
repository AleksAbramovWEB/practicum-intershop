## Сдача проектной работы шестого спринта (Spring Web: Reactive Stack) 

### ✅  Устанавливаем переменное окружение в .env

```
UPLOAD_IMAGE_DIR=

DB_URL=r2dbc:postgresql:
DB_USERNAME=
DB_PASSWORD=

TEST_UPLOAD_IMAGE_DIR=

TEST_DB_URL=r2dbc:postgresql:
TEST_DB_USERNAME=
TEST_DB_PASSWORD=

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

### ✅ Создание образа и запуск контейнера докер
```bash
chmod +x run-docker.sh
./run-docker.sh
```