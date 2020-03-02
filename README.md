# spring-batch-tutorial

## Cofiguration
- spring-batch
- quartsz : jdbc-jobstore
- jasypt
- replication datasource
- MmBatis
- JPA-hibernate

## Arguments
### VM Arguments
```
-Dspring.profiles.active=mysql
```

### Program Arguments
```
version=3 job.name=stepNextConditionalJob requestDate=20200201
```

## Main Concept

### Job
- 하나의 배치 프로세스를 캡슐화 하는 객체
#### JobInstance
#### JobParameters
#### JobExecution

### Step
#### StepExecution

### ExecutionContext

### JobRepository
- Job, Step 들에 대한 영속성 관리 객체

### JobLauncher
- Job 실행 시켜주는 객체

### Item
#### ItemReader
- Step 에 대한 입력 객체

#### ItemWriter
- Step 의 출력 객체

#### ItemProcessor
- Item 에 대한 비즈니스 작업 처리 객체

#### Status
- BatchStatus : Job 또는 Step 의 실행 결과
- ExitStatus : Step의 실행 후 상태


## Meta-Data Schema

#### Version
- Many of the database tables discussed in this appendix contain a version column. This column is important because Spring Batch employs an optimistic locking strategy when dealing with updates to the database. This means that each time a record is 'touched' (updated) the value in the version column is incremented by one. When the repository goes back to save the value, if the version number has changed it throws an OptimisticLockingFailureException, indicating there has been an error with concurrent access. This check is necessary, since, even though different batch jobs may be running in different machines, they all use the same database tables.

#### Identity
```
CREATE SEQUENCE BATCH_STEP_EXECUTION_SEQ;
CREATE SEQUENCE BATCH_JOB_EXECUTION_SEQ;
CREATE SEQUENCE BATCH_JOB_SEQ;
```
```
CREATE TABLE BATCH_STEP_EXECUTION_SEQ (ID BIGINT NOT NULL) type=InnoDB;
INSERT INTO BATCH_STEP_EXECUTION_SEQ values(0);
CREATE TABLE BATCH_JOB_EXECUTION_SEQ (ID BIGINT NOT NULL) type=InnoDB;
INSERT INTO BATCH_JOB_EXECUTION_SEQ values(0);
CREATE TABLE BATCH_JOB_SEQ (ID BIGINT NOT NULL) type=InnoDB;
INSERT INTO BATCH_JOB_SEQ values(0);
```


#### BATCH_JOB_INSTANCE
- Job Parameter 에 따라 생성되는 JobInstance 데이터
```
CREATE TABLE BATCH_JOB_INSTANCE  (
  JOB_INSTANCE_ID BIGINT  PRIMARY KEY ,
  VERSION BIGINT,
  JOB_NAME VARCHAR(100) NOT NULL ,
  JOB_KEY VARCHAR(2500)
);
```

#### BATCH_JOB_EXECUTION_PARAMS
- JobParameters 와 관련된 모든 데이터 : Job(execution) 에 전달된 key/value 쌍이 0개 이상 등록되어, JobParameters 에 대한 기록 역할
```
CREATE TABLE BATCH_JOB_EXECUTION_PARAMS  (
	JOB_EXECUTION_ID BIGINT NOT NULL ,
	TYPE_CD VARCHAR(6) NOT NULL ,
	KEY_NAME VARCHAR(100) NOT NULL ,
	STRING_VAL VARCHAR(250) ,
	DATE_VAL DATETIME DEFAULT NULL ,
	LONG_VAL BIGINT ,
	DOUBLE_VAL DOUBLE PRECISION ,
	IDENTIFYING CHAR(1) NOT NULL ,
	constraint JOB_EXEC_PARAMS_FK foreign key (JOB_EXECUTION_ID)
	references BATCH_JOB_EXECUTION(JOB_EXECUTION_ID)
);
```

