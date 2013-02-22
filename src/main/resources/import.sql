INSERT INTO User(user_login, user_password, question, answer, dateOfBirth, email) VALUES ( 'Mateusz', 'ba7816bf8f01cfea414140de5dae2223b00361a396177a9cb410ff61f20015ad', 'Kto to jest władek?', 'laptop', '1991-08-04', 'mateusz@wp.pl');

INSERT INTO UsrGroup(groupName, user_login ) VALUES ('USERS', 'Mateusz')

INSERT INTO TaskList(name, info) values ('Test TaskList', 'Test TaskList Info')
INSERT INTO User_TaskList(User_id, Lists_id) values (1,1)

INSERT INTO Task(name,info,deadline, priority,done) VALUES ('test Task', 'test task info', '2014-01-10',0,false)
INSERT INTO TaskList_Task(TaskList_id, Tasks_id) VALUES (1,1)

INSERT INTO Task(name,info,deadline, priority,done) VALUES ('test Task 2', 'test task info', '2013-01-31',5,false)
INSERT INTO TaskList_Task(TaskList_id, Tasks_id) VALUES (1,2)

INSERT INTO Task(name,info,deadline, priority,done) VALUES ('test Task 3', 'test task info', '2014-01-09',10,false)
INSERT INTO TaskList_Task(TaskList_id, Tasks_id) VALUES (1,3)