# Homework 1
## Introduction
####
This homework focuses on creating different simulations using Cloud Sim Plus. Experimented with single datacenter,multiple datacenters ,different policies,schedulers and analyzed the results. 
All the simulations are written in Scala, can be compiled using SBT
## Installation and Run Instructions
Follow the steps to run the simulations 
1) Open IntellJ IDEA, in the  welcome screen select “Check out from Version Control” and then “Git”.
2) Enter the following URL and click “Clone”: https://sbalki3@bitbucket.org/sbalki3/cs441_hw1.git
3) When prompted click “Yes” in the dialog box
4) The SBT import screen will appear, so proceed with the default options and click on “OK”
5) Confirm overwriting with “Yes”
6) Please go to src/main/scala/simulations and start running the simulation of your choice. An IntelliJ run configuration is auto-created when you click the green arrow next to the main method of the simulation file you want to run.
###Note to run service_simulation.scala
To run the "service_simulation" => for SaaS - change the  service tag to "SaaS" in service_simulation.conf file.Similarly , for PaaS and IaaS.

## Project Structure

In this section project structure is described.
### Simulations (src/main/scala/simulations)
The following Simulation classes written in Scala using Cloud Sim Plus Framework are provided -
- Simulation_1 : Simulation with 2 Network Datacenters with schedulers and utilization ratio
- Simulation_2 : Simulation with 1 Datacenter and using different schedulers
- Simulation_3 : Simulation with 1 cloudlet_length 
- horizontalVmScaling_scaling : Simulation with horizontal scaling given array of cloudlet_lengths
- verticalVmScaling_simulation : Simulation with horizontal scaling given array of cloudlet_lengths
- service_simulation : Simulation implementing SaaS, PaaS, IaaS

In detail explaination on these simulations will be given below 

### Resources (src/main/resources)
This directory contains the different configuration files used for all the created simulations.

- my_simulation1.conf    : Configuration file for Simulation_1
- my_simulation2.conf    : Configuration file for Simulation_2
- my_simulation3.conf    : Configuration file for Simulation_3
- service_simulation.conf : Configuration file for service_simulation
##Note 
To understand the difference between scalings and its relation with clouldlet_lengths ,"horizontalVmScaling_simulation","verticalVmScaling_simulation" and "Simulation_3" takes configurations from my_simulation3.conf file.

### Tests (src/main/test/scala)
The following test class is provided: 
- Simulation_2Test :
  The following methods are tested:
    - checks if the Utilization ratio is matched with the given
    - checks if the MIPS capacity is matched with the given
    - checks if the storage capacity is matched with the given
    

- Simulation_3Test :
  The following methods are tested:
    - createDatacenter: the test checks whether the created Datacenter is not null and that it contains the correct number of hosts.
    - createVms: the test checks whether the list of VMs created is not empty and the number of VMs created matches the input configuration.
    - createHost: the test checks whether an individual host object created during simulations is not null and it's properties match the input configuration.
    - createCloudlets: the test checks whether the list of cloudlets created is not empty and the number of cloudlets created matches the input configuration.



### Implementation of Simulation_1
In this simulation:
 - Datcenter -> 2 Network Datacenters
 - VmPolicy -> RoundRobin
 - VmScheduler -> TimeShared
 - CloudletScheduler ->Space shared
 - Utilization model- Full
#### Observations
 - Shows Cloudlets being allocated to 2 data centers
 - When we have Clouldlet Scheduler as **Time** shared and increase the utilization ratio - It results in higher cost and more Actual CPU time
 .Because when ClouldletScheduler is timeShared cloudlets are assigned to VM’s simultaneously and they share the VM resources for their execution by continuously switching between one another.Therefore increasing utilization ratio will result in higher cost
 - When we have Clouldlet Scheduler as **Space** shared and decreasing the utilization ratio - It results in higher cost and more Actual CPU time
 Because when ClouldletScheduler is spaceShared, if a Cloudlet is assigned to a VM for execution, then no other Cloudlet will be executed by that VM until the current Cloudlet will not be dispatched by the VM. Therefore, full utilization will result in a better result

#### RESULTS
The optimised results were obtained from ClouldletSpaceShared and UtilizationRatio - 1
You will see that Cloudlet ID - 12,13,14 are allocated to second data center
                                            
                                                                        SIMULATION_1 RESULTS

