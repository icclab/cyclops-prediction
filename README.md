<a href="http://icclab.github.io/cyclops" target="_blank"><img align="middle" src="http://icclab.github.io/cyclops/assets/images/logo_big.png"></img></a>



## Prediction microservice
Prediction micro service as part of <a href="http://icclab.github.io/cyclops">Cyclops framework</a> has been designed to predict values based on historical data.
It is also capable of generic factual forecasting by encapsulating mllib as a prediction engine. The prediction microservice queries both UDR and RC services which
are parts of Cyclops project to get the Usage Data Record and Charge Data Record of a user, respectively for user perspective. For an admin, the data records of a
recourse will indicate the entire overall consumption and not just individual user's consumption. To predict the values of resources, mllib is used to provide
multiple algorithms support along with machine-learning capability. The prediction engine is deployed over Apache Spark engine which contains a scalable machine
learning library(mllib). As a package for engine development spark.ml is used to provide high-level APIs built on top of DataFrames. DataFrame is a collection of
data  organized into named columns, and is distributed by Apache Spark.


### Download
     $ git clone https://github.com/icclab/cyclops-prediction.git

#### Installation
If you have already deployed UDR microservice, you can skip install_prereq script, as Prediction microservice has the same prerequisites.

      $ cd cyclops-prediction/install
      $ chmod +x ./*
      $ bash install_prereq.sh
      $ bash setup_prediction.sh

#### Configuration
 * At the end of the installation process you will be asked for your deployment credentials and to modify any configuration parameters, **please do not ignore this step.**
 * If there is a need to update your configuration, you can find it stored here cyclops-prediction/src/main/webapp/WEB-INF/configuration.txt

### Deployment
     $ bash deploy_prediction.sh

### Documentation
  Visit the <a href="https://github.com/icclab/cyclops-prediction/wiki">Wiki</a> for detailed explanation and API reference guide.

### Cyclops architecture
<img align="middle" src="http://blog.zhaw.ch/icclab/files/2013/05/overall_architecture.png" alt="CYCLOPS Architecture" height="500" width="600"></img>

### Bugs and issues
  To report any bugs or issues, please use <a href="https://github.com/icclab/cyclops-prediction/issues">Github Issues</a>
  
### Communication
  * Email: icclab-rcb-cyclops[at]dornbirn[dot]zhaw[dot]ch
  * Website: <a href="http://icclab.github.io/cyclops" target="_blank">icclab.github.io/cyclops</a>
  * Blog: <a href="http://blog.zhaw.ch/icclab" target="_blank">http://blog.zhaw.ch/icclab</a>
  * Tweet us @<a href="https://twitter.com/ICC_Lab">ICC_Lab</a>
   
### Developed @
<img src="http://blog.zhaw.ch/icclab/files/2014/04/icclab_logo.png" alt="ICC Lab" height="180" width="620"></img>

### License
 
      Licensed under the Apache License, Version 2.0 (the "License"); you may
      not use this file except in compliance with the License. You may obtain
      a copy of the License at
 
           http://www.apache.org/licenses/LICENSE-2.0
 
      Unless required by applicable law or agreed to in writing, software
      distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
      WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
      License for the specific language governing permissions and limitations
      under the License.