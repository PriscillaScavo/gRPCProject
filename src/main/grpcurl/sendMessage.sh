#!/bin/bash

BASE_DIR=`dirname "$0"`
DATA=sendMessage.json
ROOT_DIR=$BASE_DIR/../../..

function sendMessage() {
    ${GRPCURL:-grpcurl} \
     -d @ -plaintext \
     -import-path ${ROOT_DIR}/target/protobuf_external \
     -import-path ${ROOT_DIR}/src/main/protobuf \
     -proto ${ROOT_DIR}/src/main/protobuf/example.proto \
     localhost:8980 akka2grpc.MessageExchangeServiceExample/SendMessage < $DATA
}
echo $ROOT_DIR
sendMessage

grpcurl -d @ -plaintext localhost:8980 akka2grpc.MessageExchangeServiceExample/StreamMessagesSingleResponse
{"id": "1", "message": "there is a new message", "extra_info": []}{"id": "2", "message": "there is a new message1", "extra_info": []}{"id": "3", "message": "there is a new message2", "extra_info": []}0<&-
