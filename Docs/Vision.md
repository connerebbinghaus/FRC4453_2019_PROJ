# Vision Specification
- [Vision Specification](#vision-specification)
    - [Endpoints](#endpoints)
    - [Data Types](#data-types)
        - [Pipeline](#pipeline)
        - [Status](#status)
    - [Operations](#operations)
        - [General](#general)
            - [No-Op](#no-op)
                - [Inputs](#inputs)
                - [Outputs](#outputs)
  
## Endpoints
The vision system uses MQTT for configuration and status. Listed below are the various endpoints, along with their purposes.

| Path                     | Data Type | Direction       | Purpose                                                                         |
| ------------------------ | --------- | --------------- | ------------------------------------------------------------------------------- |
| /Vision/RegisterPipeline | Pipeline  | Robot -> Vision | Registers a new pipeline into the vision system. Invalid Pipelines are ignored. |
| /Vision/Status           | Status    | Vision -> Robot | Gives the status of the Vision system.                                          |

## Data Types
The vision system uses JSON payloads. These payloads are described below.

### Pipeline
| Key        | Description                      |
| ---------- | -------------------------------- |
| operations | Array of Operations. (see below) |
| id         | The ID of the Pipeline.          |

### Status
| Key          | Description                                               |
| ------------ | --------------------------------------------------------- |
| pipelines    | Array of PipelineStatuses (see [Operations](#operations)) |
| systemStatus | String representing overall status.                       |

## Operations
Various "operations" are used in conjunction with "channels" to form a Pipeline. A listing of all implemented operations follows.

### General
These Operations are always available.

#### No-Op
Does nothing; passes data from its input channel to output channel unchanged.

##### Inputs
| #   | Type | Description |
| --- | ---- | ----------- |
| 0   | Any  | Input.      |

##### Outputs
| #   | Type | Description |
| --- | ---- | ----------- |
| 0   | Any  | Output.      |

| Key    | Description                                          |
| ------ | ---------------------------------------------------- |
| type   | String. Must be "noop".                              |
| name   | Optional String. Descriptive name of this Operation. |
| input  | Integer. ID of input channel.                        |
| output | Integer. ID of output channel.                       |
