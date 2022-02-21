CREATE TABLE IF NOT EXISTS profile (
	id serial PRIMARY KEY,
	user_id varchar(50) NOT NULL,
	type varchar(50) NOT NULL,
	email varchar(100)
);

CREATE INDEX profile_type_index ON profile(type);
CREATE INDEX profile_user_id_index ON profile("user_id");