create database postSample;
use postSample;
create table post(
 id int(11) NOT NULL AUTO_INCREMENT primary key,
 title nvarchar(100),
 url nvarchar(200),
 content longtext
);
