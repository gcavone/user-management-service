-- V2__seed_data.sql
-- Sample data for development and evaluation purposes
-- 100 users covering all statuses and roles

INSERT INTO users (id, username, email, codice_fiscale, nome, cognome, status, created_by, updated_by) VALUES ('a1000001-0000-0000-0000-000000000000', 'emanuele.vitale64', 'emanuele.vitale1@example.com', 'VTLMNL85D21H501O', 'Emanuele', 'Vitale', 'ACTIVE', 'system', 'system');
INSERT INTO user_roles (user_id, role) VALUES ('a1000001-0000-0000-0000-000000000000', 'REPORTER');

INSERT INTO users (id, username, email, codice_fiscale, nome, cognome, status, created_by, updated_by) VALUES ('a1000002-0000-0000-0000-000000000000', 'valentina.fontana72', 'valentina.fontana2@example.com', 'FNTVLN73D64H501A', 'Valentina', 'Fontana', 'ACTIVE', 'system', 'system');
INSERT INTO user_roles (user_id, role) VALUES ('a1000002-0000-0000-0000-000000000000', 'REPORTER');

INSERT INTO users (id, username, email, codice_fiscale, nome, cognome, status, created_by, updated_by) VALUES ('a1000003-0000-0000-0000-000000000000', 'elisa.greco66', 'elisa.greco3@example.com', 'GRCLSE88D45H501D', 'Elisa', 'Greco', 'DELETED', 'system', 'system');
INSERT INTO user_roles (user_id, role) VALUES ('a1000003-0000-0000-0000-000000000000', 'REPORTER');
INSERT INTO user_roles (user_id, role) VALUES ('a1000003-0000-0000-0000-000000000000', 'MAINTAINER');

INSERT INTO users (id, username, email, codice_fiscale, nome, cognome, status, created_by, updated_by) VALUES ('a1000004-0000-0000-0000-000000000000', 'antonio.caruso77', 'antonio.caruso4@example.com', 'CRSNTN75S14H501O', 'Antonio', 'Caruso', 'ACTIVE', 'system', 'system');
INSERT INTO user_roles (user_id, role) VALUES ('a1000004-0000-0000-0000-000000000000', 'OPERATOR');

INSERT INTO users (id, username, email, codice_fiscale, nome, cognome, status, created_by, updated_by) VALUES ('a1000005-0000-0000-0000-000000000000', 'barbara.fontana15', 'barbara.fontana5@example.com', 'FNTBRB65S64H501V', 'Barbara', 'Fontana', 'ACTIVE', 'system', 'system');
INSERT INTO user_roles (user_id, role) VALUES ('a1000005-0000-0000-0000-000000000000', 'REPORTER');

INSERT INTO users (id, username, email, codice_fiscale, nome, cognome, status, created_by, updated_by) VALUES ('a1000006-0000-0000-0000-000000000000', 'chiara.serra1', 'chiara.serra6@example.com', 'SRRCHR92C55H501M', 'Chiara', 'Serra', 'ACTIVE', 'system', 'system');
INSERT INTO user_roles (user_id, role) VALUES ('a1000006-0000-0000-0000-000000000000', 'DEVELOPER');

INSERT INTO users (id, username, email, codice_fiscale, nome, cognome, status, created_by, updated_by) VALUES ('a1000007-0000-0000-0000-000000000000', 'nicola.russo82', 'nicola.russo7@example.com', 'RSSNCL71S10H501U', 'Nicola', 'Russo', 'ACTIVE', 'system', 'system');
INSERT INTO user_roles (user_id, role) VALUES ('a1000007-0000-0000-0000-000000000000', 'REPORTER');

INSERT INTO users (id, username, email, codice_fiscale, nome, cognome, status, created_by, updated_by) VALUES ('a1000008-0000-0000-0000-000000000000', 'paolo.leone68', 'paolo.leone8@example.com', 'LNEPLA75P25H501X', 'Paolo', 'Leone', 'ACTIVE', 'system', 'system');
INSERT INTO user_roles (user_id, role) VALUES ('a1000008-0000-0000-0000-000000000000', 'OWNER');

INSERT INTO users (id, username, email, codice_fiscale, nome, cognome, status, created_by, updated_by) VALUES ('a1000009-0000-0000-0000-000000000000', 'giulia.ricci8', 'giulia.ricci9@example.com', 'RCCGLI88E48H501J', 'Giulia', 'Ricci', 'ACTIVE', 'system', 'system');
INSERT INTO user_roles (user_id, role) VALUES ('a1000009-0000-0000-0000-000000000000', 'DEVELOPER');

INSERT INTO users (id, username, email, codice_fiscale, nome, cognome, status, created_by, updated_by) VALUES ('a1000010-0000-0000-0000-000000000000', 'andrea.costa69', 'andrea.costa10@example.com', 'CSTNDR96B25H501R', 'Andrea', 'Costa', 'DISABLED', 'system', 'system');
INSERT INTO user_roles (user_id, role) VALUES ('a1000010-0000-0000-0000-000000000000', 'MAINTAINER');
INSERT INTO user_roles (user_id, role) VALUES ('a1000010-0000-0000-0000-000000000000', 'REPORTER');

INSERT INTO users (id, username, email, codice_fiscale, nome, cognome, status, created_by, updated_by) VALUES ('a1000011-0000-0000-0000-000000000000', 'davide.moretti97', 'davide.moretti11@example.com', 'MRTDVD92D18H501G', 'Davide', 'Moretti', 'ACTIVE', 'system', 'system');
INSERT INTO user_roles (user_id, role) VALUES ('a1000011-0000-0000-0000-000000000000', 'DEVELOPER');

INSERT INTO users (id, username, email, codice_fiscale, nome, cognome, status, created_by, updated_by) VALUES ('a1000012-0000-0000-0000-000000000000', 'silvia.marini32', 'silvia.marini12@example.com', 'MRNSLV98M44H501D', 'Silvia', 'Marini', 'ACTIVE', 'system', 'system');
INSERT INTO user_roles (user_id, role) VALUES ('a1000012-0000-0000-0000-000000000000', 'DEVELOPER');
INSERT INTO user_roles (user_id, role) VALUES ('a1000012-0000-0000-0000-000000000000', 'MAINTAINER');

