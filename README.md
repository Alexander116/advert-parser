## Перед запуском:

### Скрипт для создания таблиц

Необходимо создать таблицы в бд
```
CREATE TABLE public.advert
(
  id serial,
  project_id integer NOT NULL,
  url text NOT NULL,
  title text,
  img text,
  sum text, -- стоимость объявления
  "desc" text, -- доп информация
  date timestamp,
  CONSTRAINT adverts_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.adverts
  OWNER TO postgres;
COMMENT ON COLUMN public.adverts.sum IS 'стоимость объявления';
COMMENT ON COLUMN public.adverts."desc" IS 'доп информация';
```

```
CREATE TABLE public.projects
(
  name text NOT NULL,
  active boolean NOT NULL DEFAULT true,
  url text NOT NULL,
  id serial,
  CONSTRAINT "Projects_pkey" PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.projects
  OWNER TO postgres;
COMMENT ON TABLE public.projects
  IS 'Таблица хранит проекты(ссылки авито) которые нужно отслеживать';
```


## Конфигурационный файл (.properties)

Данный файл хранит конфигурации необходимые приложению. 
Путь к нему указывается через среду окружения в переменной(AD_PARSER_CONFIG). 
Если переменная не будет указана, то программа будет искать конфиг
в домашней дериктории с названием config.properties. Если и этого файла не будет,
то будут применены настройки по умолчанию.

### Параметры конфигурационного файла.

 * `JDBC_URL` - jdbcUrl формата (jdbc:postgresql://localhost:5432/mydb)
 * `DB_LOGIN`- логин бд
 * `DB_PASSWORD` - пароль бд
 * `OBSERVERS` - в этом параметре указываются наблюдатели которые будут оповещать 
 о новых объявлениях.Если существует несколько наблюдателей они записываются через /.
 * `SINGLE_INSTANCE_PORT` - данный порт исп. для запрета одновременного запуска приложения.
 * `STORAGE` - указывает какой класс хранения данных исп., пока существует
  только один парамет `Pg`, для работы с бд postgresql.
  
#### Пример файла конфигурации

```
DB_PASSWORD=denisov727

PORT=6666

DB_LOGIN=postgres

STORAGE=Pg

JDBC_URL=jdbc:postgresql://localhost:5432/mydb

OBSERVERS=Logger/Telegram
```

## Запуск

После создания бд, её таблиц и настройки файла конфигурации можно преступить
к запуску,для этого :

* Переходим в корень проекта.
* mvn package
* java -cp target/AvitoParser-1.0.jar clientcli.Launcher -h
* Вызов предыдущей команды выведет список команд для работы с приложением.
* Параметр settings, присутствующий в командах - это поле для передачи настроек 
вида(DB_PASSWORD=123, DB_LOGIN=yourLogin), данное поле можно исп вместо настройки 
файла конфигурации, если же вы уже настроили данный файл, то можно оставить
 параметр пустым. Если же присутствует и файл и параметр, то 
 `приоритет отдается данному параметру`
 
