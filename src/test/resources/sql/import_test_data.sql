delete from public.products;
delete from public.store_products;
delete from public.stores;
delete from public.regions;
delete from public.clusters;

INSERT INTO public.clusters (id, created_at, updated_at, name) VALUES (1, '2021-10-04 16:03:49.445106', '2021-10-04 16:03:49.445094', 'Europe');
INSERT INTO public.clusters (id, created_at, updated_at, name) VALUES (2, '2021-10-04 16:03:49.546820', '2021-10-04 16:03:49.546804', 'Asia');
INSERT INTO public.clusters (id, created_at, updated_at, name) VALUES (3, '2021-10-04 16:03:49.553803', '2021-10-04 16:03:49.553786', 'USA');

INSERT INTO public.regions (id, created_at, updated_at, name, type, clusters_id) VALUES (1, '2021-10-04 16:03:49.713766', '2021-10-04 16:03:49.713750', 'North EU', 'XY', 1);
INSERT INTO public.regions (id, created_at, updated_at, name, type, clusters_id) VALUES (2, '2021-10-04 16:03:49.730857', '2021-10-04 16:03:49.730843', 'South EU', 'XY', 1);
INSERT INTO public.regions (id, created_at, updated_at, name, type, clusters_id) VALUES (3, '2021-10-04 16:03:49.741751', '2021-10-04 16:03:49.741741', 'North AS', 'ZZ', 2);
INSERT INTO public.regions (id, created_at, updated_at, name, type, clusters_id) VALUES (4, '2021-10-04 16:03:49.750724', '2021-10-04 16:03:49.750710', 'South AS', 'ZZ', 2);
INSERT INTO public.regions (id, created_at, updated_at, name, type, clusters_id) VALUES (5, '2021-10-04 16:03:49.758832', '2021-10-04 16:03:49.758820', 'Japan', 'JP', 2);

INSERT INTO public.stores (id, created_at, updated_at, name, name_alias, theme, region_id) VALUES (1, '2021-10-04 16:03:49.898344', '2021-10-04 16:03:49.898334', 'An unpaged store', 'An unpaged store', 'A bootless store focused on a cougar', 4);
INSERT INTO public.stores (id, created_at, updated_at, name, name_alias, theme, region_id) VALUES (2, '2021-10-04 16:03:49.898793', '2021-10-04 16:03:49.898353', 'An earnest store', 'An earnest store', 'A mangy store focused on a prose', 2);
INSERT INTO public.stores (id, created_at, updated_at, name, name_alias, theme, region_id) VALUES (3, '2021-10-04 16:03:49.899318', '2021-10-04 16:03:49.899309', 'A woolen store', 'A woolen store', 'What an a footling Store!', 5);
INSERT INTO public.stores (id, created_at, updated_at, name, name_alias, theme, region_id) VALUES (4, '2021-10-04 16:03:49.898273', '2021-10-04 16:03:49.898262', 'A chard store', 'A chard store', 'A reviled store focused on a sauce', 5);
INSERT INTO public.stores (id, created_at, updated_at, name, name_alias, theme, region_id) VALUES (5, '2021-10-04 16:03:49.898293', '2021-10-04 16:03:49.898283', 'A Sunday store', 'A Sunday store', 'What an a shaky Store!', 1);
INSERT INTO public.stores (id, created_at, updated_at, name, name_alias, theme, region_id) VALUES (6, '2021-10-04 16:03:49.898313', '2021-10-04 16:03:49.898303', 'A breasted store', 'A breasted store', 'What an a wieldy Store!', 5);
INSERT INTO public.stores (id, created_at, updated_at, name, name_alias, theme, region_id) VALUES (7, '2021-10-04 16:03:49.897723', '2021-10-04 16:03:49.897711', 'A quiet store', 'A quiet store', 'A conjoined store focused on an accordion', 4);
INSERT INTO public.stores (id, created_at, updated_at, name, name_alias, theme, region_id) VALUES (8, '2021-10-04 16:03:49.898406', '2021-10-04 16:03:49.898396', 'A gloomy store', 'A gloomy store', 'A freer store focused on a whistle', 5);
INSERT INTO public.stores (id, created_at, updated_at, name, name_alias, theme, region_id) VALUES (9, '2021-10-04 16:03:49.898426', '2021-10-04 16:03:49.898416', 'A displeased store', 'A displeased store', 'What an an apish Store!', 1);
INSERT INTO public.stores (id, created_at, updated_at, name, name_alias, theme, region_id) VALUES (10, '2021-10-04 16:03:49.898283', '2021-10-04 16:03:49.898273', 'A faucial store', 'A faucial store', 'What an a wigless Store!', 1);


