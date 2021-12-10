CREATE TABLE TB_CLIENT (
    ID INTEGER PRIMARY KEY AUTO_INCREMENT,
    NAME VARCHAR(100),
    CPF VARCHAR(11) NOT NULL UNIQUE
);

CREATE TABLE TB_PRODUCT (
    CODE INTEGER PRIMARY KEY,
    DESCRIPTION VARCHAR(100),
    UNIT_PRICE NUMERIC(20,2)
);

CREATE TABLE TB_ORDER (
    ID INTEGER PRIMARY KEY AUTO_INCREMENT,
    CLIENT_ID INTEGER REFERENCES CLIENT (ID),
    ORDER_DATE TIMESTAMP,
    STATUS VARCHAR(20),
    TOTAL NUMERIC(20,2)
);

CREATE TABLE TB_ORDER_ITEM (
    ID INTEGER PRIMARY KEY AUTO_INCREMENT,
    ORDER_ID INTEGER REFERENCES TB_ORDER (ID),
    PRODUCT_CODE INTEGER REFERENCES PRODUCT (CODE),
    AMOUNT INTEGER
);

CREATE TABLE TB_USER (
    LOGIN VARCHAR(50) PRIMARY KEY,
    PASSWORD VARCHAR(255) NOT NULL,
    ADMIN BOOL DEFAULT FALSE
);