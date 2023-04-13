-- Dreamers Table
CREATE TABLE dreamers (
	id int auto_increment not null,
    dreamer_id VARCHAR(32) not null,
	first_name VARCHAR(32) not null,
    last_name VARCHAR(32) not null,
    date_of_birth VARCHAR(32) not null,
    gender VARCHAR(32) not null,
    email VARCHAR(128) not null,
    password VARCHAR(512) not null,
    profile_image_url VARCHAR(256) not null,
    last_login date,
    last_login_display date,
    join_date date not null,
    role VARCHAR(32) not null,
    active boolean not null,
    not_locked boolean not null,
    UNIQUE(dreamer_id),
    UNIQUE(email),
    primary key(id)
);

-- PROFILE IMAGE Table
CREATE TABLE images (
    id int auto_increment not null,
    dreamer_id VARCHAR(32) not null,
    profile_image longblob,
    primary key(id),
    constraint fk_images_dreamer_id foreign key(dreamer_id) references dreamers(dreamer_id)
);