INSERT INTO public.store_products (id, created_at, updated_at, hash_id, product, season, store_id) VALUES (1, '2021-10-04 16:03:52.075174', '2021-10-04 16:03:52.075168', 940350415, 'PD_WATCHMAKER', 'S18', 1);
INSERT INTO public.store_products (id, created_at, updated_at, hash_id, product, season, store_id) VALUES (2, '2021-10-04 16:03:52.069262', '2021-10-04 16:03:52.069255', 271780377, 'PD_BERRY', 'S19', 1);
INSERT INTO public.store_products (id, created_at, updated_at, hash_id, product, season, store_id) VALUES (3, '2021-10-04 16:03:52.069518', '2021-10-04 16:03:52.069343', 505828757, 'PD_OFFICE', 'S19', 1);
INSERT INTO public.store_products (id, created_at, updated_at, hash_id, product, season, store_id) VALUES (4, '2021-10-04 16:03:52.075161', '2021-10-04 16:03:52.075155', -515311920, 'PD66_MASCARA', 'S18', 2);
INSERT INTO public.store_products (id, created_at, updated_at, hash_id, product, season, store_id) VALUES (5, '2021-10-04 16:03:52.082083', '2021-10-04 16:03:52.082078', 1604688406, 'PD74_DONNA', 'S19', 2);
INSERT INTO public.store_products (id, created_at, updated_at, hash_id, product, season, store_id) VALUES (6, '2021-10-04 16:03:52.081627', '2021-10-04 16:03:52.081621', -968467582, 'PD90_RATE', 'S19', 3);
INSERT INTO public.store_products (id, created_at, updated_at, hash_id, product, season, store_id) VALUES (7, '2021-10-04 16:03:52.077898', '2021-10-04 16:03:52.077892', -942001087, 'PD51_SHOCK', 'S19', 3);
INSERT INTO public.store_products (id, created_at, updated_at, hash_id, product, season, store_id) VALUES (8, '2021-10-04 16:03:52.068288', '2021-10-04 16:03:52.068282', -218413861, 'PD57_FAMILY', 'S19', 3);
INSERT INTO public.store_products (id, created_at, updated_at, hash_id, product, season, store_id) VALUES (9, '2021-10-04 16:03:52.073612', '2021-10-04 16:03:52.073606', -320588558, 'PD63_SMILE', 'S20', 4);
INSERT INTO public.store_products (id, created_at, updated_at, hash_id, product, season, store_id) VALUES (10, '2021-10-04 16:03:52.075148', '2021-10-04 16:03:52.075142', -515311951, 'PD66_MASCARA', 'S17', 4);
INSERT INTO public.store_products (id, created_at, updated_at, hash_id, product, season, store_id) VALUES (11, '2021-10-04 16:04:13.630229', '2021-10-04 16:04:13.630222', 280292041, 'PD73_WEEDER', 'S19', 5);
INSERT INTO public.store_products (id, created_at, updated_at, hash_id, product, season, store_id) VALUES (12, '2021-10-04 16:04:13.630242', '2021-10-04 16:04:13.630236', 280292754, 'PD73_WEEDER', 'S21', 6);
INSERT INTO public.store_products (id, created_at, updated_at, hash_id, product, season, store_id) VALUES (13, '2021-10-04 16:04:13.630258', '2021-10-04 16:04:13.630249', 280292785, 'PD73_WEEDER', 'S22', 7);
INSERT INTO public.store_products (id, created_at, updated_at, hash_id, product, season, store_id) VALUES (14, '2021-10-04 16:04:13.630274', '2021-10-04 16:04:13.630267', 2032089674, 'PD74_DONNA', 'S18', 7);
INSERT INTO public.store_products (id, created_at, updated_at, hash_id, product, season, store_id) VALUES (15, '2021-10-04 16:04:13.630288', '2021-10-04 16:04:13.630281', 2032089736, 'PD74_DONNA', 'S19', 7);
INSERT INTO public.store_products (id, created_at, updated_at, hash_id, product, season, store_id) VALUES (16, '2021-10-04 16:04:13.630303', '2021-10-04 16:04:13.630296', 2032090418, 'PD74_DONNA', 'S20', 8);
INSERT INTO public.store_products (id, created_at, updated_at, hash_id, product, season, store_id) VALUES (17, '2021-10-04 16:04:13.630316', '2021-10-04 16:04:13.630310', 2032090449, 'PD74_DONNA', 'S21', 8);
INSERT INTO public.store_products (id, created_at, updated_at, hash_id, product, season, store_id) VALUES (18, '2021-10-04 16:04:13.630330', '2021-10-04 16:04:13.630323', 2032090480, 'PD74_DONNA', 'S22', 9);
INSERT INTO public.store_products (id, created_at, updated_at, hash_id, product, season, store_id) VALUES (19, '2021-10-04 16:04:13.630343', '2021-10-04 16:04:13.630337', 1519128906, 'PD75_ACTOR', 'S17', 9);
INSERT INTO public.store_products (id, created_at, updated_at, hash_id, product, season, store_id) VALUES (20, '2021-10-04 16:04:13.630359', '2021-10-04 16:04:13.630350', 1519128937, 'PD75_ACTOR', 'S18', 10);
INSERT INTO public.store_products (id, created_at, updated_at, hash_id, product, season, store_id) VALUES (21, '2021-10-04 16:04:13.630374', '2021-10-04 16:04:13.630367', 1519128968, 'PD75_ACTOR', 'S19', 10);

