CREATE USER IF NOT EXISTS 'week21_xsw0803'@'localhost'
IDENTIFIED BY 'Vivianxuling2';

GRANT ALL PRIVILEGES ON week21_db.*
TO 'week21_xsw0803'@'localhost';

FLUSH PRIVILEGES;
