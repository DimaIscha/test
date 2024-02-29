Перед запуском приложения надо выполнить команду для docker по запуску контейнера с PostgreSQL базой данных.

docker run --name pg -p 5432:5432 -e POSTGRES_USER=myuser -e POSTGRES_PASSWORD=qwe -e POSTGRES_DB=mydb -d postgres
