# spring-batch-tutorial

## 0. Arguments
### VM Arguments
```
-Dspring.profiles.active=mysql
```

### Program Arguments
```
version=3 job.name=stepNextConditionalJob requestDate=20200201
```

## 1. Main Concept

### Job
#### Flow
#### Parameter & Scope
##### JobScope, StepScope

### Step
### Item
#### ItemReader
#### ItemProcessor
#### ItemWriter

## 2. DB Schema

### JOB
#### BATCH_JOB_INSTANCE
- Job Parameter 에 따라 생성되는 테이블 : 동일한 Job Parameter 는 여러개 존재할 수 없음 
- Job Parameter : Spring Batch Job 이 실행될 때 외부에서 받을 수 있는 파라미터

#### BATCH_JOB_SEQ


### JOB_EXECUTION
#### BATCH_JOB_EXECUTION
- BATCH_JOB 에 대한 실행 이력이 생성되는 테이블
#### BATCH_JOB_EXECUTION_SEQ
#### BATCH_JOB_EXECUTION_CONTEXT
#### BATCH_JOB_EXECUTION_PARAMS

### STEP_EXECUTION
#### BATCH_STEP_EXECUTION
#### BATCH_STEP_EXECUTION_SEQ
#### BATCH_STEP_EXECUTION_CONTEXT

## Reference
- https://github.com/jojoldu/spring-batch-in-action
- https://ahndy84.tistory.com/24?category=339592