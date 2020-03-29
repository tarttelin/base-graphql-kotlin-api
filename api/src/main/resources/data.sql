DELETE FROM THING;
DELETE FROM MEMBER;
DELETE FROM HOUSEHOLD;
DELETE FROM COMMUNITY;

INSERT INTO community VALUES (1, 'Deans Farm'), (2, 'Amersham massiv');

INSERT INTO household VALUES (1, '3', 'RG4 5JZ', 1);
INSERT INTO household VALUES (2, '4', 'RG4 5JZ', 1);


INSERT INTO MEMBER VALUES (1, 'Chris', 1, 'chris_t'),
                          (2, 'Freya', 1, 'shipsmouse'),
                          (3, 'Jessica', 1, 'jessicat'),
                          (4, 'Mary', 2, 'mary-reed'),
                          (5, 'Alan', 2, 'al-da-man');

INSERT INTO THING VALUES
                         (1, 'Strong plain bread flour', '1.5KG', 'Groceries', 1 ),
                         (2, 'Dairy milk chocolate', '100G bar', 'Groceries', 3 ),
                         (3, 'Cheese', '200G', 'Groceries', 4 ),
                         (4, 'Prescription from Boots', '1 item', 'Medicine', 5 );