INSERT INTO users (id, username, email, codice_fiscale, nome, cognome, status, created_by, updated_by) VALUES ('a1000013-0000-0000-0000-000000000000', 'emanuele.martinelli81', 'emanuele.martinelli13@example.com', 'MRTMNL65B23H501F', 'Emanuele', 'Martinelli', 'ACTIVE', 'system', 'system');
INSERT INTO user_roles (user_id, role) VALUES ('a1000013-0000-0000-0000-000000000000', 'OWNER');

INSERT INTO users (id, username, email, codice_fiscale, nome, cognome, status, created_by, updated_by) VALUES ('a1000014-0000-0000-0000-000000000000', 'roberto.esposito86', 'roberto.esposito14@example.com', 'SPSRBR97D09H501K', 'Roberto', 'Esposito', 'ACTIVE', 'system', 'system');
INSERT INTO user_roles (user_id, role) VALUES ('a1000014-0000-0000-0000-000000000000', 'REPORTER');

INSERT INTO users (id, username, email, codice_fiscale, nome, cognome, status, created_by, updated_by) VALUES ('a1000015-0000-0000-0000-000000000000', 'emanuele.villa61', 'emanuele.villa15@example.com', 'VLLMNL95D26H501X', 'Emanuele', 'Villa', 'ACTIVE', 'system', 'system');
INSERT INTO user_roles (user_id, role) VALUES ('a1000015-0000-0000-0000-000000000000', 'REPORTER');

INSERT INTO users (id, username, email, codice_fiscale, nome, cognome, status, created_by, updated_by) VALUES ('a1000016-0000-0000-0000-000000000000', 'alessandro.barbieri94', 'alessandro.barbieri16@example.com', 'BRBLSS92L15H501K', 'Alessandro', 'Barbieri', 'ACTIVE', 'system', 'system');
INSERT INTO user_roles (user_id, role) VALUES ('a1000016-0000-0000-0000-000000000000', 'OWNER');

INSERT INTO users (id, username, email, codice_fiscale, nome, cognome, status, created_by, updated_by) VALUES ('a1000017-0000-0000-0000-000000000000', 'marco.greco25', 'marco.greco17@example.com', 'GRCMRC86B08H501H', 'Marco', 'Greco', 'ACTIVE', 'system', 'system');
INSERT INTO user_roles (user_id, role) VALUES ('a1000017-0000-0000-0000-000000000000', 'DEVELOPER');

INSERT INTO users (id, username, email, codice_fiscale, nome, cognome, status, created_by, updated_by) VALUES ('a1000018-0000-0000-0000-000000000000', 'alessandro.bruno10', 'alessandro.bruno18@example.com', 'BRNLSS82M08H501U', 'Alessandro', 'Bruno', 'DELETED', 'system', 'system');
INSERT INTO user_roles (user_id, role) VALUES ('a1000018-0000-0000-0000-000000000000', 'REPORTER');

INSERT INTO users (id, username, email, codice_fiscale, nome, cognome, status, created_by, updated_by) VALUES ('a1000019-0000-0000-0000-000000000000', 'marco.caruso97', 'marco.caruso19@example.com', 'CRSMRC99A03H501X', 'Marco', 'Caruso', 'DISABLED', 'system', 'system');
INSERT INTO user_roles (user_id, role) VALUES ('a1000019-0000-0000-0000-000000000000', 'DEVELOPER');

INSERT INTO users (id, username, email, codice_fiscale, nome, cognome, status, created_by, updated_by) VALUES ('a1000020-0000-0000-0000-000000000000', 'cristina.gallo49', 'cristina.gallo20@example.com', 'GLLCRS90A46H501T', 'Cristina', 'Gallo', 'ACTIVE', 'system', 'system');
INSERT INTO user_roles (user_id, role) VALUES ('a1000020-0000-0000-0000-000000000000', 'OWNER');

INSERT INTO users (id, username, email, codice_fiscale, nome, cognome, status, created_by, updated_by) VALUES ('a1000021-0000-0000-0000-000000000000', 'paola.serra72', 'paola.serra21@example.com', 'SRRPLA92T64H501W', 'Paola', 'Serra', 'ACTIVE', 'system', 'system');
INSERT INTO user_roles (user_id, role) VALUES ('a1000021-0000-0000-0000-000000000000', 'REPORTER');

INSERT INTO users (id, username, email, codice_fiscale, nome, cognome, status, created_by, updated_by) VALUES ('a1000022-0000-0000-0000-000000000000', 'maria.romano41', 'maria.romano22@example.com', 'RMNMRA99A64H501A', 'Maria', 'Romano', 'ACTIVE', 'system', 'system');
INSERT INTO user_roles (user_id, role) VALUES ('a1000022-0000-0000-0000-000000000000', 'OWNER');
INSERT INTO user_roles (user_id, role) VALUES ('a1000022-0000-0000-0000-000000000000', 'DEVELOPER');

INSERT INTO users (id, username, email, codice_fiscale, nome, cognome, status, created_by, updated_by) VALUES ('a1000023-0000-0000-0000-000000000000', 'marco.moretti77', 'marco.moretti23@example.com', 'MRTMRC70C03H501G', 'Marco', 'Moretti', 'ACTIVE', 'system', 'system');
INSERT INTO user_roles (user_id, role) VALUES ('a1000023-0000-0000-0000-000000000000', 'OPERATOR');

INSERT INTO users (id, username, email, codice_fiscale, nome, cognome, status, created_by, updated_by) VALUES ('a1000024-0000-0000-0000-000000000000', 'riccardo.ricci6', 'riccardo.ricci24@example.com', 'RCCRCC80R20H501J', 'Riccardo', 'Ricci', 'DELETED', 'system', 'system');
INSERT INTO user_roles (user_id, role) VALUES ('a1000024-0000-0000-0000-000000000000', 'REPORTER');
INSERT INTO user_roles (user_id, role) VALUES ('a1000024-0000-0000-0000-000000000000', 'DEVELOPER');

INSERT INTO users (id, username, email, codice_fiscale, nome, cognome, status, created_by, updated_by) VALUES ('a1000025-0000-0000-0000-000000000000', 'maria.fabbri51', 'maria.fabbri25@example.com', 'FBBMRA85D49H501T', 'Maria', 'Fabbri', 'DISABLED', 'system', 'system');
INSERT INTO user_roles (user_id, role) VALUES ('a1000025-0000-0000-0000-000000000000', 'MAINTAINER');

