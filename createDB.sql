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
