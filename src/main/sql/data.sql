insert into kpacs(title, description) values ('1', '1');
insert into kpacs(title, description) values ('2', '2');
insert into kpacs(title, description) values ('3', '3');
insert into kpacs(title, description) values ('4', '4');

insert into kpac_sets(title) values ('1');
insert into kpac_sets(title) values ('2');
insert into kpac_sets(title) values ('2');

insert into kpac_set_connections (kpac_id, set_id) values (1, 1);
insert into kpac_set_connections (kpac_id, set_id) values (2, 1);
insert into kpac_set_connections (kpac_id, set_id) values (3, 2);
insert into kpac_set_connections (kpac_id, set_id) values (4, 2);
insert into kpac_set_connections (kpac_id, set_id) values (1, 3);
insert into kpac_set_connections (kpac_id, set_id) values (2, 3);
insert into kpac_set_connections (kpac_id, set_id) values (3, 3);
insert into kpac_set_connections (kpac_id, set_id) values (4, 3);