#### BATCH_JOB_EXECUTION
- 실행되는 모든 Job 에 대한 이력 등록
```
CREATE TABLE BATCH_JOB_EXECUTION  (
  JOB_EXECUTION_ID BIGINT  PRIMARY KEY ,
  VERSION BIGINT,
  JOB_INSTANCE_ID BIGINT NOT NULL,
  CREATE_TIME TIMESTAMP NOT NULL,
  START_TIME TIMESTAMP DEFAULT NULL,
  END_TIME TIMESTAMP DEFAULT NULL,
  STATUS VARCHAR(10),
  EXIT_CODE VARCHAR(20),
  EXIT_MESSAGE VARCHAR(2500),
  LAST_UPDATED TIMESTAMP,
  JOB_CONFIGURATION_LOCATION VARCHAR(2500) NULL,
  constraint JOB_INSTANCE_EXECUTION_FK foreign key (JOB_INSTANCE_ID)
  references BATCH_JOB_INSTANCE(JOB_INSTANCE_ID)
) ;
```

#### BATCH_STEP_EXECUTION
- 실행되는 모든 Step(Job 에 1개 이상 포함되는 하위 개념) 에 대한 이력이 등록됨
```
CREATE TABLE BATCH_STEP_EXECUTION  (
  STEP_EXECUTION_ID BIGINT  PRIMARY KEY ,
  VERSION BIGINT NOT NULL,
  STEP_NAME VARCHAR(100) NOT NULL,
  JOB_EXECUTION_ID BIGINT NOT NULL,
  START_TIME TIMESTAMP NOT NULL ,
  END_TIME TIMESTAMP DEFAULT NULL,
  STATUS VARCHAR(10),
  COMMIT_COUNT BIGINT ,
  READ_COUNT BIGINT ,
  FILTER_COUNT BIGINT ,
  WRITE_COUNT BIGINT ,
  READ_SKIP_COUNT BIGINT ,
  WRITE_SKIP_COUNT BIGINT ,
  PROCESS_SKIP_COUNT BIGINT ,
  ROLLBACK_COUNT BIGINT ,
  EXIT_CODE VARCHAR(20) ,
  EXIT_MESSAGE VARCHAR(2500) ,
  LAST_UPDATED TIMESTAMP,
  constraint JOB_EXECUTION_STEP_FK foreign key (JOB_EXECUTION_ID)
  references BATCH_JOB_EXECUTION(JOB_EXECUTION_ID)
) ;
```

#### BATCH_JOB_EXECUTION_CONTEXT
- BATCH_JOB_EXECUTION_CONTEXT 표에는 작업의 실행 컨텍스트와 관련된 모든 정보가 수록
- JobExecution 당 정확히 하나의 Job ExecutionContext 존재
- 특정 작업 실행에 필요한 모든 작업 수준 데이터를 포함하고 있다. 
```
CREATE TABLE BATCH_JOB_EXECUTION_CONTEXT  (
  JOB_EXECUTION_ID BIGINT PRIMARY KEY,
  SHORT_CONTEXT VARCHAR(2500) NOT NULL,
  SERIALIZED_CONTEXT CLOB,
  constraint JOB_EXEC_CTX_FK foreign key (JOB_EXECUTION_ID)
  references BATCH_JOB_EXECUTION(JOB_EXECUTION_ID)
) ;
```

#### BATCH_STEP_EXECUTION_CONTEXT
- BATCH_STEP_EXECUTION_CONTEXT 에는 Step 의 실행 컨텍스트와 관련된 모든 정보가 수록
- StepExecution 당 정확히 하나의 ExecutionContext 존재 
- 특정 단계 실행을 위해 지속되어야 하는 모든 데이터를 포함하고 있다. 
```
CREATE TABLE BATCH_STEP_EXECUTION_CONTEXT  (
  STEP_EXECUTION_ID BIGINT PRIMARY KEY,
  SHORT_CONTEXT VARCHAR(2500) NOT NULL,
  SERIALIZED_CONTEXT CLOB,
  constraint STEP_EXEC_CTX_FK foreign key (STEP_EXECUTION_ID)
  references BATCH_STEP_EXECUTION(STEP_EXECUTION_ID)
) ;
```



## Reference
- https://docs.spring.io/spring-batch/docs/current/reference/html/
- https://github.com/jojoldu/spring-batch-in-action
- https://ahndy84.tistory.com/24?category=339592
- https://blog.kingbbode.com/posts/spring-batch-quartz
- https://medium.com/@ChamithKodikara/spring-boot-2-quartz-2-scheduler-integration-a8eaaf850805
- https://howtodoinjava.com/spring-batch/java-config-multiple-steps/