Cloudlet|Status |DC|Host|Host PEs |VM|VM PEs   |CloudletLen|CloudletPEs|StartTime|FinishTime|ExecTime|Actual CPU Time|Total Cost
ID|       |ID|  ID|CPU cores|ID|CPU cores|         MI|  CPU cores|  Seconds|   Seconds| Seconds|               |
--------------------------------------------------------------------------------------------------------------------------------
       0|SUCCESS| 2|   0|        8| 0|        2|       1000|          2|        0|         1|       1|           1.00|     10.00
       3|SUCCESS| 2|   0|        8| 3|        2|       1000|          2|        0|         1|       1|           1.00|     10.00
       6|SUCCESS| 2|   0|        8| 6|        2|       1000|          2|        0|         1|       1|           1.00|     10.00
       9|SUCCESS| 2|   0|        8| 9|        2|       1000|          2|        0|         1|       1|           1.00|     10.00
       1|SUCCESS| 2|   1|        8| 1|        2|       1000|          2|        0|         1|       1|           1.00|     10.00
       4|SUCCESS| 2|   1|        8| 4|        2|       1000|          2|        0|         1|       1|           1.00|     10.00
       7|SUCCESS| 2|   1|        8| 7|        2|       1000|          2|        0|         1|       1|           1.00|     10.00
      10|SUCCESS| 2|   1|        8|10|        2|       1000|          2|        0|         1|       1|           1.00|     10.00
       2|SUCCESS| 2|   2|        8| 2|        2|       1000|          2|        0|         1|       1|           1.00|     10.00
       5|SUCCESS| 2|   2|        8| 5|        2|       1000|          2|        0|         1|       1|           1.00|     10.00
       8|SUCCESS| 2|   2|        8| 8|        2|       1000|          2|        0|         1|       1|           1.00|     10.00
      11|SUCCESS| 2|   2|        8|11|        2|       1000|          2|        0|         1|       1|           1.00|     10.00
      12|SUCCESS| 3|   0|        8|12|        2|       1000|          2|        0|         1|       1|           1.00|     10.00
      13|SUCCESS| 3|   1|        8|13|        2|       1000|          2|        0|         1|       1|           1.00|     10.00
      14|SUCCESS| 3|   2|        8|14|        2|       1000|          2|        0|         1|       1|           1.00|     10.00
      15|SUCCESS| 2|   0|        8| 0|        2|       1000|          2|        1|         2|       1|           1.00|     10.00
      18|SUCCESS| 2|   0|        8| 3|        2|       1000|          2|        1|         2|       1|           1.00|     10.00
      21|SUCCESS| 2|   0|        8| 6|        2|       1000|          2|        1|         2|       1|           1.00|     10.00
      24|SUCCESS| 2|   0|        8| 9|        2|       1000|          2|        1|         2|       1|           1.00|     10.00
      16|SUCCESS| 2|   1|        8| 1|        2|       1000|          2|        1|         2|       1|           1.00|     10.00
      19|SUCCESS| 2|   1|        8| 4|        2|       1000|          2|        1|         2|       1|           1.00|     10.00
      22|SUCCESS| 2|   1|        8| 7|        2|       1000|          2|        1|         2|       1|           1.00|     10.00
      25|SUCCESS| 2|   1|        8|10|        2|       1000|          2|        1|         2|       1|           1.00|     10.00
      17|SUCCESS| 2|   2|        8| 2|        2|       1000|          2|        1|         2|       1|           1.00|     10.00
      20|SUCCESS| 2|   2|        8| 5|        2|       1000|          2|        1|         2|       1|           1.00|     10.00
      23|SUCCESS| 2|   2|        8| 8|        2|       1000|          2|        1|         2|       1|           1.00|     10.00
      26|SUCCESS| 2|   2|        8|11|        2|       1000|          2|        1|         2|       1|           1.00|     10.00
      27|SUCCESS| 3|   0|        8|12|        2|       1000|          2|        1|         2|       1|           1.00|     10.00
      28|SUCCESS| 3|   1|        8|13|        2|       1000|          2|        1|         2|       1|           1.00|     10.00
      29|SUCCESS| 3|   2|        8|14|        2|       1000|          2|        1|         2|       1|           1.00|     10.00
--------------------------------------------------------------------------------------------------------------------------------
### Implementation of Simulation_2
In this simulation:
- Datcenter -> 1 Simple Datacenter
- VmPolicy -> BestFit
- VmScheduler -> TimeShared
- CloudletScheduler ->Space shared

#### Observations

- When we have VM Scheduler as **Time** shared , implementing Clouldlet Scheduler as **Space** shared will result in simulation with comparitively less expensive.
  (i.e) If both VM Scheduler and Cloudlet scheduler is **Time** shared ,then the cost is high
    - Because : Using Time-Shared policies for VM and Cloudlet schedulers means whenever there are not enough VMs to run all the cloudlets independently, the TimeShared policy allocates cloudlets on the same VM and runs than concurrently, therefore yielding a higher cost.
- When we have Clouldlet Scheduler as **Space** and Vm is **Time** shared, with increase of cloudlets the cost and Actual CPU time increases
  Because when Clouldlet Scheduler is SpaceShared ,clouldlet leaves vm only after completion of its execution

#### RESULTS 
Results  obtained from ClouldletSpaceShared and VmTimeShared schedulers

                                                                           SIMULATION_2 RESULTS