INSERT INTO users (id, username, email, codice_fiscale, nome, cognome, status, created_by, updated_by) VALUES ('a1000026-0000-0000-0000-000000000000', 'paola.colombo80', 'paola.colombo26@example.com', 'CLMPLA69A55H501Y', 'Paola', 'Colombo', 'ACTIVE', 'system', 'system');
INSERT INTO user_roles (user_id, role) VALUES ('a1000026-0000-0000-0000-000000000000', 'REPORTER');

INSERT INTO users (id, username, email, codice_fiscale, nome, cognome, status, created_by, updated_by) VALUES ('a1000027-0000-0000-0000-000000000000', 'giorgio.gallo45', 'giorgio.gallo27@example.com', 'GLLGRG97E05H501V', 'Giorgio', 'Gallo', 'DISABLED', 'system', 'system');
INSERT INTO user_roles (user_id, role) VALUES ('a1000027-0000-0000-0000-000000000000', 'OPERATOR');

INSERT INTO users (id, username, email, codice_fiscale, nome, cognome, status, created_by, updated_by) VALUES ('a1000028-0000-0000-0000-000000000000', 'martina.bruno39', 'martina.bruno28@example.com', 'BRNMRT93P63H501P', 'Martina', 'Bruno', 'ACTIVE', 'system', 'system');
INSERT INTO user_roles (user_id, role) VALUES ('a1000028-0000-0000-0000-000000000000', 'REPORTER');

INSERT INTO users (id, username, email, codice_fiscale, nome, cognome, status, created_by, updated_by) VALUES ('a1000029-0000-0000-0000-000000000000', 'giorgio.serra15', 'giorgio.serra29@example.com', 'SRRGRG71C09H501V', 'Giorgio', 'Serra', 'DISABLED', 'system', 'system');
INSERT INTO user_roles (user_id, role) VALUES ('a1000029-0000-0000-0000-000000000000', 'OPERATOR');

INSERT INTO users (id, username, email, codice_fiscale, nome, cognome, status, created_by, updated_by) VALUES ('a1000030-0000-0000-0000-000000000000', 'davide.serra27', 'davide.serra30@example.com', 'SRRDVD78T11H501E', 'Davide', 'Serra', 'ACTIVE', 'system', 'system');
INSERT INTO user_roles (user_id, role) VALUES ('a1000030-0000-0000-0000-000000000000', 'DEVELOPER');

INSERT INTO users (id, username, email, codice_fiscale, nome, cognome, status, created_by, updated_by) VALUES ('a1000031-0000-0000-0000-000000000000', 'sara.esposito1', 'sara.esposito31@example.com', 'SPSSRA92E42H501Q', 'Sara', 'Esposito', 'ACTIVE', 'system', 'system');
INSERT INTO user_roles (user_id, role) VALUES ('a1000031-0000-0000-0000-000000000000', 'DEVELOPER');

INSERT INTO users (id, username, email, codice_fiscale, nome, cognome, status, created_by, updated_by) VALUES ('a1000032-0000-0000-0000-000000000000', 'francesca.costa55', 'francesca.costa32@example.com', 'CSTFRN93P63H501Z', 'Francesca', 'Costa', 'ACTIVE', 'system', 'system');
INSERT INTO user_roles (user_id, role) VALUES ('a1000032-0000-0000-0000-000000000000', 'REPORTER');
INSERT INTO user_roles (user_id, role) VALUES ('a1000032-0000-0000-0000-000000000000', 'MAINTAINER');

INSERT INTO users (id, username, email, codice_fiscale, nome, cognome, status, created_by, updated_by) VALUES ('a1000033-0000-0000-0000-000000000000', 'giorgio.romano19', 'giorgio.romano33@example.com', 'RMNGRG88R18H501A', 'Giorgio', 'Romano', 'ACTIVE', 'system', 'system');
INSERT INTO user_roles (user_id, role) VALUES ('a1000033-0000-0000-0000-000000000000', 'REPORTER');
INSERT INTO user_roles (user_id, role) VALUES ('a1000033-0000-0000-0000-000000000000', 'DEVELOPER');

INSERT INTO users (id, username, email, codice_fiscale, nome, cognome, status, created_by, updated_by) VALUES ('a1000034-0000-0000-0000-000000000000', 'sara.vitale32', 'sara.vitale34@example.com', 'VTLSRA87D62H501V', 'Sara', 'Vitale', 'ACTIVE', 'system', 'system');
INSERT INTO user_roles (user_id, role) VALUES ('a1000034-0000-0000-0000-000000000000', 'OPERATOR');

INSERT INTO users (id, username, email, codice_fiscale, nome, cognome, status, created_by, updated_by) VALUES ('a1000035-0000-0000-0000-000000000000', 'beatrice.costa21', 'beatrice.costa35@example.com', 'CSTBTR74D68H501M', 'Beatrice', 'Costa', 'ACTIVE', 'system', 'system');
INSERT INTO user_roles (user_id, role) VALUES ('a1000035-0000-0000-0000-000000000000', 'MAINTAINER');

INSERT INTO users (id, username, email, codice_fiscale, nome, cognome, status, created_by, updated_by) VALUES ('a1000036-0000-0000-0000-000000000000', 'francesco.costa86', 'francesco.costa36@example.com', 'CSTFRN86L26H501R', 'Francesco', 'Costa', 'ACTIVE', 'system', 'system');
INSERT INTO user_roles (user_id, role) VALUES ('a1000036-0000-0000-0000-000000000000', 'DEVELOPER');

INSERT INTO users (id, username, email, codice_fiscale, nome, cognome, status, created_by, updated_by) VALUES ('a1000037-0000-0000-0000-000000000000', 'riccardo.sorrentino26', 'riccardo.sorrentino37@example.com', 'SRRRCC67M08H501A', 'Riccardo', 'Sorrentino', 'DELETED', 'system', 'system');
INSERT INTO user_roles (user_id, role) VALUES ('a1000037-0000-0000-0000-000000000000', 'REPORTER');

