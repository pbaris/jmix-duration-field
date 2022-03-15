[![license](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0)

# Jmix DurationField

This add-on provides support for the `java.time.Duration` data type through a specific UI component.

## Installation

The following table shows which version of the add-on is compatible with which version of the platform:

| Jmix Version | Add-on Version | Implementation                             |
|--------------|----------------|--------------------------------------------|
| 1.1.*        | 1.1.0          | gr.netmechanics.jmix:azurefs-starter:1.1.0 |
| 1.2.*        | 1.2.0          | gr.netmechanics.jmix:azurefs-starter:1.2.0 |

Add to your project's `build.gradle` dependencies:

```gradle
implementation 'gr.netmechanics.jmix:azurefs-starter:1.2.0'
```

# Configuration
You should define your Azure Storage settings in the `application.properties`:

| Name                          | Default       | Description                                                                                                            |
|-------------------------------|---------------|------------------------------------------------------------------------------------------------------------------------|
| jmix.azurefs.connectionString |               | Azure Storage account name                                                                                             |         
| jmix.azurefs.containerName    |               | Azure Storage container name                                                                                           |         
| jmix.azurefs.blockSize        | 1048576 (1MB) | The block size (chunk size) to transfer at a time.                                                                     |        
| jmix.azurefs.maxConcurrency   | 2             | The maximum number of parallel requests that will be issued at any given time as a part of a single parallel transfer. |         

 **Example:**
 ```properties
 jmix.azurefs.connectionString = DefaultEndpointsProtocol=https;AccountName=myAccount;AccountKey=1WE6oxxWosQ745ClyQP/tfRT1H6zGoDKo8FOOtnVFZ3rkPZy+8J71f9vGcGgcQKXWCsA2iER5Pmnop0wBuU3Gg==;EndpointSuffix=core.windows.net
jmix.azurefs.containerName = myfiles
 ```
