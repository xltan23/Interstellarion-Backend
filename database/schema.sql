-- DREAMERS Table
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

-- PLANETS Table
CREATE TABLE planets (
	id int auto_increment not null,
    name VARCHAR(32) not null,
    mass double not null,
    radius double not null,
    period double not null,
    semi_major_axis double not null,
    temperature int not null,
    distance_light_years double not null,
    host_star_mass double not null,
    host_star_temperature double not null,
    gravity double not null,
    solar_insolation double not null,
    star_type VARCHAR(32) not null,
    travel_time double not null,
    cost double not null,
    description longtext not null,
    thumbnailUrl VARCHAR(256) not null,
    coverUrl VARCHAR(256) not null,
	UNIQUE(name),
    primary key(id)
);

-- BACKGROUND Table
CREATE TABLE background (
	id int auto_increment not null,
	title VARCHAR(64) not null,
	background VARCHAR(256) not null,
	primary key(id)
);

-- BOOKINGS Table
CREATE TABLE bookings (
    id int auto_increment not null,
    dreamer_id VARCHAR(32) not null,
    planet VARCHAR(32) not null,
    number_of_pax int not null,
    travel_date VARCHAR(128) not null,
    total_cost double not null,
    date_of_booking Date not null,
    primary key(id),
    constraint fk_bookings_dreamer_id foreign key(dreamer_id) references dreamers(dreamer_id)
);