Cloudlet|Status |DC|Host|Host PEs |VM|VM PEs   |CloudletLen|CloudletPEs|StartTime|FinishTime|ExecTime|Actual CPU Time|Total Cost
ID|       |ID|  ID|CPU cores|ID|CPU cores|         MI|  CPU cores|  Seconds|   Seconds| Seconds|               |
--------------------------------------------------------------------------------------------------------------------------------
       0|SUCCESS| 2|   0|        8| 0|        2|       1000|          2|        0|         1|       1|           1.00|      3.00
       1|SUCCESS| 2|   0|        8| 1|        2|       1000|          2|        0|         1|       1|           1.00|      3.00
       2|SUCCESS| 2|   0|        8| 2|        2|       1000|          2|        0|         1|       1|           1.00|      3.00
       3|SUCCESS| 2|   0|        8| 3|        2|       1000|          2|        0|         1|       1|           1.00|      3.00
       4|SUCCESS| 2|   1|        8| 4|        2|       1000|          2|        0|         1|       1|           1.00|      3.00
       5|SUCCESS| 2|   1|        8| 5|        2|       1000|          2|        0|         1|       1|           1.00|      3.00
       6|SUCCESS| 2|   1|        8| 6|        2|       1000|          2|        0|         1|       1|           1.00|      3.00
       7|SUCCESS| 2|   1|        8| 7|        2|       1000|          2|        0|         1|       1|           1.00|      3.00
       8|SUCCESS| 2|   2|        8| 8|        2|       1000|          2|        0|         1|       1|           1.00|      3.00
       9|SUCCESS| 2|   2|        8| 9|        2|       1000|          2|        0|         1|       1|           1.00|      3.00
      10|SUCCESS| 2|   2|        8|10|        2|       1000|          2|        0|         1|       1|           1.00|      3.00
      11|SUCCESS| 2|   2|        8|11|        2|       1000|          2|        0|         1|       1|           1.00|      3.00
      12|SUCCESS| 2|   3|        8|12|        2|       1000|          2|        0|         1|       1|           1.00|      3.00
      13|SUCCESS| 2|   3|        8|13|        2|       1000|          2|        0|         1|       1|           1.00|      3.00
      14|SUCCESS| 2|   3|        8|14|        2|       1000|          2|        0|         1|       1|           1.00|      3.00
      15|SUCCESS| 2|   0|        8| 0|        2|       1000|          2|        1|         2|       1|           1.11|      3.33
      16|SUCCESS| 2|   0|        8| 1|        2|       1000|          2|        1|         2|       1|           1.11|      3.33
      17|SUCCESS| 2|   0|        8| 2|        2|       1000|          2|        1|         2|       1|           1.11|      3.33
      18|SUCCESS| 2|   0|        8| 3|        2|       1000|          2|        1|         2|       1|           1.11|      3.33
      19|SUCCESS| 2|   1|        8| 4|        2|       1000|          2|        1|         2|       1|           1.11|      3.33
      20|SUCCESS| 2|   1|        8| 5|        2|       1000|          2|        1|         2|       1|           1.11|      3.33
      21|SUCCESS| 2|   1|        8| 6|        2|       1000|          2|        1|         2|       1|           1.11|      3.33
      22|SUCCESS| 2|   1|        8| 7|        2|       1000|          2|        1|         2|       1|           1.11|      3.33
      23|SUCCESS| 2|   2|        8| 8|        2|       1000|          2|        1|         2|       1|           1.11|      3.33
      24|SUCCESS| 2|   2|        8| 9|        2|       1000|          2|        1|         2|       1|           1.11|      3.33
      25|SUCCESS| 2|   2|        8|10|        2|       1000|          2|        1|         2|       1|           1.11|      3.33
      26|SUCCESS| 2|   2|        8|11|        2|       1000|          2|        1|         2|       1|           1.11|      3.33
      27|SUCCESS| 2|   3|        8|12|        2|       1000|          2|        1|         2|       1|           1.11|      3.33
      28|SUCCESS| 2|   3|        8|13|        2|       1000|          2|        1|         2|       1|           1.11|      3.33
      29|SUCCESS| 2|   3|        8|14|        2|       1000|          2|        1|         2|       1|           1.11|      3.33
--------------------------------------------------------------------------------------------------------------------------------

### Implementation of Simulation_3 and HorizontalVmScaling
This Simulation is to experiment on cloudlet lengths , Simulation_3 takes single cloudlet_length and horizontalVmScaling takes array of cloudlet_lengths.All the other configurations remains the same for both

- Datcenter -> 1 Simple Datacenter
- cloudlet_lengths given :
   - Simulation_3 : 2000
   - horizontalVm : [2000, 4000, 10000, 16000, 2000, 30000, 20000]
   
#### Observations
- Change in the clouldlet_lengths increases the CPU Time and Cost
#### RESULTS 
        
                                                               SIMULATION_3 RESULTS

