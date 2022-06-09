CREATE SCHEMA "metadata" AUTHORIZATION "postgres";

CREATE TABLE "metadata"."app_log" (
  "log_level" VARCHAR, 
  "location" VARCHAR, 
  "message" VARCHAR, 
  "category" VARCHAR, 
  "log_date" TIMESTAMP WITHOUT TIME ZONE
) WITHOUT OIDS;