INSERT INTO users (id, username, email, codice_fiscale, nome, cognome, status, created_by, updated_by) VALUES ('a1000038-0000-0000-0000-000000000000', 'lorenzo.ferrari36', 'lorenzo.ferrari38@example.com', 'FRRLRN77L11H501J', 'Lorenzo', 'Ferrari', 'ACTIVE', 'system', 'system');
INSERT INTO user_roles (user_id, role) VALUES ('a1000038-0000-0000-0000-000000000000', 'OPERATOR');

INSERT INTO users (id, username, email, codice_fiscale, nome, cognome, status, created_by, updated_by) VALUES ('a1000039-0000-0000-0000-000000000000', 'silvia.caruso69', 'silvia.caruso39@example.com', 'CRSSLV97L62H501Q', 'Silvia', 'Caruso', 'DISABLED', 'system', 'system');
INSERT INTO user_roles (user_id, role) VALUES ('a1000039-0000-0000-0000-000000000000', 'DEVELOPER');

INSERT INTO users (id, username, email, codice_fiscale, nome, cognome, status, created_by, updated_by) VALUES ('a1000040-0000-0000-0000-000000000000', 'davide.bruno77', 'davide.bruno40@example.com', 'BRNDVD81A04H501A', 'Davide', 'Bruno', 'ACTIVE', 'system', 'system');
INSERT INTO user_roles (user_id, role) VALUES ('a1000040-0000-0000-0000-000000000000', 'REPORTER');

INSERT INTO users (id, username, email, codice_fiscale, nome, cognome, status, created_by, updated_by) VALUES ('a1000041-0000-0000-0000-000000000000', 'monica.longo74', 'monica.longo41@example.com', 'LNGMNC97B53H501Q', 'Monica', 'Longo', 'ACTIVE', 'system', 'system');
INSERT INTO user_roles (user_id, role) VALUES ('a1000041-0000-0000-0000-000000000000', 'DEVELOPER');

INSERT INTO users (id, username, email, codice_fiscale, nome, cognome, status, created_by, updated_by) VALUES ('a1000042-0000-0000-0000-000000000000', 'giulia.moretti95', 'giulia.moretti42@example.com', 'MRTGLI99S64H501Y', 'Giulia', 'Moretti', 'ACTIVE', 'system', 'system');
INSERT INTO user_roles (user_id, role) VALUES ('a1000042-0000-0000-0000-000000000000', 'DEVELOPER');

INSERT INTO users (id, username, email, codice_fiscale, nome, cognome, status, created_by, updated_by) VALUES ('a1000043-0000-0000-0000-000000000000', 'roberto.longo16', 'roberto.longo43@example.com', 'LNGRBR85S28H501V', 'Roberto', 'Longo', 'ACTIVE', 'system', 'system');
INSERT INTO user_roles (user_id, role) VALUES ('a1000043-0000-0000-0000-000000000000', 'DEVELOPER');

INSERT INTO users (id, username, email, codice_fiscale, nome, cognome, status, created_by, updated_by) VALUES ('a1000044-0000-0000-0000-000000000000', 'federica.greco25', 'federica.greco44@example.com', 'GRCFDR83P45H501X', 'Federica', 'Greco', 'ACTIVE', 'system', 'system');
INSERT INTO user_roles (user_id, role) VALUES ('a1000044-0000-0000-0000-000000000000', 'REPORTER');

INSERT INTO users (id, username, email, codice_fiscale, nome, cognome, status, created_by, updated_by) VALUES ('a1000045-0000-0000-0000-000000000000', 'francesca.longo1', 'francesca.longo45@example.com', 'LNGFRN84L58H501I', 'Francesca', 'Longo', 'ACTIVE', 'system', 'system');
INSERT INTO user_roles (user_id, role) VALUES ('a1000045-0000-0000-0000-000000000000', 'DEVELOPER');

INSERT INTO users (id, username, email, codice_fiscale, nome, cognome, status, created_by, updated_by) VALUES ('a1000046-0000-0000-0000-000000000000', 'elisa.longo57', 'elisa.longo46@example.com', 'LNGLSE85M55H501R', 'Elisa', 'Longo', 'ACTIVE', 'system', 'system');
INSERT INTO user_roles (user_id, role) VALUES ('a1000046-0000-0000-0000-000000000000', 'DEVELOPER');

INSERT INTO users (id, username, email, codice_fiscale, nome, cognome, status, created_by, updated_by) VALUES ('a1000047-0000-0000-0000-000000000000', 'andrea.serra80', 'andrea.serra47@example.com', 'SRRNDR97S21H501L', 'Andrea', 'Serra', 'ACTIVE', 'system', 'system');
INSERT INTO user_roles (user_id, role) VALUES ('a1000047-0000-0000-0000-000000000000', 'DEVELOPER');
INSERT INTO user_roles (user_id, role) VALUES ('a1000047-0000-0000-0000-000000000000', 'MAINTAINER');

INSERT INTO users (id, username, email, codice_fiscale, nome, cognome, status, created_by, updated_by) VALUES ('a1000048-0000-0000-0000-000000000000', 'laura.gentile6', 'laura.gentile48@example.com', 'GNTLRA77C41H501N', 'Laura', 'Gentile', 'ACTIVE', 'system', 'system');
INSERT INTO user_roles (user_id, role) VALUES ('a1000048-0000-0000-0000-000000000000', 'DEVELOPER');

INSERT INTO users (id, username, email, codice_fiscale, nome, cognome, status, created_by, updated_by) VALUES ('a1000049-0000-0000-0000-000000000000', 'simone.lombardi50', 'simone.lombardi49@example.com', 'LMBSMN77T23H501X', 'Simone', 'Lombardi', 'ACTIVE', 'system', 'system');
INSERT INTO user_roles (user_id, role) VALUES ('a1000049-0000-0000-0000-000000000000', 'REPORTER');

INSERT INTO users (id, username, email, codice_fiscale, nome, cognome, status, created_by, updated_by) VALUES ('a1000050-0000-0000-0000-000000000000', 'luca.vitale23', 'luca.vitale50@example.com', 'VTLLCU71L08H501Q', 'Luca', 'Vitale', 'ACTIVE', 'system', 'system');
INSERT INTO user_roles (user_id, role) VALUES ('a1000050-0000-0000-0000-000000000000', 'REPORTER');

