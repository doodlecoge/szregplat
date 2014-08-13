INSERT INTO ws320.hospitals
(id, name, address, phone, wsdl)
VALUES
  ('FQYY', 'FQYY name', 'FQYY addr', '13812345678', 'http://localhost:8080/'),
  ('MDYY', 'MDYY name', 'MDYY addr', '13812345678', 'http://localhost:8080/'),
  ('SLBB', 'SLBB name', 'SLBB addr', '13812345678', 'http://localhost:8080/'),
  ('SLBQ', 'SLBQ name', 'SLBQ addr', '13812345678', 'http://localhost:8080/'),
  ('SLDQ', 'SLDQ name', 'SLDQ addr', '13812345678', 'http://localhost:8080/'),
  ('SZWY', 'SZWY name', 'SZWY addr', '13812345678', 'http://localhost:8080/'),
  ('ZYYY', 'ZYYY name', 'ZYYY addr', '13812345678', 'http://localhost:8080/');


INSERT INTO
  ws320.triggers (selector, attribute, oldvalue, newvalue, action, stage)
VALUES
  (NULL, 'status', '0', '1', 'cancel_reg', 'cmp'),
  ('(doctor=''包广勤'')', 'department', '专家门诊', '儿科', 'replace_val', 'rep');


INSERT INTO
  ws320.configs (key, value, annotation)
VALUES
  ('auto-start', 'N', 'when system allow registration');