Cloudlet|Status |DC|Host|Host PEs |VM|VM PEs   |CloudletLen|CloudletPEs|StartTime|FinishTime|ExecTime|Actual CPU Time|Total Cost
ID|       |ID|  ID|CPU cores|ID|CPU cores|         MI|  CPU cores|  Seconds|   Seconds| Seconds|               |
--------------------------------------------------------------------------------------------------------------------------------
       0|SUCCESS| 2|   0|        6| 0|        1|       1000|          1|        0|         1|       1|           1.34|      4.03
       5|SUCCESS| 2|   0|        6| 5|        1|       1000|          1|        0|         1|       1|           1.34|      4.03
      10|SUCCESS| 2|   0|        6|10|        1|       1000|          1|        0|         1|       1|           1.34|      4.03
       1|SUCCESS| 2|   1|        6| 1|        1|       1000|          1|        0|         1|       1|           1.34|      4.03
       6|SUCCESS| 2|   1|        6| 6|        1|       1000|          1|        0|         1|       1|           1.34|      4.03
      11|SUCCESS| 2|   1|        6|11|        1|       1000|          1|        0|         1|       1|           1.34|      4.03
       2|SUCCESS| 2|   2|        6| 2|        1|       1000|          1|        0|         1|       1|           1.34|      4.03
       7|SUCCESS| 2|   2|        6| 7|        1|       1000|          1|        0|         1|       1|           1.34|      4.03
      12|SUCCESS| 2|   2|        6|12|        1|       1000|          1|        0|         1|       1|           1.34|      4.03
       3|SUCCESS| 2|   3|        6| 3|        1|       1000|          1|        0|         1|       1|           1.34|      4.03
       8|SUCCESS| 2|   3|        6| 8|        1|       1000|          1|        0|         1|       1|           1.34|      4.03
      13|SUCCESS| 2|   3|        6|13|        1|       1000|          1|        0|         1|       1|           1.34|      4.03
       4|SUCCESS| 2|   4|        6| 4|        1|       1000|          1|        0|         1|       1|           1.34|      4.03
       9|SUCCESS| 2|   4|        6| 9|        1|       1000|          1|        0|         1|       1|           1.34|      4.03
      14|SUCCESS| 2|   4|        6|14|        1|       1000|          1|        0|         1|       1|           1.34|      4.03
      15|SUCCESS| 2|   0|        6| 0|        1|       1000|          1|        2|         2|       1|           1.44|      4.33
      20|SUCCESS| 2|   0|        6| 5|        1|       1000|          1|        2|         2|       1|           1.44|      4.33
      25|SUCCESS| 2|   0|        6|10|        1|       1000|          1|        2|         2|       1|           1.44|      4.33
      16|SUCCESS| 2|   1|        6| 1|        1|       1000|          1|        2|         2|       1|           1.44|      4.33
      21|SUCCESS| 2|   1|        6| 6|        1|       1000|          1|        2|         2|       1|           1.44|      4.33
      26|SUCCESS| 2|   1|        6|11|        1|       1000|          1|        2|         2|       1|           1.44|      4.33
      17|SUCCESS| 2|   2|        6| 2|        1|       1000|          1|        2|         2|       1|           1.44|      4.33
      22|SUCCESS| 2|   2|        6| 7|        1|       1000|          1|        2|         2|       1|           1.44|      4.33
      27|SUCCESS| 2|   2|        6|12|        1|       1000|          1|        2|         2|       1|           1.44|      4.33
      18|SUCCESS| 2|   3|        6| 3|        1|       1000|          1|        2|         2|       1|           1.44|      4.33
      23|SUCCESS| 2|   3|        6| 8|        1|       1000|          1|        2|         2|       1|           1.44|      4.33
      28|SUCCESS| 2|   3|        6|13|        1|       1000|          1|        2|         2|       1|           1.44|      4.33
      19|SUCCESS| 2|   4|        6| 4|        1|       1000|          1|        2|         2|       1|           1.44|      4.33
      24|SUCCESS| 2|   4|        6| 9|        1|       1000|          1|        2|         2|       1|           1.44|      4.33
      29|SUCCESS| 2|   4|        6|14|        1|       1000|          1|        2|         2|       1|           1.44|      4.33
--------------------------------------------------------------------------------------------------------------------------------

                                                       HORIZONTAL_VM_SCALING RESULTS