INSERT INTO users (id, username, email, codice_fiscale, nome, cognome, status, created_by, updated_by) VALUES ('a1000051-0000-0000-0000-000000000000', 'giuseppe.marini68', 'giuseppe.marini51@example.com', 'MRNGSP73M22H501R', 'Giuseppe', 'Marini', 'DELETED', 'system', 'system');
INSERT INTO user_roles (user_id, role) VALUES ('a1000051-0000-0000-0000-000000000000', 'REPORTER');

INSERT INTO users (id, username, email, codice_fiscale, nome, cognome, status, created_by, updated_by) VALUES ('a1000052-0000-0000-0000-000000000000', 'beatrice.pellegrini71', 'beatrice.pellegrini52@example.com', 'PLLBTR97L67H501I', 'Beatrice', 'Pellegrini', 'ACTIVE', 'system', 'system');
INSERT INTO user_roles (user_id, role) VALUES ('a1000052-0000-0000-0000-000000000000', 'REPORTER');

INSERT INTO users (id, username, email, codice_fiscale, nome, cognome, status, created_by, updated_by) VALUES ('a1000053-0000-0000-0000-000000000000', 'paola.fontana99', 'paola.fontana53@example.com', 'FNTPLA80S49H501B', 'Paola', 'Fontana', 'ACTIVE', 'system', 'system');
INSERT INTO user_roles (user_id, role) VALUES ('a1000053-0000-0000-0000-000000000000', 'REPORTER');

INSERT INTO users (id, username, email, codice_fiscale, nome, cognome, status, created_by, updated_by) VALUES ('a1000054-0000-0000-0000-000000000000', 'davide.marini31', 'davide.marini54@example.com', 'MRNDVD69T10H501U', 'Davide', 'Marini', 'ACTIVE', 'system', 'system');
INSERT INTO user_roles (user_id, role) VALUES ('a1000054-0000-0000-0000-000000000000', 'DEVELOPER');

INSERT INTO users (id, username, email, codice_fiscale, nome, cognome, status, created_by, updated_by) VALUES ('a1000055-0000-0000-0000-000000000000', 'antonio.conti20', 'antonio.conti55@example.com', 'CNTNTN79L23H501U', 'Antonio', 'Conti', 'ACTIVE', 'system', 'system');
INSERT INTO user_roles (user_id, role) VALUES ('a1000055-0000-0000-0000-000000000000', 'DEVELOPER');
INSERT INTO user_roles (user_id, role) VALUES ('a1000055-0000-0000-0000-000000000000', 'REPORTER');

INSERT INTO users (id, username, email, codice_fiscale, nome, cognome, status, created_by, updated_by) VALUES ('a1000056-0000-0000-0000-000000000000', 'anna.marini54', 'anna.marini56@example.com', 'MRNNNA91A47H501Z', 'Anna', 'Marini', 'ACTIVE', 'system', 'system');
INSERT INTO user_roles (user_id, role) VALUES ('a1000056-0000-0000-0000-000000000000', 'REPORTER');

INSERT INTO users (id, username, email, codice_fiscale, nome, cognome, status, created_by, updated_by) VALUES ('a1000057-0000-0000-0000-000000000000', 'emanuele.greco39', 'emanuele.greco57@example.com', 'GRCMNL95A12H501B', 'Emanuele', 'Greco', 'ACTIVE', 'system', 'system');
INSERT INTO user_roles (user_id, role) VALUES ('a1000057-0000-0000-0000-000000000000', 'REPORTER');

INSERT INTO users (id, username, email, codice_fiscale, nome, cognome, status, created_by, updated_by) VALUES ('a1000058-0000-0000-0000-000000000000', 'anna.costa63', 'anna.costa58@example.com', 'CSTNNA99R48H501M', 'Anna', 'Costa', 'ACTIVE', 'system', 'system');
INSERT INTO user_roles (user_id, role) VALUES ('a1000058-0000-0000-0000-000000000000', 'DEVELOPER');

INSERT INTO users (id, username, email, codice_fiscale, nome, cognome, status, created_by, updated_by) VALUES ('a1000059-0000-0000-0000-000000000000', 'giulia.greco52', 'giulia.greco59@example.com', 'GRCGLI86S62H501K', 'Giulia', 'Greco', 'ACTIVE', 'system', 'system');
INSERT INTO user_roles (user_id, role) VALUES ('a1000059-0000-0000-0000-000000000000', 'MAINTAINER');

INSERT INTO users (id, username, email, codice_fiscale, nome, cognome, status, created_by, updated_by) VALUES ('a1000060-0000-0000-0000-000000000000', 'pietro.ferretti73', 'pietro.ferretti60@example.com', 'FRRPTR66L19H501Q', 'Pietro', 'Ferretti', 'ACTIVE', 'system', 'system');
INSERT INTO user_roles (user_id, role) VALUES ('a1000060-0000-0000-0000-000000000000', 'OWNER');
INSERT INTO user_roles (user_id, role) VALUES ('a1000060-0000-0000-0000-000000000000', 'DEVELOPER');

INSERT INTO users (id, username, email, codice_fiscale, nome, cognome, status, created_by, updated_by) VALUES ('a1000061-0000-0000-0000-000000000000', 'simone.bruno42', 'simone.bruno61@example.com', 'BRNSMN68E13H501T', 'Simone', 'Bruno', 'ACTIVE', 'system', 'system');
INSERT INTO user_roles (user_id, role) VALUES ('a1000061-0000-0000-0000-000000000000', 'DEVELOPER');

INSERT INTO users (id, username, email, codice_fiscale, nome, cognome, status, created_by, updated_by) VALUES ('a1000062-0000-0000-0000-000000000000', 'roberta.fontana11', 'roberta.fontana62@example.com', 'FNTRBR91E67H501V', 'Roberta', 'Fontana', 'ACTIVE', 'system', 'system');
INSERT INTO user_roles (user_id, role) VALUES ('a1000062-0000-0000-0000-000000000000', 'REPORTER');
INSERT INTO user_roles (user_id, role) VALUES ('a1000062-0000-0000-0000-000000000000', 'MAINTAINER');

INSERT INTO users (id, username, email, codice_fiscale, nome, cognome, status, created_by, updated_by) VALUES ('a1000063-0000-0000-0000-000000000000', 'laura.caruso97', 'laura.caruso63@example.com', 'CRSLRA69S42H501T', 'Laura', 'Caruso', 'ACTIVE', 'system', 'system');
INSERT INTO user_roles (user_id, role) VALUES ('a1000063-0000-0000-0000-000000000000', 'OWNER');

