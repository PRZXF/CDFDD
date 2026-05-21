@echo off
"C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe" -u root -p123456 < "d:\数据库应用技术\create_tables.sql"
"C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe" -u root -p123456 < "d:\数据库应用技术\insert_data.sql"
echo Database initialized successfully!