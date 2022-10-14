CREATE TABLE IF NOT EXISTS style
(
    name       varchar(255) NOT NULL
        constraint pk_style
            primary key,
    created_at timestamp DEFAULT now()
);

CREATE TABLE IF NOT EXISTS genre
(
    name       varchar(255) NOT NULL
        constraint pk_genre
            primary key,
    created_at timestamp DEFAULT now()
);

CREATE TABLE IF NOT EXISTS artist
(
    id               integer NOT NULL
        constraint pk_artist
            primary key,
    created_at       timestamp DEFAULT now(),
    last_modified_at timestamp DEFAULT now(),
    data_quality     varchar(255),
    name             varchar(1000),
    profile          text,
    real_name        varchar(2000)
);

CREATE TABLE IF NOT EXISTS label
(
    id               integer NOT NULL
        constraint pk_label
            primary key,
    created_at       timestamp DEFAULT now(),
    last_modified_at timestamp DEFAULT now(),
    contact_info     text,
    data_quality     varchar(255),
    name             varchar(255),
    profile          text,
    parent_label_id  integer
        constraint fk_label_parent_label_id_label_id
            references label
);

CREATE TABLE IF NOT EXISTS master
(
    id               integer NOT NULL
        constraint pk_master
            primary key,
    created_at       timestamp DEFAULT now(),
    last_modified_at timestamp DEFAULT now(),
    data_quality     varchar(255),
    title            varchar(2000),
    year             smallint
);

CREATE TABLE IF NOT EXISTS release
(
    id                  integer NOT NULL
        constraint pk_release
            primary key,
    created_at          timestamp DEFAULT now(),
    last_modified_at    timestamp DEFAULT now(),
    country             varchar(255),
    data_quality        varchar(255),
    has_valid_day       boolean,
    has_valid_month     boolean,
    has_valid_year      boolean,
    is_master           boolean,
    master_id           integer
        constraint fk_release_master_id_master
            references master,
    listed_release_date varchar(255),
    notes               text,
    release_date        date,
    status              varchar(255),
    title               varchar(10000)
);

CREATE TABLE IF NOT EXISTS release_genre
(
    created_at timestamp DEFAULT now(),
    genre      varchar(255) NOT NULL
        constraint fk_release_genre_genre_genre
            references genre,
    release_id integer      NOT NULL
        constraint fk_release_genre_release_id_release
            references release,
    constraint pk_release_genre
        primary key (release_id, genre)
);

CREATE TABLE IF NOT EXISTS release_track
(
    created_at timestamp DEFAULT now(),
    hash       integer NOT NULL,
    duration   text,
    position   text,
    title      text,
    release_id integer NOT NULL
        constraint fk_release_track_release_id_release
            references release,
    constraint pk_release_track
        primary key (release_id, hash)
);

CREATE TABLE IF NOT EXISTS label_release
(
    created_at        timestamp DEFAULT now(),
    category_notation varchar(1000),
    label_id          integer NOT NULL
        constraint fk_label_release_label_id_label
            references label,
    release_id        integer NOT NULL
        constraint fk_label_release_release_id_release
            references release,
    constraint pk_label_release
        primary key (label_id, release_id)
);

CREATE TABLE IF NOT EXISTS release_image
(
    id               uuid    NOT NULL
        constraint pk_release_image
            primary key,
    created_at       timestamp DEFAULT now(),
    last_modified_at timestamp DEFAULT now(),
    hash             integer NOT NULL,
    data             text,
    release_id       integer NOT NULL
        constraint fk_release_image_release_id_release
            references release,
    constraint uq_release_image_release_id_hash
        unique (release_id, hash)
);

CREATE TABLE IF NOT EXISTS release_contract
(
    created_at timestamp DEFAULT now(),
    hash       integer NOT NULL,
    contract   varchar(5000),
    label_id   integer NOT NULL
        constraint fk_release_contract_label_id_label
            references label,
    release_id integer NOT NULL
        constraint fk_release_contract_release_id_release
            references release,
    constraint pk_release_contract
        primary key (release_id, label_id, hash)
);

CREATE TABLE IF NOT EXISTS release_identifier
(
    created_at  timestamp DEFAULT now(),
    hash        integer NOT NULL,
    description text,
    type        text,
    value       text,
    release_id  integer NOT NULL
        constraint fk_release_identifier_release_id_release
            references release,
    constraint pk_release_identifier
        primary key (release_id, hash)
);

CREATE TABLE IF NOT EXISTS master_video
(
    created_at  timestamp DEFAULT now(),
    hash        integer NOT NULL,
    description varchar(15000),
    title       varchar(1000),
    url         varchar(1000),
    master_id   integer NOT NULL
        constraint fk_master_video_master_id_master
            references master,
    constraint pk_master_video
        primary key (master_id, hash)
);