INSERT INTO public.products (id, created_at, updated_at, description, ean, hash_id, model, season, size, sku) VALUES (1, '2021-10-04 16:03:53.957061', '2021-10-04 16:03:53.957053', 'Fugiat est aliqua labore do labore ea consectetur consequat amet qui laboris Lorem culpa exercitation est.', 4574659842242, -494894673, 'PD_WATCHMAKER', 'S18', 'XXS', '12000001-XXS');
INSERT INTO public.products (id, created_at, updated_at, description, ean, hash_id, model, season, size, sku) VALUES (2, '2021-10-04 16:03:53.957138', '2021-10-04 16:03:53.957126', 'Fugiat est aliqua labore do labore ea consectetur consequat amet qui laboris Lorem culpa exercitation est.', 8246844217552, -1121901939, 'PD_BERRY', 'S19', 'XS', '12000001-XS');
INSERT INTO public.products (id, created_at, updated_at, description, ean, hash_id, model, season, size, sku) VALUES (3, '2021-10-04 16:03:53.957627', '2021-10-04 16:03:53.957618', 'Aliqua commodo consequat adipisicing do.', 8140247247844, -493971152, 'PD74_DONNA', 'S18', 'XXS', '12000002-XXS');
INSERT INTO public.products (id, created_at, updated_at, description, ean, hash_id, model, season, size, sku) VALUES (4, '2021-10-04 16:03:53.960655', '2021-10-04 16:03:53.960648', 'In aute dolor officia occaecat.', 2785558169979, 1905945002, 'PD13_POWER', 'S17', 'L', '12000013-L');
INSERT INTO public.products (id, created_at, updated_at, description, ean, hash_id, model, season, size, sku) VALUES (5, '2021-10-04 16:03:53.958385', '2021-10-04 16:03:53.958377', 'Cillum tempor ex ipsum aute.', 7722174946771, 1905917134, 'PD5_PIPE', 'S17', 'M', '12000005-M');