Cloudlet|Status |DC|Host|Host PEs |VM|VM PEs   |CloudletLen|CloudletPEs|StartTime|FinishTime|ExecTime|Actual CPU Time|Total Cost
ID|       |ID|  ID|CPU cores|ID|CPU cores|         MI|  CPU cores|  Seconds|   Seconds| Seconds|               |
--------------------------------------------------------------------------------------------------------------------------------
       0|SUCCESS| 2|   0|        6| 0|        1|       2000|          1|        0|         4|       4|           4.01|     12.03
       7|SUCCESS| 2|   2|        6| 7|        1|       2000|          1|        0|         4|       4|           4.01|     12.03
       8|SUCCESS| 2|   3|        6| 8|        1|       2000|          1|        0|         4|       4|           4.01|     12.03
       9|SUCCESS| 2|   4|        6| 9|        1|       2000|          1|        0|         4|       4|           4.01|     12.03
      10|SUCCESS| 2|   0|        6|10|        1|       4000|          1|        0|         8|       8|           8.11|     24.33
       3|SUCCESS| 2|   3|        6| 3|        1|       4000|          1|        0|         8|       8|           8.11|     24.33
      14|SUCCESS| 2|   4|        6|14|        1|       4000|          1|        0|         8|       8|           8.11|     24.33
      23|SUCCESS| 2|   3|        6| 8|        1|       4000|          1|        4|        12|       8|           8.11|     24.33
      18|SUCCESS| 2|   3|        6| 3|        1|       4000|          1|        8|        16|       8|           8.11|     24.33
       1|SUCCESS| 2|   1|        6| 1|        1|      10000|          1|        0|        20|      20|          20.11|     60.33
       4|SUCCESS| 2|   4|        6| 4|        1|      10000|          1|        0|        20|      20|          20.11|     60.33
      22|SUCCESS| 2|   2|        6| 7|        1|      10000|          1|        4|        24|      20|          20.11|     60.33
      19|SUCCESS| 2|   4|        6| 4|        1|       2000|          1|       20|        24|       4|           4.01|     12.03
       6|SUCCESS| 2|   1|        6| 6|        1|      16000|          1|        0|        32|      32|          32.11|     96.33
      13|SUCCESS| 2|   3|        6|13|        1|      16000|          1|        0|        32|      32|          32.11|     96.33
      21|SUCCESS| 2|   1|        6| 6|        1|       2000|          1|       32|        36|       4|           4.11|     12.33
       5|SUCCESS| 2|   0|        6| 5|        1|      20000|          1|        0|        40|      40|          40.11|    120.33
      11|SUCCESS| 2|   1|        6|11|        1|      20000|          1|        0|        40|      40|          40.11|    120.33
       2|SUCCESS| 2|   2|        6| 2|        1|      20000|          1|        0|        40|      40|          40.11|    120.33
      12|SUCCESS| 2|   2|        6|12|        1|      20000|          1|        0|        40|      40|          40.11|    120.33
      16|SUCCESS| 2|   1|        6| 1|        1|      10000|          1|       20|        40|      20|          20.11|     60.33
      17|SUCCESS| 2|   2|        6| 2|        1|       2000|          1|       40|        44|       4|           4.11|     12.33
      29|SUCCESS| 2|   4|        6|14|        1|      20000|          1|        8|        48|      40|          40.11|    120.33
      15|SUCCESS| 2|   0|        6| 0|        1|      30000|          1|        4|        64|      60|          60.11|    180.33
      24|SUCCESS| 2|   4|        6| 9|        1|      30000|          1|        4|        64|      60|          60.11|    180.33
      28|SUCCESS| 2|   3|        6|13|        1|      16000|          1|       32|        64|      32|          32.12|     96.36
      25|SUCCESS| 2|   0|        6|10|        1|      30000|          1|        8|        68|      60|          60.11|    180.33
      27|SUCCESS| 2|   2|        6|12|        1|      20000|          1|       40|        80|      40|          40.11|    120.33
      20|SUCCESS| 2|   0|        6| 5|        1|      30000|          1|       40|       100|      60|          60.11|    180.33
      26|SUCCESS| 2|   1|        6|11|        1|      30000|          1|       40|       100|      60|          60.11|    180.33
--------------------------------------------------------------------------------------------------------------------------------

### Implementation of HorizontalVmSCaling and VerticalVmSCaling
This Simulation is to experiment on scaling policy with array of clouldlet lengths.Both the simulations take the same configuration file

- Datcenter -> 1 Datacenter
- cloudlet_lengths given : [2000, 4000, 10000, 16000, 2000, 30000, 20000]
- Clouldlet_Scheduler : Spaceshared
- VmScheduler : TimeShared

#### Observations
- Horizontal scaling refers to adding additional machines to your infrastructure to cope with new demands.
- Vertical scaling refers to adding additional resources to meet new demands
- Obeserve the simulation results of Horizontal and Vertical Scaling, the cost is almost same but observe the Cloudlet ID and Vm column

#### RESULTS

              VERTICAL_VM_SCALING

