CREATE TABLE public.advert
(
  id serial,
  project_id integer NOT NULL,
  c_url text NOT NULL,
  c_title text,
  c_img text,
  c_sum text, -- стоимость объявления
  c_desc text, -- доп информация
  c_date timestamp,
  CONSTRAINT advert_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.advert
  OWNER TO postgres;
COMMENT ON COLUMN public.advert.c_sum IS 'стоимость объявления';
COMMENT ON COLUMN public.advert.c_desc IS 'доп информация';


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
