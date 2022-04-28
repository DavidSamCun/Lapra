mysql -u master -p

create schema lapra;

use lapra;

create table laptop(
id int not null primary key,
brand_name varchar(50),
model_name varchar(50),
screen_size Float,
memory int,
storage float,
screen_birghtness int,
screen_refresh_hz int,
price float
);
//added laptop

create table questionnaire (
id int not null primary key,
answer_2_type varchar(100),
answer_3_size Integer,
answer_4_function varchar(100),
answer_5_price Integer
);      
//added questionnaire

create table user(
id int not null primary key,
login varchar(50) not null,
password_hash varchar(6) not null,
first_name varchar(50),


)