INSERT INTO users (id, username, email, codice_fiscale, nome, cognome, status, created_by, updated_by) VALUES ('a1000064-0000-0000-0000-000000000000', 'luca.longo61', 'luca.longo64@example.com', 'LNGLCU74D05H501K', 'Luca', 'Longo', 'ACTIVE', 'system', 'system');
INSERT INTO user_roles (user_id, role) VALUES ('a1000064-0000-0000-0000-000000000000', 'OPERATOR');

INSERT INTO users (id, username, email, codice_fiscale, nome, cognome, status, created_by, updated_by) VALUES ('a1000065-0000-0000-0000-000000000000', 'simone.mancini78', 'simone.mancini65@example.com', 'MNCSMN81H06H501B', 'Simone', 'Mancini', 'DISABLED', 'system', 'system');
INSERT INTO user_roles (user_id, role) VALUES ('a1000065-0000-0000-0000-000000000000', 'REPORTER');

INSERT INTO users (id, username, email, codice_fiscale, nome, cognome, status, created_by, updated_by) VALUES ('a1000066-0000-0000-0000-000000000000', 'francesco.serra40', 'francesco.serra66@example.com', 'SRRFRN71R01H501O', 'Francesco', 'Serra', 'ACTIVE', 'system', 'system');
INSERT INTO user_roles (user_id, role) VALUES ('a1000066-0000-0000-0000-000000000000', 'REPORTER');

INSERT INTO users (id, username, email, codice_fiscale, nome, cognome, status, created_by, updated_by) VALUES ('a1000067-0000-0000-0000-000000000000', 'roberta.mancini89', 'roberta.mancini67@example.com', 'MNCRBR77B59H501X', 'Roberta', 'Mancini', 'DISABLED', 'system', 'system');
INSERT INTO user_roles (user_id, role) VALUES ('a1000067-0000-0000-0000-000000000000', 'DEVELOPER');
INSERT INTO user_roles (user_id, role) VALUES ('a1000067-0000-0000-0000-000000000000', 'REPORTER');

INSERT INTO users (id, username, email, codice_fiscale, nome, cognome, status, created_by, updated_by) VALUES ('a1000068-0000-0000-0000-000000000000', 'emanuele.gentile55', 'emanuele.gentile68@example.com', 'GNTMNL67H18H501C', 'Emanuele', 'Gentile', 'ACTIVE', 'system', 'system');
INSERT INTO user_roles (user_id, role) VALUES ('a1000068-0000-0000-0000-000000000000', 'DEVELOPER');
INSERT INTO user_roles (user_id, role) VALUES ('a1000068-0000-0000-0000-000000000000', 'REPORTER');

INSERT INTO users (id, username, email, codice_fiscale, nome, cognome, status, created_by, updated_by) VALUES ('a1000069-0000-0000-0000-000000000000', 'alessandro.pellegrini47', 'alessandro.pellegrini69@example.com', 'PLLLSS96B14H501X', 'Alessandro', 'Pellegrini', 'DELETED', 'system', 'system');
INSERT INTO user_roles (user_id, role) VALUES ('a1000069-0000-0000-0000-000000000000', 'REPORTER');

INSERT INTO users (id, username, email, codice_fiscale, nome, cognome, status, created_by, updated_by) VALUES ('a1000070-0000-0000-0000-000000000000', 'francesca.costa79', 'francesca.costa70@example.com', 'CSTFRN98S49H501Y', 'Francesca', 'Costa', 'ACTIVE', 'system', 'system');
INSERT INTO user_roles (user_id, role) VALUES ('a1000070-0000-0000-0000-000000000000', 'REPORTER');

INSERT INTO users (id, username, email, codice_fiscale, nome, cognome, status, created_by, updated_by) VALUES ('a1000071-0000-0000-0000-000000000000', 'monica.pellegrini32', 'monica.pellegrini71@example.com', 'PLLMNC82H68H501P', 'Monica', 'Pellegrini', 'ACTIVE', 'system', 'system');
INSERT INTO user_roles (user_id, role) VALUES ('a1000071-0000-0000-0000-000000000000', 'OPERATOR');

INSERT INTO users (id, username, email, codice_fiscale, nome, cognome, status, created_by, updated_by) VALUES ('a1000072-0000-0000-0000-000000000000', 'laura.leone86', 'laura.leone72@example.com', 'LNELRA94R60H501H', 'Laura', 'Leone', 'DELETED', 'system', 'system');
INSERT INTO user_roles (user_id, role) VALUES ('a1000072-0000-0000-0000-000000000000', 'REPORTER');

INSERT INTO users (id, username, email, codice_fiscale, nome, cognome, status, created_by, updated_by) VALUES ('a1000073-0000-0000-0000-000000000000', 'federica.bruno34', 'federica.bruno73@example.com', 'BRNFDR96D52H501X', 'Federica', 'Bruno', 'ACTIVE', 'system', 'system');
INSERT INTO user_roles (user_id, role) VALUES ('a1000073-0000-0000-0000-000000000000', 'DEVELOPER');

INSERT INTO users (id, username, email, codice_fiscale, nome, cognome, status, created_by, updated_by) VALUES ('a1000074-0000-0000-0000-000000000000', 'anna.ferrari31', 'anna.ferrari74@example.com', 'FRRNNA98D43H501W', 'Anna', 'Ferrari', 'DELETED', 'system', 'system');
INSERT INTO user_roles (user_id, role) VALUES ('a1000074-0000-0000-0000-000000000000', 'REPORTER');

INSERT INTO users (id, username, email, codice_fiscale, nome, cognome, status, created_by, updated_by) VALUES ('a1000075-0000-0000-0000-000000000000', 'fabio.caruso3', 'fabio.caruso75@example.com', 'CRSFBA96M26H501T', 'Fabio', 'Caruso', 'ACTIVE', 'system', 'system');
INSERT INTO user_roles (user_id, role) VALUES ('a1000075-0000-0000-0000-000000000000', 'OPERATOR');

