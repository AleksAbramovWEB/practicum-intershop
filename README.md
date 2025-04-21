## Сдача проектной работы пятого спринта (Создание витрины магазина Spring Boot) 

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