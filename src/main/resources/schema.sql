--drop table MPAS, FILMS, GENRES, FILMS_GENRES, USERS, FRIENDS, LIKES;

--drop table if exists FILMS_GENRES;
--drop table if exists LIKES;
--drop table if exists FRIENDS;
--drop table if exists FILMS;
--drop table if exists MPAS;
--drop table if exists GENRES;
--drop table if exists USERS;

drop table if exists FILMS_GENRES, LIKES, FRIENDS, FILMS, MPAS, GENRES, USERS;

create table if not exists MPAS (
    MPA_ID   INTEGER generated by default as identity primary key,
    MPA_NAME CHARACTER VARYING,
    constraint MPAS_PK
        primary key (MPA_ID)
);

create table if not exists FILMS (
    FILM_ID      BIGINT generated by default as identity primary key,
    FILM_NAME    CHARACTER VARYING not null,
    DESCRIPTION  CHARACTER VARYING(200),
    RELEASE_DATE DATE,
    DURATION     INTEGER,
    MPA_ID       INTEGER,
    constraint FILMS_PK
        primary key (FILM_ID),
    constraint FILMS_MPAS_MPA_ID_FK
        foreign key (MPA_ID) references MPAS
);

create table if not exists GENRES (
    GENRE_ID   INTEGER generated by default as identity primary key,
    GENRE_NAME CHARACTER VARYING,
    constraint GENRES_PK
        primary key (GENRE_ID)
);

create table if not exists FILMS_GENRES (
    FILMS_GENRE_ID BIGINT generated by default as identity primary key,
    FILM_ID        INTEGER,
    GENRE_ID       INTEGER,
    constraint FILMS_GENRES_PK
        primary key (FILMS_GENRE_ID),
    constraint FILMS_GENRES_FILMS_FILM_ID_FK
        foreign key (FILM_ID) references FILMS,
    constraint FILMS_GENRES_GENRES_GENRE_ID_FK
        foreign key (GENRE_ID) references GENRES
);

create table if not exists USERS (
    USER_ID   BIGINT generated by default as identity primary key,
    EMAIL     CHARACTER VARYING not null unique,
    LOGIN     CHARACTER VARYING not null unique,
    USER_NAME CHARACTER VARYING,
    BIRTHDAY  DATE,
    constraint USERS_PK
        primary key (USER_ID)
);

create table if not exists FRIENDS (
    REQUEST_ID BIGINT generated by default as identity primary key,
    USER_ID    BIGINT,
    FRIEND_ID  BIGINT,
    constraint FRIENDS_PK
        primary key (REQUEST_ID),
    constraint FRIENDS_USERS_USER_ID_FK
        foreign key (USER_ID) references USERS,
    constraint FRIENDS_USERS_USER_ID_FK_2
        foreign key (FRIEND_ID) references USERS
);

create table if not exists LIKES (
    LIKE_ID BIGINT generated by default as identity primary key,
    FILM_ID BIGINT,
    USER_ID BIGINT,
    constraint LIKES_PK
        primary key (LIKE_ID),
    constraint LIKES_FILMS_FILM_ID_FK
        foreign key (FILM_ID) references FILMS,
    constraint LIKES_USERS_USER_ID_FK
        foreign key (USER_ID) references USERS
);