Cloudlet|Status |DC|Host|Host PEs |VM|VM PEs   |CloudletLen|CloudletPEs|StartTime|FinishTime|ExecTime|Actual CPU Time|Total Cost
ID|       |ID|  ID|CPU cores|ID|CPU cores|         MI|  CPU cores|  Seconds|   Seconds| Seconds|               |
--------------------------------------------------------------------------------------------------------------------------------
       0|SUCCESS| 2|   0|        6| 0|        1|       2000|          1|        0|         4|       4|           4.01|     12.03
       7|SUCCESS| 2|   1|        6| 7|        1|       2000|          1|        0|         4|       4|           4.01|     12.03
       8|SUCCESS| 2|   1|        6| 8|        1|       2000|          1|        0|         4|       4|           4.01|     12.03
       9|SUCCESS| 2|   1|        6| 9|        1|       2000|          1|        0|         4|       4|           4.01|     12.03
       3|SUCCESS| 2|   0|        6| 3|        1|       4000|          1|        0|         8|       8|           8.11|     24.33
      10|SUCCESS| 2|   1|        6|10|        1|       4000|          1|        0|         8|       8|           8.11|     24.33
      14|SUCCESS| 2|   2|        6|14|        2|       4000|          1|        0|         8|       8|           8.11|     24.33
      23|SUCCESS| 2|   1|        6| 8|        1|       4000|          1|        4|        12|       8|           8.11|     24.33
      18|SUCCESS| 2|   0|        6| 3|        1|       4000|          1|        8|        16|       8|           8.11|     24.33
       1|SUCCESS| 2|   0|        6| 1|        1|      10000|          1|        0|        20|      20|          20.11|     60.33
       4|SUCCESS| 2|   0|        6| 4|        1|      10000|          1|        0|        20|      20|          20.11|     60.33
      19|SUCCESS| 2|   0|        6| 4|        1|       2000|          1|       20|        24|       4|           4.01|     12.03
      22|SUCCESS| 2|   1|        6| 7|        1|      10000|          1|        4|        24|      20|          20.11|     60.33
       6|SUCCESS| 2|   1|        6| 6|        1|      16000|          1|        0|        32|      32|          32.11|     96.33
      13|SUCCESS| 2|   2|        6|13|        2|      16000|          1|        0|        32|      32|          32.11|     96.33
      28|SUCCESS| 2|   2|        6|13|        2|      16000|          1|        2|        34|      32|          32.11|     96.33
      21|SUCCESS| 2|   1|        6| 6|        1|       2000|          1|       32|        36|       4|           4.11|     12.33
       2|SUCCESS| 2|   0|        6| 2|        1|      20000|          1|        0|        40|      40|          40.11|    120.33
       5|SUCCESS| 2|   0|        6| 5|        1|      20000|          1|        0|        40|      40|          40.11|    120.33
      11|SUCCESS| 2|   1|        6|11|        1|      20000|          1|        0|        40|      40|          40.11|    120.33
      12|SUCCESS| 2|   2|        6|12|        2|      20000|          1|        0|        40|      40|          40.11|    120.33
      16|SUCCESS| 2|   0|        6| 1|        1|      10000|          1|       20|        40|      20|          20.11|     60.33
      27|SUCCESS| 2|   2|        6|12|        2|      20000|          1|        2|        42|      40|          40.11|    120.33
      29|SUCCESS| 2|   2|        6|14|        2|      20000|          1|        2|        42|      40|          40.11|    120.33
      17|SUCCESS| 2|   0|        6| 2|        1|       2000|          1|       40|        44|       4|           4.11|     12.33
      15|SUCCESS| 2|   0|        6| 0|        1|      30000|          1|        4|        64|      60|          60.11|    180.33
      24|SUCCESS| 2|   1|        6| 9|        1|      30000|          1|        4|        64|      60|          60.11|    180.33
      25|SUCCESS| 2|   1|        6|10|        1|      30000|          1|        8|        68|      60|          60.11|    180.33
      20|SUCCESS| 2|   0|        6| 5|        1|      30000|          1|       40|       100|      60|          60.11|    180.33
      26|SUCCESS| 2|   1|        6|11|        1|      30000|          1|       40|       100|      60|          60.11|    180.33
--------------------------------------------------------------------------------------------------------------------------------

### Implementation of Service_Simulation
In this Simulation,the services offered across 3 datacenters are modelled as per the following plan-
To implement any service, minor changes in config file is all we need to do.
- Datcenter0 -> offers Software as a Service
- Datcenter1 -> offers Platform as a Service
- Datcenter2 -> offers Infrastructure as a Service

The following tag is used to differentiate between the different data centers for simulating the services as per configurations :
There is a service tag in "service_simulation.conf" where the tag value should be changed to "SaaS"/"PaaS"/"IaaS".
- If service tag is **"SaaS"** -> 
  - The simulation takes places in DataCenter 0 as this is configured to correspond to SaaS.
  - Here all the configurations are fixed. Takes the default index list values

- If service tag is **"PaaS"** -> 
  - The simulation takes place in DataCenter1 as this is configured to correspond to PaaS.
  - Here to show the flexibility in choosing the required instances, number_of_cloudlets, cloudlet_length, cloudlet_pes are given as array of combinations in config file .Each time we run, a new simulation is produced taking different combination


- If service tag is **"IaaS"** -> 
  - The simulation takes place in DataCenter2 as this is configured to correspond to IaaS.
  - For IaaS, list of different OS,number_of_cloudlets,cloudlet_length,cloudlet_pes are given.Every time we run ,a new simulation from any of the combinations is created 
    
  
