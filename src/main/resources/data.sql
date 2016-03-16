INSERT INTO event
(caption, description, start, end, all_day, style_name)
VALUES
  ('Brunch', 'Dies ist ein Test', {ts '2016-03-17 10:00:00.00'}, {ts '2016-03-17 13:00:00.00'}, FALSE, 'color2');

INSERT INTO event
(caption, description, start, end, all_day, style_name)
VALUES
  ('Welt', 'Noch einer', {ts '2016-03-15 12:00:00.00'}, {ts '2016-03-15 17:30:00.00'}, FALSE, 'color4');

INSERT INTO event
(caption, description, start, end, all_day, style_name)
VALUES
  ('All-day event', 'Some description', {ts '2016-03-14 00:00:00.00'}, {ts '2016-03-14 00:00:00.00'}, TRUE, 'color3');

INSERT INTO Customer (first_name, last_name) VALUES ('Christian', 'Gebauer');
INSERT INTO Customer (first_name, last_name) VALUES ('Jack', 'Bauer');
INSERT INTO Customer (first_name, last_name) VALUES ('Chloe', 'O Brian');
INSERT INTO Customer (first_name, last_name) VALUES ('Kim', 'Bauer');
INSERT INTO Customer (first_name, last_name) VALUES ('David', 'Palmer');
INSERT INTO Customer (first_name, last_name) VALUES ('Michelle', 'Dessler');
