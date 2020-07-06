#!/bin/sh

#
# name : sendSMSDaemon
# /!\ Attention `jq` command required (https://stedolan.github.io/jq/) /!\
#

# urls api
urlRandomNamesApi="https://randomuser.me/api/?nat=gb&inc=name"
urlKafkaApi="http://localhost:8080/kafka/sms/send"

# tests phone numbers
# corresponding to users filled in topic `demo.user` :
# 112233|{"phoneNumber":"112233", "firstName":"Hubert", "lastName":"Bonisseur de la Bath"}
# 998877|{"phoneNumber":"998877", "firstName":"Jean", "lastName":"Soudajman"}
# 446655|{"phoneNumber":"446655", "firstName":"Henri", "lastName":"Tathan"}
phoneNumbers=("112233" "446655" "998877")

# main
while true; do

    # random emitter
    randomIndex=$[$RANDOM % ${#phoneNumbers[@]}]
    emitter=${phoneNumbers[randomIndex]}

    # random name to include in message
    response=$(curl -H "Accept: application/json" -H "Content-Type: application/json;charset=utf-8" -XGET -s "$urlRandomNamesApi")
    randomName=$(echo $response | jq -r '.results[0].name.first')
    message="Bonjour $randomName, Blah kung Foo"
    
    # random receiver
    receiver=$((100000 + RANDOM % 999999))

    # print
    echo -e "Emitter : \t [$emitter]"
    echo -e "Message : \t [$message]"    
    echo -e "Receiver : \t [$receiver]"

    # post
    data="{\"phoneNumberEmitter\":\"$emitter\", \"message\":\"$message\", \"phoneNumberReceiver\":\"$receiver\"}"
    curl -s -H "Accept: application/json" -H "Content-Type: application/json;charset=utf-8" -X POST "$urlKafkaApi" -d "$data"

    echo -e "\r\n======================="

    sleep 2;

done