#### Observations
- Running the simulation for SaaS, we get a fixed simulation results.
- For PaaS, the simulation results vary depending on the random config values it takes
- For IaaS, the simulation results vary depending on the random config values it takes
#### RESULTS
                                                    **SIMULATION RESULTS -SAAS**

Cloudlet|Status |DC|Host|Host PEs |VM|VM PEs   |CloudletLen|CloudletPEs|StartTime|FinishTime|ExecTime|Actual CPU Time|Total Cost
ID|       |ID|  ID|CPU cores|ID|CPU cores|         MI|  CPU cores|  Seconds|   Seconds| Seconds|               |
--------------------------------------------------------------------------------------------------------------------------------
       0|SUCCESS| 2|   0|        2| 0|        2|       2000|          4|        0|         2|       2|           1.71|      8.55
       1|SUCCESS| 2|   1|        2| 1|        2|       2000|          4|        0|         2|       2|           1.71|      8.55
       2|SUCCESS| 2|   2|        2| 2|        2|       2000|          4|        0|         2|       2|           1.71|      8.55
       3|SUCCESS| 2|   3|        2| 3|        2|       2000|          4|        0|         2|       2|           1.71|      8.55
       4|SUCCESS| 2|   4|        2| 4|        2|       2000|          4|        0|         2|       2|           1.71|      8.55
       5|SUCCESS| 2|   5|        2| 5|        2|       2000|          4|        0|         2|       2|           1.71|      8.55
       6|SUCCESS| 2|   6|        2| 6|        2|       2000|          4|        0|         2|       2|           1.71|      8.55
       7|SUCCESS| 2|   7|        2| 7|        2|       2000|          4|        0|         2|       2|           1.71|      8.55
       8|SUCCESS| 2|   8|        2| 8|        2|       2000|          4|        0|         2|       2|           1.71|      8.55
       9|SUCCESS| 2|   9|        2| 9|        2|       2000|          4|        0|         2|       2|           1.71|      8.55
      10|SUCCESS| 2|  10|        2|10|        2|       2000|          4|        0|         2|       2|           1.71|      8.55
      11|SUCCESS| 2|  11|        2|11|        2|       2000|          4|        0|         2|       2|           1.71|      8.55
      12|SUCCESS| 2|  12|        2|12|        2|       2000|          4|        0|         2|       2|           1.71|      8.55
      13|SUCCESS| 2|  13|        2|13|        2|       2000|          4|        0|         2|       2|           1.71|      8.55
      14|SUCCESS| 2|  14|        2|14|        2|       2000|          4|        0|         2|       2|           1.71|      8.55
--------------------------------------------------------------------------------------------------------------------------------
                            
  
                                            **SIMULATION RESULTS -PAAS**

Cloudlet|Status |DC|Host|Host PEs |VM|VM PEs   |CloudletLen|CloudletPEs|StartTime|FinishTime|ExecTime|Actual CPU Time|Total Cost
ID|       |ID|  ID|CPU cores|ID|CPU cores|         MI|  CPU cores|  Seconds|   Seconds| Seconds|               |
--------------------------------------------------------------------------------------------------------------------------------
       0|SUCCESS| 2|   0|        2| 0|        2|        800|          4|        0|         1|       1|           0.75|      7.50
       1|SUCCESS| 2|   1|        2| 1|        2|        800|          4|        0|         1|       1|           0.75|      7.50
       2|SUCCESS| 2|   2|        2| 2|        2|        800|          4|        0|         1|       1|           0.75|      7.50
       3|SUCCESS| 2|   3|        2| 3|        2|        800|          4|        0|         1|       1|           0.75|      7.50
       4|SUCCESS| 2|   4|        2| 4|        2|        800|          4|        0|         1|       1|           0.75|      7.50
       5|SUCCESS| 2|   5|        2| 5|        2|        800|          4|        0|         1|       1|           0.75|      7.50
       6|SUCCESS| 2|   6|        2| 6|        2|        800|          4|        0|         1|       1|           0.75|      7.50
       7|SUCCESS| 2|   7|        2| 7|        2|        800|          4|        0|         1|       1|           0.75|      7.50
       8|SUCCESS| 2|   8|        2| 8|        2|        800|          4|        0|         1|       1|           0.75|      7.50
       9|SUCCESS| 2|   9|        2| 9|        2|        800|          4|        0|         1|       1|           0.75|      7.50
      10|SUCCESS| 2|  10|        2|10|        2|        800|          4|        0|         1|       1|           0.75|      7.50
      11|SUCCESS| 2|  11|        2|11|        2|        800|          4|        0|         1|       1|           0.75|      7.50
      12|SUCCESS| 2|  12|        2|12|        2|        800|          4|        0|         1|       1|           0.75|      7.50
      13|SUCCESS| 2|  13|        2|13|        2|        800|          4|        0|         1|       1|           0.75|      7.50
      14|SUCCESS| 2|  14|        2|14|        2|        800|          4|        0|         1|       1|           0.75|      7.50
