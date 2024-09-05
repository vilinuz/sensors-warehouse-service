#!/bin/bash

echo "This script starts sensors-routing-service, sensors-central-service and NATS message queue, used for communication between the services."
echo "sensors-routing-service listens on ports 3344 (for temperature sensor messages) and 3355 (for the humidity sensor messages)"
echo "The service transforms the received message in JSON format and publishes the message on the respective message subject (temperature|humidity)"
echo "central-service is subscribed for bot subjects, and when message is received, it validates if the sensor value is in the pre-configured threshold"
echo "In case when the sensor value is above the threshold, an alarm event is initiated (observe the logs, please)"

red=$(tput setaf 1)
green=$(tput setaf 2)
reset=$(tput sgr0)

# The min and max temperature values
min_temperature=10
max_temperature=100

# The min and max humidity values
min_humidity=25
max_humidity=99

# waiting between requests. value is in seconds
min_waiting_time=1
max_waiting_time=10

if [[ "$(uname)" == "MINGW"* ]]; then
    os_type='GitBash'
else
    os_type='Linux'
fi

if [[ "$os_type" == "GitBash" ]]; then
    nc='ncat'
else
    nc='nc'
fi

if [[ $(which $nc | wc -l) -ne 1 ]]; then
  echo "${red}Command '$nc' cannot be found in your PATH. Please install it first and try again.${reset}"
  exit 1
fi

if [[ $(which git | wc -l) -ne 1 ]]; then
  echo "${red}Command git' cannot be found in your PATH. Please install it first and try again.${reset}"
  exit 1
fi

if [[ $(which docker-compose | wc -l) -ne 1 ]]; then
  echo "${red}docker-compose cannot be found in your PATH. Please install it first and try again.${reset}"
  exit 1
fi

start_warehouse_clients() {
  start_t1_client & start_h1_client
}

start_t1_client() {
  while true ; do
    get_random_value $min_temperature $max_temperature
    value=$?
    message="sensor_id=t1; value=$value"

    echo "${green}Sending message '$message'${reset}"
    echo  "$message" | $nc -u localhost 3344
    result=$?
    check_message_result $result

    get_random_value $min_waiting_time $max_waiting_time
    sleep $?
  done
}

start_h1_client() {
  while true ; do
    get_random_value $min_humidity $max_humidity
    value=$?
    message="sensor_id=h1; value=$value"

    echo "${green}Sending message '$message'${reset}"
    echo  "$message" | $nc -u localhost 3355
    result=$?
    check_message_result $result

    get_random_value $min_waiting_time $max_waiting_time
    sleep $?
  done
}

check_message_result() {
    result=$1
    if [[ $result -eq 0 ]]; then
      echo "${green}Message: '$message' sent successfully ${reset}"
    else
      echo "${red}Message: '$message' failed ${reset}"
    fi
}

get_random_value() {
  min=$1
  max=$2
  return $(( RANDOM % (max - min + 1) + min ))
}

echo "${green}Cleaning up the Docker environment..${reset}"
mvn clean install
echo "${green}Done..${reset}"

echo "${green}Cleaning up the Docker environment..${reset}"
docker-compose down
echo "${green}Done..${reset}"

echo "${green}Building and deploying services (sensors-routing-service, sensors-central-service and sensor-nats-server)..${reset}"
docker-compose up --build -d --wait && echo "${green}Done..${reset}"
echo "${green}Starting test clients..${reset}"
start_t1_client & start_h1_client