INSERT INTO users (id, username, email, codice_fiscale, nome, cognome, status, created_by, updated_by) VALUES ('a1000076-0000-0000-0000-000000000000', 'laura.serra68', 'laura.serra76@example.com', 'SRRLRA88M58H501K', 'Laura', 'Serra', 'DISABLED', 'system', 'system');
INSERT INTO user_roles (user_id, role) VALUES ('a1000076-0000-0000-0000-000000000000', 'DEVELOPER');

INSERT INTO users (id, username, email, codice_fiscale, nome, cognome, status, created_by, updated_by) VALUES ('a1000077-0000-0000-0000-000000000000', 'silvia.mancini33', 'silvia.mancini77@example.com', 'MNCSLV94E50H501W', 'Silvia', 'Mancini', 'ACTIVE', 'system', 'system');
INSERT INTO user_roles (user_id, role) VALUES ('a1000077-0000-0000-0000-000000000000', 'DEVELOPER');
INSERT INTO user_roles (user_id, role) VALUES ('a1000077-0000-0000-0000-000000000000', 'MAINTAINER');

INSERT INTO users (id, username, email, codice_fiscale, nome, cognome, status, created_by, updated_by) VALUES ('a1000078-0000-0000-0000-000000000000', 'chiara.costa25', 'chiara.costa78@example.com', 'CSTCHR99T46H501E', 'Chiara', 'Costa', 'ACTIVE', 'system', 'system');
INSERT INTO user_roles (user_id, role) VALUES ('a1000078-0000-0000-0000-000000000000', 'DEVELOPER');

INSERT INTO users (id, username, email, codice_fiscale, nome, cognome, status, created_by, updated_by) VALUES ('a1000079-0000-0000-0000-000000000000', 'elisa.leone13', 'elisa.leone79@example.com', 'LNELSE98R50H501T', 'Elisa', 'Leone', 'DISABLED', 'system', 'system');
INSERT INTO user_roles (user_id, role) VALUES ('a1000079-0000-0000-0000-000000000000', 'DEVELOPER');

INSERT INTO users (id, username, email, codice_fiscale, nome, cognome, status, created_by, updated_by) VALUES ('a1000080-0000-0000-0000-000000000000', 'francesca.serra17', 'francesca.serra80@example.com', 'SRRFRN65T58H501V', 'Francesca', 'Serra', 'DISABLED', 'system', 'system');
INSERT INTO user_roles (user_id, role) VALUES ('a1000080-0000-0000-0000-000000000000', 'DEVELOPER');
INSERT INTO user_roles (user_id, role) VALUES ('a1000080-0000-0000-0000-000000000000', 'MAINTAINER');

INSERT INTO users (id, username, email, codice_fiscale, nome, cognome, status, created_by, updated_by) VALUES ('a1000081-0000-0000-0000-000000000000', 'valentina.caruso2', 'valentina.caruso81@example.com', 'CRSVLN96B68H501S', 'Valentina', 'Caruso', 'ACTIVE', 'system', 'system');
INSERT INTO user_roles (user_id, role) VALUES ('a1000081-0000-0000-0000-000000000000', 'REPORTER');

INSERT INTO users (id, username, email, codice_fiscale, nome, cognome, status, created_by, updated_by) VALUES ('a1000082-0000-0000-0000-000000000000', 'paola.colombo62', 'paola.colombo82@example.com', 'CLMPLA76A49H501E', 'Paola', 'Colombo', 'ACTIVE', 'system', 'system');
INSERT INTO user_roles (user_id, role) VALUES ('a1000082-0000-0000-0000-000000000000', 'OPERATOR');

INSERT INTO users (id, username, email, codice_fiscale, nome, cognome, status, created_by, updated_by) VALUES ('a1000083-0000-0000-0000-000000000000', 'cristina.esposito73', 'cristina.esposito83@example.com', 'SPSCRS68C45H501H', 'Cristina', 'Esposito', 'ACTIVE', 'system', 'system');
INSERT INTO user_roles (user_id, role) VALUES ('a1000083-0000-0000-0000-000000000000', 'DEVELOPER');
INSERT INTO user_roles (user_id, role) VALUES ('a1000083-0000-0000-0000-000000000000', 'MAINTAINER');

INSERT INTO users (id, username, email, codice_fiscale, nome, cognome, status, created_by, updated_by) VALUES ('a1000084-0000-0000-0000-000000000000', 'giorgio.leone80', 'giorgio.leone84@example.com', 'LNEGRG91R20H501B', 'Giorgio', 'Leone', 'DISABLED', 'system', 'system');
INSERT INTO user_roles (user_id, role) VALUES ('a1000084-0000-0000-0000-000000000000', 'DEVELOPER');

INSERT INTO users (id, username, email, codice_fiscale, nome, cognome, status, created_by, updated_by) VALUES ('a1000085-0000-0000-0000-000000000000', 'paola.russo76', 'paola.russo85@example.com', 'RSSPLA93E68H501M', 'Paola', 'Russo', 'DELETED', 'system', 'system');
INSERT INTO user_roles (user_id, role) VALUES ('a1000085-0000-0000-0000-000000000000', 'REPORTER');

INSERT INTO users (id, username, email, codice_fiscale, nome, cognome, status, created_by, updated_by) VALUES ('a1000086-0000-0000-0000-000000000000', 'pietro.costa28', 'pietro.costa86@example.com', 'CSTPTR71D21H501A', 'Pietro', 'Costa', 'ACTIVE', 'system', 'system');
INSERT INTO user_roles (user_id, role) VALUES ('a1000086-0000-0000-0000-000000000000', 'DEVELOPER');

INSERT INTO users (id, username, email, codice_fiscale, nome, cognome, status, created_by, updated_by) VALUES ('a1000087-0000-0000-0000-000000000000', 'lorenzo.bruno53', 'lorenzo.bruno87@example.com', 'BRNLRN69C01H501Q', 'Lorenzo', 'Bruno', 'ACTIVE', 'system', 'system');
INSERT INTO user_roles (user_id, role) VALUES ('a1000087-0000-0000-0000-000000000000', 'REPORTER');

INSERT INTO users (id, username, email, codice_fiscale, nome, cognome, status, created_by, updated_by) VALUES ('a1000088-0000-0000-0000-000000000000', 'martina.romano37', 'martina.romano88@example.com', 'RMNMRT79E63H501W', 'Martina', 'Romano', 'DISABLED', 'system', 'system');
INSERT INTO user_roles (user_id, role) VALUES ('a1000088-0000-0000-0000-000000000000', 'REPORTER');
INSERT INTO user_roles (user_id, role) VALUES ('a1000088-0000-0000-0000-000000000000', 'MAINTAINER');