--------------------------------------------------------------------------------------------------------------------------------
                                          **SIMULATION RESULTS - IAAS**


Cloudlet|Status |DC|Host|Host PEs |VM|VM PEs   |CloudletLen|CloudletPEs|StartTime|FinishTime|ExecTime|Actual CPU Time|Total Cost
ID|       |ID|  ID|CPU cores|ID|CPU cores|         MI|  CPU cores|  Seconds|   Seconds| Seconds|               |
--------------------------------------------------------------------------------------------------------------------------------
       0|SUCCESS| 2|   0|        2| 0|        2|       4000|          2|        0|         3|       3|           3.31|     49.65
      15|SUCCESS| 2|   0|        2| 0|        2|       4000|          2|        0|         3|       3|           3.31|     49.65
       1|SUCCESS| 2|   1|        2| 1|        2|       4000|          2|        0|         3|       3|           3.31|     49.65
      16|SUCCESS| 2|   1|        2| 1|        2|       4000|          2|        0|         3|       3|           3.31|     49.65
       2|SUCCESS| 2|   2|        2| 2|        2|       4000|          2|        0|         3|       3|           3.31|     49.65
      17|SUCCESS| 2|   2|        2| 2|        2|       4000|          2|        0|         3|       3|           3.31|     49.65
       3|SUCCESS| 2|   3|        2| 3|        2|       4000|          2|        0|         3|       3|           3.31|     49.65
      18|SUCCESS| 2|   3|        2| 3|        2|       4000|          2|        0|         3|       3|           3.31|     49.65
       4|SUCCESS| 2|   4|        2| 4|        2|       4000|          2|        0|         3|       3|           3.31|     49.65
      19|SUCCESS| 2|   4|        2| 4|        2|       4000|          2|        0|         3|       3|           3.31|     49.65
       5|SUCCESS| 2|   5|        2| 5|        2|       4000|          2|        0|         3|       3|           3.31|     49.65
      20|SUCCESS| 2|   5|        2| 5|        2|       4000|          2|        0|         3|       3|           3.31|     49.65
       6|SUCCESS| 2|   6|        2| 6|        2|       4000|          2|        0|         3|       3|           3.31|     49.65
      21|SUCCESS| 2|   6|        2| 6|        2|       4000|          2|        0|         3|       3|           3.31|     49.65
       7|SUCCESS| 2|   7|        2| 7|        2|       4000|          2|        0|         3|       3|           3.31|     49.65
      22|SUCCESS| 2|   7|        2| 7|        2|       4000|          2|        0|         3|       3|           3.31|     49.65
       8|SUCCESS| 2|   8|        2| 8|        2|       4000|          2|        0|         3|       3|           3.31|     49.65
      23|SUCCESS| 2|   8|        2| 8|        2|       4000|          2|        0|         3|       3|           3.31|     49.65
       9|SUCCESS| 2|   9|        2| 9|        2|       4000|          2|        0|         3|       3|           3.31|     49.65
      24|SUCCESS| 2|   9|        2| 9|        2|       4000|          2|        0|         3|       3|           3.31|     49.65
      10|SUCCESS| 2|  10|        2|10|        2|       4000|          2|        0|         3|       3|           3.31|     49.65
      25|SUCCESS| 2|  10|        2|10|        2|       4000|          2|        0|         3|       3|           3.31|     49.65
      11|SUCCESS| 2|  11|        2|11|        2|       4000|          2|        0|         3|       3|           3.31|     49.65
      26|SUCCESS| 2|  11|        2|11|        2|       4000|          2|        0|         3|       3|           3.31|     49.65
      12|SUCCESS| 2|  12|        2|12|        2|       4000|          2|        0|         3|       3|           3.31|     49.65
      27|SUCCESS| 2|  12|        2|12|        2|       4000|          2|        0|         3|       3|           3.31|     49.65
      13|SUCCESS| 2|  13|        2|13|        2|       4000|          2|        0|         3|       3|           3.31|     49.65
      28|SUCCESS| 2|  13|        2|13|        2|       4000|          2|        0|         3|       3|           3.31|     49.65
      14|SUCCESS| 2|  14|        2|14|        2|       4000|          2|        0|         3|       3|           3.31|     49.65
      29|SUCCESS| 2|  14|        2|14|        2|       4000|          2|        0|         3|       3|           3.31|     49.65
--------------------------------------------------------------------------------------------------------------------------------


- Only the comparitively better simulation results are posted here.
- For results of few experiments with different configurations,policies,schedulers,utilization model check here [experiments.md](experiments.md)
####Reference Links
- [CloudSimPlus](https://javadoc.io/doc/org.cloudsimplus/cloudsim-plus/latest/index.html)
- [Performance Analysis of Scheduling](https://www.ijrdet.com/files/Volume8Issue2/IJRDET_0219_07.pdf)


------X-------