CREATE TABLE IF NOT EXISTS master_genre
(
    created_at timestamp DEFAULT now(),
    genre      varchar(255) NOT NULL
        constraint fk_master_genre_genre_genre
            references genre,
    master_id  integer      NOT NULL
        constraint fk_master_genre_master_id_master
            references master,
    constraint pk_master_genre
        primary key (master_id, genre)
);

CREATE TABLE IF NOT EXISTS master_style
(
    created_at timestamp DEFAULT now(),
    master_id  integer      NOT NULL
        constraint fk_master_style_master_id_master
            references master,
    style      varchar(255) NOT NULL
        constraint fk_master_style_style_style
            references style,
    constraint pk_master_style
        primary key (master_id, style)
);

CREATE TABLE IF NOT EXISTS release_style
(
    created_at timestamp DEFAULT now(),
    release_id integer      NOT NULL
        constraint fk_release_style_release_id_release
            references release,
    style      varchar(255) NOT NULL
        constraint fk_release_style_style_style
            references style,
    constraint pk_release_style
        primary key (release_id, style)
);

CREATE TABLE IF NOT EXISTS release_video
(
    created_at  timestamp DEFAULT now(),
    hash        integer NOT NULL,
    description text,
    title       text,
    url         text,
    release_id  integer NOT NULL
        constraint fk_release_video_release_id_release
            references release,
    constraint pk_release_video
        primary key (release_id, hash)
);

CREATE TABLE IF NOT EXISTS label_url
(
    created_at timestamp DEFAULT now(),
    hash       integer       NOT NULL,
    url        varchar(5000) NOT NULL,
    label_id   integer       NOT NULL
        constraint fk_label_url_label_id_label
            references label,
    constraint pk_label_url
        primary key (label_id, hash)
);

CREATE TABLE IF NOT EXISTS release_format
(
    created_at  timestamp DEFAULT now(),
    hash        integer NOT NULL,
    description varchar(10000),
    name        varchar(255),
    quantity    integer,
    text        varchar(5000),
    release_id  integer NOT NULL
        constraint fk_release_format_release_id_release
            references release,
    constraint pk_release_format
        primary key (release_id, hash)
);

CREATE TABLE IF NOT EXISTS artist_alias
(
    created_at timestamp DEFAULT now(),
    alias_id   integer NOT NULL
        constraint fk_artist_alias_alias_id_artist
            references artist,
    artist_id  integer NOT NULL
        constraint fk_artist_alias_artist_id_artist
            references artist,
    constraint pk_artist_alias
        primary key (artist_id, alias_id)
);

CREATE TABLE IF NOT EXISTS artist_name_variation
(
    created_at     timestamp DEFAULT now(),
    hash           integer       NOT NULL,
    name_variation varchar(2000) NOT NULL,
    artist_id      integer       NOT NULL
        constraint fk_artist_name_variation_artist_id_artist
            references artist,
    constraint pk_artist_name_variation
        primary key (artist_id, hash)
);

CREATE TABLE IF NOT EXISTS master_artist
(
    created_at timestamp DEFAULT now(),
    artist_id  integer NOT NULL
        constraint fk_master_artist_artist_id_artist
            references artist,
    master_id  integer NOT NULL
        constraint fk_master_artist_master_id_master
            references master,
    constraint pk_master_artist
        primary key (master_id, artist_id)
);

CREATE TABLE IF NOT EXISTS release_artist
(
    created_at timestamp DEFAULT now(),
    artist_id  integer NOT NULL
        constraint fk_release_artist_artist_id_artist
            references artist,
    release_id integer NOT NULL
        constraint fk_release_artist_release_id_release
            references release,
    constraint pk_release_artist
        primary key (release_id, artist_id)
);

CREATE TABLE IF NOT EXISTS release_credited_artist
(
    created_at timestamp DEFAULT now(),
    hash       integer NOT NULL,
    role       varchar(20000),
    artist_id  integer NOT NULL
        constraint fk_release_credited_artist_artist_id_artist
            references artist,
    release_id integer NOT NULL
        constraint fk_release_credited_artist_release_id_release
            references release,
    constraint pk_release_credited_artist
        primary key (release_id, artist_id, hash)
);

CREATE TABLE IF NOT EXISTS artist_url
(
    created_at timestamp DEFAULT now(),
    hash       integer       NOT NULL,
    url        varchar(5000) NOT NULL,
    artist_id  integer       NOT NULL
        constraint fk_artist_url_artist_id_artist
            references artist,
    constraint pk_artist_url
        primary key (artist_id, hash)
);

CREATE TABLE IF NOT EXISTS artist_group
(
    created_at timestamp DEFAULT now(),
    artist_id  integer NOT NULL
        constraint fk_artist_group_artist_id_artist
            references artist,
    group_id   integer NOT NULL
        constraint fk_artist_group_group_id_artist
            references artist
);