INSERT INTO users (id, username, email, codice_fiscale, nome, cognome, status, created_by, updated_by) VALUES ('a1000089-0000-0000-0000-000000000000', 'elisa.fabbri70', 'elisa.fabbri89@example.com', 'FBBLSE77L44H501F', 'Elisa', 'Fabbri', 'ACTIVE', 'system', 'system');
INSERT INTO user_roles (user_id, role) VALUES ('a1000089-0000-0000-0000-000000000000', 'DEVELOPER');

INSERT INTO users (id, username, email, codice_fiscale, nome, cognome, status, created_by, updated_by) VALUES ('a1000090-0000-0000-0000-000000000000', 'valentina.esposito40', 'valentina.esposito90@example.com', 'SPSVLN68C66H501J', 'Valentina', 'Esposito', 'ACTIVE', 'system', 'system');
INSERT INTO user_roles (user_id, role) VALUES ('a1000090-0000-0000-0000-000000000000', 'REPORTER');

INSERT INTO users (id, username, email, codice_fiscale, nome, cognome, status, created_by, updated_by) VALUES ('a1000091-0000-0000-0000-000000000000', 'paola.ricci90', 'paola.ricci91@example.com', 'RCCPLA94T50H501N', 'Paola', 'Ricci', 'DISABLED', 'system', 'system');
INSERT INTO user_roles (user_id, role) VALUES ('a1000091-0000-0000-0000-000000000000', 'REPORTER');

INSERT INTO users (id, username, email, codice_fiscale, nome, cognome, status, created_by, updated_by) VALUES ('a1000092-0000-0000-0000-000000000000', 'paola.esposito42', 'paola.esposito92@example.com', 'SPSPLA67L64H501B', 'Paola', 'Esposito', 'ACTIVE', 'system', 'system');
INSERT INTO user_roles (user_id, role) VALUES ('a1000092-0000-0000-0000-000000000000', 'REPORTER');

INSERT INTO users (id, username, email, codice_fiscale, nome, cognome, status, created_by, updated_by) VALUES ('a1000093-0000-0000-0000-000000000000', 'lorenzo.fabbri35', 'lorenzo.fabbri93@example.com', 'FBBLRN66S27H501H', 'Lorenzo', 'Fabbri', 'ACTIVE', 'system', 'system');
INSERT INTO user_roles (user_id, role) VALUES ('a1000093-0000-0000-0000-000000000000', 'REPORTER');
INSERT INTO user_roles (user_id, role) VALUES ('a1000093-0000-0000-0000-000000000000', 'MAINTAINER');

INSERT INTO users (id, username, email, codice_fiscale, nome, cognome, status, created_by, updated_by) VALUES ('a1000094-0000-0000-0000-000000000000', 'barbara.caruso75', 'barbara.caruso94@example.com', 'CRSBRB93E46H501A', 'Barbara', 'Caruso', 'ACTIVE', 'system', 'system');
INSERT INTO user_roles (user_id, role) VALUES ('a1000094-0000-0000-0000-000000000000', 'REPORTER');

INSERT INTO users (id, username, email, codice_fiscale, nome, cognome, status, created_by, updated_by) VALUES ('a1000095-0000-0000-0000-000000000000', 'elena.galli42', 'elena.galli95@example.com', 'GLLLNE87L51H501M', 'Elena', 'Galli', 'ACTIVE', 'system', 'system');
INSERT INTO user_roles (user_id, role) VALUES ('a1000095-0000-0000-0000-000000000000', 'OPERATOR');

INSERT INTO users (id, username, email, codice_fiscale, nome, cognome, status, created_by, updated_by) VALUES ('a1000096-0000-0000-0000-000000000000', 'monica.mancini52', 'monica.mancini96@example.com', 'MNCMNC96E62H501T', 'Monica', 'Mancini', 'ACTIVE', 'system', 'system');
INSERT INTO user_roles (user_id, role) VALUES ('a1000096-0000-0000-0000-000000000000', 'REPORTER');
INSERT INTO user_roles (user_id, role) VALUES ('a1000096-0000-0000-0000-000000000000', 'MAINTAINER');

INSERT INTO users (id, username, email, codice_fiscale, nome, cognome, status, created_by, updated_by) VALUES ('a1000097-0000-0000-0000-000000000000', 'alessia.colombo66', 'alessia.colombo97@example.com', 'CLMLSS72L68H501R', 'Alessia', 'Colombo', 'DELETED', 'system', 'system');
INSERT INTO user_roles (user_id, role) VALUES ('a1000097-0000-0000-0000-000000000000', 'OWNER');

INSERT INTO users (id, username, email, codice_fiscale, nome, cognome, status, created_by, updated_by) VALUES ('a1000098-0000-0000-0000-000000000000', 'monica.romano80', 'monica.romano98@example.com', 'RMNMNC77P52H501L', 'Monica', 'Romano', 'ACTIVE', 'system', 'system');
INSERT INTO user_roles (user_id, role) VALUES ('a1000098-0000-0000-0000-000000000000', 'REPORTER');

INSERT INTO users (id, username, email, codice_fiscale, nome, cognome, status, created_by, updated_by) VALUES ('a1000099-0000-0000-0000-000000000000', 'matteo.fontana90', 'matteo.fontana99@example.com', 'FNTMTT73E15H501K', 'Matteo', 'Fontana', 'ACTIVE', 'system', 'system');
INSERT INTO user_roles (user_id, role) VALUES ('a1000099-0000-0000-0000-000000000000', 'REPORTER');
INSERT INTO user_roles (user_id, role) VALUES ('a1000099-0000-0000-0000-000000000000', 'MAINTAINER');

INSERT INTO users (id, username, email, codice_fiscale, nome, cognome, status, created_by, updated_by) VALUES ('a1000100-0000-0000-0000-000000000000', 'stefano.ferretti12', 'stefano.ferretti100@example.com', 'FRRSTF65P14H501H', 'Stefano', 'Ferretti', 'DISABLED', 'system', 'system');
INSERT INTO user_roles (user_id, role) VALUES ('a1000100-0000-0000-0000-000000000000', 'DEVELOPER');

